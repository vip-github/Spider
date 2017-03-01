package com.spider.utils;

import java.util.Map;

import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 基于webmagic的Selectable选择器工具类
 * @author czq
 * @date   2016年12月12日 上午10:55:56
 */
public class SelectableUtils
{
	public static final String xpath = "xpath";
	public static final String css = "css";
	public static final String regex = "regex";
	
	/**
	 * 根据传入的标签集合,拼凑出选择器(可能会包含多个选择器)
	 * @param html
	 * @param args
	 * @return
	 */
	public static Selectable select(Html html, Map<String, String> args)
	{
		Selectable selectable = html;
		for (Map.Entry<String, String> entry : args.entrySet()) 
		{
			String key = entry.getKey();
			String value = entry.getValue();
			if(key.equalsIgnoreCase(xpath))
			{
				selectable = xpath(selectable, value);
			}else if(key.equalsIgnoreCase(css) || key.equals("$"))
			{
				selectable = $(selectable, value);
			}
			else if(key.equalsIgnoreCase(regex))
			{
				selectable = regex(selectable, value);
			}
		}
		return selectable;
	}

	private static Selectable $(Selectable selectable, String selector)
	{
		return selectable.$(selector);
	}
	
	private static Selectable xpath(Selectable selectable, String selector)
	{
		return selectable.xpath(selector);
	}
	
	private static Selectable regex(Selectable selectable, String selector)
	{
		return selectable.regex(selector);
	}
}
