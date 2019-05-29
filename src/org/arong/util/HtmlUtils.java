package org.arong.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class HtmlUtils {

	public static String Html2Text(String inputString) {
        if (StringUtils.isEmpty(inputString)) {
            return "";
        }

        // 含html标签的字符串
        String htmlStr = inputString.trim();
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_space;
        Matcher m_space;
        Pattern p_escape;
        Matcher m_escape;

        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";

            // 定义空格回车换行符
            //String regEx_space = "\\s*|\t|\r|\n";

            // 定义转义字符
            String regEx_escape = "&.{2,6}?;";

            // 过滤script标签
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll("");

            // 过滤style标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("");

            // 过滤html标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("");

            // 过滤空格回车标签
            /*p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
            m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll("");*/

            // 过滤转义字符
            p_escape = Pattern.compile(regEx_escape, Pattern.CASE_INSENSITIVE);
            m_escape = p_escape.matcher(htmlStr);
            htmlStr = m_escape.replaceAll("");

            textStr = htmlStr;

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回文本字符串
        return textStr;
    }
	
	public static String redColorHtml(String msg){
		if(StringUtils.isNotBlank(msg)){
			return String.format("<font style='color:red'>%s</font>", msg);
		}
		return null;
	}
	public static String redColorLabelHtml(String msg){
		if(StringUtils.isNotBlank(msg)){
			return String.format("<html><font style='color:red'>%s</font></html>", msg);
		}
		return null;
	}
	public static String greenColorHtml(String msg){
		if(StringUtils.isNotBlank(msg)){
			return String.format("<font style='color:green'>%s</font>", msg);
		}
		return null;
	}
	public static String colorHtml(String msg, String color){
		if(StringUtils.isNotBlank(msg)){
			return String.format("<font style='color:%s'>%s</font>", color, msg);
		}
		return null;
	}
	public static final String[] EMOJI_FILTER_CHARS = {"\\\\ud83e\\\\udddb\\\\u200d\\\\u2640\\\\ufe0f",
		"\\\\ud83e\\\\udddf\\\\u200d\\\\u2640\\\\ufe0f", "\\\\u26be\\\\ud83d\\\\udc08", 
		"\\\\ud83d\\\\udc7b", "\\\\ud83e\\\\udd16", "\\\\ud83d\\\\udc3b", "\\\\ud83d\\\\udc7d",
		"\\\\ud83d\\\\udc2a", "\\\\ud83d\\\\udc08", "\\\\ud83d\\\\udc04", "\\\\ud83e\\\\udd80",
		"\\\\ud83e\\\\udd95", "\\\\ud83d\\\\udc29", "\\\\ud83d\\\\udc2c", "\\\\ud83d\\\\udc09",
		"\\\\ud83d\\\\udc18", "\\\\ud83d\\\\udc1f", "\\\\ud83d\\\\udc51", "\\\\ud83d\\\\udc53",
		"\\\\ud83d\\\\udc8b", "\\\\ud83d\\\\udc8f", "\\\\ud83d\\\\udeac", "\\\\ud83d\\\\udcaa",
		"\\\\ud83d\\\\udc59", "\\\\ud83d\\\\udc58", "\\\\ud83d\\\\udca9",
		"\\\\u270f\\\\ufe0f", "\\\\ud83d\\\\udc6a", "\\\\ud83c\\\\udf20", "\\\\ud83d\\\\udcd6",
		"\\\\ud83d\\\\udd2a", "\\\\ud83c\\\\udfc0", "\\\\ud83c\\\\udfae", "\\\\ud83c\\\\udf74",
		"\\\\u26a2", "\\\\u26a3", "\\\\u26a4"/*, , "\\\\u26e9", "\\\\ufe0f"*/, "\\\\u9891"};
	// "\u270f"✏, "\u2744"❄, "\u2200"∀, "\u2764"❤, "\u2642"♂ 默认支持
	public static final String[] UNPARSE_FILTER_CHARS = {"\\\\u26e9", "\\\\ufe0f", "\\\\ud83e\\\\udd36", "\ud83e\udd21"};//无法解析的字符，替换为空
	public final static Pattern EMOJI_PATTERN = Pattern.compile("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]");
	public static String filterEmoji2SegoeUISymbolFont(String source){
		if(StringUtils.isNotBlank(source)){
			String tmp = UnicodeUtil.stringToUnicode(source);
			for(String c : UNPARSE_FILTER_CHARS){
				tmp = tmp.replaceAll(c, "");
			}
			for(String c : EMOJI_FILTER_CHARS){
				tmp = tmp.replaceAll(c, "\\\\u003c\\\\u0073\\\\u0070\\\\u0061\\\\u006e\\\\u0020\\\\u0073\\\\u0074\\\\u0079\\\\u006c\\\\u0065\\\\u003d\\\\u0022\\\\u0066\\\\u006f\\\\u006e\\\\u0074\\\\u002d\\\\u0066\\\\u0061\\\\u006d\\\\u0069\\\\u006c\\\\u0079\\\\u003a\\\\u0027\\\\u0053\\\\u0065\\\\u0067\\\\u006f\\\\u0065\\\\u0020\\\\u0055\\\\u0049\\\\u0020\\\\u0053\\\\u0079\\\\u006d\\\\u0062\\\\u006f\\\\u006c\\\\u0027\\\\u0022\\\\u003e" + c + "\\\\u003c\\\\u002f\\\\u0073\\\\u0070\\\\u0061\\\\u006e\\\\u003e");
			}
			tmp = UnicodeUtil.unicodeToString(tmp);
			
			Matcher mt = EMOJI_PATTERN.matcher(tmp);
			String formatGroup = null;
			while(mt.find()){
 	 			formatGroup = mt.group();
 	 			tmp = tmp.replaceAll(formatGroup, String.format("<span style=\"font-family:'Segoe UI Symbol','Segoe UI Emoji'\">%s</span>", formatGroup));
 	 		}
			
		    if (!EmojiFilter.containsEmoji(tmp)) {
		        return tmp;//如果不包含，直接返回
		    }
		    //到这里铁定包含
		    StringBuilder buf = null;
		    int len = tmp.length();
		    for (int i = 0; i < len; i++) {
		        char codePoint = tmp.charAt(i);

		        if (EmojiFilter.isEmojiCharacter(codePoint)) {
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
		return source;
	}
}
