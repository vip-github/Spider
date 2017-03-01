package com.spider.formatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;

public class DateFormatter {
	public final String type = "date";
	
	public final Pattern datetimePattern = Pattern.compile("([1-2]\\d{3})\\D{1,3}([0-1]?\\d)\\D{1,3}([0-2]?\\d)\\D{1,3}([0-2]?\\d?)\\D{0,3}([0-5]?\\d?)\\D{0,3}([0-5]?\\d?)");
	
	public String getType() {
		return type;
	}
	
	public String format(String content, String format){
		if(!Strings.isNullOrEmpty(content)){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				Matcher matcher = datetimePattern.matcher(content);
				if(matcher.find())
				{
					int groupCount = matcher.groupCount();
					if(groupCount>=3)
					{
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						for(int i=1; i<=groupCount; i++)
						{
							String str = matcher.group(i);
							if(StringUtils.isNotBlank(str))
							{
								Integer intStr = Integer.parseInt(str);
								switch (i)
								{
								case 1:
									calendar.set(Calendar.YEAR, intStr);
									break;
								case 2:
									calendar.set(Calendar.MONTH, intStr-1);
									break;
								case 3:
									calendar.set(Calendar.DAY_OF_MONTH, intStr);
									break;
								case 4:
									calendar.set(Calendar.HOUR_OF_DAY, intStr);
									break;
								case 5:
									calendar.set(Calendar.MINUTE, intStr);
									break;
								case 6:
									calendar.set(Calendar.SECOND, intStr);
									break;
								}
							}else
							{
								break;
							}
						}
						content = dateFormat.format(calendar.getTime());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}
}
