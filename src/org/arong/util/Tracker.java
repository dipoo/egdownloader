package org.arong.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 控制台追踪
 * @author 阿荣
 * @since 2014-05-27
 */
public final class Tracker {
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	public static void println(@SuppressWarnings("rawtypes") Class clazz, String message){
		System.out.println(sdf.format(new Date()) + "-" + clazz.getSimpleName() + "-" + message);
	}
}
