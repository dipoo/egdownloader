package org.arong.egdownloader.model;

/**
 * 图片模型
 * @author 阿荣
 * @since 2014-05-22
 */
public class Picture {
	private String num;//序号
	private String name;//真实名称
	private String url;//下载地址
	private String size;//大小
	private String time;//下载时间
	private boolean saveAsName;//是否以真实名称保存，否则以人物名+num保存
	private boolean isCompleted;//是否下载完成
	private boolean isRunning;//是否正在下载
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isSaveAsName() {
		return saveAsName;
	}
	public void setSaveAsName(boolean saveAsName) {
		this.saveAsName = saveAsName;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public String toString() {
		return "Picture [name=" + name + ", url=" + url + "]";
	}
}
