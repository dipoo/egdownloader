package org.arong.egdownloader.spider;

public class SpiderException extends Exception{
	private static final long serialVersionUID = -4969885113130258910L;

	public SpiderException(String url, String message, String identify){
		super(url + message + identify);
	}
	public SpiderException(String message, String identify){
		super("html源码中" + message + identify);
	}
	public SpiderException(String message){
		super(message);
	}
}
