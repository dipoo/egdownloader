package org.arong.egdownloader.caiji;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 获取远程url地址页面的源文件
 * @author 阿荣
 * @since 2013-8-18
 *
 */
public class WebClient {
	
	public static void main(String[] args) throws WebClientException {
		String cookie = "igneous=4baadb8381b3bb5c20257b33b725e4ec93f51b4fe2ab7e97621c9fe260bbda7de47a44d6394b31783a0af329a20197c80d2ab687ccf0b667ca5c558ee1b9310b;";
		cookie += "ipb_member_id=1059070;";
		cookie += "ipb_pass_hash=e8e36f507753214279ee9df5d98c476c;";
		String s;
		s = postRequestWithCookie("http://exhentai.org/hathdler.php?gid=446779&t=553f5c4086", cookie);
		System.out.println(s);
		
	}
	
	public static String postRequest(String url){
		try {
			return postRequestWithCookie(url, "utf-8", null, null);
		} catch (WebClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String postRequest(String url, String encoding){
		try {
			return postRequestWithCookie(url, encoding, null, null);
		} catch (WebClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String postRequestWithCookie(String url, String cookieInfo){
		try {
			return postRequestWithCookie(url, "utf-8", null, cookieInfo);
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
	 */
	public static String postRequestWithCookie(String url, String encoding, Map<String, String> rawParams, String cookieInfo) throws WebClientException {
		HttpClient httpClient = new HttpClient();
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
		postMethod.setRequestHeader("X-Forwarded-For", "115.239.210.27, 192.168.101.111, 192.168.101.112");
		postMethod.setRequestHeader("CLIENT_IP", "115.239.210.27");
		postMethod.setRequestHeader("Proxy-Client-IP", "115.239.210.27");
		postMethod.setRequestHeader("WL-Proxy-Client-IP", "115.239.210.27");
		//设置cookie
		if(cookieInfo != null){
			postMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			postMethod.setRequestHeader("Cookie", cookieInfo);
		}
		int statusCode = 0;
		String result = null;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			// 如果服务器成功地返回响应
			if (statusCode == 200 || statusCode == 201) {
				// 获取服务器响应字符串
				try{
					long length = postMethod.getResponseContentLength();
					InputStream in = postMethod.getResponseBodyAsStream();
					long size = length == -1 ? 4096 : length;
	                    ByteArrayOutputStream out = new ByteArrayOutputStream();
	                    byte[] bytes = new byte[(int) size];
	                    int read;
	                    while ((read = in.read(bytes)) >= 0) {
	                        out.write(bytes, 0, read);
	                    }
	                    result = new String(out.toByteArray(), encoding);
	                    in.close();
	                    out.close();
					
				}catch(UnsupportedEncodingException e){
					throw new WebClientException(url + "-不支持的编码字符串：" + encoding);
				}
			}else if (statusCode == 302) {
                // 重定向
                String location = postMethod.getResponseHeader("Location").getValue();
                return postRequestWithCookie(location, encoding, rawParams, cookieInfo);
            }
		} catch (HttpException e1) {
			throw new WebClientException(url + "：连接异常");
		} catch (IOException e1) {
			throw new WebClientException(url + "：IO异常，请检查网络是否正常");
		}
		return result;
	}
	
    
    public static InputStream getStreamUseJava(final String urlString)
            throws IOException {

        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        String method = "GET";
        String post = null;
        String digest = null;

        InputStream inputStream = null;

        boolean foundRedirect = false;

        Map<String, String> headers = new HashMap<String, String>();

        URL url = new URL(nURL);

        try{
	        do {
	            HttpURLConnection urlConnection = (HttpURLConnection) url
	                    .openConnection();
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
	            if (responseCode == 302) {
	                // 重定向
	                String location = urlConnection.getHeaderField("Location");
	                url = new URL(location);
	                foundRedirect = true;
	            } else {
	                if (responseCode == 200 || responseCode == 201) {
	                	inputStream = urlConnection.getInputStream();
	                }
	                foundRedirect = false;
	            }
	            // 如果重定向则继续
	        } while (foundRedirect);
        }catch (SocketTimeoutException e) {
        	//捕获到超时，不再请资源，返回null
		}
        return inputStream;
    }
    
    public static boolean storeStream(String path, String name, InputStream in){
    	File dir = new File(path);
    	if(! dir.exists()){
    		dir.mkdir();
    	}
    	BufferedInputStream bis = null;
    	BufferedOutputStream bos = null;
    	try {
    		File fs = new File(path + "/" + name);
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(new FileOutputStream(fs));
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = bis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return false;
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
    public static String getRequestUseJava(final String urlString,
            final String encoding)
            throws IOException {

        String nURL = (urlString.startsWith("http://") || urlString
                .startsWith("https://")) ? urlString : ("http:" + urlString)
                .intern();
        String method = "GET";
        String post = null;
        String digest = null;

        String responseContent = null;

        boolean foundRedirect = false;

        Map<String, String> headers = new HashMap<String, String>();

        URL url = new URL(nURL);

        try{
	        do {
	
	            HttpURLConnection urlConnection = (HttpURLConnection) url
	                    .openConnection();
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
	            // 获得返回的数据长度
	            int responseLength = urlConnection.getContentLength();
	            if (responseCode == 302) {
	                // 重定向
	                String location = urlConnection.getHeaderField("Location");
	                url = new URL(location);
	                foundRedirect = true;
	            } else {
	                BufferedInputStream in;
	                if (responseCode == 200 || responseCode == 201) {
	                    in = new BufferedInputStream(urlConnection.getInputStream());
	                } else {
	                    in = new BufferedInputStream(urlConnection.getErrorStream());
	                }
	                int size = responseLength == -1 ? 4096 : responseLength;
	                if (encoding != null) {
	                    responseContent = read(in, size, encoding);
	                } else {
	                    ByteArrayOutputStream out = new ByteArrayOutputStream();
	                    byte[] bytes = new byte[size];
	                    int read;
	                    while ((read = in.read(bytes)) >= 0) {
	                        out.write(bytes, 0, read);
	                    }
	                    responseContent = new String(out.toByteArray());
	                    in.close();
	                    out.close();
	                }
	                foundRedirect = false;
	            }
	            // 如果重定向则继续
	        } while (foundRedirect);
        }catch (SocketTimeoutException e) {
        	//捕获到超时，不再请资源，返回null
		}
        return responseContent;
    }
    
    /**
     * 转化InputStream为String
     * 
     * @param in
     * @param size
     * @return
     * @throws IOException
     */
    private static String read(final InputStream in, final int size,
            final String encoding){
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        try {
			while ((offset = isr.read(buffer)) != -1) {
			    sbr.append(buffer, 0, offset);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			in.close();
			 isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
        return sbr.toString();
    }
}
