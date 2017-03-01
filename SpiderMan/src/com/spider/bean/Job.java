package com.spider.bean;

import java.util.List;

public class Job {
	private Link link;
	
	private Header header;
	
	private List<Page> page;
	
	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<Page> getPage() {
		return page;
	}

	public void setPage(List<Page> page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "Job [link=" + link + ", header=" + header + ", page=" + page + "]";
	}
}
