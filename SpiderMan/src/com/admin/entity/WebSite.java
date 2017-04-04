package com.admin.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity(value = "tb_admin_site", noClassnameStored = true)
public class WebSite
{
	@Id
	private String id;// id
	
	private String pid;// 父站点id
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Timestamp addtime;// 添加时间

	private String url;// url链接

	private String name;// 站点名称

	private String comment;// 备注,说明

	private String type;// 站点类型
	
	private Header header;// header
	
	private String cycle = "10";// 采集周期
	
	private List<Map<String, Object>> pages;// 页面配置

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Timestamp getAddtime()
	{
		return addtime;
	}

	public void setAddtime(Timestamp addtime)
	{
		this.addtime = addtime;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	public List<Map<String, Object>> getPages()
	{
		return pages;
	}

	public void setPages(List<Map<String, Object>> pages)
	{
		this.pages = pages;
	}
	
	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

	public Header getHeader()
	{
		return header;
	}

	public void setHeader(Header header)
	{
		this.header = header;
	}

	public String getCycle()
	{
		return cycle;
	}

	public void setCycle(String cycle)
	{
		this.cycle = cycle;
	}
}
