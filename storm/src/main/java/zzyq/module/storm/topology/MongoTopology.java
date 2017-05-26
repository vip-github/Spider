package zzyq.module.storm.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;

import zzyq.module.storm.bolt.NewsBuildIndexBolt;
import zzyq.module.storm.bolt.WeiBoBuildIndexBolt;
import zzyq.module.storm.bolt.WeiXinBuildIndexBolt;
import zzyq.module.storm.spout.MongoBbsSpout;
import zzyq.module.storm.spout.MongoNewsSpout;
import zzyq.module.storm.spout.MongoWeiBoSpout;
import zzyq.module.storm.spout.MongoWeiXinSpout;

public class MongoTopology {
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		// 数据源
		builder.setSpout("MongoNewsSpout", new MongoNewsSpout(), 1);
		builder.setSpout("MongoBbsSpout", new MongoBbsSpout(), 1);
		builder.setSpout("MongoWeiBoSpout", new MongoWeiBoSpout(), 1);
		builder.setSpout("MongoWeiXinSpout", new MongoWeiXinSpout(), 1);
		
		// 构建索引
		builder.setBolt("NewsBuildIndexBolt", new NewsBuildIndexBolt(), 1).shuffleGrouping("MongoNewsSpout");
		builder.setBolt("BbsBuildIndexBolt", new NewsBuildIndexBolt(), 1).shuffleGrouping("MongoBbsSpout");
		builder.setBolt("WeiBoBuildIndexBolt", new WeiBoBuildIndexBolt(), 1).shuffleGrouping("MongoWeiBoSpout");
		builder.setBolt("WeiXinBuildIndexBolt", new WeiXinBuildIndexBolt(), 1).shuffleGrouping("MongoWeiXinSpout");

		Config conf = new Config();
		conf.setDebug(false);
		conf.put(Config.TOPOLOGY_DEBUG, false);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(10);
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("MongoTopology", conf, builder.createTopology());
			// Utils.sleep(30000);
			// cluster.killTopology("SpiderTopology");
			// cluster.shutdown();
		}
	}
}
