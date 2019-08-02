package org.arong.util.https;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpsUtils {

        public static HttpURLConnection getConnection(String urlStr, Proxy proxy) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
                HttpURLConnection conn = null;
                if (urlStr.toLowerCase().startsWith("https"))
                        conn = getHttpsConnection(urlStr, proxy);
                else
                        conn = getHttpConnection(urlStr, proxy);
                return conn;
        }
        private static HttpURLConnection getHttpConnection(String urlStr, Proxy proxy) throws MalformedURLException, IOException {
                URL url = new URL(urlStr);
                System.out.println(urlStr + ",proxy:" + proxy);
                HttpURLConnection conn = null;
                if(proxy != null){
                	conn = (HttpURLConnection) url.openConnection(proxy);
                }else{
                	conn = (HttpURLConnection) url.openConnection();
                }
                return conn;
        }
       
        private static HttpsURLConnection getHttpsConnection(String urlStr, Proxy proxy) throws IOException {
                URL url = new URL(urlStr);
                System.out.println(urlStr + ",proxy:" + proxy);
                System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
                System.setProperty("jsse.enableSNIExtension", "false");
                HttpsURLConnection conn = null;
                if(proxy != null){
                	conn = (HttpsURLConnection) url.openConnection(proxy);
                }else{
                	conn = (HttpsURLConnection) url.openConnection();
                }
                /*try {
					SslUtils.ignoreSsl();
				} catch (Exception e) {
					e.printStackTrace();
				}*/
                try{
	                // 不验证服务器主机名和证书
	                conn.setHostnameVerifier(new IgnoreHostnameVerifier());
	                TrustManager[] tm = { new X509TrustManager4None() };
	                SSLContext sslContext = SSLContext.getInstance("TLS");
	                sslContext.init(null, tm, new java.security.SecureRandom());
	                
	                SSLSocketFactory ssf = sslContext.getSocketFactory();
	                /*for(String cs : ssf.getSupportedCipherSuites()){
	                	System.out.println(cs);
	                }*/
	                
	                conn.setSSLSocketFactory(ssf);
                }catch (Exception e){
                	System.out.println("java版本：" + System.getProperty("java.version") + "，当前版本无法支持Https协议，建议使用JDK(JRE)1.7及以上版本:" + e.getMessage());
                	e.printStackTrace();
                }
                return conn;
        }
}

