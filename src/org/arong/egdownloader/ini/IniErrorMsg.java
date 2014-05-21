package org.arong.egdownloader.ini;

/**
 * 定义基本的config.ini文件约定错误消息
 * 
 * @author 阿荣
 * @since 2013-8-18
 * 
 */
public final class IniErrorMsg {
	public final static String NULL_MESSAGE = "不能为空";
	public final static String BOOLEAN_MESSAGE = "只能取true或者false的值";
	public final static String NULL_GEKAIFU = "：因为" + IniPropertyName.IS_GEKAI + "为true,所以" + IniPropertyName.GEKAIFU + "不能为空";
	public final static String ARRAY_NUM_MESSAGE = "数组个数上下不一致，注意要以英文逗号(,)隔开";
	public final static String HREF_MESSAGE = "url格式不正确，请以http或者https开头，如http://www.shujixiazai.com";
	public final static String NUMBER_MESSAGE = "不能包含非正整数字符，包括大于" + Integer.MAX_VALUE + "的正整数";
}
