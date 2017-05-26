package zzyq.module.storm;

import java.util.Map;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;

public class BatchBolt extends BaseFunction {
	private static final long serialVersionUID = 3029437710486077264L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		int data = tuple.getIntegerByField("data");
		System.out.println(data);
		//collector.emit(new Values(data));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		
	}
}
