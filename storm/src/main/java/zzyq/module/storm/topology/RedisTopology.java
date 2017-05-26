package zzyq.module.storm.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import zzyq.module.storm.bolt.NewsBuildIndexBolt;
import zzyq.module.storm.bolt.RedisNewsBolt;
import zzyq.module.storm.bolt.RedisWeiBoBolt;
import zzyq.module.storm.bolt.RedisWeiXinBolt;
import zzyq.module.storm.bolt.WeiBoBuildIndexBolt;
import zzyq.module.storm.bolt.WeiXinBuildIndexBolt;
import zzyq.module.storm.spout.RedisNewsSpout;
import zzyq.module.storm.spout.RedisWeiBoSpout;
import zzyq.module.storm.spout.RedisWeiXinSpout;

public class RedisTopology {
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		// 数据源
		builder.setSpout("RedisNewsSpout", new RedisNewsSpout(), 1);
		builder.setSpout("RedisWeiBoSpout", new RedisWeiBoSpout(), 1);
		builder.setSpout("RedisWeiXinSpout", new RedisWeiXinSpout(), 1);
		// 数据处理和存储
		builder.setBolt("RedisNewsBolt", new RedisNewsBolt(), 1).shuffleGrouping("RedisNewsSpout");
		builder.setBolt("RedisWeiBoBolt", new RedisWeiBoBolt(), 1).shuffleGrouping("RedisWeiBoSpout");
		builder.setBolt("RedisWeiXinBolt", new RedisWeiXinBolt(), 1).shuffleGrouping("RedisWeiXinSpout");
		// 构建索引
		builder.setBolt("NewsBuildIndexBolt", new NewsBuildIndexBolt(), 1).shuffleGrouping("RedisNewsBolt");
		builder.setBolt("WeiBoBuildIndexBolt", new WeiBoBuildIndexBolt(), 1).shuffleGrouping("RedisWeiBoBolt");
		builder.setBolt("WeiXinBuildIndexBolt", new WeiXinBuildIndexBolt(), 1).shuffleGrouping("RedisWeiXinBolt");
		
		Config config = new Config();
		config.setDebug(false);
		config.put(Config.TOPOLOGY_DEBUG, false);
		
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("RedisTopology", config, builder.createTopology());
	}
}
