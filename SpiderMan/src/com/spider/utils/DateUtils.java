package com.spider.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static ThreadLocal<SimpleDateFormat> dateformatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	public static String current() {
		SimpleDateFormat dateFormat = dateformatThreadLocal.get();
		return dateFormat.format(Calendar.getInstance().getTime());
	}
	
	public static String format(Date date){
		SimpleDateFormat dateFormat = dateformatThreadLocal.get();
		return dateFormat.format(date);
	}
}
