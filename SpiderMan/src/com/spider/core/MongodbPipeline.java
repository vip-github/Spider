package com.spider.core;

import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.utils.MongodbUtils;
import com.common.utils.StringUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MongodbPipeline implements Pipeline {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	MongodbUtils client = MongodbUtils.getInstance();
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		if (null != resultItems && null != resultItems.getAll() && resultItems.getAll().size() > 0) {
			Map<String, Object> data = resultItems.getAll();
			if(data.containsKey("url")){
				data.put("_id", StringUtils.md5((String)data.get("url")));
				client.save(new Document(data));
			}else{
				logger.warn(String.format("未找到url字段！集合：%s", data));
			}
		}
	}

}
