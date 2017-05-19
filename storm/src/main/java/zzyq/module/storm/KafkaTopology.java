package zzyq.module.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;

public class KafkaTopology {
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder =  new TopologyBuilder();
        builder.setSpout("KafkaSpout", new KafkaSpout(), 1);
        builder.setBolt("KafkaBolt", new KafkaBolt(), 1).shuffleGrouping("KafkaSpout");
         
        Config conf = new Config();
        conf.setDebug(false);
        conf.put(Config.TOPOLOGY_DEBUG, false);
         
        if (args != null && args.length > 0) {
            conf.setNumWorkers(10);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("SpiderTopology", conf, builder.createTopology());
//            Utils.sleep(30000);
//            cluster.killTopology("SpiderTopology");
//            cluster.shutdown();
        }
	}
}
