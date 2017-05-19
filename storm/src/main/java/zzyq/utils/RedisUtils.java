package zzyq.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis操作类
 */
public class RedisUtils {
	protected static Logger LOGGER = Logger.getLogger(RedisUtils.class.getName());
	// jedis操作工具
	private static JedisPool jedisPool;

	public RedisUtils() {
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数, 默认8个
		config.setMaxTotal(3000);
		// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
		// 默认-1
		config.setMaxWaitMillis(2000);
		// 设置最大空闲连接数, 默认8个
		config.setMaxIdle(20);
		// 初始化jedisPool,设置IP和端口号
		jedisPool = new JedisPool(config, "localhost", 6379);
	}

	/**
	 * 返回队列第一个元素
	 * 
	 * @param str_key
	 */
	public String lPop(String str_key) {
		Jedis jedis = getRedis();
		try {
			if (null != jedis && jedis.exists(str_key)) {
				return jedis.lpop(str_key);
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return "";
	}

	public void rPush(String str_key, String value) {
		Jedis jedis = getRedis();
		try {
			if (null != jedis) {
				jedis.lpush(str_key, value);
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	private Jedis getRedis() {
		return jedisPool.getResource();
	}
}