package com.spider.bean;

import java.util.Arrays;

public class Link {
	private String[] url;
	
	private String max;
	
	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "Link [url=" + Arrays.toString(url) + ", max=" + max + "]";
	}
}
