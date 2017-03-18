package com.admin.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity(value = "tb_admin_parentsite", noClassnameStored = true)
public class ParentSite
{
	@Id
	private String id;// id
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Timestamp addtime;// 添加时间

	private String url;// url链接

	private String name;// 站点名称

	private String comment;// 备注,说明

	private String type;// 站点类型
	
	private int cycle = 10;// 采集周期
	
	private int threads = 10; //线程数量
	
	private String cookie; //cookie
	
	private String charset; //字符编码
	
	private int sleep = 100; //休眠时间
	
	private int timeout = 1000*30;//超时时间
	
	private int retry = 3;//重试次数
	
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

	public int getCycle()
	{
		return cycle;
	}

	public void setCycle(int cycle)
	{
		this.cycle = cycle;
	}

	public int getThreads()
	{
		return threads;
	}

	public void setThreads(int threads)
	{
		this.threads = threads;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCharset()
	{
		return charset;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	public int getSleep()
	{
		return sleep;
	}

	public void setSleep(int sleep)
	{
		this.sleep = sleep;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public int getRetry()
	{
		return retry;
	}

	public void setRetry(int retry)
	{
		this.retry = retry;
	}

	@Override
	public String toString()
	{
		return "ParentSite [id=" + id + ", addtime=" + addtime + ", url=" + url + ", name=" + name + ", comment="
				+ comment + ", type=" + type + ", cycle=" + cycle + ", threads=" + threads + ", cookie=" + cookie
				+ ", charset=" + charset + ", sleep=" + sleep + ", timeout=" + timeout + ", retry=" + retry + ", pages="
				+ pages + "]";
	}
}
