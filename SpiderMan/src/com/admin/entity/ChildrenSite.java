package com.admin.entity;

import java.sql.Timestamp;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity(value="tb_admin_childrensite", noClassnameStored=true)
public class ChildrenSite {
	@Id
	private String id;
	
	private String pid;//父站点id
	
	private String pname;//父站点名称
	
	private String url;//url链接
	
	private String name;//站点名称
	
	private String comment;//备注,说明
	
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Timestamp addtime;//添加时间
	
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Timestamp runtime;//最后一次抓取时间
	
	private String type;//站点类型
	
	private String cycle;//采集周期

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public Timestamp getRuntime() {
		return runtime;
	}

	public void setRuntime(Timestamp runtime) {
		this.runtime = runtime;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getPname()
	{
		return pname;
	}

	public void setPname(String pname)
	{
		this.pname = pname;
	}
}
