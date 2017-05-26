package zzyq.module.storm.bolt;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;

import com.alibaba.fastjson.JSONObject;

import zzyq.bean.Config;
import zzyq.bean.DataType;
import zzyq.bean.GlobalObject;
import zzyq.bean.Index;
import zzyq.bean.Weibo;

public class WeiBoBuildIndexBolt extends BaseRichBolt {
	private static final long serialVersionUID = 3029437710486077264L;
	private Logger logger = LogManager.getLogger(this.getClass());
	private OutputCollector collector;
	private Map<String, Object> cachedMap = Collections.synchronizedMap(new LinkedHashMap<>());
	private AtomicLong count = new AtomicLong();

	public void execute(Tuple tuple) {
		try {
			boolean batch = false;
			if (tuple.contains(DataType.Micro.name().toLowerCase())) {
				Object object = (Object) tuple.getValueByField(DataType.Micro.name().toLowerCase());
				if (object instanceof String && ((String) object).equals(Config.batch)) {
					batch = true;
				} else {
					Weibo weibo = (Weibo) object;
					cachedMap.put(weibo.getId(), weibo);
				}
			}
			// 批量提交
			if (batch || (cachedMap.size() > 0 && cachedMap.size() >= Config.batchSize)) {
				BulkRequestBuilder bulkRequestBuilder = GlobalObject.elasticSearchClient.getClient().prepareBulk();
				int i=0;
				for (Entry<String, Object> entry : cachedMap.entrySet()) {
					String id = entry.getKey();
					Object value = entry.getValue();
					String json = JSONObject.toJSONString(value);
					if (value instanceof Weibo) {
						IndexRequestBuilder requestBuilder = GlobalObject.elasticSearchClient.getClient()
								.prepareIndex(Config.es_indexName, DataType.Micro.name(), id).setSource(json);
						bulkRequestBuilder.add(requestBuilder);
						if(i==cachedMap.size()-1) {
							Weibo weibo = (Weibo)value;
							Index index = (Index) GlobalObject.crawlerMongo.find(Index.class, Weibo.class.getSimpleName().toLowerCase());
							if (null == index) {
								index = new Index(Weibo.class.getSimpleName().toLowerCase(), weibo.getTimestamp());
							}
							index.setTimestamp(weibo.getTimestamp());
							GlobalObject.crawlerMongo.saveOrUpdate(index);
						}
					}
					i++;
				}
				int numberOfActions = bulkRequestBuilder.numberOfActions();
				if (numberOfActions > 0) {
					BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
					if (!bulkResponse.hasFailures()) {
						long took = bulkResponse.getTook().getMillis();
						long amount = count.addAndGet(numberOfActions);
						logger.info(String.format("微博索引构建成功！批量提交数量：%s 耗时：%s毫秒  总量：%s", numberOfActions, took, amount));
					}
					cachedMap.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			collector.ack(tuple);
		}
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map config, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		GlobalObject.elasticSearchClient.mapping(Config.es_indexName, DataType.Micro.name());
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("index"));
	}
}
