package com.spider.core;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serialization.Record;
import serialization.RecordEncoder;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class KafkaPipeline implements Pipeline {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private static KafkaProducer<String, Record> producer;
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		try {
			if (null == producer) {
				init();
			}
			if (null != resultItems && null != resultItems.getAll() && !resultItems.getAll().isEmpty()) {
				Map<String, Object> data = resultItems.getAll();
				if (data.containsKey("url")) {
					String url = StringUtils.strip((String) data.get("url"));
					if (StringUtils.isNotBlank(url)) {
						String md5 = com.common.utils.StringUtils.md5(url);
						Record dataRecord = new Record(data);
						ProducerRecord<String, Record> record = new ProducerRecord<String, Record>("Spider", md5, dataRecord);
						producer.send(record);
					}
				} else {
					logger.warn(String.format("未找到url字段！集合：%s", data));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	private void init() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		// The "all" setting we have specified will result in blocking on the
		// full commit of the record, the slowest but most durable setting.
		// “所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
		props.put("acks", "all");
		// 如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
		props.put("retries", 0);

		// The producer maintains buffers of unsent records for each partition.
		props.put("batch.size", 16384);
		// 默认立即发送，这里这是延时毫秒数
		props.put("linger.ms", 1);
		// 生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
		props.put("buffer.memory", 33554432);
		// The key.serializer and value.serializer instruct how to turn the key
		// and value objects the user provides with their ProducerRecord into
		// bytes.
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", RecordEncoder.class.getName());

		producer = new KafkaProducer<String, Record>(props);
	}
}
