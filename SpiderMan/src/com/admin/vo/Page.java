package com.admin.vo;

import java.util.List;

public class Page
{
	private String priority;
	
	private String type;
	
	private List<Selector> selectors;

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	public List<Selector> getSelectors()
	{
		return selectors;
	}

	public void setSelectors(List<Selector> selectors)
	{
		this.selectors = selectors;
	}

	@Override
	public String toString()
	{
		return "Page [priority=" + priority + ", type=" + type + ", selectors=" + selectors + "]";
	}
}
