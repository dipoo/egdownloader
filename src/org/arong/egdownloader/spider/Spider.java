package org.arong.egdownloader.spider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
/**
 * 分析指定的url或者html源码页面，抓取网页特定内容
 * @author 阿荣
 * @since 2013-8-18
 *
 */
public final class Spider {
	/**
     * 分析指定链接结果，并返回字符串数值
     * @param searchURL
     * @param anchor 所要抓取字符串的前面的特定字符串
     * @param trail 所要抓取字符串的后面紧跟的特定字符串
     * @return
	 * @throws SpiderException 
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception 
     */
    public static String getText(final String url, final String encoding, final String prefix,
            final String suffix) throws Exception {
        String text = null;
        String serverResponse = WebClient.getRequestUseJava(url, encoding);//WebClient.postRequest(url, encoding);
        if(serverResponse != null){
	        int pos = serverResponse.indexOf(prefix);
	        if (pos != -1) {
	            serverResponse = serverResponse.substring(pos + prefix.length());
	            pos = serverResponse.indexOf(suffix);
	            if(pos != -1) {
	            	text = serverResponse.substring(0, pos).trim();
	            } else{
		        	throw new SpiderException(url, "---" + prefix + "之后找不到标识符：", suffix);
		        }
	        } else {
	        	throw new SpiderException(url, "-找不到标识符：", prefix);
	        }
        }
        return text;
    }
    public static String getTextUseJava(final String url, final String encoding, final String prefix,
            final String suffix) throws Exception {
        String text = null;
        try {
			String serverResponse = WebClient.getRequestUseJava(url, encoding);
			if(serverResponse != null){
			    int pos = serverResponse.indexOf(prefix);
			    if (pos != -1) {
			        serverResponse = serverResponse.substring(pos + prefix.length());
			        pos = serverResponse.indexOf(suffix);
			        if(pos != -1) {
			        	text = serverResponse.substring(0, pos).trim();
			        } else{
			        	throw new SpiderException(url, "---" + prefix + "之后找不到标识符：", suffix);
			        }
			    } else {
			    	throw new SpiderException(url, "-找不到标识符：", prefix);
			    }
			}
		} catch (IOException e) {
			throw new WebClientException(url + ":解析错误");
		}
        return text;
    }
    /**
     * 分析指定html源码，并返回字符串数值
     * @param source
     * @param prefix 所要抓取字符串的前面的特定字符串
     * @param suffix 所要抓取字符串的后面紧跟的特定字符串
     * @return
	 * @throws SpiderException 
	 * @throws Exception 
     */
    public static String getTextFromSource(final String source, final String prefix,
            final String suffix) throws SpiderException{
        String text = null;
        String serverResponse = source;
        if(serverResponse != null){
	        int pos = serverResponse.indexOf(prefix);
	        if (pos != -1) {
	            serverResponse = serverResponse.substring(pos + prefix.length());
	            pos = serverResponse.indexOf(suffix);
	            if(pos != -1){
	            	text = serverResponse.substring(0, pos).trim();
	            } else {
	            	throw new SpiderException("---" + prefix + "之后找不到标识符：", suffix);
	            }
	        } else{
	        	throw new SpiderException("找不到标识符：", prefix);
	        }
        }
        return text;
    }
    
    public static String substring(String htmlSource, String prefix) throws SpiderException{
    	String text = null;
    	if(htmlSource != null){
	        int pos = htmlSource.indexOf(prefix);
	        if (pos != -1) {
	        	text = htmlSource.substring(pos + prefix.length());
	        } else{
	        	throw new SpiderException("找不到标识符：", prefix);
	        }
        }
        return text;
    }
    
    public static String substring(String htmlSource, String prefix, int step) throws SpiderException{
    	String text = null;
    	if(htmlSource != null){
	        int pos = htmlSource.indexOf(prefix);
	        if (pos != -1) {
	        	text = htmlSource.substring(pos + prefix.length() + step);
	        } else{
	        	throw new SpiderException("找不到标识符：", prefix);
	        }
        }
        return text;
    }
    
    public static Boolean containText(String url, String encoding, String text) throws Exception{
    	String serverResponse = WebClient.getRequestUseJava(url, encoding);//WebClient.postRequest(url, encoding);
    	containTextFromSource(serverResponse, text);
    	return true;
    }
    
    /**
     * 判断源码中是否包含指定的字符串，如果不包含则抛出一个异常
     * @param htmlSource
     * @param text
     * @return
     * @throws SpiderException
     */
    public static Boolean containTextFromSource(String htmlSource, String text) throws SpiderException{
    	if(htmlSource != null){
	        int pos = htmlSource.indexOf(text);
	        if (pos != -1) {
	        	return true;
	        } else{
	        	throw new SpiderException("找不到标识符：", text);
	        }
        }
    	return false;
    }
}
