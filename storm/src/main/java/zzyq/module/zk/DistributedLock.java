package zzyq.module.zk;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * zookeeper实现分布式锁
 */
public class DistributedLock implements Watcher
{
	private static Logger logger = LogManager.getLogger(DistributedLock.class);

	private static final String path = "locks";

	private static final String subPath = "lock";

	private static ZooKeeper zooKeeper = null;

	public static void main(String[] args) throws Exception
	{
		final DistributedLock lock = new DistributedLock();
		lock.getZooKeeper();
		lock.lock();
		lock.close();
	}

	private synchronized ZooKeeper getZooKeeper()
	{
		try
		{
			if (null == zooKeeper)
			{
				zooKeeper = new ZooKeeper("localhost:2181", 30000, this);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return zooKeeper;
	}

	private synchronized boolean checkMin(String checkPath)
	{
		boolean min = false;
		try
		{
			List<String> nodes = zooKeeper.getChildren("/" + path, false);
			if (null != nodes && !nodes.isEmpty())
			{
				Collections.sort(nodes);
				if (!nodes.get(0).equals(checkPath))
				{
					min = true;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return min;
	}

	private synchronized void createLockRoot(ZooKeeper zooKeeper) throws Exception
	{
		if (null == zooKeeper.exists("/" + path, true))
		{
			String createPath = zooKeeper.create("/" + path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info(String.format("创建路径：%s", createPath));
		}
	}

	private synchronized boolean lock()
	{
		boolean getLock = false;
		try
		{
			createLockRoot(zooKeeper);
			String createPath = zooKeeper.create("/" + path + "/" + subPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			logger.info(String.format("创建路径：%s", createPath));
			if (checkMin(createPath))
			{
				getLock = true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return getLock;
	}

	private synchronized void unlock()
	{

	}

	private synchronized void islock()
	{

	}

	private synchronized void close()
	{
		try
		{
			if (null != zooKeeper)
			{
				zooKeeper.close();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			logger.error("zk关闭异常！" + e.toString());
		}
	}

	public void process(WatchedEvent event)
	{
		if (event == null)
		{
			return;
		}
		Event.KeeperState keeperState = event.getState();
		Event.EventType eventType = event.getType();
		if (Event.KeeperState.SyncConnected == keeperState)
		{
			if (Event.EventType.None == eventType)
			{
				logger.info("成功连接上ZK服务器");
			} else if (event.getType() == Event.EventType.NodeDeleted)
			{
				logger.info("删除节点");
			}
		} else if (Event.KeeperState.Disconnected == keeperState)
		{
			logger.info("与ZK服务器断开连接");
		} else if (Event.KeeperState.AuthFailed == keeperState)
		{
			logger.info("权限检查失败");
		} else if (Event.KeeperState.Expired == keeperState)
		{
			logger.info("会话失效");
		}
	}
}
