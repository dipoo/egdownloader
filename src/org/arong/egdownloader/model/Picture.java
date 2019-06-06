package org.arong.egdownloader.model;

/**
 * 图片模型
 * @author 阿荣
 * @since 2014-05-22
 */
public class Picture {
	private String tid;
	private String id;//uuid
	private String num;//序号
	private String name;//真实名称
	private String url;//浏览地址
	private String oldurl;//旧版本地址
	private String realUrl;//真实下载地址
	private int size;//大小
	private String ppi;//分辨率
	private String time;//下载时间
	private boolean saveAsName = true;//是否以真实名称保存，否则以人物名+num保存
	private boolean isCompleted;//是否下载完成
	private boolean isRunning;//是否正在下载
	
	private long totalSize;
	
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
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
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
		return "Picture [tid=" + tid + ", id=" + id + ", num=" + num
				+ ", name=" + name + ", url=" + url + ", realUrl=" + realUrl
				+ ", size=" + size + ", time=" + time + ", saveAsName="
				+ saveAsName + ", isCompleted=" + isCompleted + ", isRunning="
				+ isRunning + "]";
	}
	public String getRealUrl() {
		return realUrl;
	}
	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getPpi() {
		return ppi;
	}
	public void setPpi(String ppi) {
		this.ppi = ppi;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public String getOldurl() {
		return oldurl;
	}
	public void setOldurl(String oldurl) {
		this.oldurl = oldurl;
	}
}
