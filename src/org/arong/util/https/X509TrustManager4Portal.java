package org.arong.util.https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class X509TrustManager4Portal implements X509TrustManager {

	private X509Certificate[] certificates;
	

	public void checkClientTrusted(X509Certificate[] chain, String authType)
	            throws CertificateException {
		if (this.certificates == null) {
			this.certificates = chain;
		}
	}
	/*
	 * Delegate to the default trust manager.
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType)
	            throws CertificateException {
	    if (this.certificates == null) {
	    	this.certificates = chain;
	    }
	}
	
	public X509Certificate[] getAcceptedIssuers() {
	    return null;
	}
}
