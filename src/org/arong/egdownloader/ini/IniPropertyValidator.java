package org.arong.egdownloader.ini;

/**
 * ini文件属性值验证器
 * 
 * @author 阿荣
 * @since 2013-8-18
 * 
 */
public final class IniPropertyValidator {

	// 非空验证
	public static Boolean isNull(String str) {
		return str == null || "".equals(str.trim());
	}

	// 布尔值验证器
	public static Boolean notBoolean(String str) {
		return str == null
				|| (!"true".equals(str.trim()) && !"false".equals(str.trim()));
	}

	// 数组个数不一致,str以英文逗号隔开
	public static Boolean invalidArrayNum(String str, int num) {
		if (isNull(str)) {
			return true;
		} else if (str.trim().split(",").length != num) {
			return true;
		}
		return false;
	}

	// 是否是不合法的url，以http或者https开头
	public static Boolean invalidHref(String str) {
		if (isNull(str)) {
			return true;
		} else if (!str.trim().contains("http://")
				&& !str.trim().contains("https://")) {
			return true;
		}
		return false;
	}

	// 数组中是否包含非正整数数字或者字符
	public static Boolean invalidPositiveInteger(String str) {
		if (isNull(str)) {
			return true;
		}
		String[] nums = str.trim().split(",");
		int length = nums.length;
		for (int i = 0; i < length; i++) {
			try {
				if (Integer.parseInt(nums[i]) < 1) {
					return true;
				}
			} catch (Exception e) {
				return true;
			}
		}
		return false;
	}
}
