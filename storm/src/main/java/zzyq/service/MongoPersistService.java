package zzyq.service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import zzyq.bean.Bbs;
import zzyq.bean.DataServiceConstants;
import zzyq.bean.FieldsConstant;
import zzyq.bean.GlobalObject;
import zzyq.bean.News;
import zzyq.bean.Weibo;
import zzyq.bean.Weixin;
import zzyq.utils.DateUtils;

/**
 * 操作mongodb业务类
 * @date   2017年5月16日 下午1:39:04
 */
public class MongoPersistService implements IMongoPersistService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Map<Integer, Object> saveWeiBo(Map<String, Object> jsonMap) {
		Map<Integer, Object> result = Collections.synchronizedMap(new LinkedHashMap<>());
		Weibo weibo = new Weibo();
		try {
			//必要字段校验
			Preconditions.checkArgument(jsonMap.containsKey(FieldsConstant.microUrl), "没有microUrl字段！");
			if(!jsonMap.containsKey(FieldsConstant.createTime)) {
				jsonMap.put(FieldsConstant.createTime, DateUtils.getDateFormat(new Date()));
			}
			if(!jsonMap.containsKey(FieldsConstant.publishTime)) {
				jsonMap.put(FieldsConstant.publishTime, DateUtils.getDateFormat(new Date()));
			}
			
			for (Entry<String, Object> entry : jsonMap.entrySet())
			{
				String key = (String)entry.getKey();
				Object value = entry.getValue();
				if(key.equalsIgnoreCase(FieldsConstant.blogerId))
				{
					weibo.setBlogerId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.blogerUrl))
				{
					weibo.setBlogerUrl(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.blogerAddr))
				{
					weibo.setBlogerAddr(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.createTime))
				{
					if(null!=value) {
						String createTime = (String)value;
						if (!Strings.isNullOrEmpty(createTime) && !DateUtils.checkDatetimeFormat(createTime))
						{
							result.put(DataServiceConstants.createTime_error, "createTime字段格式错误！");
							return result;
						}
						weibo.setCreateTime(new Timestamp(DateUtils.getDateTime(createTime, DateUtils.format1).getTime()));
					} else {
						weibo.setCreateTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.origForwardNum))
				{
					long origForwardNum = Long.parseLong(zzyq.utils.StringUtils.getNumber((String)value));
					weibo.setOrigForwardNum(origForwardNum);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.origReplyCount))
				{
					long origReplyCount = Long.parseLong(zzyq.utils.StringUtils.getNumber((String)value));
					weibo.setOrigReplyCount(origReplyCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.origLikeNum))
				{
					long origLikeNum = Long.parseLong(zzyq.utils.StringUtils.getNumber((String)value));
					weibo.setOrigLikeNum(origLikeNum);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.repForwardNum))
				{
					long repForwardNum = Long.parseLong(zzyq.utils.StringUtils.getNumber((String)value));
					weibo.setRepForwardNum(repForwardNum);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.repRelyCount))
				{
					long repRelyCount = Long.parseLong(zzyq.utils.StringUtils.getNumber((String)value));
					weibo.setRepRelyCount(repRelyCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.repLikeNum))
				{
					long repLikeNum = Long.parseLong(zzyq.utils.StringUtils.getNumber((String)value));
					weibo.setRepLikeNum(repLikeNum);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.isForward))
				{
					weibo.setIsForward(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.microContent)||key.equalsIgnoreCase(FieldsConstant.content))
				{
					weibo.setMicroContent(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.publishTime))
				{
					if(null!=value) {
						String publishTime = (String)value;
						if (!Strings.isNullOrEmpty(publishTime) && !DateUtils.checkDatetimeFormat(publishTime))
						{
							result.put(DataServiceConstants.publishTime_error, "publishTime字段格式错误！");
							return result;
						}
						weibo.setPublishTime(new Timestamp(DateUtils.getDateTime(publishTime, DateUtils.format1).getTime()));
					} else {
						weibo.setPublishTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.forwardTime))
				{
					if(null!=value && !((String)value).equalsIgnoreCase("None")) {
						String forwardTime = (String)value;
						if (!Strings.isNullOrEmpty(forwardTime) && !DateUtils.checkDatetimeFormat(forwardTime)) {
							result.put(DataServiceConstants.forwardTime_error, "forwardTime字段格式错误！");
							return result;
						}
						weibo.setForwardTime(new Timestamp(DateUtils.getDateTime(forwardTime, DateUtils.format1).getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteName))
				{
					weibo.setWebsiteName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.microUrl))
				{
					String microUrl = StringUtils.strip((String)value);
					if(Strings.isNullOrEmpty(microUrl))
					{
						result.put(DataServiceConstants.url_empty, "microUrl字段不能为空！");
						return result;
					}
					if(!microUrl.startsWith("http"))
					{
						result.put(DataServiceConstants.url_begin_error, "microUrl字段必须以http开头！");
						return result;
					}
					weibo.setMicroUrl(microUrl);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.blogerDec))
				{
					weibo.setBlogerDec(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.blogerName))
				{
					weibo.setBlogerName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.seedType))
				{
					String seedType = StringUtils.strip((String)value);
					if(StringUtils.isBlank(seedType) || !seedType.toLowerCase().contains("weibo"))
					{
						result.put(DataServiceConstants.seedType_error, "seedType字段必须包含WeiBo！(忽略大小写)");
						return result;
					}
					weibo.setSeedType(seedType);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.fcontent))
				{
					weibo.setFcontent(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.forgiBologerName))
				{
					weibo.setForgiBologerName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.origblogerName))
				{
					weibo.setOrigblogerName(StringUtils.strip((String)value));
				}
			}
			weibo.setId(zzyq.utils.StringUtils.md5(weibo.getMicroUrl()));
			weibo.setTimestamp(System.currentTimeMillis());
			boolean success = GlobalObject.crawlerMongo.saveOrUpdate(weibo);
			if(success) {
				result.put(DataServiceConstants.success, weibo);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("%s%n%s", weibo.toString(), e.getMessage()));
		}
		result.put(DataServiceConstants.error, "其他错误导致保存失败！");
		return result;
	}
	
	public Map<Integer, Object> saveNews(Map<String, Object> jsonMap) {
		Map<Integer, Object> result = Collections.synchronizedMap(new LinkedHashMap<>());
		News news = new News();
		try {
			//必要字段校验
			Preconditions.checkArgument(jsonMap.containsKey(FieldsConstant.url), "没有url字段！");
			if(!jsonMap.containsKey(FieldsConstant.createTime)) {
				jsonMap.put(FieldsConstant.createTime, DateUtils.getDateFormat(new Date()));
			}
			if(!jsonMap.containsKey(FieldsConstant.publishTime)) {
				jsonMap.put(FieldsConstant.publishTime, DateUtils.getDateFormat(new Date()));
			}
			
			for (Entry<String, Object> entry : jsonMap.entrySet())
			{
				String key = (String)entry.getKey();
				Object value = entry.getValue();
				if(key.equalsIgnoreCase(FieldsConstant.title))
				{
					String title = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(title))
					{
						result.put(DataServiceConstants.title_empty, "标题不能为空！");
						return result;
					}
					news.setTitle(title);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.createTime))
				{
					if(null!=value) {
						String createTime = (String)value;
						if (!Strings.isNullOrEmpty(createTime) && !DateUtils.checkDatetimeFormat(createTime))
						{
							result.put(DataServiceConstants.createTime_error, "createTime格式错误！");
							return result;
						}
						news.setCreateTime(new Timestamp(DateUtils.getDateTime(createTime, DateUtils.format1).getTime()));
					} else {
						news.setCreateTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.url))
				{
					String url = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(url))
					{
						result.put(DataServiceConstants.url_empty, "url字段不能为空！");
						return result;
					}
					if (!url.startsWith("http"))
					{
						result.put(DataServiceConstants.url_begin_error, "url字段必须以http开头！");
						return result;
					}
					news.setUrl(url);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.seedType))
				{
					String seedType = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(seedType))
					{
						result.put(DataServiceConstants.seedType_empty, "seedType字段不能为空！");
						return result;
					}
					news.setSeedType(seedType);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainId))
				{
					news.setDomainId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domain))
				{
					news.setDomain(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainName))
				{
					news.setDomainName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainSign))
				{
					news.setDomainSign(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteName))
				{
					news.setWebsiteName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteId))
				{
					news.setWebsiteId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteSign))
				{
					news.setWebsiteSign(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteUrl) || key.equalsIgnoreCase("via"))
				{
					news.setWebsiteUrl(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.publishTime))
				{
					if(null!=value) {
						String publishTime = (String)value;
						if (!Strings.isNullOrEmpty(publishTime) && !DateUtils.checkDatetimeFormat(publishTime))
						{
							result.put(DataServiceConstants.publishTime_error, "publishTime字段格式错误！");
							return result;
						}
						Date date = DateUtils.getDateTime(publishTime, DateUtils.format1);
						date = DateUtils.changeDateTime(date);
						news.setPublishTime(new Timestamp(date.getTime()));
					} else {
						news.setPublishTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.source))
				{
					news.setSource(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.author))
				{
					news.setAuthor(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.content))
				{
					String content = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(content))
					{
						result.put(DataServiceConstants.content_empty, "content字段不能为空！");
						return result;
					}
					news.setContent(content);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.keyWords))
				{
					news.setKeyWords(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.businessType))
				{
					news.setBusinessType(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.isOriginal))
				{
					news.setIsOriginal(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.likeCount))
				{
					Long likeCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					news.setLikeCount(likeCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.viewCount))
				{
					Long viewCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					news.setViewCount(viewCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.replyCount))
				{
					Long replyCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					news.setReplyCount(replyCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.dataSource)){
					news.setDataSource(StringUtils.strip((String)value));
				}
			}
			news.setId(zzyq.utils.StringUtils.md5(news.getUrl()));
			news.setTimestamp(System.currentTimeMillis());
			boolean success = GlobalObject.crawlerMongo.saveOrUpdate(news);
			if(success) {
				result.put(DataServiceConstants.success, news);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("%s%n%s", news.toString(), e.getMessage()));
		}
		result.put(DataServiceConstants.error, "其他错误导致保存失败！");
		return result;
	}
	
	public Map<Integer, Object> saveBBS(Map<String, Object> jsonMap) {
		Map<Integer, Object> result = Collections.synchronizedMap(new LinkedHashMap<>());
		Bbs bbs = new Bbs();
		try {
			//必要字段校验
			Preconditions.checkArgument(jsonMap.containsKey(FieldsConstant.url), "没有url字段！");
			if(!jsonMap.containsKey(FieldsConstant.createTime)) {
				jsonMap.put(FieldsConstant.createTime, DateUtils.getDateFormat(new Date()));
			}
			if(!jsonMap.containsKey(FieldsConstant.publishTime)) {
				jsonMap.put(FieldsConstant.publishTime, DateUtils.getDateFormat(new Date()));
			}
			
			for (Entry<String, Object> entry : jsonMap.entrySet())
			{
				String key = (String)entry.getKey();
				Object value = entry.getValue();
				if(key.equalsIgnoreCase(FieldsConstant.title))
				{
					String title = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(title))
					{
						result.put(DataServiceConstants.title_empty, "标题不能为空！");
						return result;
					}
					bbs.setTitle(title);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.createTime))
				{
					if(null!=value) {
						String createTime = (String)value;
						if (!Strings.isNullOrEmpty(createTime) && !DateUtils.checkDatetimeFormat(createTime))
						{
							result.put(DataServiceConstants.createTime_error, "createTime字段格式错误！");
							return result;
						}
						bbs.setCreateTime(new Timestamp(DateUtils.getDateTime(createTime, DateUtils.format1).getTime()));
					} else {
						bbs.setCreateTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.url))
				{
					String url = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(url))
					{
						result.put(DataServiceConstants.url_empty, "url字段不能为空！");
						return result;
					}
					if (!url.startsWith("http"))
					{
						result.put(DataServiceConstants.url_begin_error, "url字段必须以http开头！");
						return result;
					}
					bbs.setUrl(url);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.seedType))
				{
					String seedType = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(seedType))
					{
						result.put(DataServiceConstants.seedType_empty, "seedType字段不能为空！");
						return result;
					}
					bbs.setSeedType(seedType);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainId))
				{
					bbs.setDomainId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domain))
				{
					bbs.setDomain(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainName))
				{
					bbs.setDomainName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainSign))
				{
					bbs.setDomainSign(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteName))
				{
					bbs.setWebsiteName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteId))
				{
					bbs.setWebsiteId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteSign))
				{
					bbs.setWebsiteSign(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteUrl) || key.equalsIgnoreCase("via"))
				{
					bbs.setWebsiteUrl(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.publishTime))
				{
					if(null!=value) {
						String publishTime = (String)value;
						if (!Strings.isNullOrEmpty(publishTime) && !DateUtils.checkDatetimeFormat(publishTime))
						{
							result.put(DataServiceConstants.publishTime_error, "publishTime字段格式错误！");
							return result;
						}
						Date date = DateUtils.getDateTime(publishTime, DateUtils.format1);
						date = DateUtils.changeDateTime(date);
						bbs.setPublishTime(new Timestamp(date.getTime()));
					} else {
						bbs.setPublishTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.source))
				{
					bbs.setSource(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.author))
				{
					bbs.setAuthor(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.content))
				{
					String content = StringUtils.strip((String)value);
					if (Strings.isNullOrEmpty(content))
					{
						result.put(DataServiceConstants.content_empty, "content字段不能为空！");
						return result;
					}
					bbs.setContent(content);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.keyWords))
				{
					bbs.setKeyWords(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.isOriginal))
				{
					bbs.setIsOriginal(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.likeCount))
				{
					Long likeCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					bbs.setLikeCount(likeCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.viewCount))
				{
					Long viewCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					bbs.setViewCount(viewCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.replyCount))
				{
					Long replyCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					bbs.setReplyCount(replyCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.dataSource)){
					bbs.setDataSource(StringUtils.strip((String)value));
				}
			}
			bbs.setId(zzyq.utils.StringUtils.md5(bbs.getUrl()));
			bbs.setTimestamp(System.currentTimeMillis());
			boolean success = GlobalObject.crawlerMongo.saveOrUpdate(bbs);
			if(success) {
				result.put(DataServiceConstants.success, bbs);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("%s%n%s", bbs.toString(), e.getMessage()));
		}
		result.put(DataServiceConstants.error, "其他错误导致保存失败！");
		return result;
	}
	
	public Map<Integer, Object> saveWeiXin(Map<String, Object> jsonMap) {
		Map<Integer, Object> result = Collections.synchronizedMap(new LinkedHashMap<>());
		Weixin weixin = new Weixin();
		try {
			//必要字段校验
			Preconditions.checkArgument(jsonMap.containsKey(FieldsConstant.url), "没有url字段！");
			if(!jsonMap.containsKey(FieldsConstant.createTime)) {
				jsonMap.put(FieldsConstant.createTime, DateUtils.getDateFormat(new Date()));
			}
			if(!jsonMap.containsKey(FieldsConstant.publishTime)) {
				jsonMap.put(FieldsConstant.publishTime, DateUtils.getDateFormat(new Date()));
			}
			
			for (Entry<String, Object> entry : jsonMap.entrySet())
			{
				String key = (String)entry.getKey();
				Object value = entry.getValue();
				if(key.equalsIgnoreCase(FieldsConstant.title))
				{
					String title = StringUtils.strip((String)value);
					if(Strings.isNullOrEmpty(title))
					{
						result.put(DataServiceConstants.title_empty, "标题不能为空！");
						return result;
					}
					weixin.setTitle(title);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.createTime))
				{
					if(null!=value) {
						String createTime = (String)value;
						if (!Strings.isNullOrEmpty(createTime) && !DateUtils.checkDatetimeFormat(createTime))
						{
							result.put(DataServiceConstants.createTime_error, "createTime字段格式错误！");
							return result;
						}
						weixin.setCreateTime(new Timestamp(DateUtils.getDateTime(createTime, DateUtils.format1).getTime()));
					} else {
						weixin.setCreateTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.url))
				{
					String url = StringUtils.strip((String)value);
					if(Strings.isNullOrEmpty(url))
					{
						result.put(DataServiceConstants.url_empty, "url字段不能为空！");
						return result;
					}
					if(!url.startsWith("http"))
					{
						result.put(DataServiceConstants.url_begin_error, "url字段必须以http开头！");
						return result;
					}
					weixin.setUrl(url);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.seedType))
				{
					weixin.setSeedType("WeChat");
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainId))
				{
					weixin.setDomainId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domain))
				{
					weixin.setDomain(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainName))
				{
					weixin.setDomainName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.domainSign))
				{
					weixin.setDomainSign(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteName))
				{
					weixin.setWebsiteName(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteId))
				{
					weixin.setWebsiteId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteSign))
				{
					weixin.setWebsiteSign(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.websiteUrl) || key.equalsIgnoreCase("via"))
				{
					weixin.setWebsiteUrl(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.publishTime))
				{
					if(null!=value) {
						String publishTime = (String)value;
						if (!Strings.isNullOrEmpty(publishTime) && !DateUtils.checkDatetimeFormat(publishTime))
						{
							result.put(DataServiceConstants.publishTime_error, "publishTime字段格式错误！");
							return result;
						}
						weixin.setPublishTime(new Timestamp(DateUtils.getDateTime(publishTime, DateUtils.format1).getTime()));
					} else {
						weixin.setPublishTime(new Timestamp(new Date().getTime()));
					}
				}
				else if(key.equalsIgnoreCase(FieldsConstant.source))
				{
					weixin.setSource(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.author))
				{
					weixin.setAuthor(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.content))
				{
					String content = StringUtils.strip((String)value);
					if(Strings.isNullOrEmpty(content))
					{
						result.put(DataServiceConstants.content_empty, "content字段不能为空！");
						return result;
					}
					weixin.setContent(content);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.keyWords))
				{
					weixin.setKeyWords(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.businessType))
				{
					weixin.setBusinessType(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.isOriginal))
				{
					weixin.setIsOriginal(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.likeCount))
				{
					Long likeCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					weixin.setLikeCount(likeCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.viewCount))
				{
					Long viewCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					weixin.setViewCount(viewCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.replyCount))
				{
					Long replyCount = Long.parseLong(zzyq.utils.StringUtils.getNumber(StringUtils.strip((String)value)));
					weixin.setReplyCount(replyCount);
				}
				else if(key.equalsIgnoreCase(FieldsConstant.weixinId))
				{
					weixin.setWebsiteId(StringUtils.strip((String)value));
				}
				else if(key.equalsIgnoreCase(FieldsConstant.dataSource)){
					weixin.setDataSource(StringUtils.strip((String)value));
				}
			}
			weixin.setId(zzyq.utils.StringUtils.md5(weixin.getUrl()));
			weixin.setTimestamp(System.currentTimeMillis());
			boolean success = GlobalObject.crawlerMongo.saveOrUpdate(weixin);
			if(success) {
				result.put(DataServiceConstants.success, weixin);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("%s%n%s", weixin.toString(), e.getMessage()));
		}
		result.put(DataServiceConstants.error, "其他错误导致保存失败！");
		return result;
	}
}
