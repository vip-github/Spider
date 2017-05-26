package zzyq.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

import zzyq.bean.Config;

/**
 * 爬虫库mongodb帮助类
 * @author czq
 * @date   2017年1月22日 下午2:00:59
 */
public class MongoUtils
{
	private Logger logger = LoggerFactory.getLogger(MongoUtils.class);

	private static Map<String, MongoUtils> instance = null;
	
	private Datastore datastore;
	
	private MongoClient mongoClient;
	
	private MapSplitter mapSplitter = Splitter.onPattern(",|，").omitEmptyStrings().trimResults().withKeyValueSeparator(":");
	
	private Morphia morphia = new Morphia();
	
	private String dbName = null;
	
	private MongoUtils(String dbName)
	{
		try
		{
			this.dbName = dbName;
			morphia.mapPackage("zzyq.bean");
			Map<String, String> serverMap = mapSplitter.split(Config.mongodb_host_list);
			List<ServerAddress> serverList = new ArrayList<>();
			if(null!=serverMap && !serverMap.isEmpty())
			{
				for (Entry<String, String> entry : serverMap.entrySet())
				{
					serverList.add(new ServerAddress(entry.getKey(), Integer.parseInt(entry.getValue())));
				}
			}
			if(serverList.size()>0 && StringUtils.isNotBlank(dbName))
			{
				MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
				builder.minConnectionsPerHost(50);
				builder.connectionsPerHost(100);
				builder.connectTimeout(1000*60*3);
				builder.socketTimeout(1000*60*3);
				builder.maxWaitTime(1000*60*3);
				builder.threadsAllowedToBlockForConnectionMultiplier(3);
				builder.maxConnectionIdleTime(1000*60*60);
				builder.maxConnectionLifeTime(1000*60*60*3);
				mongoClient = new MongoClient(serverList, builder.build());
				datastore = morphia.createDatastore(mongoClient, StringUtils.strip(dbName));
				datastore.ensureIndexes();
				logger.info("morphia连接池初始化完毕！");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public static MongoUtils getInstance(String dbName)
	{
		if(null==instance){
			instance = Collections.synchronizedMap(new HashMap<>());
		}
		if(!instance.containsKey(dbName)) {
			instance.put(dbName, new MongoUtils(dbName));
		}
		return instance.get(dbName);
	}
	
	public Datastore getDatastore()
	{
		return MongoUtils.getInstance(this.dbName).datastore;
	}
	
	public MongoClient getMongoClient()
	{
		return MongoUtils.getInstance(this.dbName).mongoClient;
	}
	
	/**
	 * 保存数据   单条
	 * 会根据Id字段判断，如果存在，就更新，如果不存在，就插入
	 * @param object
	 */
	public synchronized boolean saveOrUpdate(Object object)
	{
		boolean result = false;
		try
		{
			Key<Object> key = getDatastore().save(object);
			if(null!=key) result = true;
		} catch (Exception e)
		{
			if (!e.getMessage().contains("E11000 duplicate key error collection")) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 保存数据   多条
	 * 会根据Id字段判断，如果存在，就更新，如果不存在，就插入
	 * @param objects
	 */
	public synchronized boolean saveOrUpdate(List<Object> objects)
	{
		boolean result = false;
		try
		{
			Iterable<Key<Object>> iterable = getDatastore().save(objects);
			if(null!=iterable) result = true;
		} catch (Exception e)
		{
			if (!e.getMessage().contains("E11000 duplicate key error collection")) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 根据_id删除一条记录
	 * @param collection
	 * @param id
	 * @return
	 */
	public synchronized boolean delete(Class<?> collection, String id)
	{
		boolean result = false;
		try
		{
			WriteResult writeResult = getDatastore().delete(collection, id);
			result = writeResult.getN()>0;
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据_id查询一条记录
	 * @param collection
	 * @param id
	 * @return
	 */
	public synchronized Object find(Class<?> collection, String id)
	{
		Object result = null;
		try
		{
			Query<?> query = getDatastore().createQuery(collection);
			query.criteria("_id").equal(id);
			result = query.get();
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据id判断记录是否存在
	 * @param collection
	 * @param id
	 * @return
	 */
	public synchronized boolean exists(Class<?> collection, String id)
	{
		boolean result = false;
		try
		{
			Query<?> query = getDatastore().createQuery(collection);
			query.criteria("_id").equal(id);
			result = query.count()>0;
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
}
