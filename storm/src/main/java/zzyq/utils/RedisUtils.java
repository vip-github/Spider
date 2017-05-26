package zzyq.utils;

import java.io.Serializable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import zzyq.bean.Config;

/**
 * redis操作类
 */
public class RedisUtils implements Serializable{
	private static final long serialVersionUID = -2445163464928125043L;
	protected static Logger LOGGER = Logger.getLogger(RedisUtils.class.getName());
	// jedis操作工具
	private static JedisPool jedisPool;
	
	private static RedisUtils instance = null;

	private RedisUtils() {
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数, 默认8个
		config.setMaxTotal(3000);
		// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
		// 默认-1
		config.setMaxWaitMillis(2000);
		// 设置最大空闲连接数, 默认8个
		config.setMaxIdle(20);
		// 初始化jedisPool,设置IP和端口号
		jedisPool = new JedisPool(config, Config.config_redis_ip, Config.config_redis_port);
	}
	
	public static RedisUtils getInstance(){
		if(null==instance) instance = new RedisUtils();
		return instance;
	}

	/**
	 * 返回队列第一个元素
	 * 
	 * @param key
	 */
	public String lpop(String key) {
		Jedis jedis = getRedis();
		try {
			if (null != jedis && jedis.exists(key)) {
				return jedis.lpop(key);
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

	public void lpush(String key, String value) {
		Jedis jedis = getRedis();
		try {
			if (null != jedis) {
				jedis.lpush(key, value);
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