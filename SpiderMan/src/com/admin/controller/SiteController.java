package com.admin.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.entity.ChildrenSite;
import com.admin.entity.ParentSite;
import com.admin.service.SiteService;
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
		}
		return json;
	}

	@RequestMapping(value = "/saveParentSite", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String saveParentSite(String pname, String purl, String ptype, String pSelectorKey1, String pSelectorKey2,
			String pSelectorKey3, String pSelectorValue1, String pSelectorValue2, String pSelectorValue3, String pcomment,
			String pcycle)
	{
		String json = "ok";
		try
		{
			if (StringUtils.isNotBlank(purl) && StringUtils.isNotBlank(pname) && StringUtils.isNotBlank(ptype)
					&& StringUtils.isNotBlank(pSelectorKey1) && StringUtils.isNotBlank(pSelectorValue1))
			{
				ParentSite object = new ParentSite();
				object.setId(com.spider.utils.StringUtils.md5(purl));
				object.setName(StringUtils.strip(pname));
				object.setType(StringUtils.strip(ptype));
				object.setUrl(StringUtils.strip(purl));
				object.setAddtime(new Timestamp(new Date().getTime()));
				object.setComment(pcomment);
				object.setCycle(pcycle);
				Map<String, String> selectors = new LinkedHashMap<>();
				if(StringUtils.isNotBlank(pSelectorKey1) && StringUtils.isNotBlank(pSelectorValue1))
				{
					selectors.put(pSelectorKey1, StringUtils.strip(pSelectorValue1));
				}
				if(StringUtils.isNotBlank(pSelectorKey2) && StringUtils.isNotBlank(pSelectorValue2))
				{
					selectors.put(pSelectorKey2, StringUtils.strip(pSelectorValue2));
				}
				if(StringUtils.isNotBlank(pSelectorKey3) && StringUtils.isNotBlank(pSelectorValue3))
				{
					selectors.put(pSelectorKey3, StringUtils.strip(pSelectorValue3));
				}
				if(selectors.size()>0)
				{
					object.setSelectors(selectors);
					siteService.save(object);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value = "/saveChildrenSite", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseBody
	public String saveChildrenSite(@RequestParam(required=true)String pid, String cname, String curl, String ctype, String cSelectorKey1, String cSelectorKey2,
			String cSelectorKey3, String cSelectorValue1, String cSelectorValue2, String cSelectorValue3, String ccomment,
			String ccycle)
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
				if(StringUtils.isNotBlank(cSelectorKey1) && StringUtils.isNotBlank(cSelectorValue1))
				{
					selectors.put(cSelectorKey1, StringUtils.strip(cSelectorValue1));
				}
				if(StringUtils.isNotBlank(cSelectorKey2) && StringUtils.isNotBlank(cSelectorValue2))
				{
					selectors.put(cSelectorKey2, StringUtils.strip(cSelectorValue2));
				}
				if(StringUtils.isNotBlank(cSelectorKey3) && StringUtils.isNotBlank(cSelectorValue3))
				{
					selectors.put(cSelectorKey3, StringUtils.strip(cSelectorValue3));
				}
				if(selectors.size()>0)
				{
					object.setSelectors(selectors);
					siteService.save(object);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return json;
	}
}
