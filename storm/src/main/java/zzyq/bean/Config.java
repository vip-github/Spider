package zzyq.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static Logger logger = LogManager.getLogger(Config.class);
	
	public static final int batchSize = 1000;//批量提交任务量
	
	public static final String batch = "batch";
	
	public static final long millisecond = 300000;
	
	/**
	 * redis配置
	 */
	public static String config_redis_ip = "localhost";
	public static int config_redis_port = 6379;
	public static String redis_queue_news = "NewsList";//redis中新闻类型的key
	public static String redis_queue_weixin = "WeiXinList";//redis中微信类型的key
	public static String redis_queue_weibo = "MicroList";//redis中微博类型的key
	
	/**
	 * mongo配置
	 */
	public static String mongodb_host_list = "localhost:27017";
	public static String mongodb_crawler_name = "db_crawler";
	public static String mongodb_distribute_name = "db_zzyq";
	
	/**
	 * es配置
	 */
	public static String es_hostList = "localhost:9300";
	public static String es_clusterName = "elasticsearch";
	public static String es_indexName = "articles";
	
	static {
		try {
			Properties properties = new Properties();
			FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir").concat("/configs/config.properties")));
			properties.load(fis);
			String[] redis = properties.getProperty("redis", "localhost:6379").split(":");
			config_redis_ip = redis[0];
			config_redis_port = Integer.parseInt(redis[1]);
			String mongo_hostlist = properties.getProperty("mongo_hostlist");
			if(StringUtils.isNotBlank(mongo_hostlist)) {
				mongodb_host_list = mongo_hostlist;
			}
			String mongo_db_crawler_name = properties.getProperty("mongo_db_crawler_name");
			if(StringUtils.isNotBlank(mongo_db_crawler_name)) {
				mongodb_crawler_name = mongo_db_crawler_name;
			}
			String mongo_db_distribute_name = properties.getProperty("mongo_db_distribute_name");
			if(StringUtils.isNotBlank(mongo_db_distribute_name)) {
				mongodb_distribute_name = mongo_db_distribute_name;
			}
			String eshostlist = properties.getProperty("es_hostList");
			if(StringUtils.isNotBlank(eshostlist)) {
				es_hostList = eshostlist;
			}
			String esClusterName = properties.getProperty("es_clusterName");
			if(StringUtils.isNotBlank(esClusterName)) {
				es_clusterName = esClusterName;
			}
			String esIndexName = properties.getProperty("es_indexName");
			if(StringUtils.isNotBlank(esIndexName)) {
				es_indexName = esIndexName;
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
}
