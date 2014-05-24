package org.arong.egdownloader.model;
/**
 * Hentai@Home 下载的配置信息
 * @author Administrator
 *
 */
public class HentaiHome {
	private String uri = "/hathdler.php";//路径
	private String firstParameterName = "gid";//画廊id参数名，一般是task url的第一个参数名
	private String secondParameterName = "t";//不详，一般是task url的第二个参数名
	public HentaiHome(){}
	public HentaiHome(String url, String firstParameterName, String secondParameterName){
		this.uri = url;
		this.firstParameterName = firstParameterName;
		this.secondParameterName = secondParameterName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getFirstParameterName() {
		return firstParameterName;
	}
	public void setFirstParameterName(String firstParameterName) {
		this.firstParameterName = firstParameterName;
	}
	public String getSecondParameterName() {
		return secondParameterName;
	}
	public void setSecondParameterName(String secondParameterName) {
		this.secondParameterName = secondParameterName;
	}
	
}
