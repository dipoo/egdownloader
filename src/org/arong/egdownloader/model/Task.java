package org.arong.egdownloader.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * 任务模型
 * @author 阿荣
 * @since 2014-05-22
 */
public class Task {
	private String url;//下载地址
	private String name;//名称
	private String saveDir;//保存目录
	private String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//创建时间
	private String completedTime;//完成时间
	private int total;//图片总数
	private int current = 0;//已下载完成总数
	private int size;//总大小
	private TaskStatus status = TaskStatus.UNSTARTED;//是否已完成
	private List<Picture> pictures;
	
	public Task(){}
	
	public Task(String url, String saveDir){
		this.url = url;
		this.saveDir = saveDir;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSaveDir() {
		return saveDir;
	}
	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCompletedTime() {
		return completedTime;
	}
	public void setCompletedTime(String completedTime) {
		this.completedTime = completedTime;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public String toString() {
		return "Task [url=" + url + ", name=" + name + ", saveDir=" + saveDir
				+ ", createTime=" + createTime + ", completedTime="
				+ completedTime + ", total=" + total + ", current=" + current
				+ ", size=" + size + ", status=" + status + ", pictures="
				+ pictures + "]";
	}
}
