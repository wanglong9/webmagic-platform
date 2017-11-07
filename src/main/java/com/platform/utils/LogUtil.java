package com.platform.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 日志工具类
 * @author 刘太信 
 * @date 2017年8月7日 下午5:34:06  
 */
public class LogUtil {
	private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

	/** 
	 * 获取系统logger
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:34:16 
	 * @return logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/** 
	 * error
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:34:28 
	 * @param message 待打印的文字
	 */
	public static void error(String message) {
		getLogger().error(message);
	}

	/** 
	 * error
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:34:33 
	 * @param message 待打印的文字
	 * @param t 异常
	 */
	public static void error(String message, Throwable t) {
		getLogger().error(message, t);
	}

	/** 
	 * error
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:34:59 
	 * @param t 异常
	 */
	public static void error(Throwable t) {
		getLogger().error(t.getMessage(), t);
	}

	/** 
	 * debug
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:01 
	 * @param message 待打印的文字
	 */
	public static void debug(String message) {
		getLogger().debug(message);
	}

	/** 
	 * debug
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:03 
	 * @param message 待打印的文字
	 * @param t 异常
	 */
	public static void debug(String message, Throwable t) {
		getLogger().debug(message, t);
	}

	/** 
	 * debug
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:05 
	 * @param t 异常
	 */
	public static void debug(Throwable t) {
		getLogger().debug(t.getMessage(), t);
	}

	/** 
	 * info
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:07 
	 * @param message 待打印的文字
	 */
	public static void info(String message) {
		getLogger().info(message);
	}

	/** 
	 * info
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:09 
	 * @param message 待打印的文字
	 * @param t 异常
	 */
	public static void info(String message, Throwable t) {
		getLogger().info(message, t);
	}

	/** 
	 * info
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:11 
	 * @param t 异常
	 */
	public static void info(Throwable t) {
		getLogger().info(t.getMessage(), t);
	}

	/** 
	 * warn
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:13 
	 * @param message 待打印的文字
	 */
	public static void warn(String message) {
		getLogger().warn(message);
	}

	/** 
	 * warn
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:17 
	 * @param message 待打印的文字
	 * @param t 异常
	 */
	public static void warn(String message, Throwable t) {
		getLogger().warn(message, t);
	}

	/** 
	 * warn
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:19 
	 * @param t 异常
	 */
	public static void warn(Throwable t) {
		getLogger().warn(t.getMessage(), t);
	}

	/** 
	 * trace
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:21 
	 * @param message 待打印的文字
	 */
	public static void trace(String message) {
		getLogger().trace(message);
	}

	/** 
	 * trace
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:23 
	 * @param message 待打印的文字
	 * @param t 异常
	 */
	public static void trace(String message, Throwable t) {
		getLogger().trace(message, t);
	}

	/** 
	 * trace
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:25 
	 * @param t 异常
	 */
	public static void trace(Throwable t) {
		getLogger().trace(t.getMessage(), t);
	}

	/** 
	 * exception
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:28 
	 * @param message 待打印的文字
	 */
	public static void exception(String message) {
		getLogger().warn(message);
	}

	/** 
	 * exception
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:30 
	 * @param message 待打印的文字
	 * @param t 异常
	 */
	public static void exception(String message, Throwable t) {
		getLogger().warn(message, t);
	}

	/** 
	 * exception
	 * @author 刘太信 
	 * @date 2017年8月7日 下午5:35:32 
	 * @param t 异常
	 */
	public static void exception(Throwable t) {
		getLogger().warn(t.getMessage(), t);
	}
}
