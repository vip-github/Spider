package com.admin.entity;

public class Header
{
	private String threads = "10"; //线程数量
	
	private String cookie; //cookie
	
	private String charset; //字符编码
	
	private String sleep = "100"; //休眠时间
	
	private String timeout = "180000";//超时时间
	
	private String retry = "3";//重试次数

	public String getThreads()
	{
		return threads;
	}

	public void setThreads(String threads)
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

	public String getSleep()
	{
		return sleep;
	}

	public void setSleep(String sleep)
	{
		this.sleep = sleep;
	}

	public String getTimeout()
	{
		return timeout;
	}

	public void setTimeout(String timeout)
	{
		this.timeout = timeout;
	}

	public String getRetry()
	{
		return retry;
	}

	public void setRetry(String retry)
	{
		this.retry = retry;
	}
}
