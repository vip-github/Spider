package zzyq.module.storm;

import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.spout.IBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

public class BatchSpout implements IBatchSpout {
	private static final long serialVersionUID = -4677444446357082897L;

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context) {
	}

	@Override
	public void emitBatch(long batchId, TridentCollector collector) {
		for (int i=0; i<1000; i++) {
			collector.emit(new Values(i));
		}
		Utils.sleep(10000);
	}

	@Override
	public void ack(long batchId) {
		System.out.println(batchId+"处理完成");
	}

	@Override
	public void close() {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("data");
	}
}
