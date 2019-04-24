package org.arong.util;

/**
 * 控制台追踪
 * @author 阿荣
 * @since 2014-05-27
 */
public final class Tracker {
	public static void println(Class<?> clazz, String message){
		System.out.println(clazz.getSimpleName() + "-" + message);
	}
	public static void println(String message){
		System.out.println(message);
	}
}
