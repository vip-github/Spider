package com.spider.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.utils.DateUtils;
import com.common.utils.MongodbUtils;
import com.spider.bean.Job;
import com.spider.formatter.DateFormatter;
import com.spider.utils.SelectableUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

public class JsonJobPageProcessor implements PageProcessor {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public Site site = Site.me()
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
			.addHeader("Accept", "*/*").addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
			.addHeader("Accept-Encoding", "gzip").addHeader("Connection", "close");

	private Job job;

	DateFormatter dateFormatter = new DateFormatter();
	
	MongodbUtils mongo = MongodbUtils.getInstance();
	
	private List<com.spider.bean.Page> getPageByPriority(long prioriy) {
		List<com.spider.bean.Page> list = new ArrayList<>();
		if (null != job && null != job.getPage()) {
			for (com.spider.bean.Page p : job.getPage()) {
				if (p.getPriority() == prioriy) {
					list.add(p);
				}
			}
		}
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void process(Page page) {
		String baseUrl = page.getUrl().get();
		long priority = page.getRequest().getPriority();
		Html html = page.getHtml();
		List<com.spider.bean.Page> PAGES = getPageByPriority(priority);
		if (PAGES.size() > 0) {
			for (com.spider.bean.Page PAGE : PAGES) {
				String PAGE_TYPE = PAGE.getType();
				Map<String, String> selectors = PAGE.getSelector();
				if (null != selectors && selectors.size() > 0) {
					if (PAGE_TYPE.equalsIgnoreCase("next")) {
						Selectable selectable = SelectableUtils.select(html, selectors);
						if (null != selectable) {
							List<String> urls = selectable.all();
							if (null != urls && urls.size() > 0) {
								page.addTargetRequests(urls, priority);
							}
						}
					} else if (PAGE_TYPE.equalsIgnoreCase("sub")) {
						Selectable selectable = SelectableUtils.select(html, selectors);
						if (null != selectable) {
							List<String> urls = selectable.all();
							if (null != urls && urls.size() > 0) {
								for (String url : urls) {
									if(StringUtils.isNotBlank(url)){
										url = UrlUtils.canonicalizeUrl(url, baseUrl);
										if(!mongo.existsUrl(url)){
											Request request = new Request(url);
											request.setPriority(priority + 1);
											page.addTargetRequest(request);
										}else{
											logger.info(String.format("%s 已经采集过了！", url));
										}
									}
								}
							}
						}
					}
				} else {
					if (PAGE_TYPE.equalsIgnoreCase("detail")) {
						List<Map<String, Object>> fields = PAGE.getField();
						if (null != fields && fields.size() > 0) {
							String url = page.getUrl().get();
							page.putField("domain", UrlUtils.getDomain(url));
							page.putField("url", StringUtils.strip(url));
							page.putField("addTime", DateUtils.current());
							page.putField("status", 0);
							for (Map<String, Object> fieldMap : fields) {
								String fieldKey = (String) fieldMap.get("key");
								boolean valueMulti = false;
								if (fieldMap.containsKey("valueMulti")) {
									valueMulti = Boolean.parseBoolean((String) fieldMap.get("valueMulti"));
								}
								Map<String, String> selectorMap = (Map<String, String>) fieldMap.get("selector");
								if (null != selectorMap && selectorMap.size() > 0) {
									Selectable selectable = SelectableUtils.select(html, selectorMap);
									if (null != selectable) {
										if (valueMulti) {
											List<String> results = selectable.all();
											if (null != results && results.size() > 0) {
												page.putField(fieldKey, results);
											}
										} else {
											String result = selectable.get();
											if (null != result) {
												if (fieldMap.containsKey("formatter")) {
													Map<String, String> formatterMap = (Map<String, String>) fieldMap.get("formatter");
													if (null != formatterMap && formatterMap.size() > 0) {
														String type = formatterMap.get("type");
														String format = formatterMap.get("format");
														if (type.equals("date")) {
															result = dateFormatter.format(result, format);
														}
													}
												}
												page.putField(fieldKey, StringUtils.strip(result));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
