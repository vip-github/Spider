package zzyq.module.storm.spout;

import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.mongodb.morphia.query.Query;

import zzyq.bean.Config;
import zzyq.bean.DataType;
import zzyq.bean.FieldsConstant;
import zzyq.bean.GlobalObject;
import zzyq.bean.Index;
import zzyq.bean.Weixin;

public class MongoWeiXinSpout extends BaseRichSpout {
	private static final long serialVersionUID = -4677444446357082897L;

	private Logger logger = LogManager.getLogger(this.getClass());

	private SpoutOutputCollector collector;

	public void nextTuple() {
		try {
			Index index = (Index) GlobalObject.crawlerMongo.find(Index.class, Weixin.class.getSimpleName().toLowerCase());
			if (null == index) {
				index = new Index(Weixin.class.getSimpleName().toLowerCase(), 0);
			}
			Query<Weixin> query = GlobalObject.crawlerMongo.getDatastore().find(Weixin.class);
			Iterator<Weixin> iterator = query.field(FieldsConstant.timestamp).greaterThan(index.getTimestamp()).iterator();
			while (iterator.hasNext()) {
				Weixin weixin = iterator.next();
				collector.emit(new Values(weixin));
			}
			collector.emit(new Values(Config.batch));
			Utils.sleep(60000);
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
