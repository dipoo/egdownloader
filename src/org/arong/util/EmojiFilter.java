package org.arong.util;

import org.apache.commons.lang.StringUtils;

public class EmojiFilter {
	/**
	 * 检测是否有emoji字符
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
	    if (StringUtils.isBlank(source)) {
	        return false;
	    }

	    int len = source.length();

	    for (int i = 0; i < len; i++) {
	        char codePoint = source.charAt(i);

	        if (isEmojiCharacter(codePoint)) {
	            //do nothing，判断到了这里表明，确认有表情字符
	            return true;
	        }
	    }

	    return false;
	}

	public static boolean isEmojiCharacter(char codePoint) {
	    return (codePoint == 0x0) || 
	            (codePoint == 0x9) ||                            
	            (codePoint == 0xA) ||
	            (codePoint == 0xD) ||
	            ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
	            ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
	            ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
	
	public static final String[] filterChars = {"\\\\ud83e\\\\udddb\\\\u200d\\\\u2640\\\\ufe0f",
			"\\\\ud83e\\\\udddf\\\\u200d\\\\u2640\\\\ufe0f", "\\\\u26be\\\\ud83d\\\\udc08", 
			"\\\\ud83d\\\\udc7b", "\\\\ud83e\\\\udd16", "\\\\ud83d\\\\udc3b", "\\\\ud83d\\\\udc7d",
			"\\\\ud83d\\\\udc2a", "\\\\ud83d\\\\udc08", "\\\\ud83d\\\\udc04", "\\\\ud83e\\\\udd80",
			"\\\\ud83e\\\\udd95", "\\\\ud83d\\\\udc29", "\\\\ud83d\\\\udc2c", "\\\\ud83d\\\\udc09",
			"\\\\ud83d\\\\udc18", "\\\\ud83d\\\\udc1f", "\\\\ud83d\\\\udc51", "\\\\ud83d\\\\udc53",
			"\\\\ud83d\\\\udc8b", "\\\\ud83d\\\\udc8f", "\\\\ud83d\\\\udeac", "\\\\ud83d\\\\udcaa",
			"\\\\ud83d\\\\udc59", "\\\\ud83e\\\\udd36", "\\\\ud83d\\\\udc58", "\\\\ud83d\\\\udca9",
			"\\\\u270f\\\\ufe0f", "\\\\ud83d\\\\udc6a", "\\\\ud83c\\\\udf20", "\\\\ud83d\\\\udcd6",
			"\\\\ud83d\\\\udd2a", "\\\\ud83c\\\\udfc0", "\\\\ud83c\\\\udfae", "\\\\ud83c\\\\udf74",
			"\\\\u26a2"/*, "\\\\u270f", "\\\\u2744", "\\\\u2200", "\\\\u2764",
			"\\\\u2642"*/, "\\\\u26e9", "\\\\ufe0f"};

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if(StringUtils.isBlank(source)){
			return source;
		}
		
		String tmp = UnicodeUtil.stringToUnicode(source);
		for(String c : filterChars){
			tmp = tmp.replaceAll(c, "");
		}
		tmp = UnicodeUtil.unicodeToString(tmp);
		tmp = tmp.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
	    if (!containsEmoji(tmp)) {
	        return tmp;//如果不包含，直接返回
	    }
	    //到这里铁定包含
	    StringBuilder buf = null;
	    int len = tmp.length();
	    for (int i = 0; i < len; i++) {
	        char codePoint = tmp.charAt(i);

	        if (isEmojiCharacter(codePoint)) {
	            if (buf == null) {
	                buf = new StringBuilder(tmp.length());
	            }
	            buf.append(codePoint);
	        } else {
	            buf.append("*");
	        }
	    }

	    if (buf == null) {
	        return tmp;//如果没有找到 emoji表情，则返回源字符串
	    } else {
	        if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
	            buf = null;
	            return tmp;
	        } else {
	            return buf.toString();
	        }
	    }
	}

}
