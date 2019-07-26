package org.arong.egdownloader.spider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang.StringUtils;

import com.ice.jni.registry.RegDWordValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;
import com.ice.jni.registry.RegistryValue;
/**
 * 代理
 * 
IE代理服务器对应于注册表中字段：HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings下面的值：ProxyServer，ProxyEnable，ProxyOverride 
ProxyEnable用来表示是否使用代理，用0,1表示，类型为REG_DWORD，不能为REG_SZ 
ProxyServer用来表示代理服务器ip:port，如http=localhost:1080;socks=localhost:1080，类型为REG_SZ 
ProxyOverride表示跳过代理的配置，比如跳过本地代理，该值为<local> 
 * @author dipoo
 * @since 2016-03-22
 */
public class Proxy {
	public static boolean useProxy;
	public static boolean useIEProxy;//直接使用IE代理
	public static java.net.Proxy.Type type = java.net.Proxy.Type.HTTP;//HTTP或者SOCKS
	public static String ip;
	public static String port;
	public static String username;
	public static String pwd;
	
	public static void init(boolean useProxy_, String type_, String ip_, String port_, String username_, String pwd_){
		useProxy = useProxy_;
		ip = ip_;
		port = port_;
		username = username_;
		pwd = pwd_;
		
		if("ie".equals(type_)){
			useIEProxy = true;
		}else{
			useIEProxy = false;
			if("http".equals(type_)){
				type = java.net.Proxy.Type.HTTP;
			}
			//jdk6的socks4代理存在bug
			else if("socks".equals(type_)){
				type = java.net.Proxy.Type.SOCKS;
			}
			//proxy();
		}
	}
	
	/**
	 * jvm全局使用代理
	 */
	public static void proxy(){
		if(useProxy){
			System.setProperty("http.maxRedirects", "50");  
	        System.getProperties().setProperty("proxySet", "true");
	        System.getProperties().setProperty("http.proxyHost", ip);  
	        System.getProperties().setProperty("http.proxyPort", port);
		}else{
			System.getProperties().setProperty("proxySet", "false");
			System.getProperties().setProperty("http.proxyHost", "");  
		    System.getProperties().setProperty("http.proxyPort", "");
		}
	}
	
	/**
	 * 取消jvm全局代理
	 */
	public static void unproxy(){
		if(!useProxy){
			 System.getProperties().setProperty("proxySet", "false");
		}
	}
	
	public static HttpClient getHttpClient(){
		HttpClient httpClient = new HttpClient();
		if(useProxy){
			if(useIEProxy){
				String[] ieproxys = getIEProxy();
				if(ieproxys != null){
					//取第一个代理设置
					String[] arr = ieproxys[0].split("=");
					if(arr.length == 2){
						//String type = arr[0];
						String[] hostport = arr[1].split(":");
						if(hostport.length == 2){
							httpClient.getHostConfiguration().setProxy(hostport[0], Integer.parseInt(hostport[1]));
						}
					}
				}
			}else{
				//设置代理服务器的ip地址和端口
				httpClient.getHostConfiguration().setProxy(ip, Integer.parseInt(port));
				//使用抢先认证
				httpClient.getParams().setAuthenticationPreemptive(true);
				//如果代理需要密码验证，这里设置用户名密码
				if(username != null && pwd != null){
					httpClient.getState().setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, pwd));
				}
			}
		}
		return httpClient;
	}
	
	public static java.net.Proxy getNetProxy(){
		if(useProxy){
			if(useIEProxy){
				String[] ieproxys = getIEProxy();
				if(ieproxys != null){
					//取第一个代理设置
					String[] arr = ieproxys[0].split("=");
					if(arr.length == 2){
						Type type = null;
						String[] hostport = arr[1].split(":");
						if(hostport.length == 2){
							if(arr[0].equals("http")){
								type = java.net.Proxy.Type.HTTP;
							}else if(arr[0].equals("socks")){
								type = java.net.Proxy.Type.SOCKS;
							}
							return new java.net.Proxy(type, new InetSocketAddress(hostport[0], Integer.parseInt(hostport[1])));
						}
					}
				}
			}else{
				return new java.net.Proxy(type, new InetSocketAddress(ip, Integer.parseInt(port)));
			}
		}
		return null;
	}
	
	public static String[] getIEProxy(){
		try {
			Class.forName("com.ice.jni.registry.RegistryKey");
			// 注册表表项值 
			RegistryKey registryKey = Registry.openSubkey( Registry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", RegistryKey.ACCESS_READ); 
			// 注册表表项键 
			RegistryValue registryValue = registryKey.getValue("ProxyEnable");
			boolean proxyEnable = ((RegDWordValue) registryValue).getData() != 0;
			//  开启了Internet代理
			if (proxyEnable == true){
				registryValue = registryKey.getValue("ProxyServer");
				String value = new String(registryValue.getByteData());
				if(StringUtils.isNotBlank(value)){
					//http=localhost:1080;socks=localhost:1080
					return value.split(";");
				}
			}
		} catch (ClassNotFoundException e){
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws IOException {  
        System.setProperty("http.maxRedirects", "50");  
        System.getProperties().setProperty("proxySet", "true");  
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以  
        String ip = "127.0.0.1";
        /*ip = "221.130.18.5";  
        ip = "221.130.23.135";  
        ip = "221.130.18.78";  
        ip = "221.130.23.134";  
        ip = "221.130.18.49";  
        ip = "111.1.32.36";  
        ip = "221.130.18.49";  
        ip = "221.130.18.49";*/  
        System.getProperties().setProperty("http.proxyHost", ip);  
        System.getProperties().setProperty("http.proxyPort", "22900");  
          
        //确定代理是否设置成功  
        System.out.println(getHtml("http://1212.ip138.com/ic.asp"));  
          
    }  
      
    private static String getHtml(String address){  
        StringBuffer html = new StringBuffer();  
        String result = null;  
        try{  
            URL url = new URL(address);  
            URLConnection conn = url.openConnection();  
            conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");  
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());  
              
            try{  
                String inputLine;  
                byte[] buf = new byte[4096];  
                int bytesRead = 0;  
                while (bytesRead >= 0) {  
                    inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");  
                    html.append(inputLine);  
                    bytesRead = in.read(buf);  
                    inputLine = null;  
                }  
                buf = null;  
            }finally{  
                in.close();  
                conn = null;  
                url = null;  
            }  
            result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();  
              
        }catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }finally{  
            html = null;              
        }  
        return result;  
    }

	public boolean isUseProxy() {
		return useProxy;
	}
}
