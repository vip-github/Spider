package com.spider.utils;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ApplicationConstant;
import com.admin.entity.ChildrenSite;
import com.admin.entity.ParentSite;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbUtils {
	
	public static void main(String[] args) {
		ParentSite parentSite = new ParentSite();
		parentSite.setId("456");
		parentSite.setName("测试");
		parentSite.setComment("无");
		parentSite.setType("测试站点");
		parentSite.setUrl("http://www.baidu.com");
		parentSite.setAddtime(new Timestamp(System.currentTimeMillis()));
		Map<String, String> selectors1 = new LinkedHashMap<>();
		selectors1.put("xpath", "//div[@class='aaa']//a/@href");
		selectors1.put("css", "//div[@class='aaa']//a/@href");
		parentSite.setSelectors(selectors1);
		ChildrenSite site1 = new ChildrenSite();
		site1.setId("333");
		site1.setName("aaa");
		site1.setPid("456");
		site1.setUrl("http://www.hao123.com");
		site1.setAddtime(new Timestamp(System.currentTimeMillis()));
		site1.setComment("无");
		site1.setRuntime(new Timestamp(System.currentTimeMillis()));
		Map<String, String> selectors2 = new LinkedHashMap<>();
		selectors2.put("xpath", "//div[@class='bbb']//a/@href");
		selectors2.put("css", "//div[@class='bbb']//a/@href");
		site1.setSelectors(selectors2);
		ChildrenSite site2 = new ChildrenSite();
		site2.setId("444");
		site2.setName("bbb");
		site2.setPid("456");
		site2.setUrl("http://www.hao456.com");
		site2.setAddtime(new Timestamp(System.currentTimeMillis()));
		site2.setComment("无");
		site2.setRuntime(new Timestamp(System.currentTimeMillis()));
		Map<String, String> selectors3 = new LinkedHashMap<>();
		selectors3.put("xpath", "//div[@class='ccc']//a/@href");
		selectors3.put("css", "//div[@class='ccc']//a/@href");
		site2.setSelectors(selectors3);
		MongodbUtils.getInstance().save(parentSite);
		MongodbUtils.getInstance().save(site1);
		MongodbUtils.getInstance().save(site2);
		
		//MongodbUtils.getInstance().delete(ParentSite.class, "123");
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Morphia morphia = new Morphia();
	private MongoClient mongoClient;
	private Datastore datastore;
	
	private static MongodbUtils instance;
	
	static{
		instance =  new MongodbUtils();
	}
	
	public static MongodbUtils getInstance(){
		return instance;
	}
	
	public Datastore getDatastore(){
		return datastore;
	}
	
	public MongoClient getMongoClient(){
		return mongoClient;
	}
	
	public MongodbUtils(){
		try {
			morphia.mapPackage("com.admin.entity");
			MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
			builder.minConnectionsPerHost(10);
			builder.connectionsPerHost(10);
			builder.connectTimeout(1000 * 60);
			builder.socketTimeout(1000 * 60);
			builder.maxWaitTime(1000 * 60);
			builder.threadsAllowedToBlockForConnectionMultiplier(5);
			builder.maxConnectionIdleTime(1000 * 60 * 60);
			builder.maxConnectionLifeTime(1000 * 60 * 60 * 2);
			mongoClient = new MongoClient(new ServerAddress(new InetSocketAddress(ApplicationConstant.mongo_host, ApplicationConstant.mongo_port)), builder.build());
			datastore = morphia.createDatastore(mongoClient, ApplicationConstant.mongo_dbname);
			datastore.ensureIndexes();
			logger.info("morphia连接池初始化完毕！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 判断url是否已存在
	 * @param url
	 * @return
	 */
	public boolean existsUrl(String url){
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_url_tbname);
		String id = StringUtils.md5(url);
		return collection.count(new Document("_id", id))>0;
	}
	
	/**
	 * 保存url
	 * @param url
	 */
	public void saveUrl(String url){
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_url_tbname);
		String id = StringUtils.md5(url);
		Document doc = new Document("_id", id);
		doc.put("url", url);
		collection.insertOne(doc);
		FindIterable<Document> iterable = collection.find(new Document("_id", id));
		if(null!=iterable && null!=iterable.first()){
			Document old = iterable.first();
			old.putAll(doc);
			collection.updateOne(new Document("_id", id), new Document("$set", old));
		}else{
			collection.insertOne(doc);
		}
	}
	
	/**
	 * 以原始方式保存数据和url
	 * @param document
	 */
	public void save(Document document){
		MongoDatabase database = getDatastore().getMongo().getDatabase(ApplicationConstant.mongo_dbname);
		MongoCollection<Document> collection = database.getCollection(ApplicationConstant.mongo_data_tbname);
		FindIterable<Document> iterable = collection.find(new Document("_id", document.get("_id")));
		if(null!=iterable && null!=iterable.first()){
			Document old = iterable.first();
			old.putAll(document);
			String id = old.getString("_id");
			collection.updateOne(new Document("_id", id), new Document("$set", old));
		}else{
			collection.insertOne(document);
		}
		saveUrl(document.getString("url"));
	}
	
	/**
	 * 以封装成实体的方式保存数据
	 * @param entity
	 */
	public void save(Object entity){
		getDatastore().save(entity);
	}
	
	public void delete(Class<?> entity, String id){
		getDatastore().delete(entity, id);
	}
	
	public Object findById(Class<?> entity, String id){
		Query<?> query = getDatastore().createQuery(entity);
		return query.field("_id").equal(id).get();
	}
	
	public List<?> find(Class<?> entity, int skip, int limit){
		List<Object> result = new ArrayList<>();
		Query<?> query = getDatastore().createQuery(entity);
		Iterator<?> iterator = query.fetch(new FindOptions().limit(limit).skip(skip)).iterator();
		while(iterator.hasNext()){
			result.add(iterator.next());
		}
		return result;
	}
	
	public List<?> find(Class<?> entity, String fieldKey, String fieldValue, int skip, int limit){
		List<Object> result = new ArrayList<>();
		Query<?> query = getDatastore().createQuery(entity);
		Iterator<?> iterator = query.field(fieldKey).equal(fieldValue)
				.fetch(new FindOptions().limit(limit).skip(skip)).iterator();
		while(iterator.hasNext()){
			result.add(iterator.next());
		}
		return result;
	}
	
	public long count(Class<?> entity, String fieldKey, String fieldValue){
		Query<?> query = getDatastore().createQuery(entity);
		return query.field(fieldKey).equal(fieldValue).count();
	}
}
