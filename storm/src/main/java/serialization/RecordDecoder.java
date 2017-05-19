package serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

public class RecordDecoder implements Deserializer<Record> {

	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	public Record deserialize(String topic, byte[] data) {
		if(null!=data && data.length>0) {
			return new Record().BytesToObject(data);
		}
		return null;
	}

	public void close() {
	}
	
}
