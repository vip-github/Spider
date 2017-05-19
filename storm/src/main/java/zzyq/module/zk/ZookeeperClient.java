package zzyq.module.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class ZookeeperClient {

	public static void main(String[] args) throws Exception {
		String address = "localhost:2181";
		CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(1000, 3));
		client.start();
		/*client.create().creatingParentsIfNeeded()
		.withMode(CreateMode.PERSISTENT)
		.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
		.forPath("/TestData", "测试数据".getBytes());*/
		
		//System.out.println(new String(client.getData().forPath("/TestData")));
		client.delete().deletingChildrenIfNeeded().forPath("/TestData");
		
		Stat stat = client.checkExists().forPath("/TestData");
		if(stat==null){
		   System.out.println("/TestData不存在");
		}else {
			 System.out.println("/TestData存在");
		}
		
		client.close();
	}
}
