package zzyq.module.storm.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import serialization.Record;

public class KafkaBolt extends BaseRichBolt {
	private static final long serialVersionUID = 3029437710486077264L;
	private OutputCollector collector;

	public void execute(Tuple tuple) {
		Record record = (Record)tuple.getValueByField("data");
		if(null!=record) {
			System.out.println(String.format("Storm处理消息--->%s", record.getData().get("url")));
		}
		collector.ack(tuple);
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map config, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}
}
