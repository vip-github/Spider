package com.spider.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Page {
	private int priority;
	
	private String type;
	
	private Map<String, String> selector = new LinkedHashMap<>();
	
	private boolean valueMulti = false;
	
	private List<Map<String, Object>> field;
	
	private Map<String, String> formatter;
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getSelector() {
		return selector;
	}

	public void setSelector(Map<String, String> selector) {
		this.selector = selector;
	}

	public boolean getValueMulti() {
		return valueMulti;
	}

	public void setValueMulti(boolean valueMulti) {
		this.valueMulti = valueMulti;
	}

	public List<Map<String, Object>> getField() {
		return field;
	}

	public void setField(List<Map<String, Object>> field) {
		this.field = field;
	}

	public Map<String, String> getFormatter() {
		return formatter;
	}

	public void setFormatter(Map<String, String> formatter) {
		this.formatter = formatter;
	}

	@Override
	public String toString() {
		return "Page [priority=" + priority + ", type=" + type + ", selector=" + selector + ", valueMulti=" + valueMulti
				+ ", field=" + field + ", formatter=" + formatter + "]";
	}
}
