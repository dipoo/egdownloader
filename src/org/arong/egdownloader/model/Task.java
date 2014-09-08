package org.arong.egdownloader.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.arong.egdownloader.ui.work.DownloadWorker;
import org.arong.egdownloader.ui.work.ReCreateWorker;
/**
 * 任务模型
 * @author 阿荣
 * @since 2014-05-22
 */
public class Task {
	private String id;//id
	private String url;//下载地址
	private String name;//名称
	private String subname;//字名称
	private String coverUrl;//封面路径
	private String saveDir;//保存目录
	private String language;//漫画语言
	private String tag;//标签
	private String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//创建时间
	private String completedTime;//完成时间
	private int total;//图片总数
	private int current = 0;//已下载完成总数
	private String size;//总大小
	private TaskStatus status = TaskStatus.UNSTARTED;//是否已完成
	private List<Picture> pictures;
	private DownloadWorker downloadWorker;//下载线程实例,不保存
	private ReCreateWorker reCreateWorker;//重新创建线程实例,不保存
	
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
	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	public String toString() {
		return "Task [id=" + id + ", url=" + url + ", name=" + name
				+ ", saveDir=" + saveDir + ", createTime=" + createTime
				+ ", completedTime=" + completedTime + ", total=" + total
				+ ", current=" + current + ", size=" + size + ", status="
				+ status + ", downloadWorker="
				+ downloadWorker + ", pictures=" + pictures + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public DownloadWorker getDownloadWorker() {
		return downloadWorker;
	}

	public void setDownloadWorker(DownloadWorker downloadWorker) {
		this.downloadWorker = downloadWorker;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public ReCreateWorker getReCreateWorker() {
		return reCreateWorker;
	}

	public void setReCreateWorker(ReCreateWorker reCreateWorker) {
		this.reCreateWorker = reCreateWorker;
	}
	
}
