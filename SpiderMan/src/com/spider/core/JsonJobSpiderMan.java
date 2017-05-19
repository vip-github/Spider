package com.spider.core;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.spider.bean.Header;
import com.spider.bean.Job;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

/**
 * json任务格式的爬虫
 * @author czq
 * @date 2017年2月17日 上午10:02:06
 */
public class JsonJobSpiderMan extends SpiderMan {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	JsonJobPageProcessor processor = new JsonJobPageProcessor();
	MongodbPipeline mongodbPipeline = new MongodbPipeline();
	KafkaPipeline kafkaPipeline = new KafkaPipeline();
	ParseJob parseJob = new ParseJob();
	private String configFilePath;
	
	public JsonJobSpiderMan(String configFilePath){
		this.configFilePath = configFilePath;
	}
	
	@Override
	public void work() {
		try {
			File file = new File(configFilePath);
			Job job = parseJob.parseJsonJob(FileUtils.readFileToString(file));
			if (null != job) {
				logger.info(job.toString());
				processor.setJob(job);
				Site site = processor.getSite();
				Header header = job.getHeader();
				if (null != header) {
					if (!Strings.isNullOrEmpty(header.getCookie())) {
						site.addHeader("Cookie", header.getCookie());
					}
					if (!Strings.isNullOrEmpty(header.getRetry())) {
						int retry = Integer.parseInt(header.getRetry());
						site.setRetryTimes(retry);
						site.setCycleRetryTimes(retry);
					}
					if (!Strings.isNullOrEmpty(header.getSleep())) {
						int sleep = Integer.parseInt(header.getSleep());
						site.setSleepTime(sleep);
						site.setRetrySleepTime(sleep);
					}
					if (!Strings.isNullOrEmpty(header.getTimeout())) {
						site.setTimeOut(Integer.parseInt(header.getTimeout()));
					}
					String[] urls = job.getLink().getUrl();
					if(null!=urls && urls.length>0){
						Spider.create(processor).addUrl(urls).addPipeline(kafkaPipeline).thread(Integer.parseInt(header.getThread())).run();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
