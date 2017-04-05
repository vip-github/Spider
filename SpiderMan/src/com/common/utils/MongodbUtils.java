package com.common.utils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.constants.ApplicationConstant;
import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import us.codecraft.webmagic.utils.UrlUtils;

public class MongodbUtils
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Morphia morphia = new Morphia();
	private MongoClient mongoClient;
	private Datastore datastore;

	private static MongodbUtils instance;

	static
	{
		instance = new MongodbUtils();
	}

	public static MongodbUtils getInstance()
	{
		return instance;
	}

	public Datastore getDatastore()
	{
		return datastore;
	}

	public MongoClient getMongoClient()
	{
		return mongoClient;
	}

	public MongodbUtils()
	{
		try
		{
			morphia.mapPackage("com.admin.entity");
			MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
			builder.minConnectionsPerHost(10);
			builder.connectionsPerHost(50);
			builder.connectTimeout(1000 * 60);
			builder.socketTimeout(1000 * 60);
			builder.maxWaitTime(1000 * 60);
			builder.threadsAllowedToBlockForConnectionMultiplier(2);
			builder.maxConnectionIdleTime(1000 * 60 * 60 * 6);
			builder.maxConnectionLifeTime(1000 * 60 * 60 * 12);
			mongoClient = new MongoClient(new ServerAddress(new InetSocketAddress(ApplicationConstant.mongo_host, ApplicationConstant.mongo_port)), builder.build());
			datastore = morphia.createDatastore(mongoClient, ApplicationConstant.mongo_dbname);
			datastore.ensureIndexes();
			logger.info("morphia连接池初始化完毕！");
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 判断url是否已存在
	 * 
	 * @param url
	 * @return
	 */
	public boolean existsUrl(String url)
	{
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_url_tbname);
		String id = StringUtils.md5(url);
		return collection.count(new Document("_id", id)) > 0;
	}

	/**
	 * 保存url
	 * 
	 * @param url
	 */
	public void saveUrl(String url)
	{
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_url_tbname);
		String id = StringUtils.md5(url);
		Document doc = new Document("_id", id);
		doc.put("domain", UrlUtils.getDomain(url));
		doc.put("url", url);
		collection.insertOne(doc);
		FindIterable<Document> iterable = collection.find(new Document("_id", id));
		if (null != iterable && null != iterable.first())
		{
			Document old = iterable.first();
			old.putAll(doc);
			collection.updateOne(new Document("_id", id), new Document("$set", old));
		} else
		{
			collection.insertOne(doc);
		}
	}

	/**
	 * 以原始方式保存数据和url
	 * 
	 * @param document
	 */
	public void save(Document document)
	{
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_data_tbname);
		FindIterable<Document> iterable = collection.find(new Document("_id", document.get("_id")));
		if (null != iterable && null != iterable.first())
		{
			Document old = iterable.first();
			old.putAll(document);
			String id = old.getString("_id");
			collection.updateOne(new Document("_id", id), new Document("$set", old));
		} else
		{
			collection.insertOne(document);
		}
		saveUrl(document.getString("url"));
	}

	/**
	 * 查询状态为0的Document
	 */
	public List<Document> queryImages(String domain, int limit)
	{
		List<Document> list = new ArrayList<>();
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_data_tbname);
		Document condition = new Document();
		condition.put("status", 0);
		if (!Strings.isNullOrEmpty(domain))
		{
			condition.put("domain", domain);
		}
		MongoCursor<Document> cursor = collection.find(condition).limit(limit).iterator();
		while (cursor.hasNext())
		{
			list.add(cursor.next());
		}
		return list;
	}

	/**
	 * 查询状态为0的Document余下数量
	 * 
	 * @param domain
	 * @return
	 */
	public long countImages(String domain)
	{
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_data_tbname);
		Document condition = new Document();
		condition.put("status", 0);
		if (!Strings.isNullOrEmpty(domain))
		{
			condition.put("domain", domain);
		}
		return collection.count(condition);
	}

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @param status
	 */
	public void updateStatus(String id, int status)
	{
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_data_tbname);
		collection.updateOne(new Document("_id", id), new Document("$set", new Document("status", status)));
	}

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @param status
	 */
	public void updateStatus(int status)
	{
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_data_tbname);
		MongoCursor<Document> cursor = collection.find().iterator();
		while (cursor.hasNext())
		{
			Document doc = cursor.next();
			String id = doc.getString("_id");
			collection.updateOne(new Document("_id", id), new Document("$set", new Document("status", status)));
		}
	}

	/**
	 * 查询图片的二进制的md5值是否存在
	 * 
	 * @param bytes
	 * @return
	 */
	public boolean existsImageBinary(byte[] bytes)
	{
		boolean exists = false;
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_image_tbname);
		String id = StringUtils.md5(new String(bytes));
		if (null != collection.find(new Document("_id", id)).first())
		{
			exists = true;
		}
		return exists;
	}

	/**
	 * 保存图片的二进制md5值
	 * 
	 * @param bytes
	 */
	public void saveImageBinary(String domain, byte[] bytes, String src)
	{
		try
		{
			MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
			MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_image_tbname);
			String id = StringUtils.md5(new String(bytes));
			Document doc = new Document();
			doc.put("_id", id);
			if (null == collection.find(doc).first())
			{
				doc.put("domain", domain);
				doc.put("src", src);
				collection.insertOne(doc);
			}
		} catch (Exception e)
		{
			if (!e.getMessage().contains("E11000 duplicate key error"))
			{
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * 以封装成实体的方式保存数据
	 * 
	 * @param entity
	 */
	public void save(Object entity)
	{
		getDatastore().save(entity);
	}

	public boolean delete(Class<?> entity, String id)
	{
		WriteResult result = getDatastore().delete(entity, id);
		if (result.getN() > 0)
		{
			return true;
		} else
		{
			return false;
		}
	}

	public Object findById(Class<?> entity, String id)
	{
		Query<?> query = getDatastore().createQuery(entity);
		return query.field("_id").equal(id).get();
	}

	public List<?> find(Class<?> entity, int skip, int limit)
	{
		List<Object> result = new ArrayList<>();
		Query<?> query = getDatastore().createQuery(entity);
		Iterator<?> iterator = query.fetch(new FindOptions().limit(limit).skip(skip)).iterator();
		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}
		return result;
	}

	public List<?> find(Class<?> entity, String fieldKey, String fieldValue, int skip, int limit)
	{
		List<Object> result = new ArrayList<>();
		Query<?> query = getDatastore().createQuery(entity);
		Iterator<?> iterator = query.field(fieldKey).equal(fieldValue).fetch(new FindOptions().limit(limit).skip(skip))
				.iterator();
		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}
		return result;
	}

	public List<?> find(Class<?> entity, String fieldKey, String fieldValue)
	{
		List<Object> result = new ArrayList<>();
		Query<?> query = getDatastore().createQuery(entity);
		Iterator<?> iterator = query.field(fieldKey).equal(fieldValue).fetch(new FindOptions()).iterator();
		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}
		return result;
	}

	public long count(Class<?> entity, String fieldKey, String fieldValue)
	{
		Query<?> query = getDatastore().createQuery(entity);
		return query.field(fieldKey).equal(fieldValue).count();
	}
}
