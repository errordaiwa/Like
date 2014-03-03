package cn.com.sina.like.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
	// public static void main(String[] args) {
	// Log.i("aa", "test");
	// }

	public static void i(String simpleClassName, String message) {
		Logger logger = LogManager.getLogger(simpleClassName);
		logger.info(message);
	}

	public static void d(String simpleClassName, String message) {
		Logger logger = LogManager.getLogger(simpleClassName);
		logger.debug(message);
	}

	public static void w(String simpleClassName, String message) {
		Logger logger = LogManager.getLogger(simpleClassName);
		logger.warn(message);
	}

	public static void e(String simpleClassName, String message) {
		Logger logger = LogManager.getLogger(simpleClassName);
		logger.error(message);
	}

	public static void f(String simpleClassName, String message) {
		Logger logger = LogManager.getLogger(simpleClassName);
		logger.fatal(message);
	}

	public static String getExceptionStackTrace(Exception e) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(os));
		try {
			os.flush();
			return os.toString();
		} catch (IOException e1) {
		} finally {
			try {
				os.close();
			} catch (IOException e2) {
			}
		}
		return null;
	}

}
