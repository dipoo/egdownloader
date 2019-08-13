package org.arong.egdownloader.model;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.FileUtil2;

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
	
	private boolean coverDownloadFail; //封面是否下载失败
	
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
		if("/".equals(url.substring(url.length() - 1, url.length()))){
			url = url.substring(0, url.length() - 1);
		}
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
	public String getDownloadCoverUrl(boolean useCoverReplaceDomain) {
		return coverUrl != null && useCoverReplaceDomain ? coverUrl.replaceAll(ComponentConst.EX_DOMAIN, ComponentConst.EX_REPLACE_COVER_DOMAIN) : coverUrl;
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
	
	public boolean isFavAuthorOrGroup(String favTags){
		//获取团队/作者
		if(StringUtils.isNotBlank(favTags)){
			if(StringUtils.isNotBlank(this.getTags())){
				if(this.getTags().contains("artist:")){
					String[] arr = this.getTags().split(";");
					for(String tag : arr){
						if(tag.startsWith("artist:")){
							if(favTags.contains(String.format("%s;", tag))){
								return true;
							}
						}
					}
				}
				if(this.getTags().contains("group:")){
					String[] arr = this.getTags().split(";");
					for(String tag : arr){
						if(tag.startsWith("group:")){
							if(favTags.contains(String.format("%s;", tag))){
								return true;
							}
						}
					}
				}
			}
			if(StringUtils.isNotBlank(this.getAuthor())){
				//包含作者及团队
				if(this.getAuthor().contains("(") && this.getAuthor().contains(")") && this.getAuthor().indexOf("(") < this.getAuthor().indexOf(")")){
					String authors = this.getAuthor().substring(this.getAuthor().indexOf("(") + 1, this.getAuthor().indexOf(")"));
					//多个作者以逗号分隔
					String[] arr = authors.split(",");
					for(String author : arr){
						if(favTags.contains(String.format("artist:%s;", author.toLowerCase()))){
							return true;
						}
					}
					String groups = this.getAuthor().substring(0, this.getAuthor().indexOf("("));
					arr =  groups.split(",");
					for(String group : arr){
						if(favTags.contains(String.format("group:%s;", group.toLowerCase()))){
							return true;
						}
					}
				}else{
					//多个作者以逗号分隔
					String[] arr = this.getAuthor().split(",");
					for(String author : arr){
						if(favTags.contains(String.format(";artist:%s;", author.toLowerCase()))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public String getCoverCachePath(){
		if(url != null){
			return ComponentConst.CACHE_PATH + "/" + getCoverCacheFileName();
		}
		return null;
	}
	public String getCoverCacheFileName(){
		if(url != null){
			return FileUtil2.filterDir((url.endsWith("/") ? url : url + "/").replaceAll("e-hentai.org", "exhentai.org").replaceAll("https:", "http:"));
		}
		return null;
	}

	public boolean isCoverDownloadFail() {
		return coverDownloadFail;
	}

	public void setCoverDownloadFail(boolean coverDownloadFail) {
		this.coverDownloadFail = coverDownloadFail;
	}
}
