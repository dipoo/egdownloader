package org.arong.egdownloader.model;

import java.util.UUID;

/**
 * 采集配置信息
 * @author 阿荣
 * @since 2014-05-25
 */
public class Setting {
	private String id = UUID.randomUUID().toString();
	private String defaultSaveDir = System.getProperty("user.dir") + "\\egdownloaderSave2";//默认保存路劲
	private boolean saveAsName;//是否以真实名称保存
	private int maxThread = 5;
	private String cookieInfo = "igneous=4baadb8381b3bb5c20257b33b725e4ec93f51b4fe2ab7e97621c9fe260bbda7de47a44d6394b31783a0af329a20197c80d2ab687ccf0b667ca5c558ee1b9310b;ipb_member_id=1059070;ipb_pass_hash=e8e36f507753214279ee9df5d98c476c;";
	//前后缀
	private String[] task_name = {"<h1 id=\"gn\">", "</h1><h1"};//名称
	private String[] task_subname = {"</h1><h1 id=\"gj\">", "</h1></div>"};//子名称
	private String[] task_coverUrl = {"<div id=\"gd1\"><img src=\"", "\" alt=\"\" /></div></div>"};//封面地址
	private String[] task_total_size = {"Images:</td><td class=\"gdt2\">", "</td></tr><tr><td class=\"gdt1\">Resized:"};//数目及大小
	private String[] task_language = {"Language:</td><td class=\"gdt2\">", "</td></tr></table></div><div id=\"gdr\""};//语言
	
	private String[] picture_listSource = {"</table><div id=\"gdt\">", "<div class=\"c\"></div></div><table"};//每页所有图片源码
	private String[] picture_intercept = {"style=\"height", "\"gdtm"};//判断是否还有及截取剩余字符串
	private String[] picture_showUrl = {"no-repeat\"><a href=\"", "\"><img alt="};//显示url
	private String[] picture_name = {"title=\"", "\" src="};//名称
	private String[] picture_realUrl = {"<img id=\"img\" src=\"", "</html>", "http", "\""};//真实下载地址,1、2为第一次截取，3、4为最终截取
	
	
	
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
	private String loginUrl = "https://forums.e-hentai.org/index.php?act=Login&CODE=01";
	
	
	public String[] getTask_name() {
		return task_name;
	}
	public void setTask_name(String[] task_name) {
		this.task_name = task_name;
	}
	public String[] getTask_subname() {
		return task_subname;
	}
	public void setTask_subname(String[] task_subname) {
		this.task_subname = task_subname;
	}
	public String[] getTask_coverUrl() {
		return task_coverUrl;
	}
	public void setTask_coverUrl(String[] task_coverUrl) {
		this.task_coverUrl = task_coverUrl;
	}
	public String[] getTask_total_size() {
		return task_total_size;
	}
	public void setTask_total_size(String[] task_total_size) {
		this.task_total_size = task_total_size;
	}
	public String[] getTask_language() {
		return task_language;
	}
	public void setTask_language(String[] task_language) {
		this.task_language = task_language;
	}
	public String[] getPicture_listSource() {
		return picture_listSource;
	}
	public void setPicture_listSource(String[] picture_listSource) {
		this.picture_listSource = picture_listSource;
	}
	public String[] getPicture_intercept() {
		return picture_intercept;
	}
	public void setPicture_intercept(String[] picture_intercept) {
		this.picture_intercept = picture_intercept;
	}
	public String[] getPicture_showUrl() {
		return picture_showUrl;
	}
	public void setPicture_showUrl(String[] picture_showUrl) {
		this.picture_showUrl = picture_showUrl;
	}
	public String[] getPicture_name() {
		return picture_name;
	}
	public void setPicture_name(String[] picture_name) {
		this.picture_name = picture_name;
	}
	public String[] getPicture_realUrl() {
		return picture_realUrl;
	}
	public void setPicture_realUrl(String[] picture_realUrl) {
		this.picture_realUrl = picture_realUrl;
	}
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSaveAsName() {
		return saveAsName;
	}
	public void setSaveAsName(boolean saveAsName) {
		this.saveAsName = saveAsName;
	}
	public int getMaxThread() {
		return maxThread;
	}
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}
