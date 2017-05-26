package zzyq.module.zk;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper实现分布式锁,目的：锁方案，同一个方案只能由一台机器操作
 */
public class ZooKeeperLock implements Watcher {
	private static Logger logger = LogManager.getLogger(ZooKeeperLock.class);

	private static final String path = "locks";

	private ZooKeeper zooKeeper = null;

	public static void main(String[] args) throws Exception {
		final ZooKeeperLock lock = new ZooKeeperLock();
		lock.getZooKeeper();
		String id = "8888";
		String path = lock.lock(id, 1);
		if (null != path) {
			boolean islock = lock.islock(id);
			System.out.println(String.format("%s锁住状态:%s", Thread.currentThread().getName(), islock));
			boolean unlock = lock.unlock(id);
			System.out.println(String.format("%s解锁状态:%s", Thread.currentThread().getName(), unlock));
		}
		lock.closeZooKeeper();
	}

	/**
	 * 获得zk连接
	 * 
	 * @return
	 */
	private synchronized ZooKeeper getZooKeeper() {
		try {
			if (null == zooKeeper) {
				zooKeeper = new ZooKeeper("localhost:2181", 30000, this);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return zooKeeper;
	}

	/**
	 * 创建分布式锁的根目录
	 */
	private synchronized void createRoot() {
		try {
			if (null == zooKeeper) {
				getZooKeeper();
			}
			if (null == zooKeeper.exists("/".concat(path), true)) {
				String createPath = zooKeeper.create("/".concat(path), null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				logger.info(String.format("创建路径：%s", createPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	/**
	 * 加锁
	 * 
	 * @param id
	 *            方案id
	 * @param maxTaskNumber
	 *            最大的任务量
	 * @return 加锁路径 如果返回null,表示加锁未成功或者任务量已达到最大值
	 */
	public synchronized String lock(String id, int maxTaskNumber) {
		String lockPath = null;
		try {
			createRoot();

			List<String> list = zooKeeper.getChildren("/".concat(path), false);
			if (null != list && !list.isEmpty()) {
				if (list.size() >= maxTaskNumber)
					return lockPath;
			}

			String subPath = "/".concat(path).concat("/".concat(id));
			if (null == zooKeeper.exists(subPath, false)) {
				lockPath = zooKeeper.create(subPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				logger.info(String.format("创建路径：%s", lockPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return lockPath;
	}

	/**
	 * 解锁
	 * 
	 * @param id
	 *            方案id
	 * @return
	 */
	public synchronized boolean unlock(String id) {
		boolean unlock = true;
		try {
			String lockPath = "/".concat(path).concat("/".concat(id));
			if (null != zooKeeper) {
				Stat stat = zooKeeper.exists(lockPath, true);
				if (null != stat) {
					zooKeeper.delete(lockPath, stat.getVersion());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			unlock = false;
		}
		return unlock;
	}

	/**
	 * 检测是否锁住
	 * 
	 * @param id
	 *            方案id
	 * @return
	 */
	public synchronized boolean islock(String id) {
		boolean lock = false;
		try {
			if (null != zooKeeper) {
				String lockPath = "/".concat(path).concat("/".concat(id));
				Stat stat = zooKeeper.exists(lockPath, true);
				if (null != stat) {
					lock = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return lock;
	}

	/**
	 * 关闭连接
	 */
	public synchronized void closeZooKeeper() {
		try {
			if (null != zooKeeper) {
				zooKeeper.close();
				zooKeeper = null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("zk关闭异常！" + e.toString());
		}
	}

	/**
	 * Watcher事件处理
	 */
	public void process(WatchedEvent event) {
		if (event == null) {
			return;
		}
		Event.KeeperState keeperState = event.getState();
		Event.EventType eventType = event.getType();
		if (Event.KeeperState.SyncConnected == keeperState) {
			if (Event.EventType.None == eventType) {
				logger.info("成功连接上zk服务器");
			} else if (event.getType() == Event.EventType.NodeDeleted) {
				logger.info("删除节点");
			}
		} else if (Event.KeeperState.Disconnected == keeperState) {
			logger.info("与zk服务器断开连接");
		} else if (Event.KeeperState.AuthFailed == keeperState) {
			logger.info("权限检查失败");
		} else if (Event.KeeperState.Expired == keeperState) {
			logger.info("会话失效");
		}
	}
}
