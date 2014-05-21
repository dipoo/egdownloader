package org.arong.book.ini;

/**
 * 定义解析ini文件的过程中文件必要字段及属性格式的验证异常
 * 
 * @author 阿荣
 * @since 2013-8-18
 */
public class IniException extends Exception {

	private static final long serialVersionUID = 1L;

	public IniException(String message){
		super(message);
	}
	
	public IniException(String key, String property, String errorMsg) {
		super(Configuration.INI_FILE_NAME + "文件不符合规范：[" + key + "]的" + property + "属性" + errorMsg);
	}
}
