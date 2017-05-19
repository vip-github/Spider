package serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class RecordEncoder implements Serializer<Record> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, Record record) {
		if(null!=record) {
			return record.ObjectToBytes(record);
		}
		return null;
	}

	@Override
	public void close() {
	}
	
}
