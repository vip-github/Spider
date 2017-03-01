package com.admin.entity;

import java.sql.Timestamp;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity(value="tb_admin_parentsite", noClassnameStored=true)
public class ParentSite {
	@Id
	private String id;//id
	
	private String url;//url链接
	
	private String name;//站点名称
	
	private String comment;//备注,说明
	
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Timestamp addtime;//添加时间
	
	private String type;//站点类型
	
	private Map<String, String> selectors;//选择器
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getSelectors() {
		return selectors;
	}

	public void setSelectors(Map<String, String> selectors) {
		this.selectors = selectors;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
}
