package com.admin.vo;

import java.util.List;

/**
 * 站点VO,用于传值
 * @date   2017年2月22日 下午2:47:51
 */
public class WebSiteVO {
	private String id;//站点id
	
	private String url;//url链接
	
	private String name;//站点名称
	
	private String comment;//备注,说明
	
	private String addTime;//添加时间
	
	private String type;//站点类型
	
	private String cycle;//采集周期
	
	private Integer threads; //线程数量
	
	private String cookie; //cookie
	
	private String charset; //字符编码
	
	private Integer sleep; //休眠时间
	
	private Integer timeout;//超时时间
	
	private Integer retry;//重试次数
	
	private List<Page> pages;//页面配置

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

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public List<Page> getPages()
	{
		return pages;
	}

	public void setPages(List<Page> pages)
	{
		this.pages = pages;
	}
	
	public Integer getThreads()
	{
		return threads;
	}

	public void setThreads(Integer threads)
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

	public Integer getSleep()
	{
		return sleep;
	}

	public void setSleep(Integer sleep)
	{
		this.sleep = sleep;
	}

	public Integer getTimeout()
	{
		return timeout;
	}

	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	public Integer getRetry()
	{
		return retry;
	}

	public void setRetry(Integer retry)
	{
		this.retry = retry;
	}

	public String getAddTime()
	{
		return addTime;
	}

	public void setAddTime(String addTime)
	{
		this.addTime = addTime;
	}
}
