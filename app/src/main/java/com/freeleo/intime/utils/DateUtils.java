package com.freeleo.intime.utils;

import android.content.Context;
import android.util.Log;

import com.freeleo.intime.ui.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description: 日期工具类
 * Author: lizhen
 * Create Date: 2018-03-06
 */
public class DateUtils {
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat format_str = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat format_hm = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat format_str2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
	public static final SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
	// 时间
	public static long SECOND = 1000;// 秒
	public static long MINUTE = SECOND * 60;// 分
	public static long HOUR = MINUTE * 60;// 时
	public static long DAY = HOUR * 24;// 天

	public static String format(Date date) {
		return format.format(date);
	}

	public static String format2(Date date) {
		return format_str2.format(date);
	}

	public static String formatDate(long date){
		return format_date.format(new Date(date));
	}

	public static String formatHM(Date date) {
		return format_hm.format(date);
	}

	public static String format(long time) {
		if (String.valueOf(time).length() == 10)
			time = time * 1000;
		return format(new Date(time));
	}

	public static String getCurrentTime() {
		try {
			Date date = new Date();
			return format_str.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0000000000";
	}

	public static long date2TimeStamp(String date_str,SimpleDateFormat format) {
		try {
			Date date = format.parse(date_str);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String formatInTime(Context context,long stamp){
		long day = stamp / (24 * 60 * 60 * 1000);
		long hour = stamp % (24 * 60 * 60 * 1000) / (60 * 60 * 1000);
		long minute = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) / (60 * 1000);
		long second = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) % (60 * 1000) / 1000;
		long millisecond = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) % (60 * 1000) % 1000;
		return addZero(day) + context.getResources().getString(R.string.days)+addZero(hour)+context.getResources().getString(R.string.hours)+addZero(minute)+context.getResources().getString(R.string.minutes)+addZero(second)+context.getResources().getString(R.string.seconds)+addTwoZero(millisecond)+context.getResources().getString(R.string.milliseconds);
	}

	public static String formatInTimeNone(Context context,long stamp){
		long day = stamp / (24 * 60 * 60 * 1000);
		long hour = stamp % (24 * 60 * 60 * 1000) / (60 * 60 * 1000);
		long minute = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) / (60 * 1000);
		long second = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) % (60 * 1000) / 1000;
		long millisecond = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) % (60 * 1000) % 1000;
		return addZero(day) + "."+addZero(hour)+"."+addZero(minute)+"."+addZero(second)+"."+addTwoZero(millisecond);
	}

	public static String formatInTimeNoneNoSceond(Context context,long stamp){
		long day = stamp / (24 * 60 * 60 * 1000);
		long hour = stamp % (24 * 60 * 60 * 1000) / (60 * 60 * 1000);
		long minute = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) / (60 * 1000);
		long second = stamp % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) % (60 * 1000) / 1000;
		return addZero(day) + "."+addZero(hour)+"."+addZero(minute)+"."+addZero(second);
	}

	public static String addZero(long i){
		return i<10?"0"+i:i+"";
	}

	public static String addTwoZero(long i){
		String result = "";
		if(i>=100){
			result = i+"";
		}else if(i<100 && i >= 10){
			result = "0"+i;
		}else{
			result = "00"+i;
		}
		return result;
	}

	public static String getUTCDate() {
		try {
			//1、取得本地时间：
			Calendar cal = Calendar.getInstance();
			//2、取得时间偏移量：
			int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
			//3、取得夏令时差：
			int dstOffset = cal.get(Calendar.DST_OFFSET);
			//4、从本地时间里扣除这些差量，即可以取得UTC时间：
			cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
			//之后调用cal.get(int x)或cal.getTimeInMillis()方法
			//所取得的时间即是UTC标准时间。
			Date date = new Date(cal.getTimeInMillis());
			return format_date.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0000000000";
	}

	public static String formatStr2(long time) {
		try {
			if (String.valueOf(time).length() == 10)
                time = time * 1000;
			return format2(new Date(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static long getTime(String dateStr) {

		try {
			Date date = format.parse(dateStr);

			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static String now() {
		return format.format(new Date());
	}

	/**
	 * 是否是同一天
	 * 
	 * @return
	 */
	public static boolean isSameDay(long data1, long data2) {
		try {
			Date date1 = new Date(data1);
			Date date2 = new Date(data2);

			return isSameDay(date1, date2);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 是否是同一天
	 * 
	 * @return
	 */
	public static boolean isSameDay(String dateStr1, String dateStr2) {
		try {
			Date date1 = format.parse(dateStr1);
			Date date2 = format.parse(dateStr2);

			return isSameDay(date1, date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 是否是同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		} else {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			return isSameDay(cal1, cal2);
		}
	}

	/**
	 * time以内
	 * 
	 * @param dateStr
	 * @return
	 */
	public static boolean isInside(String dateStr, long time) {

		long last = DateUtils.getTime(dateStr);
		long now = System.currentTimeMillis();
		if ((now - last) < time)
			return true;

		return false;
	}

	/**
	 * 一小时以外
	 * 
	 * @param dateStr
	 * @return
	 */
	public static boolean isOutHour(String dateStr, long time) {
		long last = DateUtils.getTime(dateStr);
		long now = System.currentTimeMillis();
		if ((now - last) >= time)
			return true;

		return false;
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null)
			throw new IllegalArgumentException("The date must not be null");
		else
			return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
					&& cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE);
	}

	public static boolean after(Date date) {
		long time = date.getTime();
		long now = System.currentTimeMillis();
		if (now > time)
			return true;

		return false;
	}

	/**
	 * 定时任务，精确到秒
	 * 
	 * @param runnable
	 *            执行的任务
	 * @param hour
	 *            时（24小时制）
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 */
	public static void schedule(final Runnable runnable, final int hour, final int minute, final int second) {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		schedule(runnable, cal.getTime());
	}

	/**
	 * 定时任务
	 * 
	 * @param runnable
	 *            执行的任务
	 * @param date
	 *            执行的时间
	 */
	public static Timer schedule(final Runnable runnable, final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		Timer mTimer = new Timer(false);// 非守候线程
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				Calendar now = Calendar.getInstance();
				now.add(Calendar.MINUTE, -1);// 允许1分钟误差

				// 首次执行时间过了首次不执行
				if (now.after(cal)) {
					return;
				}

				runnable.run();// 执行操作
			}
		};

		mTimer.schedule(timerTask, cal.getTime());

		return mTimer;
	}

	public static Timer schedule(final Runnable runnable, final Date date, long period) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar now = Calendar.getInstance();
		Date d = cal.getTime();
		if (now.after(cal)) {
			d = addDay(d, 1);
		}
		Timer mTimer = new Timer(false);// 非守候线程
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				runnable.run();// 执行操作
			}
		};

		mTimer.scheduleAtFixedRate(timerTask, d, period);

		return mTimer;
	}

	// 增加或减少天数
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	public static String getRegisterTopicTime(long time){
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//		calendar.add(Calendar.DAY_OF_MONTH,1);
		return "register_"+sdf.format(new Date(Long.valueOf(time+"000")));
	}

	/**
	 * 得到当前时间戳
	 * 
	 * @return
	 */
	public static String getStrTimestamp() {

		return getStrTimestamp(10);

	}

	/**
	 * 得到当前时间戳
	 * 
	 * @return
	 */
	public static String getStrTimestamp(int maxLength) {
		String t = String.valueOf(System.currentTimeMillis());
		t = t.substring(0, Math.min(maxLength, t.length()));

		return t;

	}

	/**
	 * 得到当前时间戳
	 * 
	 * @return
	 */
	public static long getTimestamp() {

		return getTimestamp(10);

	}

	/**
	 * 得到当前时间戳
	 * 
	 * @return
	 */
	public static long getTimestamp(int maxLength) {
		String t = String.valueOf(System.currentTimeMillis());
		t = t.substring(0, Math.min(maxLength, t.length()));
		return Long.valueOf(t);
	}

	/**
	 * 获取当前0区时间
	 * @return
	 */
	public static long getNowTimeStamp(){
		Calendar calendar = Calendar.getInstance();
		// 获取当前时区下日期时间对应的时间戳
		long unixTime = calendar.getTimeInMillis();
		// 获取标准格林尼治时间下日期时间对应的时间戳
		long unixTimeGMT = unixTime;
		return unixTimeGMT;
	}

	/**
	 * 根据0区时间获取当前时间戳
	 * @return
	 */
	public static long getGMT0TimeStamp(long time){
		return time + TimeZone.getDefault().getRawOffset();
	}


	/**
	 * 获取更改时区后的日期
	 *
	 * @param date    日期
	 * @param oldZone 旧时区对象
	 * @param newZone 新时区对象
	 * @return 日期
	 */
	public static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
		Date dateTmp = null;
		if (date != null) {
			int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
			dateTmp = new Date(date.getTime() - timeOffset);
		}
		return dateTmp;
	}


	/**
	 * 默认是本地时区进行格式化
	 * @param time
	 * @param format
	 * @return
	 */
	public static String simpleDateFormat(Date time, String format){
		String result=null;
		SimpleDateFormat fm=new SimpleDateFormat(format);
		result =fm.format(time);
		return result;
	}


    /**
     * 对时间进行格式化，根据时间返回不同的时间区段“刚刚” “11：23”等等
     * @param timestamp    毫秒级时间戳
     * @return
     */
	public static String timeFormat(long timestamp) {
        String str_time = format(timestamp);
        return timeFormat(str_time);
    }

	/**
	 * 对时间进行格式化，根据时间返回不同的时间区段“刚刚” “11：23”等等
	 *
	 * @param commentTime	2016-10-31 12:30:00
	 * @return
	 */
	public static String timeFormat(String commentTime) {
		String interval = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = format.parse(commentTime);
			long createtime = date.getTime();

			long time = Math.abs(System.currentTimeMillis() - createtime); // 得出的时间间隔是毫秒

			if (time / 1000 < 60 && time / 1000 >= 0) {
				// 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
				interval = "刚刚";

			} else if (time / 1000 >= 60 && time / 1000 / 60 < 60) {
				interval = (int) (time / 1000 / 60) + "分钟前";
			} else if (time / 1000 / 60 >= 60 && time / 1000 / 60 / 60 < 24) {
				// 如果时间间隔小于24小时则显示多少小时前
//                int h = (int) (time / 1000 / 60 / 60);// 得出的时间间隔的单位是小时
//                interval = h + "小时前";
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				interval = sdf.format(createtime);
			} else {
				// 大于24小时，则显示正常的时间，但是不显示秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				interval = sdf.format(createtime);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return interval;
	}

    public static String formatInterval(long interval) {
		if(interval <= 0){
			return " several times";
		}
        StringBuilder sb = new StringBuilder();
		sb.append(" ");
        if (interval > DAY) {
            int day = (int)(interval/DAY);
            sb.append(day + " days ");
            interval = interval - day * DAY;
        }
        if (interval > HOUR) {
            int hour = (int) (interval/HOUR);
            sb.append(hour + " hours ");
            interval = interval - hour * HOUR;
        }
        if (interval > MINUTE) {
            int min = (int) (interval / MINUTE);
            sb.append(min + " minutes ");
            interval = interval - min * MINUTE;
        }
        sb.append((interval/1000<=0?1:interval/1000)+" s");

        return sb.toString();
    }



	public static String formatInterval2(long interval) {
		if(interval <= 0){
			return "Haven't played recently.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Have played ");
		if (interval > DAY) {
			int day = (int)(interval/DAY);
			sb.append(day + " days ");
			interval = interval - day * DAY;
		}
		if (interval > HOUR) {
			int hour = (int) (interval/HOUR);
			sb.append(hour + " hours ");
			interval = interval - hour * HOUR;
		}
		if (interval > MINUTE) {
			int min = (int) (interval / MINUTE);
			sb.append(min + " minutes ");
			interval = interval - min * MINUTE;
		}
		sb.append(interval/1000+" s");

		return sb.toString();
	}

	public static String formatInterval3(long interval){
		if(interval <= 0){
			return "Haven't played recently.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Have played ");
		if (interval > DAY) {
			int day = (int)(interval/DAY);
			return sb.append(day + " days").toString();
		}
		if (interval > HOUR) {
			int hour = (int) (interval/HOUR);
			return sb.append(hour + " hours ").toString();
		}
		if (interval > MINUTE) {
			int min = (int) (interval / MINUTE);
			return sb.append(min + " minutes ").toString();
		}else {
			return sb.append(interval/1000 + " s ").toString();
		}
	}

	public static String formatPlayAgo(long time){
		time = System.currentTimeMillis() -time;
		if (time > DAY) {
			int day = (int)(time/DAY);
			return day + " days";
		}
		if (time > HOUR) {
			int hour = (int) (time/HOUR);
			return hour + " hours ";
		}
		if (time > MINUTE) {
			int min = (int) (time / MINUTE);
			return min + " minutes ";
		}else {
			return "1 minutes";
		}
	}

	public static long toLong(String date1){
		long ts1 = System.currentTimeMillis();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		Date dt1;
		try {
			dt1 = sdf.parse(date1);
			ts1 = dt1.getTime();
			System.out.println(date1 + ": " + ts1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ts1;
	}
	
	
	/**
     * 只显示前两个单位，如果出现0 后移一个单位显示
     *
	 */
	public static String formatTotalPlayedTime(long time){
		String str = "";
        int day = (int)(time/DAY);
        int hour = (int)((time%DAY)/HOUR) ;
        int minute = (int)(((time%DAY)%HOUR)/MINUTE);
        int second = (int)((((time%DAY)%HOUR)%MINUTE)/SECOND);
        
        int num = 2 ;
        
        if (day > 1) {
            str += day + " days " ;
            num -- ;
            if(num == 0){
                return str ;
            }
        } else if (day == 1) {
            str += day + " day ";
            num -- ;
            if(num == 0){
                return str ;
            }
        }
        
        if (hour > 1) {
            str += hour + " hours " ;
            num -- ;
            if(num == 0){
                return str ;
            }
        } else if (hour == 1) {
            str += hour + " hour ";
            num -- ;
            if(num == 0){
                return str ;
            }
        }
        
        
        if (minute > 1) {
            str += minute + " mins " ;
            num -- ;
            if(num == 0){
                return str ;
            }
        } else if (minute == 1) {
            str += minute + " min ";
            num -- ;
            if(num == 0){
                return str ;
            }
        }
        
        if (second > 1) {
            str += second + " sec " ;
            num -- ;
            if(num == 0){
                return str ;
            }
        } else if (second == 1) {
            str += second + " min ";
            num -- ;
            if(num == 0){
                return str ;
            }
        } else {
            if(num == 2){
                // 从来没玩过 No data
                str = "";
            }else{
            
            }
            return str ;
        }
        
		
		return str ;
	}
    
    /**
     *
     * 只显示第一个不为0的单位
     *
     */
	public static String formatLastPlayedTime(long intervalTime){
		String str = "";
        
        int day = (int)(intervalTime/DAY);
        int hour = (int)((intervalTime%DAY)/HOUR) ;
        int minute = (int)(((intervalTime%DAY)%HOUR)/MINUTE);
        int second = (int)((((intervalTime%DAY)%HOUR)%MINUTE)/SECOND);
        
        if (day > 1){
            str = day + " days ago";
        } else if(day == 1){
            str = day + " day ago";
        } else {
            if (hour > 1){
                str = hour + " hours ago";
            } else if(hour == 1){
                str = hour + " hour ago";
            } else {
                if (minute > 1){
                    str = minute + " mins ago";
                } else if(minute == 1){
                    str = minute + " min ago";
                } else {
                    if (second > 1){
                        str = second + " sec ago";
                    } else if(second == 1){
                        str = second + " sec ago";
                    } else {
                        str = ""; // No data
                    }
                }
            }
        }
		
		return str ;
	}
    
    /**
     *
     * 只显示第一个不为0的单位
     *
     */
	public static String formatFirstPlayedTime(long intervalTime){
        return formatLastPlayedTime(intervalTime);
	}
}
