package com.freeleo.intime.utils;

/**
 * Description: log工具类、设置init
 * Author: lizhen
 * Create Date: 2018-03-06
 */
public class Logger {

	public static final int VERBOSE = android.util.Log.VERBOSE;
	public static final int DEBUG = android.util.Log.DEBUG;
	public static final int INFO = android.util.Log.INFO;
	public static final int WARN = android.util.Log.WARN;
	public static final int ERROR = android.util.Log.ERROR;
	public static final int ASSERT = android.util.Log.ASSERT;

	/** 是否显示日志*/
	private static boolean isLog = true;// false 不显示，true 显示

	private static int filter = DEBUG;

	/**
	 * 设置
	 * @param isLogable	 是否打印log
	 * @param level  当前log级别
     */
	public static void init(boolean isLogable,int level){
		isLog = isLogable;
		filter = level;
	}

	/**
	 * @param level 设置log显示级别
     */
	public static void setFilter(int level) {
		filter = level;
	}

	/**
	 * 获取level级别log是否显示
	 * @param level
	 * @return boolean
     */
	public static boolean isLogable(int level) {
		if (isLog && level >= filter)
			return true;
		else
			return false;
	}

	public static int i(String tag, String msg) {
		return isLogable(INFO) ? android.util.Log.i(tag, msg) : 0;
	}

	public static int i(String tag, String msg, Throwable tr) {
		return isLogable(INFO) ? android.util.Log.i(tag, msg, tr) : 0;
	}

	public static int e(String tag, String msg) {
		return isLogable(ERROR) ? android.util.Log.e(tag, msg) : 0;
	}

	public static int e(String tag, String msg, Throwable tr) {
		return isLogable(ERROR) ? android.util.Log.e(tag, msg, tr) : 0;
	}

	public static int d(String tag, String msg) {
		return isLogable(DEBUG) ? android.util.Log.d(tag, msg) : 0;
	}

	public static int d(String tag, String msg, Throwable tr) {
		return isLogable(DEBUG) ? android.util.Log.d(tag, msg, tr) : 0;
	}

	public static int w(String tag, String msg) {
		return isLogable(WARN) ? android.util.Log.w(tag, msg) : 0;
	}

	public static int w(String tag, String msg, Throwable tr) {
		return isLogable(WARN) ? android.util.Log.w(tag, msg, tr) : 0;
	}

	public static int v(String tag, String msg) {
		return isLogable(VERBOSE) ? android.util.Log.v(tag, msg) : 0;
	}

	public static int v(String tag, String msg, Throwable tr) {
		return isLogable(VERBOSE) ? android.util.Log.v(tag, msg, tr) : 0;
	}

	public static void getTraces(String tag) {
		Exception e = new Exception(tag);
		Logger.w(tag, tag, e);
	}

	/**
	 * 打印程序调用的Task信息
	 * 
	 * @param str
	 */
	public static void printStackTrace(String str) {
		StackTraceElement st[] = Thread.currentThread().getStackTrace();
		for (int i = 0; i < st.length; i++)
			Logger.d(str, i + ":" + st[i]);
	}

	/**
	 * 打印程序调用的Task信息
	 * 
	 * @param str
	 * @param index
	 *            StackTrace起始位置
	 */
	public static void printStackTrace(String str, int index) {
		StackTraceElement st[] = Thread.currentThread().getStackTrace();
		if (index < st.length) {
			for (int i = index; i < st.length; i++)
				Logger.d(str, i + ":" + st[i]);
		} else {
			Logger.d(str, "index invalid");
		}
	}

	/**
	 * 打印程序调用的Task信息
	 * 
	 * @param str
	 * @param begin
	 *            StackTrace起始位置
	 * @param end
	 *            StackTrace结束位置
	 */
	public static void printStackTrace(String str, int begin, int end) {
		StackTraceElement st[] = Thread.currentThread().getStackTrace();
		if (begin < st.length) {
			end = end < st.length ? ++end : st.length;
			for (int i = begin; i < end; i++)
				Logger.d(str, i + ":" + st[i]);
		} else {
			Logger.d(str, "index invalid");
		}
	}
}
