package com.kdkj.koudailicai.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/*
 * 格式化异常信息为字符串
 * @author railszhu
 */
public final class ExceptionHelper {

	private ExceptionHelper() {
	}

	public static String stackTraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
