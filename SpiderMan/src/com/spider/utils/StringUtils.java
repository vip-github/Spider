package com.spider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

public class StringUtils
{
	static Pattern numberPattern = Pattern.compile("\\D*([\\d.,]+)\\D*");
	
	/**
	 * MD5
	 * @param content
	 * @return
	 */
	public static String md5(String content)
	{
		return Hashing.md5().hashString(org.apache.commons.lang3.StringUtils.strip(content), Charsets.UTF_8).toString();
	}

	/**
	 * 判断是否只包含处数字外的其他字符
	 * @param str
	 * @return
	 */
	public static boolean isAllNumeric(String str)
	{
		for (int i = str.length(); --i >= 0;)
		{
			if (!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 提取数字
	 * 99万+替换成990000
	 * 9.9万+替换成99000
	 * 99+替换成99
	 * 999.999,999替换成999999999
	 * 999,999.999替换成999999999
	 * @param content
	 * @return
	 */
	public static String getNumber(String content)
	{
		if(!Strings.isNullOrEmpty(content))
		{
			if(content.trim().equalsIgnoreCase("null"))
			{
				content = "0";
			}else if(content.contains("万"))
			{
				Matcher matcher = numberPattern.matcher(content);
				if(matcher.find())
				{
					String number = matcher.group(1);
					if(!Strings.isNullOrEmpty(number))
					{
						if(number.contains(".") || number.contains(","))
						{
							content = number.replaceAll("\\.|,", "")+"000";
						}else
						{
							content = number+"0000";
						}
					}
				}
			}else
			{
				Matcher matcher = numberPattern.matcher(content);
				if(matcher.find())
				{
					content = matcher.group(1);
				}
			}
		}
		if(Strings.isNullOrEmpty(content))
		{
			content = "0";
		}
		return content;
	}
}
