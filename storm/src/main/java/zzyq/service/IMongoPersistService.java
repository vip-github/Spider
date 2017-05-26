package zzyq.service;

import java.util.Map;

/**
 * mongodb存储接口
 * @date   2017年5月24日 上午9:42:30
 */
public interface IMongoPersistService {
	/**
	 * 保存微博
	 * @param jsonMap
	 * @return
	 */
	public Map<Integer, Object> saveWeiBo(Map<String, Object> jsonMap);
	
	/**
	 * 保存新闻
	 * @param jsonMap
	 * @return
	 */
	public Map<Integer, Object> saveNews(Map<String, Object> jsonMap);
	
	/**
	 * 保存论坛
	 * @param jsonMap
	 * @return
	 */
	public Map<Integer, Object> saveBBS(Map<String, Object> jsonMap);
	
	/**
	 * 保存微信
	 * @param jsonMap
	 * @return
	 */
	public Map<Integer, Object> saveWeiXin(Map<String, Object> jsonMap);
}
