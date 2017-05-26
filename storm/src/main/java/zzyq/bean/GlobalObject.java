package zzyq.bean;

import zzyq.service.IMongoPersistService;
import zzyq.service.MongoPersistService;
import zzyq.utils.MongoUtils;
import zzyq.utils.ElasticSearchClient;

/**
 * 全局对象
 * @date   2017年5月24日 上午11:16:43
 */
public class GlobalObject {
	/**
	 * 爬虫数据存储库操作
	 */
	public final static MongoUtils crawlerMongo = MongoUtils.getInstance(Config.mongodb_crawler_name);
	/**
	 * 存储业务接口
	 */
	public final static IMongoPersistService persistService = new MongoPersistService();
	/**
	 * es连接客户端
	 */
	public final static ElasticSearchClient elasticSearchClient = ElasticSearchClient.getInstance();
}
