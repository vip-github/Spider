package zzyq.module.storm;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import serialization.Record;
import serialization.RecordDecoder;

public class KafkaSpout extends BaseRichSpout {
	private static final long serialVersionUID = -4677444446357082897L;

	private SpoutOutputCollector collector;

	private KafkaConsumer<String, Record> consumer;

	public void nextTuple() {
		ConsumerRecords<String, Record> records = consumer.poll(100);
		for (ConsumerRecord<String, Record> record : records) {
			// 正常这里应该使用线程池处理，不应该在这里处理
			collector.emit(new Values(record.value()));
		}
	}

	@SuppressWarnings("rawtypes")
	public void open(Map config, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		if (null == consumer) {
			Properties props = new Properties();

			props.put("bootstrap.servers", "localhost:9092");
			// 消费者的组id
			props.put("group.id", "Spider");

			props.put("enable.auto.commit", "true");
			props.put("auto.commit.interval.ms", "1000");

			// 从poll(拉)的回话处理时长
			props.put("session.timeout.ms", "30000");
			// poll的数量限制
			// props.put("max.poll.records", "100");

			props.put("key.deserializer", StringDeserializer.class.getName());
			props.put("value.deserializer", RecordDecoder.class.getName());

			consumer = new KafkaConsumer<String, Record>(props);
			// 订阅主题列表topic
			consumer.subscribe(Arrays.asList("Spider"));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}

	@Override
	public void close() {
		consumer.close();
		super.close();
	}
	
	@Override
	public void fail(Object msgId) {
		super.fail(msgId);
	}

}
