package zzyq.module.storm.spout;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import zzyq.bean.Config;
import zzyq.bean.DataType;
import zzyq.utils.RedisUtils;

public class RedisWeiXinSpout extends BaseRichSpout {
	private static final long serialVersionUID = -4677444446357082897L;

	private Logger logger = LogManager.getLogger(this.getClass());

	private SpoutOutputCollector collector;

	private RedisUtils redisUtils = RedisUtils.getInstance();

	public void nextTuple() {
		try {
			String json = redisUtils.lpop(Config.redis_queue_weixin);
			if (StringUtils.isNotBlank(json)) {
				collector.emit(new Values(json));
			} else {
				collector.emit(new Values(Config.batch));
				Utils.sleep(60000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("取出微信数据异常：" + e.toString());
		}
	}

	@SuppressWarnings("rawtypes")
	public void open(Map config, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(DataType.WeiXin.name().toLowerCase()));
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public void fail(Object msgId) {
		super.fail(msgId);
	}

}
