package com.spider.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫程序入口
 * @author czq
 * @date   2017年2月17日 上午10:01:49
 */
public abstract class SpiderMan {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public final void run(){
		begin();
		long start = System.currentTimeMillis();
		work();
		long stop = System.currentTimeMillis();
		finish();
		logger.info(String.format("耗时：%s秒", (stop-start)/1000));
	}
	
	public void begin(){
		logger.info("Spider启动！");
	}
	
	public void finish(){
		logger.info("Spider停止！");
	}
	
	public abstract void work();
}
