package zzyq.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间工具类
 * 
 * @author mg
 */
public class DateUtils {
	public static final String format1 = "yyyy-MM-dd HH:mm:ss";

	public static final String format2 = "yyyyMMddHHmmss";
	
	private static Random random = new Random();

	/**
	 * 由于SimpleDateFormat的线程不安全问题,这里使用ThreadLocal
	 */
	public static ThreadLocal<SimpleDateFormat> simpleDateFormat1 = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(format1);
		};
	};

	public static ThreadLocal<SimpleDateFormat> simpleDateFormat2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(format2);
		}
	};

	/**
	 * 获得时间戳 格式：yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getTimestamp() {
		return simpleDateFormat2.get().format(new Date());
	}
	
	public static long getTimestamp(String datetime) throws Exception {
		return simpleDateFormat1.get().parse(datetime).getTime();
	}

	/**
	 * 获取格式化后的时间
	 */
	public static String getDateFormat(Date date) {
		return simpleDateFormat1.get().format(date);
	}

	/**
	 * 比较两个时间
	 * 
	 * @return 前者大返回1，后者大返回-1
	 */
	public int compareToTime(String publishTime, String createtime) {
		int result = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date ptime = df.parse(publishTime);
			Date ctime = df.parse(createtime);
			Calendar Calendar1 = Calendar.getInstance();
			Calendar1.setTime(ptime);
			Calendar Calendar2 = Calendar.getInstance();
			Calendar2.setTime(ctime);
			result = Calendar1.compareTo(Calendar2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 比较2个Date 如果calendar1>calendar2 返回1 如果calendar1<calendar2 返回-1 相等返回0
	 * 
	 * @param calendar1
	 * @param calendar2
	 * @return
	 */
	public static int compareToTime(Calendar calendar1, Calendar calendar2) {
		return calendar1.compareTo(calendar2);
	}

	public static String getStringTimeFormat(Date date, String dateformat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		return sdf.format(date);
	}

	public static Date getDateTime(String date, String dateformat) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查日期时间是否符合格式并且datetime不能比当前时间大72小时以上 yyyy-MM-dd HH:mm:ss
	 * @param datetime
	 * @return true符合条件,false不符合条件
	 */
	public static boolean checkDatetimeFormat(String datetime) {
		boolean result = false;
		if (StringUtils.isNotBlank(datetime)) {
			try {
				SimpleDateFormat format = simpleDateFormat1.get();
				if (datetime.trim().length() != format.toPattern().length()) {
					result = false;
				}
				Date date = format.parse(datetime.trim());
				Calendar calendar1 = Calendar.getInstance();
				calendar1.setTime(date);
				if (calendar1.get(Calendar.YEAR) < 2000) {
					result = false;
				} else {
					Calendar calendar2 = Calendar.getInstance();
					int hour = calendar2.get(Calendar.HOUR_OF_DAY);
					calendar2.set(Calendar.HOUR_OF_DAY, hour + 72);
					int compareResult = compareToTime(calendar1, calendar2);
					if (compareResult < 0) {
						result = true;
					}
				}
			} catch (ParseException pe) {
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * 如果传入时间为0点,且距离当前时间26小时内,替换时分秒为当前时间的时分秒
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date changeDateTime(Date date) {
		Date current = Calendar.getInstance().getTime();
		if (date.before(current) && date.getHours() == 0 && org.apache.commons.lang3.time.DateUtils.addHours(date, 26).after(current)) {
			if (current.getDay() != date.getDay()) {
				date.setHours(23);
				date.setMinutes(random.nextInt(60));
			} else {
				date.setHours(current.getHours());
				date.setMinutes(current.getMinutes());
				date.setSeconds(current.getSeconds());
			}
		}
		return date;
	}
}
