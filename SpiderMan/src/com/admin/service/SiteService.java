package com.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dao.SiteDao;
import com.admin.entity.ChildrenSite;
import com.admin.entity.ParentSite;
import com.google.common.base.Strings;

@Service
public class SiteService {
	@Autowired
	private SiteDao siteDao;
	
	public List<ParentSite> getParentSiteList(int skip, int limit){
		List<ParentSite> results = new ArrayList<>();
		List<?> list = siteDao.find(ParentSite.class, skip, limit);
		if(null!=list && list.size()>0){
			for (Object object : list) {
				results.add((ParentSite)object);
			}
		}
		return results;
	}
	
	public long getChildrenSiteCount(String pid){
		return siteDao.count(ChildrenSite.class, "pid", pid);
	}
	
	public List<ChildrenSite> getChildrenSiteList(String pid, int skip, int limit){
		List<ChildrenSite> results = new ArrayList<>();
		ParentSite parentSite = (ParentSite)siteDao.findById(ParentSite.class, pid);
		if(null!=parentSite){
			List<?> list = siteDao.find(ChildrenSite.class, "pid", pid, skip, limit);
			if(null!=list && list.size()>0){
				for (Object object : list) {
					ChildrenSite childrenSite = (ChildrenSite)object;
					if(Strings.isNullOrEmpty(childrenSite.getComment()) && !Strings.isNullOrEmpty(parentSite.getComment())){
						childrenSite.setComment(parentSite.getComment());
					}
					if(Strings.isNullOrEmpty(childrenSite.getName()) && !Strings.isNullOrEmpty(parentSite.getName())){
						childrenSite.setName(parentSite.getName());
					}
					if(Strings.isNullOrEmpty(childrenSite.getType()) && !Strings.isNullOrEmpty(parentSite.getType())){
						childrenSite.setType(parentSite.getType());
					}
					if(null==childrenSite.getSelectors() || childrenSite.getSelectors().size()==0){
						if(null!=parentSite.getSelectors() && parentSite.getSelectors().size()>0){
							childrenSite.setSelectors(parentSite.getSelectors());
						}
					}
					results.add(childrenSite);
				}
			}
		}
		return results;
	}
	
	public void save(Object object){
		siteDao.save(object);
	}
}
