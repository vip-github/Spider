package com.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dao.SiteDao;
import com.admin.entity.WebSite;
import com.google.common.base.Strings;

@Service
public class SiteService
{
	@Autowired
	private SiteDao siteDao;

	public List<WebSite> getParentSiteList(int skip, int limit)
	{
		List<WebSite> results = new ArrayList<>();
		List<?> list = siteDao.find(WebSite.class, skip, limit);
		if (null != list && list.size() > 0)
		{
			for (Object object : list)
			{
				results.add((WebSite) object);
			}
		}
		return results;
	}

	public long getChildrenSiteCount(String pid)
	{
		return siteDao.count(WebSite.class, "pid", pid);
	}

	public List<WebSite> getChildrenSiteList(String pid, int skip, int limit)
	{
		List<WebSite> results = new ArrayList<>();
		WebSite parentSite = (WebSite) siteDao.findById(WebSite.class, pid);
		if (null != parentSite)
		{
			List<?> list = siteDao.find(WebSite.class, "pid", pid, skip, limit);
			if (null != list && list.size() > 0)
			{
				for (Object object : list)
				{
					WebSite childrenSite = (WebSite) object;
					if (Strings.isNullOrEmpty(childrenSite.getComment())
							&& !Strings.isNullOrEmpty(parentSite.getComment()))
					{
						childrenSite.setComment(parentSite.getComment());
					}
					if (Strings.isNullOrEmpty(childrenSite.getName()) && !Strings.isNullOrEmpty(parentSite.getName()))
					{
						childrenSite.setName(parentSite.getName());
					}
					if (Strings.isNullOrEmpty(childrenSite.getType()) && !Strings.isNullOrEmpty(parentSite.getType()))
					{
						childrenSite.setType(parentSite.getType());
					}
					results.add(childrenSite);
				}
			}
		}
		return results;
	}

	public void save(Object object)
	{
		siteDao.save(object);
	}

	public boolean delete(String id)
	{
		return siteDao.delete(WebSite.class, id);
	}
	
	public WebSite getSite(String id)
	{
		return (WebSite)siteDao.findById(WebSite.class, id);
	}
}
