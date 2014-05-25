package org.arong.egdownloader.model;
/**
 * 采集配置信息
 * @author 阿荣
 * @since 2014-05-25
 */
public class Setting {
	private String defaultSaveDir = System.getProperty("user.dir") + "\\egdownloaderSave";//默认保存路劲
	private String cookieInfo = "igneous=4baadb8381b3bb5c20257b33b725e4ec93f51b4fe2ab7e97621c9fe260bbda7de47a44d6394b31783a0af329a20197c80d2ab687ccf0b667ca5c558ee1b9310b;ipb_member_id=1059070;ipb_pass_hash=e8e36f507753214279ee9df5d98c476c;";
	private String gidPrefix = "/g/";//url地址中获取gid值的前缀
	private HentaiHome hentaiHome = new HentaiHome();
	private String totalPrefix = "FILES";//用于获取图片总数
	private String namePrefix = "TITLE";//用于获取漫画名
	private String fileListPrefix = "FILELIST";//漫画集合前缀
	private String fileListSuffix = "INFORMATION";//漫画集合后缀
	private int pageCount = 40;//一页最多图片数
	private String pageParam = "p";//切换分页的参数名
	private String sourcePrefix = "&gt;";//获取url源码的前缀
	private String sourceSuffix = "</html>";//获取url源码的后缀
	private String showPicPrefix = "/s/";//浏览漫画的地址前缀
	private String showPicSuffix = "\"";//浏览漫画的地址后缀
	private String realUrlPrefix = "<img id=\"img\" src=\"";
	private String realUrlSuffix = "\"";
	
	public String getRealUrlPrefix() {
		return realUrlPrefix;
	}
	public void setRealUrlPrefix(String realUrlPrefix) {
		this.realUrlPrefix = realUrlPrefix;
	}
	public String getRealUrlSuffix() {
		return realUrlSuffix;
	}
	public void setRealUrlSuffix(String realUrlSuffix) {
		this.realUrlSuffix = realUrlSuffix;
	}
	public String getSourcePrefix() {
		return sourcePrefix;
	}
	public void setSourcePrefix(String sourcePrefix) {
		this.sourcePrefix = sourcePrefix;
	}
	public String getSourceSuffix() {
		return sourceSuffix;
	}
	public void setSourceSuffix(String sourceSuffix) {
		this.sourceSuffix = sourceSuffix;
	}
	
	
	
	
	
	public String getPageParam() {
		return pageParam;
	}
	public void setPageParam(String pageParam) {
		this.pageParam = pageParam;
	}
	public String getShowPicPrefix() {
		return showPicPrefix;
	}
	public void setShowPicPrefix(String showPicPrefix) {
		this.showPicPrefix = showPicPrefix;
	}
	public HentaiHome getHentaiHome() {
		return hentaiHome;
	}
	public void setHentaiHome(HentaiHome hentaiHome) {
		this.hentaiHome = hentaiHome;
	}
	public String getTotalPrefix() {
		return totalPrefix;
	}
	public void setTotalPrefix(String totalPrefix) {
		this.totalPrefix = totalPrefix;
	}
	public String getNamePrefix() {
		return namePrefix;
	}
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
	public String getFileListPrefix() {
		return fileListPrefix;
	}
	public void setFileListPrefix(String fileListPrefix) {
		this.fileListPrefix = fileListPrefix;
	}
	public String getGidPrefix() {
		return gidPrefix;
	}
	public void setGidPrefix(String gidPrefix) {
		this.gidPrefix = gidPrefix;
	}
	public String getCookieInfo() {
		return cookieInfo;
	}
	public void setCookieInfo(String cookieInfo) {
		this.cookieInfo = cookieInfo;
	}
	public String getFileListSuffix() {
		return fileListSuffix;
	}
	public void setFileListSuffix(String fileListSuffix) {
		this.fileListSuffix = fileListSuffix;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public String getDefaultSaveDir() {
		return defaultSaveDir;
	}
	public void setDefaultSaveDir(String defaultSaveDir) {
		this.defaultSaveDir = defaultSaveDir;
	}
	public String getShowPicSuffix() {
		return showPicSuffix;
	}
	public void setShowPicSuffix(String showPicSuffix) {
		this.showPicSuffix = showPicSuffix;
	}
	public String toString() {
		return "Setting [defaultSaveDir=" + defaultSaveDir + ", cookieInfo="
				+ cookieInfo + ", gidPrefix=" + gidPrefix + ", hentaiHome="
				+ hentaiHome + ", totalPrefix=" + totalPrefix + ", namePrefix="
				+ namePrefix + ", fileListPrefix=" + fileListPrefix
				+ ", fileListSuffix=" + fileListSuffix + ", pageCount="
				+ pageCount + ", pageParam=" + pageParam + ", sourcePrefix="
				+ sourcePrefix + ", sourceSuffix=" + sourceSuffix
				+ ", showPicPrefix=" + showPicPrefix + ", showPicSuffix="
				+ showPicSuffix + ", realUrlPrefix=" + realUrlPrefix
				+ ", realUrlSuffix=" + realUrlSuffix + "]";
	}
}
