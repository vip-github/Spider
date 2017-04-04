package com.admin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.entity.Header;
import com.admin.entity.WebSite;
import com.admin.service.SiteService;
import com.admin.vo.Page;
import com.admin.vo.Selector;
import com.admin.vo.WebSiteVO;
import com.alibaba.fastjson.JSON;
import com.spider.utils.DateUtils;

/**
 * 获取数据
 * 
 * @author czq
 * @date 2017年2月20日 上午11:20:24
 */
@Controller
public class SiteController
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SiteService siteService;

	/**
	 * 获得父站点列表,并返回json数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getParentSiteList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String getParentSiteList(@RequestParam(defaultValue = "1", required = true, value = "page") int page,
			@RequestParam(defaultValue = "50", required = true, value = "rows") int rows)
	{
		String json = "";
		try
		{
			List<WebSite> list = siteService.getParentSiteList((page - 1) * rows, rows);
			if (null != list && list.size() > 0)
			{
				List<WebSiteVO> results = new ArrayList<>();
				for (WebSite site : list)
				{
					WebSiteVO vo = new WebSiteVO();
					vo.setComment(site.getComment());
					vo.setId(site.getId());
					vo.setType(site.getType());
					vo.setCycle(site.getCycle());
					vo.setName(site.getName());
					vo.setUrl(site.getUrl());
					if(null!=site.getHeader())
					{
						Header header = site.getHeader();
						vo.setThreads(StringUtils.isNotBlank(header.getThreads())?Integer.parseInt(header.getThreads()):10);
					}
					vo.setAddTime(DateUtils.format(site.getAddtime()));
					List<Map<String, Object>> pageList = site.getPages();
					if(null!=pageList && !pageList.isEmpty())
					{
						List<Page> pages = new ArrayList<>();
						for (Map<String, Object> pageMap : pageList)
						{
							Page pageVO = new Page();
							pageVO.setPriority((String)pageMap.getOrDefault("priority", "0"));
							pageVO.setType((String)pageMap.get("type"));
							Map<String, String> selectorMap = (Map<String, String>)pageMap.get("selector");
							if(null!=selectorMap && !selectorMap.isEmpty())
							{
								List<Selector> selectors = new ArrayList<>();
								for (Entry<String, String> entry : selectorMap.entrySet())
								{
									Selector selector = new Selector();
									selector.setKey(entry.getKey());
									selector.setValue(entry.getValue());
									selectors.add(selector);
								}
								pageVO.setSelectors(selectors);
							}
						}
						vo.setPages(pages);
					}
					results.add(vo);
				}
				json = JSON.toJSONString(results);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return json;
	}

	@RequestMapping(value = "/getChildrenSiteCount", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String getChildrenSiteCount(@RequestParam(required = true, value = "pid") String pid)
	{
		String json = "";
		try
		{
			long count = siteService.getChildrenSiteCount(pid);
			json = String.valueOf(count);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return json;
	}

	@RequestMapping(value = "/getChildrenSiteList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String getChildrenSiteList(@RequestParam(defaultValue = "1", value = "page") int page,
			@RequestParam(defaultValue = "30", value = "rows") int rows,
			@RequestParam(required = true, value = "pid") String pid)
	{
		String json = "";
		try
		{
			List<WebSite> list = siteService.getChildrenSiteList(pid, (page - 1) * rows, rows);
			if (null != list && list.size() > 0)
			{
				json = JSON.toJSONString(list);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return json;
	}

	@RequestMapping(value = "/saveChildrenSite", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String saveChildrenSite(@RequestParam(required = true) String pid, WebSiteVO vo)
	{
		String json = "ok";
		try
		{
			if(StringUtils.isNotBlank(pid) && null!=vo && StringUtils.isNotBlank(vo.getUrl()))
			{
				WebSite psite = siteService.getSite(pid);
				if(null!=psite)
				{
					WebSite site = new WebSite();
					Header header = new Header();
					BeanUtils.copyProperties(site, psite);
					String id = com.spider.utils.StringUtils.md5(vo.getUrl());
					site.setId(id);
					site.setPid(pid);
					if(StringUtils.isNotBlank(vo.getName()))
					{
						site.setName(StringUtils.stripToEmpty(vo.getName()));
					}
					if(StringUtils.isNotBlank(vo.getComment()))
					{
						site.setComment(StringUtils.stripToEmpty(vo.getComment()));
					}
					site.setUrl(StringUtils.strip(vo.getUrl()));
					if(StringUtils.isNotBlank(vo.getType()))
					{
						site.setType(vo.getType());
					}
					if(StringUtils.isNotBlank(vo.getCycle()))
					{
						site.setCycle(vo.getCycle());
					}
					site.setHeader(header);
					site.setAddtime(new Timestamp(new Date().getTime()));
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
		return json;
	}

	@RequestMapping(value = "/saveParentSite", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String saveParentSite(WebSiteVO vo)
	{
		String result = "error";
		if (null != vo && StringUtils.isNotBlank(vo.getUrl()) && StringUtils.isNotBlank(vo.getName()))
		{
			try
			{
				WebSite site = new WebSite();
				Header header = new Header();
				site.setId(com.spider.utils.StringUtils.md5(vo.getUrl()));
				site.setName(StringUtils.stripToEmpty(vo.getName()));
				site.setUrl(StringUtils.stripToEmpty(vo.getUrl()));
				site.setType(StringUtils.stripToEmpty(vo.getType()));
				site.setCycle(StringUtils.stripToEmpty(vo.getCycle()));
				site.setComment(StringUtils.stripToEmpty(vo.getComment()));
				site.setAddtime(new Timestamp(System.currentTimeMillis()));
				if (StringUtils.isNotBlank(vo.getCharset()) && !vo.getCharset().equals("自动设置"))
				{
					header.setCharset(vo.getCharset());
				}
				if (null != vo.getRetry() && vo.getRetry() > 0)
				{
					header.setRetry(String.valueOf(vo.getRetry()));
				}
				if (null != vo.getSleep() && vo.getSleep() > 0)
				{
					header.setSleep(String.valueOf(vo.getSleep()));
				}
				if (null != vo.getThreads() && vo.getThreads() > 0)
				{
					header.setThreads(String.valueOf(vo.getThreads()));
				}
				if (null != vo.getTimeout() && vo.getTimeout() > 0)
				{
					header.setTimeout(String.valueOf(vo.getTimeout()));
				}
				if (StringUtils.isNotBlank(vo.getCookie()))
				{
					header.setCookie(vo.getCookie());
				}
				site.setHeader(header);
				List<Map<String, Object>> pages = new ArrayList<>();
				if (null != vo.getPages() && vo.getPages().size() > 0)
				{
					for (Page page : vo.getPages())
					{
						Map<String, Object> pageMap = new LinkedHashMap<>();
						if (null != page && null != page.getSelectors() && page.getSelectors().size() > 0)
						{
							String priority = page.getPriority();
							String type = page.getType();
							List<Selector> selectors = page.getSelectors();
							pageMap.put("priority", StringUtils.stripToEmpty(priority));
							pageMap.put("type", StringUtils.stripToEmpty(type));
							Map<String, String> selectorsMap = new LinkedHashMap<>();
							for (Selector selector : selectors)
							{
								selectorsMap.put(StringUtils.stripToEmpty(selector.getKey()),
										StringUtils.stripToEmpty(selector.getValue()));
							}
							pageMap.put("selector", selectorsMap);
							pages.add(pageMap);
						}
					}
				}
				site.setPages(pages);
				siteService.save(site);
				logger.info(String.format("父站点保存成功！%s", site));
				result = "ok";
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e.toString());
			}
		}
		return result;
	}

	@RequestMapping(value = "/deleteParentSite")
	@ResponseBody
	public String deleteParentSite(String id)
	{
		String result = "error";
		if (StringUtils.isNotBlank(id))
		{
			boolean res = siteService.delete(id);
			if (res)
			{
				logger.info(String.format("id:%s 父站点删除成功！", id));
				result = "ok";
			}
		}
		return result;
	}
}
