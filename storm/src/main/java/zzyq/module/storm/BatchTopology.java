package zzyq.module.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;

public class BatchTopology {
	public static void main(String[] args) throws Exception {
		TridentTopology topology = new TridentTopology();
		topology.newStream("BatchSpout", new BatchSpout())
		.each(new Fields("data"), new BatchBolt(), new Fields("outdata"));
		
		Config config = new Config();
		config.setDebug(false);
		config.put(Config.TOPOLOGY_DEBUG, false);
		
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("BatchTopology", config, topology.build());
	}
}
