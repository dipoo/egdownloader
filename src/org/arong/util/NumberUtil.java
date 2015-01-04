package org.arong.util;
/**
 * int整型工具类
 * @author arong
 * @since 1.0.5
 */
import java.util.regex.Pattern;

public class NumberUtil {
	/**
	 * 判断一个字符串是否可以转换成int
	 * @param str
	 * @return
	 */
	public static boolean can2Int(String str){
		if(str == null && "".equals(str))
			return false;
		if(str.startsWith("-")){
			String str2 = str.substring(1, str.length());
			return Pattern.matches("[0]||[1-9][0-9]{0,9}", str2) && Long.parseLong(str2) < Long.parseLong("2147483649");
		}
		else
			return Pattern.matches("[0]||[1-9][0-9]{0,9}", str) && Long.parseLong(str) < Long.parseLong("2147483648");
	}
	/**
	 * 判断一个long是否可以转换成int
	 * @param lon
	 * @return
	 */
	public static boolean can2Int(long lon){
		return lon < 2147483647 && lon > -2147483648;
	}
	/**
	 * 判断一个字符串是否可以转换为long
	 * @param str
	 * @return
	 */
	public static boolean can2Long(String str){
		if(str == null && "".equals(str))
			return false;
		if(str.startsWith("-")){
			String str2 = str.substring(1, str.length());
			return Pattern.matches("[0]||[1-9][0-9]{0,18}", str2);
		}
		else
			return Pattern.matches("[0]||[1-9][0-9]{0,18}", str);
	}
	/**
	 * 将一个字符串数组转换成int数组
	 * @param strs
	 * @return
	 */
	public static int[] parseInt(String[] strs){
		int[] strs_int = new int[strs.length];
		for (int i = 0; i < strs_int.length; i++) {
			if(can2Int(strs[i])){
				strs_int[i] = Integer.parseInt(strs[i]);
			}
		}
		return strs_int;
	}
	/**
	 * 把int数组转换成long数组
	 * @param ints
	 * @return
	 */
	public static long[] parseLong(int[] ints){
		long[] longs = new long[ints.length];
		for (int i = 0; i < longs.length; i++) {
			longs[i] = (long)ints[i];
		}
		return longs;
	}
	/**
	 * 把long数组转换成int数组
	 * @param longs
	 * @return
	 */
	public static int[] parseInt(long[] longs){
		int[] strs_int = new int[longs.length];
		for (int i = 0; i < strs_int.length; i++) {
			if(can2Int(longs[i])){
				strs_int[i] = (int)longs[i];
			}
		}
		return strs_int;
	}
}
