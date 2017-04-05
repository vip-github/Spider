package com.admin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.common.utils.MongodbUtils;

@Repository
public class SiteDao
{
	MongodbUtils mongodbUtils = MongodbUtils.getInstance();

	public void save(Object entity)
	{
		mongodbUtils.save(entity);
	}

	public Object findById(Class<?> entity, String id)
	{
		return mongodbUtils.findById(entity, id);
	}

	public List<?> find(Class<?> entity, int skip, int limit)
	{
		return mongodbUtils.find(entity, skip, limit);
	}

	public List<?> find(Class<?> entity, String fieldKey, String fieldValue, int skip, int limit)
	{
		return mongodbUtils.find(entity, fieldKey, fieldValue, skip, limit);
	}

	public List<?> find(Class<?> entity, String fieldKey, String fieldValue)
	{
		return mongodbUtils.find(entity, fieldKey, fieldValue);
	}

	public long count(Class<?> entity, String fieldKey, String fieldValue)
	{
		return mongodbUtils.count(entity, fieldKey, fieldValue);
	}
	
	public boolean delete(Class<?> entity, String id)
	{
		return mongodbUtils.delete(entity, id);
	}
}
