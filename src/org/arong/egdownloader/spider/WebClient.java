package org.arong.egdownloader.spider;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.arong.util.https.HttpsUtils;

/**
 * 获取远程url地址页面的源文件
 * @author 阿荣
 * @since 2013-8-18
 *
 */
public class WebClient {
	
	public static String postRequest(String url) throws ConnectTimeoutException, SocketTimeoutException, WebClientException{
		return postRequestWithCookie(url, "utf-8", null, null);
	}
	
	public static String postRequest(String url, String encoding) throws ConnectTimeoutException, SocketTimeoutException, WebClientException{
		return postRequestWithCookie(url, encoding, null, null);
	}
	
	public static String postRequestWithCookie(String url, String cookieInfo) throws ConnectTimeoutException, SocketTimeoutException, WebClientException{
		return postRequestWithCookie(url, "utf-8", null, cookieInfo);
	}
	public static String postRequestWithCookie(String url, String encoding, Map<String, String> rawParams, String cookieInfo) throws WebClientException, ConnectTimeoutException, SocketTimeoutException {
		return postRequestWithCookie( url,  encoding,  rawParams, cookieInfo, true);
	}
	
	/**
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数
	 * @return 服务器响应字符串
	 * @throws WebClientException 
	 * @throws ConnectTimeoutException 
	 * @throws SocketTimeoutException 
	 */
	public static String postRequestWithCookie(String url, String encoding, Map<String, String> rawParams, String cookieInfo, boolean requestLocation) throws WebClientException, ConnectTimeoutException, SocketTimeoutException {
		HttpClient httpClient = Proxy.getHttpClient();
		// 创建HttpPost对象。
		PostMethod postMethod = new PostMethod(url);
		postMethod.setDoAuthentication(true);
		postMethod.setFollowRedirects(false);
		//如果参数不为空则添加参数
		if(rawParams != null){
			// 如果传递参数个数比较多的话可以对传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : rawParams.keySet()) {
				// 封装请求参数
				params.add(new NameValuePair(key, rawParams.get(key)));
			}
			NameValuePair[] array = new NameValuePair[params.size()];
			// 设置请求参数
			postMethod.setRequestBody(params.toArray(array));
		}
		//设置cookie
		if(cookieInfo != null){
			postMethod.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
			postMethod.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
			postMethod.setRequestHeader("Cookie", cookieInfo);
		}
		//设置连接超时为20秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		//设置读取超时为20秒
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		int statusCode = 0;
		String result = null;
		try {
			statusCode = httpClient.executeMethod(postMethod);
//			System.out.println("type:" + postMethod.getResponseHeader("content-type"));
			// 如果服务器成功地返回响应
			if (statusCode == 200 || statusCode == 201) {
				// 获取服务器响应字符串
				postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
				result = postMethod.getResponseBodyAsString();
			}else if (statusCode == 302 || statusCode == 301) {
                // 重定向
                String location = postMethod.getResponseHeader("Location").getValue();
                if(requestLocation){
                	return postRequestWithCookie(location, encoding, rawParams, cookieInfo);
                }else{
                	return location;
                }
                
            }
		} catch (SocketTimeoutException e1){
			throw e1;
		} catch (ConnectTimeoutException e1){
			throw e1;
		} catch (HttpException e1) {
			throw new ConnectTimeoutException(url + "：连接异常");
		} catch (IOException e1) {
			throw new WebClientException(url + "：IO异常，请检查网络是否正常");
		} finally{
			postMethod.releaseConnection();
		}
		return result;
	}
	/**
	 * 通过post方式不携带cookie信息请求，并获取cookie信息
	 * @param url
	 * @param encoding
	 * @param rawParams
	 * @return 
	 * @throws WebClientException
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 */
	public static String getCookieByPostWithoutCookie(String url, String encoding, Map<String, String> rawParams) throws WebClientException, ConnectTimeoutException, SocketTimeoutException {
		return getCookieByPostWithCookie(url, encoding, rawParams, null);
	}
	/**
	 * 通过post方式携带cookie信息请求，并获取cookie信息
	 * @param url
	 * @param encoding
	 * @param rawParams
	 * @param cookieInfo
	 * @return 
	 * @throws WebClientException
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 */
	public static String getCookieByPostWithCookie(String url, String encoding, Map<String, String> rawParams, String cookieInfo) throws WebClientException, ConnectTimeoutException, SocketTimeoutException {
		HttpClient httpClient = Proxy.getHttpClient();
		// 创建HttpPost对象。
		PostMethod postMethod = new PostMethod(url);
		postMethod.setDoAuthentication(true);
		postMethod.setFollowRedirects(false);
		//如果参数不为空则添加参数
		if(rawParams != null){
			// 如果传递参数个数比较多的话可以对传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : rawParams.keySet()) {
				// 封装请求参数
				params.add(new NameValuePair(key, rawParams.get(key)));
			}
			NameValuePair[] array = new NameValuePair[params.size()];
			// 设置请求参数
			postMethod.setRequestBody(params.toArray(array));
		}
		//设置cookie
		if(cookieInfo != null){
			postMethod.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
			postMethod.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
			postMethod.setRequestHeader("Cookie", cookieInfo);
		}
		//设置连接超时为20秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		//设置读取超时为20秒
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		int statusCode = 0;
		String result = "";
		try {
			statusCode = httpClient.executeMethod(postMethod);
//			System.out.println("statusCode:" + statusCode);
			// 如果服务器成功地返回响应
			if (statusCode == 200 || statusCode == 201) {
				// 查看 cookie 信息  
			      Cookie[] cookies = httpClient.getState().getCookies();  
//			      System.out.println("body:" + postMethod.getResponseBodyAsString());
			      if (cookies.length == 0) {  
			         System.out.println( "None" );  
			      } else {  
			         for ( int i = 0; i < cookies.length; i++) {
			        	 result += cookies[i].toString() + ";";
//			             System.out.println(cookies[i].toString());
			         }  
			      }
			}else if (statusCode == 302 || statusCode == 301) {
                // 重定向
                String location = postMethod.getResponseHeader("Location").getValue();
                return getCookieByPostWithCookie(location, encoding, rawParams, cookieInfo);
            }
		} catch (SocketTimeoutException e1){
			throw e1;
		} catch (ConnectTimeoutException e1){
			throw e1;
		} catch (HttpException e1) {
			throw new ConnectTimeoutException(url + "：连接异常");
		} catch (IOException e1) {
			throw new WebClientException(url + "：IO异常，请检查网络是否正常");
		} finally{
			postMethod.releaseConnection();
		}
		return result;
	}
	
	public static InputStream postRequestAsStream(String url) throws ConnectTimeoutException, SocketTimeoutException{
		try {
			return postRequestAsStreamWithCookie(url, "utf-8", null, null);
		} catch (WebClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream postRequestAsStream(String url, String encoding) throws ConnectTimeoutException, SocketTimeoutException{
		try {
			return postRequestAsStreamWithCookie(url, encoding, null, null);
		} catch (WebClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream postRequestAsStreamWithCookie(String url, String cookieInfo) throws ConnectTimeoutException, SocketTimeoutException{
		try {
			return postRequestAsStreamWithCookie(url, "utf-8", null, cookieInfo);
		} catch (WebClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数
	 * @return 服务器响应字符串
	 * @throws WebClientException 
	 * @throws ConnectTimeoutException 
	 * @throws SocketTimeoutException 
	 */
	public static InputStream postRequestAsStreamWithCookie(String url, String encoding, Map<String, String> rawParams, String cookieInfo) throws WebClientException, ConnectTimeoutException, SocketTimeoutException {
		HttpClient httpClient = Proxy.getHttpClient();
		// 创建HttpPost对象。
		PostMethod postMethod = new PostMethod(url);
		postMethod.setDoAuthentication(true);
		postMethod.setFollowRedirects(false);
		//如果参数不为空则添加参数
		if(rawParams != null){
			// 如果传递参数个数比较多的话可以对传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : rawParams.keySet()) {
				// 封装请求参数
				params.add(new NameValuePair(key, rawParams.get(key)));
			}
			NameValuePair[] array = new NameValuePair[params.size()];
			// 设置请求参数
			postMethod.setRequestBody(params.toArray(array));
		}
		//设置cookie
		if(cookieInfo != null){
			postMethod.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
			postMethod.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
			postMethod.setRequestHeader("Cookie", cookieInfo);
		}
		//设置连接超时为20秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		//设置读取超时为20秒
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		int statusCode = 0;
		InputStream result = null;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			// 如果服务器成功地返回响应
			if (statusCode == 200 || statusCode == 201) {
				// 获取服务器响应流
				result = postMethod.getResponseBodyAsStream();
			}else if (statusCode == 302 || statusCode == 301) {
                // 重定向
                String location = postMethod.getResponseHeader("Location").getValue();
                return postRequestAsStreamWithCookie(location, encoding, rawParams, cookieInfo);
            }
		} catch (SocketTimeoutException e1){
			throw e1;
		} catch (ConnectTimeoutException e1){
			throw e1;
		} catch (HttpException e1) {
			throw new ConnectTimeoutException(url + "：连接异常");
		} catch (IOException e1) {
			throw new WebClientException(url + "：IO异常，请检查网络是否正常");
		}
		return result;
	}
	
	 public static InputStream getStreamUseJava(final String urlString)
	            throws Exception {
		 return getStreamUseJavaWithCookie(urlString, null);
	 }
	 public static InputStream getStreamUseJavaWithCookie(final String urlString, final String cookie) throws Exception {
		 return (InputStream) getStreamAndLengthUseJavaWithCookie(urlString, cookie)[0];
	 }
	 public static Object[] getStreamAndLengthUseJavaWithCookie(final String urlString, final String cookie)
	            throws Exception {
		 return getStreamAndLengthUseJavaWithCookie(urlString, cookie, 20000);
	 }
	 
     public static Object[] getStreamAndLengthUseJavaWithCookie(final String urlString, final String cookie, int timeout)
            throws Exception {
    	Object[] objects = new Object[2];
        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        String method = "GET";
        String post = null;
        String digest = null;

        InputStream inputStream = null;

        boolean foundRedirect = false;

        //Map<String, String> headers = new HashMap<String, String>();

        //URL url = new URL(nURL);

        do {
        	HttpURLConnection urlConnection = null;
            if(Proxy.getNetProxy() != null){
            	urlConnection = HttpsUtils.getConnection(nURL, Proxy.getNetProxy());
            	if(Proxy.username != null && !"".equals(Proxy.username) && Proxy.pwd != null && !"".equals(Proxy.pwd)){
            		//格式如下：  
            		//"Proxy-Authorization"= "Basic Base64.encode(user:password)"  
            		String headerKey = "Proxy-Authorization";  
            		String headerValue = "Basic " + Base64.encodeBase64((Proxy.username+":"+Proxy.pwd).getBytes());
            		urlConnection.setRequestProperty(headerKey, headerValue);
            	}
            }else{
            	urlConnection = HttpsUtils.getConnection(nURL, null);
            }
            // 添加访问授权
            if (digest != null) {
                urlConnection.setRequestProperty("Authorization", digest);
            }
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            //模拟http头文件
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            urlConnection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            
            if(cookie != null){
            	urlConnection.setRequestProperty("Cookie", cookie); 
            }
            
            //追加http头文件
           /* Set<Entry<String, String>> headersSet = headers.entrySet();
            for (Iterator<Entry<String, String>> it = headersSet.iterator(); it.hasNext();) {
                Entry<String, String> entry = (Entry<String, String>) it.next();
                urlConnection.setRequestProperty((String) entry.getKey(),
                        (String) entry.getValue());
            }*/
            if (post != null) {
                OutputStreamWriter outRemote = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                outRemote.write(post);
                outRemote.flush();
            }
            // 获得响应状态
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 301 || responseCode == 302) {
                // 重定向
                String location = urlConnection.getHeaderField("Location");
                System.out.println(responseCode + " " + location);
                nURL = location;
                foundRedirect = true;
            } else {
                if (responseCode == 200 || responseCode == 201) {
                	//inputStream不能关闭，应由调用方进行关闭
                	if("gzip".equals(urlConnection.getContentEncoding())){
                		inputStream = new GZIPInputStream(urlConnection.getInputStream());
                	}else{
                		inputStream = urlConnection.getInputStream();
                	}
                	objects[1] = urlConnection.getContentLength();
                }
                foundRedirect = false;
            }
            // 如果重定向则继续
        } while (foundRedirect);
        objects[0] = inputStream;
        return objects;
    }
    
    public static String getRequestUseJava(final String urlString, final String encoding) throws Exception {
    	return getRequestUseJavaWithCookie(urlString, encoding, null);
    }
    public static String getRequestUseJavaWithCookie(final String urlString, final String encoding, String cookie) throws Exception {
    	return getRequestUseJavaWithCookie(urlString, encoding, cookie, 20000);
    }
   
    /**
     * 向指定url发送请求并获得响应数据(使用原生JDK API)
     * 
     * @param urlString
     * @param encoding
     * @param parameter
     * @return
     */
    public static String getRequestUseJavaWithCookie(final String urlString, final String encoding, String cookie, int timeout)
            throws Exception {

        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        if(nURL.contains("hentai.org")){
        	nURL = nURL.replaceAll("http://", "https://");
        }
        String method = "GET";
        String post = null;
        String digest = null;

        String responseContent = null;

        boolean foundRedirect = false;
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;

        //Map<String, String> headers = new HashMap<String, String>();

        //URL url = new URL(nURL);
        do {
        	try{
	            HttpURLConnection urlConnection = null;
	            if(Proxy.getNetProxy() != null){
	            	urlConnection = HttpsUtils.getConnection(nURL, Proxy.getNetProxy());
	            	if(Proxy.username != null && !"".equals(Proxy.username) && Proxy.pwd != null && !"".equals(Proxy.pwd)){
	            		//格式如下：  
	            		//"Proxy-Authorization"= "Basic Base64.encode(user:password)"  
	            		String headerKey = "Proxy-Authorization";  
	            		String headerValue = "Basic " + Base64.encodeBase64((Proxy.username + ":" + Proxy.pwd).getBytes());
	            		urlConnection.setRequestProperty(headerKey, headerValue);
	            	}
	            }else{
	            	urlConnection = HttpsUtils.getConnection(nURL, null);
	            }
	            
	            // 添加访问授权
	            if (digest != null) {
	                urlConnection.setRequestProperty("Authorization", digest);
	            }
	            urlConnection.setDoOutput(true);
	            urlConnection.setDoInput(true);
	            urlConnection.setUseCaches(false);
	            urlConnection.setInstanceFollowRedirects(false);
	            urlConnection.setRequestMethod(method);
	            urlConnection.setConnectTimeout(timeout);  
	            urlConnection.setReadTimeout(timeout);
	            //模拟http头文件
	            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0;)");
	            urlConnection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
	            urlConnection.setRequestProperty("Accept-Encoding", "gzip");
	            if(cookie != null){
	            	urlConnection.setRequestProperty("Cookie", cookie); 
	            }
	            
	            //追加http头文件
	            /*Set<Entry<String, String>> headersSet = headers.entrySet();
	            for (Iterator<Entry<String, String>> it = headersSet.iterator(); it.hasNext();) {
	                Entry<String, String> entry = (Entry<String, String>) it.next();
	                urlConnection.setRequestProperty((String) entry.getKey(),
	                        (String) entry.getValue());
	            }*/
	            if (post != null) {
	                OutputStreamWriter outRemote = new OutputStreamWriter(
	                        urlConnection.getOutputStream());
	                outRemote.write(post);
	                outRemote.flush();
	            }
	            // 获得响应状态
	            int responseCode = urlConnection.getResponseCode();
	            // 获得返回的数据长度
	            int responseLength = urlConnection.getContentLength();
	            if (responseCode == 301 || responseCode == 302) {
	                // 重定向
	                String location = urlConnection.getHeaderField("Location");
	                nURL = location;
	                foundRedirect = true;
	            } else {
	            	InputStream tempInputStream = null;
	            	if("gzip".equals(urlConnection.getContentEncoding())){
	            		if (responseCode == 200 || responseCode == 201) {
            				tempInputStream = urlConnection.getInputStream();
            			}else{
            				tempInputStream = urlConnection.getErrorStream();
            			}
	            		tempInputStream = new GZIPInputStream(tempInputStream);
	            	}else{
	            		if (responseCode == 200 || responseCode == 201) {
	            			tempInputStream = urlConnection.getInputStream();
		                } else {
		                	tempInputStream = urlConnection.getErrorStream();
		                }
	            	}
	                if(tempInputStream != null){
                		in = new BufferedInputStream(tempInputStream);
	                	int size = responseLength == -1 ? 4096 : responseLength;
		                if (encoding != null) {
		                    responseContent = read(in, size, encoding);
		                } else {
		                    out = new ByteArrayOutputStream();
		                    byte[] bytes = new byte[size];
		                    int read;
		                    while ((read = in.read(bytes)) > 0) {
		                        out.write(bytes, 0, read);
		                    }
		                    responseContent = new String(out.toByteArray());
		                }
	                }
	                foundRedirect = false;
	            }
        	}catch (Exception e) {
    			throw e;
    		}finally{
    			if(in != null){
    				try{in.close();}catch(Exception e){}
    			}
    			if(out != null){
    				try{out.close();}catch(Exception e){}
    			}
    		}
            // 如果重定向则继续
        } while (foundRedirect);
        
        return responseContent;
    }
    
    
    public static String getRequestUseJavaWithCookie_POST(final String urlString, final String encoding, String cookie, int timeout)
            throws Exception {

        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        String method = "POST";
        String post = null;
        String digest = null;

        String responseContent = null;

        boolean foundRedirect = false;
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;

        //Map<String, String> headers = new HashMap<String, String>();

        //URL url = new URL(nURL);
        do {
        	try{
	            HttpURLConnection urlConnection = null;
	            if(Proxy.getNetProxy() != null){
	            	urlConnection = HttpsUtils.getConnection(nURL, Proxy.getNetProxy());
	            	if(Proxy.username != null && !"".equals(Proxy.username) && Proxy.pwd != null && !"".equals(Proxy.pwd)){
	            		//格式如下：  
	            		//"Proxy-Authorization"= "Basic Base64.encode(user:password)"  
	            		String headerKey = "Proxy-Authorization";  
	            		String headerValue = "Basic " + Base64.encodeBase64((Proxy.username + ":" + Proxy.pwd).getBytes());
	            		urlConnection.setRequestProperty(headerKey, headerValue);
	            	}
	            }else{
	            	urlConnection = HttpsUtils.getConnection(nURL, null);
	            }
	            
	            // 添加访问授权
	            if (digest != null) {
	                urlConnection.setRequestProperty("Authorization", digest);
	            }
	            urlConnection.setDoOutput(true);
	            urlConnection.setDoInput(true);
	            urlConnection.setUseCaches(false);
	            urlConnection.setInstanceFollowRedirects(false);
	            urlConnection.setRequestMethod(method);
	            urlConnection.setConnectTimeout(timeout);
	            urlConnection.setReadTimeout(timeout);
	            //模拟http头文件
	            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0;)");
	            urlConnection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
	           
	            if(cookie != null){
	            	urlConnection.setRequestProperty("Cookie", cookie); 
	            }
	            
	            //追加http头文件
	            /*Set<Entry<String, String>> headersSet = headers.entrySet();
	            for (Iterator<Entry<String, String>> it = headersSet.iterator(); it.hasNext();) {
	                Entry<String, String> entry = (Entry<String, String>) it.next();
	                urlConnection.setRequestProperty((String) entry.getKey(),
	                        (String) entry.getValue());
	            }*/
	            if (post != null) {
	                OutputStreamWriter outRemote = new OutputStreamWriter(
	                        urlConnection.getOutputStream());
	                outRemote.write(post);
	                outRemote.flush();
	            }
	            // 获得响应状态
	            int responseCode = urlConnection.getResponseCode();
	            // 获得返回的数据长度
	            int responseLength = urlConnection.getContentLength();
	            if (responseCode == 301 || responseCode == 302) {
	                // 重定向
	                String location = urlConnection.getHeaderField("Location");
	                nURL = location;
	                foundRedirect = true;
	            } else {
	                if (responseCode == 200 || responseCode == 201) {
	                    in = new BufferedInputStream(urlConnection.getInputStream());
	                } else {
	                    in = new BufferedInputStream(urlConnection.getErrorStream());
	                }
	                int size = responseLength == -1 ? 4096 : responseLength;
	                if (encoding != null) {
	                    responseContent = read(in, size, encoding);
	                } else {
	                    out = new ByteArrayOutputStream();
	                    byte[] bytes = new byte[size];
	                    int read;
	                    while ((read = in.read(bytes)) > 0) {
	                        out.write(bytes, 0, read);
	                    }
	                    responseContent = new String(out.toByteArray());
	                }
	                foundRedirect = false;
	            }
        	}catch (Exception e) {
    			throw e;
    		}finally{
    			if(in != null){
    				try{in.close();}catch(Exception e){}
    			}
    			if(out != null){
    				try{out.close();}catch(Exception e){}
    			}
    		}
            // 如果重定向则继续
        } while (foundRedirect);
        
        return responseContent;
    }
    
    /**
     * 向指定url发送请求并获得响应数据(使用原生JDK API)
     * 
     * @param urlString
     * @param encoding
     * @param parameter
     * @return
     * @throws IOException
     */
    public static String getCookieUseJava(final String urlString,
            final String encoding)
            throws IOException {

        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        String method = "GET";
        String post = null;
        String digest = null;

        String cookie = "";

        boolean foundRedirect = false;

        Map<String, String> headers = new HashMap<String, String>();

        URL url = new URL(nURL);

        try{
	        do {
	
	        	HttpURLConnection urlConnection = null;
	            if(Proxy.getNetProxy() != null){
	            	urlConnection = (HttpURLConnection) url
                    .openConnection(Proxy.getNetProxy());
	            	if(Proxy.username != null && !"".equals(Proxy.username) && Proxy.pwd != null && !"".equals(Proxy.pwd)){
	            		//格式如下：  
	            		//"Proxy-Authorization"= "Basic Base64.encode(user:password)"  
	            		String headerKey = "Proxy-Authorization";  
	            		String headerValue = "Basic " + Base64.encodeBase64((Proxy.username+":"+Proxy.pwd).getBytes());
	            		urlConnection.setRequestProperty(headerKey, headerValue);
	            	}
	            }else{
	            	urlConnection = (HttpURLConnection) url
	                        .openConnection();
	            }
	            // 添加访问授权
	            if (digest != null) {
	                urlConnection.setRequestProperty("Authorization", digest);
	            }
	            urlConnection.setDoOutput(true);
	            urlConnection.setDoInput(true);
	            urlConnection.setUseCaches(false);
	            urlConnection.setInstanceFollowRedirects(false);
	            urlConnection.setRequestMethod(method);
	            //模拟http头文件
	            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0;)");
	            urlConnection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
	            //追加http头文件
	            Set<Entry<String, String>> headersSet = headers.entrySet();
	            for (Iterator<Entry<String, String>> it = headersSet.iterator(); it.hasNext();) {
	                Entry<String, String> entry = (Entry<String, String>) it.next();
	                urlConnection.setRequestProperty((String) entry.getKey(),
	                        (String) entry.getValue());
	            }
	            if (post != null) {
	                OutputStreamWriter outRemote = new OutputStreamWriter(
	                        urlConnection.getOutputStream());
	                outRemote.write(post);
	                outRemote.flush();
	            }
	            // 获得响应状态
	            int responseCode = urlConnection.getResponseCode();
	            if (responseCode == 301 || responseCode == 302) {
	                // 重定向
	                String location = urlConnection.getHeaderField("Location");
	                url = new URL(location);
	                foundRedirect = true;
	            } else {
	                if (responseCode == 200 || responseCode == 201) {
	                	 String key = null;
	                     for (int i = 1; (key = urlConnection.getHeaderFieldKey(i)) != null; i++){
	                   	  System.out.print(key+":");
	                   	  System.out.println(urlConnection.getHeaderField(key)); 
	                     }
	                	cookie = urlConnection.getHeaderField("set-cookie");
	                }
	                foundRedirect = false;
	            }
	            // 如果重定向则继续
	        } while (foundRedirect);
        }catch (SocketTimeoutException e) {
        	//捕获到超时，不再请资源，返回null
		}
        return cookie;
    }
    
    /**
     * 转化InputStream为String
     * 
     * @param in
     * @param size
     * @return
     * @throws Exception 
     * @throws IOException
     */
    private static String read(final InputStream in, final int size,
            final String encoding) throws Exception{
        StringBuilder sbr = new StringBuilder();
        int nSize = size;
        if (nSize == 0) {
            nSize = 1;
        }
        char[] buffer = new char[nSize];
        int offset = 0;
        InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, encoding);
			while ((offset = isr.read(buffer)) != -1) {
			    sbr.append(buffer, 0, offset);
			}
		} catch (Exception e) {
			throw e;
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(isr != null){
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return sbr.toString();
    }
    
    public static String read(final InputStream in, final Integer start){
        byte[] buffer = new byte[4092];
        int offset = 0;
        int cursize = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
//    		in.skip(start);
			while ((offset = in.read(buffer)) != -1) {
				cursize += offset;
				if(cursize >= start){
					out.write(buffer, 0, offset);
				}
			}
			try {
				return new String(out.toByteArray(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
    }
    
    public static String getUrlStr(String url, Map<String, ?> params){
    	if(params != null){
    		Iterator<String> it = params.keySet().iterator();
    		String key;
    		while (it.hasNext()) {
				key = it.next();
				url += key + "=" + params.get(key) + "&";
			}
    		url = url.substring(0, url.length() - 1);
    	}
    	return url;
    }
}
