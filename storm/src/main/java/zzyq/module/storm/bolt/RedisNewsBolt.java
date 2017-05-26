package zzyq.module.storm.bolt;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.alibaba.fastjson.JSON;

import zzyq.bean.Config;
import zzyq.bean.DataServiceConstants;
import zzyq.bean.DataType;
import zzyq.bean.FieldsConstant;
import zzyq.bean.GlobalObject;

public class RedisNewsBolt extends BaseRichBolt {
	private static final long serialVersionUID = 3029437710486077264L;
	private Logger logger = LogManager.getLogger(this.getClass());
	private OutputCollector collector;
	private AtomicLong success = new AtomicLong();
	private AtomicLong error = new AtomicLong();

	@SuppressWarnings("unchecked")
	public void execute(Tuple tuple) {
		String data = "";
		try {
			data = tuple.getStringByField(DataType.News.name().toLowerCase());
			if(StringUtils.isNotBlank(data)) {
				if (data.equalsIgnoreCase(Config.batch)) {
					collector.emit(new Values(Config.batch));
				} else {
					Map<String, Object> jsonMap = (Map<String, Object>) JSON.parseObject(data, Map.class);
					if (null != jsonMap && !jsonMap.isEmpty()) {
						if (jsonMap.containsKey(FieldsConstant.seedType)) {
							Map<Integer, Object> result = save(jsonMap);
							if (result.containsKey(DataServiceConstants.success) && result.size() == 1) {
								collector.emit(new Values(result.values().toArray()[0]));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("处理新闻数据异常：%s%n原始数据：%s%n", e.toString(), data));
		}
	}

	/**
	 * 持久化数据
	 * 
	 * @param jsonMap
	 */
	private synchronized Map<Integer, Object> save(Map<String, Object> jsonMap) {
		String seedType = (String) jsonMap.get(FieldsConstant.seedType);
		if (seedType.equalsIgnoreCase(DataType.BBS.name()) || seedType.equalsIgnoreCase(DataType.QandA.name())) {
			Map<Integer, Object> result = GlobalObject.persistService.saveBBS(jsonMap);
			if (!result.containsKey(DataServiceConstants.success)) {
				logger.warn(String.format("论坛持久化失败！错误编码：%s 原始数据：%s", result.keySet(), jsonMap));
				error.incrementAndGet();
			} else {
				success.incrementAndGet();
			}
			if(success.get()%1000==0) {
				logger.info(String.format("BBS类型入库成功：%s 失败：%s", success.get(), error.get()));
			}
			return result;
		} else {
			Map<Integer, Object> result = GlobalObject.persistService.saveNews(jsonMap);
			if (!result.containsKey(DataServiceConstants.success)) {
				logger.warn(String.format("新闻持久化失败！错误编码：%s 原始数据：%s", result.keySet(), jsonMap));
				error.incrementAndGet();
			} else {
				success.incrementAndGet();
			}
			if(success.get()%1000==0) {
				logger.info(String.format("News类型入库成功：%s 失败：%s", success.get(), error.get()));
			}
			return result;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(DataType.News.name().toLowerCase()));
	}
}
