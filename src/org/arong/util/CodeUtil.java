package org.arong.util;

import java.security.MessageDigest;
import java.util.Random;

public class CodeUtil {
	// 定义分隔符常量
		public static final String DIVIDE_CHAR = "%";
		private static final int DIS1 = 18500;
		private static final int DIS2 = -222;
		private static final int RANDOM_NUM = 12345;

		/**
		 * 返回中文(所有字符)字符串的unicode码
		 * 
		 * @param s
		 * @return
		 */
		public static String myEncode(String s) {
			String str = null;
			// 如果待转化的字符串不为空
			if (s != null && s.trim().length() > 0) {
				// 创建字符串缓冲区
				StringBuffer sb = new StringBuffer();
				// 将待转化的字符串转成字符数组
				// 创建一个随机数对象
				Random random = new Random();
				int start = random.nextInt(RANDOM_NUM);
				int end = random.nextInt(RANDOM_NUM);
				sb.append(start + DIVIDE_CHAR);
				char[] chars = s.toCharArray();
				// 遍历此字符数组
				for (int i = 0; i < chars.length; i++) {
					char ch = chars[i];
					sb.append((int) ch + DIS1 + i + DIS2 + start - end
							+ DIVIDE_CHAR);
				}
				sb.append(end);
				// 转化为字符串
				str = sb.toString();
			}
			return str;
		}

		/**
		 * 解码
		 * 
		 * @param codeStr
		 * @return
		 */
		public static String myDecode(String codeStr) {
			String str = null;
			// 待反编码的字符串不为空
			if (codeStr != null && codeStr.trim().length() > 0) {
				// 将字符串以逗号分隔成字符串数组
				String[] codes = codeStr.split("" + DIVIDE_CHAR + "");
				int start = Integer.parseInt(codes[0]);
				int end = Integer.parseInt(codes[codes.length - 1]);
				// 建立字符串缓冲区
				StringBuffer sb = new StringBuffer();
				// 定义一个初始ASCII码变量
				int code = 0;
				// 遍历字符串数组
				for (int i = 1; i < codes.length - 1; i++) {
					try {
						code = Integer.parseInt(codes[i]) - DIS1 - i + 1 - DIS2
								- start + end;
					} catch (NumberFormatException e) {
						e.printStackTrace();
						throw new RuntimeException("这个字符串不符合规格，请传入形如：“1"
								+ DIVIDE_CHAR + "2" + DIVIDE_CHAR + "3"
								+ DIVIDE_CHAR + "4”之类的字符串");
					}
					sb.append((char) code);
				}
				str = sb.toString();
			}
			return str;
		}

		/**
		 * MD5加密相关
		 */
		private static String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "A", "B", "C", "D", "E", "F" };

		private static String byteArrayToHexString(byte[] byteArray) {
			StringBuffer sb = new StringBuffer();
			for (byte b : byteArray) {
				// 将每一个byte转换十六进制数
				sb.append(byteToHexChar(b));
			}
			return sb.toString();
		}

		// 核心
		private static Object byteToHexChar(byte b) {// -128到+127之间
			int n = b;// n=110
			// 如果n为负数
			if (n < 0) {
				// 转正
				n = 256 + n;
			}
			// 第一位是商
			int d1 = n / 16;// d1 = 6
			// 第二位是余数
			int d2 = n % 16;// d2 = 14
			// 拼接字符串
			return hex[d1] + hex[d2];
		}
		/**
		 * MD5加密
		 * @param password
		 * @return
		 * @throws Exception
		 */
		public static String md5Encode(String password) throws Exception {
			if (password == null) {
				throw new Exception();
			}
			// 创建密码生成器(MD5或SHA)
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 将明文转成byte[]
			byte[] byteArray = md5.digest(password.getBytes());
			// 将byte[]转成十六进制的字符串
			String passwordMD5 = byteArrayToHexString(byteArray);
			return passwordMD5;
		}
}
