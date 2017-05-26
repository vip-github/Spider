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

import zzyq.bean.Bbs;
import zzyq.bean.Config;
import zzyq.bean.DataType;
import zzyq.bean.GlobalObject;
import zzyq.bean.Index;
import zzyq.bean.News;

public class NewsBuildIndexBolt extends BaseRichBolt {
	private static final long serialVersionUID = 3029437710486077264L;
	private Logger logger = LogManager.getLogger(this.getClass());
	private OutputCollector collector;
	private Map<String, Object> cachedMap = Collections.synchronizedMap(new LinkedHashMap<>());
	private AtomicLong count = new AtomicLong();

	@Override
	public void execute(Tuple tuple) {
		try {
			boolean batch = false;
			if (tuple.contains(DataType.News.name().toLowerCase())) {
				Object object = (Object) tuple.getValueByField(DataType.News.name().toLowerCase());
				if (object instanceof String && ((String) object).equals(Config.batch)) {
					batch = true;
				} else if(object instanceof News) {
					News news = (News) object;
					cachedMap.put(news.getId(), news);
				} else if(object instanceof Bbs) {
					Bbs bbs = (Bbs) object;
					cachedMap.put(bbs.getId(), bbs);
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
					if (value instanceof Bbs) {
						IndexRequestBuilder requestBuilder = GlobalObject.elasticSearchClient.getClient()
								.prepareIndex(Config.es_indexName, DataType.BBS.name(), id).setSource(json);
						bulkRequestBuilder.add(requestBuilder);
						if(i==cachedMap.size()-1) {
							Bbs bbs = (Bbs)value;
							Index index = (Index) GlobalObject.crawlerMongo.find(Index.class, Bbs.class.getSimpleName().toLowerCase());
							if (null == index) {
								index = new Index(Bbs.class.getSimpleName().toLowerCase(), bbs.getTimestamp());
							}
							index.setTimestamp(bbs.getTimestamp());
							GlobalObject.crawlerMongo.saveOrUpdate(index);
						}
					} else if (value instanceof News) {
						IndexRequestBuilder requestBuilder = GlobalObject.elasticSearchClient.getClient()
								.prepareIndex(Config.es_indexName, DataType.News.name(), id).setSource(json);
						bulkRequestBuilder.add(requestBuilder);
						if(i==cachedMap.size()-1) {
							News news = (News)value;
							Index index = (Index) GlobalObject.crawlerMongo.find(Index.class, News.class.getSimpleName().toLowerCase());
							if (null == index) {
								index = new Index(News.class.getSimpleName().toLowerCase(), news.getTimestamp());
							}
							index.setTimestamp(news.getTimestamp());
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
						logger.info(String.format("新闻索引构建成功！批量提交数量：%s 耗时：%s毫秒  总量：%s", numberOfActions, took, amount));
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
		GlobalObject.elasticSearchClient.mapping(Config.es_indexName, DataType.News.name());
		GlobalObject.elasticSearchClient.mapping(Config.es_indexName, DataType.BBS.name());
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("index"));
	}
}
