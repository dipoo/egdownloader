package org.arong.egdownloader.model;

import org.arong.egdownloader.ui.ComponentConst;

/**
 * 搜索绅士站漫画列表任务模型
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchTask {
	
	private String url;//下载地址
	
	private String name;//名称
	
	private String type;//分类
	
	private String coverUrl;//封面路径
	
	private String date;//发布时间
	
	private String btUrl;//bt下载地址
	
	private String uploader;//上传者
	
	private String rating;//评分
	
	private String author;//作者
	
	private String filenum;//图片个数
	
	private String tags;//标签
	
	private int coverLength;//封面字节大小
	
	public String getAuthor() {
		return author;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setBtUrl(String btUrl) {
		this.btUrl = btUrl;
	}

	public String getBtUrl() {
		return btUrl;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getUploader() {
		return uploader;
	}


	@Override
	public String toString() {
		return "SearchTask [url=" + url + ", name=" + name + ", type=" + type
				+ ", coverUrl=" + coverUrl + ", date=" + date + ", btUrl="
				+ btUrl + ", uploader=" + uploader + ", rating=" + rating
				+ ", author=" + author + ", filenum=" + filenum + "]";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setName(String name) {
		this.name = name;
		if(name != null){
			if(name.indexOf("[") != -1 && name.indexOf("]") != -1 && name.indexOf("[") < name.indexOf("]")){
				author = name.substring(name.indexOf("[") + 1, name.indexOf("]"));
			}else if(name.indexOf("【") != -1 && name.indexOf("】") != -1 && name.indexOf("【") < name.indexOf("】")){
				author = name.substring(name.indexOf("【") + 1, name.indexOf("】"));
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getCoverUrl() {
		return coverUrl;
	}
	public String getDownloadCoverUrl() {
		return coverUrl != null ? coverUrl.replaceAll(ComponentConst.EX_DOMAIN, ComponentConst.EX_COVER_DOMAIN) : coverUrl;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getFilenum() {
		return filenum;
	}

	public void setFilenum(String filenum) {
		this.filenum = filenum;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getCoverLength() {
		return coverLength;
	}

	public void setCoverLength(int coverLength) {
		this.coverLength = coverLength;
	}
	
}
