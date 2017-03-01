package com.spider.bean;

public class Header {
	private String cookie;
	
	private String sleep = "1000";
	
	private String timeout = "1000*30";
	
	private String retry = "3";
	
	private String charset;
	
	private String thread = "1";

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getSleep() {
		return sleep;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getRetry() {
		return retry;
	}

	public void setRetry(String retry) {
		this.retry = retry;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	@Override
	public String toString() {
		return "Header [cookie=" + cookie + ", sleep=" + sleep + ", timeout=" + timeout + ", retry=" + retry
				+ ", charset=" + charset + ", thread=" + thread + "]";
	}
}
