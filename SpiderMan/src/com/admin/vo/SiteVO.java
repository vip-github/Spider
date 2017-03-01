package com.admin.vo;

import java.util.Map;

/**
 * 站点VO,用于传值
 * @date   2017年2月22日 下午2:47:51
 */
public class SiteVO {
	private String pid;//父站点id
	
	private String url;//url链接
	
	private String name;//站点名称
	
	private String comment;//备注,说明
	
	private String type;//站点类型
	
	private Map<String, String> selectors;//选择器
	
	private String cycle;//采集周期

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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
