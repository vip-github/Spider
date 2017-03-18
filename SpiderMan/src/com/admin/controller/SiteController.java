package com.admin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.entity.ChildrenSite;
import com.admin.entity.ParentSite;
import com.admin.service.SiteService;
import com.admin.vo.Page;
import com.admin.vo.Selector;
import com.admin.vo.SiteVO;
import com.alibaba.fastjson.JSON;

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
	@RequestMapping(value = "/getParentSiteList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String getParentSiteList(@RequestParam(defaultValue = "1", required = true, value = "page") int page,
			@RequestParam(defaultValue = "50", required = true, value = "rows") int rows)
	{
		String json = "";
		try
		{
			List<ParentSite> list = siteService.getParentSiteList((page - 1) * rows, rows);
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
			List<ChildrenSite> list = siteService.getChildrenSiteList(pid, (page - 1) * rows, rows);
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
	public String saveChildrenSite(@RequestParam(required = true) String pid, String cname, String curl, String ctype,
			String cSelectorKey1, String cSelectorKey2, String cSelectorKey3, String cSelectorValue1,
			String cSelectorValue2, String cSelectorValue3, String ccomment, String ccycle)
	{
		String json = "ok";
		try
		{
			if (StringUtils.isNotBlank(curl) && StringUtils.isNotBlank(cname) && StringUtils.isNotBlank(ctype)
					&& StringUtils.isNotBlank(cSelectorKey1) && StringUtils.isNotBlank(cSelectorValue1))
			{
				ChildrenSite object = new ChildrenSite();
				object.setId(com.spider.utils.StringUtils.md5(curl));
				object.setName(StringUtils.strip(cname));
				object.setType(StringUtils.strip(ctype));
				object.setUrl(StringUtils.strip(curl));
				object.setAddtime(new Timestamp(new Date().getTime()));
				object.setComment(ccomment);
				object.setCycle(ccycle);
				object.setPid(pid);
				Map<String, String> selectors = new LinkedHashMap<>();
				if (StringUtils.isNotBlank(cSelectorKey1) && StringUtils.isNotBlank(cSelectorValue1))
				{
					selectors.put(cSelectorKey1, StringUtils.strip(cSelectorValue1));
				}
				if (StringUtils.isNotBlank(cSelectorKey2) && StringUtils.isNotBlank(cSelectorValue2))
				{
					selectors.put(cSelectorKey2, StringUtils.strip(cSelectorValue2));
				}
				if (StringUtils.isNotBlank(cSelectorKey3) && StringUtils.isNotBlank(cSelectorValue3))
				{
					selectors.put(cSelectorKey3, StringUtils.strip(cSelectorValue3));
				}
				if (selectors.size() > 0)
				{
					// object.setSelectors(selectors);
					siteService.save(object);
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
	public String saveParentSite(SiteVO vo)
	{
		String result = "error";
		if (null != vo && StringUtils.isNotBlank(vo.getUrl()) && StringUtils.isNotBlank(vo.getName()))
		{
			try
			{
				ParentSite parentSite = new ParentSite();
				parentSite.setId(com.spider.utils.StringUtils.md5(vo.getUrl()));
				parentSite.setName(StringUtils.stripToEmpty(vo.getName()));
				parentSite.setUrl(StringUtils.stripToEmpty(vo.getUrl()));
				parentSite.setType(StringUtils.stripToEmpty(vo.getType()));
				parentSite.setCycle(Integer.parseInt(StringUtils.stripToEmpty(vo.getCycle())));
				parentSite.setComment(StringUtils.stripToEmpty(vo.getComment()));
				parentSite.setAddtime(new Timestamp(System.currentTimeMillis()));
				if (StringUtils.isNotBlank(vo.getCharset()) && !vo.getCharset().equals("自动设置"))
				{
					parentSite.setCharset(vo.getCharset());
				}
				if (null != vo.getRetry() && vo.getRetry() > 0)
				{
					parentSite.setRetry(vo.getRetry());
				}
				if (null != vo.getSleep() && vo.getSleep() > 0)
				{
					parentSite.setSleep(vo.getSleep());
				}
				if (null != vo.getThreads() && vo.getThreads() > 0)
				{
					parentSite.setThreads(vo.getThreads());
				}
				if (null != vo.getTimeout() && vo.getTimeout() > 0)
				{
					parentSite.setTimeout(vo.getTimeout());
				}
				if (StringUtils.isNotBlank(vo.getCookie()))
				{
					parentSite.setCookie(vo.getCookie());
				}
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
				parentSite.setPages(pages);
				siteService.save(parentSite);
				logger.info(String.format("父站点保存成功！%s", parentSite));
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
			boolean res = siteService.deleteParent(id);
			if (res)
			{
				logger.info(String.format("id:%s 父站点删除成功！", id));
				result = "ok";
			}
		}
		return result;
	}
}
