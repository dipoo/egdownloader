package org.arong.egdownloader.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.work.CreateWorker;
import org.arong.egdownloader.ui.work.DownloadWorker;
import org.arong.egdownloader.ui.work.ReCreateWorker;
import org.arong.util.DateUtil;
import org.arong.util.FileUtil2;
/**
 * 任务模型
 * @author 阿荣
 * @since 2014-05-22
 */
public class Task {
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);//属性变化监听支持
	private DbTemplate<Picture> pictureSqliteDbTemplate;
	
	private String id;//id
	private String groupname;//任务组名称
	private String url;//下载地址
	private String oldurl;//旧版本下载地址
	private String name;//名称
	private String subname;//子名称
	private String coverUrl;//封面路径
	private String saveDir;//保存目录
	private String language;//漫画语言
	private String type;//分类
	private String tag;//标签
	private String tags;
	private boolean readed;//已读
	private String createTime = DateUtil.YYYY_MM_DD_HH_MM_SS_FORMAT.format(new Date());//创建时间
	private String syncTime;//同步时间，比如标签，初始与createTime一致
	private String completedTime;//完成时间
	private int total;//图片总数
	private int current = 0;//已下载完成总数
	private String size;//总大小
	private TaskStatus status = TaskStatus.UNSTARTED;//是否已完成
	private int start = 1;//下载开始索引
	private int end = total;//下载结束索引
	private String postedTime;//发布时间
	private String uploader;//上传者
	private boolean original;//是否下载原图
	private boolean saveDirAsSubname;//以子名称作为保存目录
	
	private List<Picture> pictures;
	private DownloadWorker downloadWorker;//下载线程实例，不保存
	private ReCreateWorker reCreateWorker;//重新创建线程实例，不保存
	private CreateWorker createWorker;
	private String author;
	private Timer timer;//下载速度刷新定时器
	private TimerTask timerTask;
	private Long byteLength = 0L;
	private Long oldByteLength = 0L;
	private String downSpeed = "";//下载速度
	private String shortCreatetime = "";
	private boolean searched;//是否被标志为搜索到的
	
	public String getDisplayName(){
		return subname == null || "".equals(subname) ? name : subname;
	}
	public String getDisplayName(Setting setting){
		return setting.isShowAsSubname() || subname == null || "".equals(subname) ? name : subname;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}
	
	/**
	 * 存储文件流，返回文件的大小
	 * @param path
	 * @throws IOException
	 */
	public int storeStream(File fs, InputStream in) throws Exception{
		if(in == null){
			return 0;
		}
    	BufferedInputStream bis = null;
    	BufferedOutputStream bos = null;
    	int size = 0;
    	try {
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(new FileOutputStream(fs));
			byte[] buff = new byte[1024 * 10];
			int len = 0;
			while ((len = bis.read(buff)) != -1) {
				size += len;
				byteLength += len;
				FileUtil2.byteLength += len; 
				bos.write(buff, 0, len);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fs.getParentFile().mkdirs();
			throw new RuntimeException(String.format("[%s]文件夹不存在，自动创建", fs.getParent()));
		}finally {
			if (bos != null) {try {bos.close();} catch (IOException e) {}}
			if (bis != null) {try {bis.close();} catch (IOException e) {}}
			if (in != null) {try {in.close();} catch (IOException e) {}}
		}
    	return size;
    }
	
	public Timer getTimer() {
		if(timer == null){
			timer = new Timer(true);
		}
		return timer;
	}
	
	public String getAuthor() {
		return author;
	}
	public String getSubAuthor() {
		String subAuthor = null;
		if(subname != null){
			if(subname.indexOf("[") != -1 && subname.indexOf("]") != -1 && subname.indexOf("[") < subname.indexOf("]")){
				subAuthor = subname.substring(subname.indexOf("[") + 1, subname.indexOf("]"));
			}else if(subname.indexOf("【") != -1 && subname.indexOf("】") != -1 && subname.indexOf("【") < subname.indexOf("】")){
				subAuthor = subname.substring(subname.indexOf("【") + 1, subname.indexOf("】"));
			}
		}
		return subAuthor;
	}
	
	public Task(){}
	
	public Task(String url, String saveDir){
		this.url = url;
		this.saveDir = saveDir;
	}
	
	public String detatil(){
		return "id:" + id + "\n" + 
			   "下载地址:" + url + "\n" + 
			   "名称:" + name + "\n" + 
			   "子名称:" + subname + "\n" + 
			   "封面地址:" + coverUrl + "\n" + 
			   "保存目录:" + saveDir + "\n" + 
			   "上传者：" + uploader + "\n" +
			   "漫画语言:" + language + "\n" + 
			   "分类:" + type + "\n" + 
			   "标签:" + tag + "\n" + 
			   "阅读:" + readed + "\n" + 
			   "创建时间:" + createTime + "\n" + 
			   "完成时间:" + completedTime + "\n" + 
			   "图片总数:" + total + "\n" + 
			   "已下载:" + current + "\n" + 
			   "总大小:" + size + "\n" + 
			   "状态:" + status + "\n" + 
			   "开始索引:" + start + "\n" + 
			   "结束索引:" + end;
	}
	
	public String getShortCreatetime() {
		if(createTime != null && createTime.length() > 10){
			this.shortCreatetime = createTime.substring(2, 10).replaceAll("-", "/");
		}
		return shortCreatetime;
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
		if(name != null){
			if(name.indexOf("[") != -1 && name.indexOf("]") != -1 && name.indexOf("[") < name.indexOf("]")){
				author = name.substring(name.indexOf("[") + 1, name.indexOf("]"));
			}else if(name.indexOf("【") != -1 && name.indexOf("】") != -1 && name.indexOf("【") < name.indexOf("】")){
				author = name.substring(name.indexOf("【") + 1, name.indexOf("】"));
			}
		}
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
	public String getDownloadCoverUrl(boolean useCoverReplaceDomain) {
		return coverUrl != null && useCoverReplaceDomain ? coverUrl.replaceAll(ComponentConst.EX_DOMAIN, ComponentConst.EX_REPLACE_COVER_DOMAIN) : coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
		changeSupport.firePropertyChange("", null, null);
	}

	public String getLanguage() {
		return language;
	}
	
	public String getRealLanguage() {
		if(StringUtils.isNotBlank(tags) && tags.contains("language:")){
			for(String tag : tags.split(";")){
				if(tag.contains("language:")){
					return tag.replace("language:", "");
				}
			}
		}
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
		changeSupport.firePropertyChange("", null, null);
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
		changeSupport.firePropertyChange("", null, null);
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current > total ? total : current < 0 ? 0 : current;
		if(this.current == total){
			setStatus(TaskStatus.COMPLETED);
		}
		changeSupport.firePropertyChange("", null, null);
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
		changeSupport.firePropertyChange("", null, null);
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		if(TaskStatus.STARTED == status){
			//启动下载速度定时器
			this.getTimer().schedule(this.getTimerTask(), 0, 1000);
		}else{
			if(this.timer != null){
				this.timer.cancel();
				timer = null;
				timerTask = null;
				byteLength = 0L;
				oldByteLength = 0L;
			}
		}
		this.status = status;
		changeSupport.firePropertyChange("", null, null);
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
		if(pictures == null && pictureSqliteDbTemplate != null){
			pictures = pictureSqliteDbTemplate.query("tid", this.getId());
		}
		return pictures == null ? new ArrayList<Picture>() : pictures;
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
		changeSupport.firePropertyChange("", null, null);
	}

	public ReCreateWorker getReCreateWorker() {
		return reCreateWorker;
	}

	public void setReCreateWorker(ReCreateWorker reCreateWorker) {
		this.reCreateWorker = reCreateWorker;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		changeSupport.firePropertyChange("", null, null);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public String getScriptMember() {
		return "[url=" + url + ", name=" + name
				+ ", subname=" + subname + ", type=" + type
				+ ", total=" + total + ", size=" + size
				+ ", language=" + language + ", coverUrl=" + coverUrl
				+ ", postedTime=" + postedTime + ", tags=" + tags + "]";
	}

	public void setCreateWorker(CreateWorker createWorker) {
		this.createWorker = createWorker;
	}

	public CreateWorker getCreateWorker() {
		return createWorker;
	}

	public Long getByteLength() {
		return byteLength;
	}

	public void setByteLength(Long byteLength) {
		this.byteLength = byteLength;
	}

	public Long getOldByteLength() {
		return oldByteLength;
	}

	public void setOldByteLength(Long oldByteLength) {
		this.oldByteLength = oldByteLength;
	}

	public TimerTask getTimerTask() {
		if(timerTask == null){
			timerTask= new TimerTask() {
				public void run() {
					//当前一秒内的流量
					Long length = byteLength - oldByteLength;
					setDownSpeed(FileUtil2.showSizeStr(length) + "/S");
					if(byteLength > 999900000){
						byteLength = 0L;
						oldByteLength = 0L;
					}else{
						oldByteLength = byteLength;
					}
				}
			};
		}
		return timerTask;
	}

	public void setTimerTask(TimerTask timerTask) {
		this.timerTask = timerTask;
	}

	public String getDownSpeed() {
		return downSpeed;
	}

	public void setDownSpeed(String downSpeed) {
		if(this.downSpeed != null && !this.downSpeed.equals(downSpeed)){
			changeSupport.firePropertyChange("", null, null);
		}
		this.downSpeed = downSpeed;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public DbTemplate<Picture> getPictureSqliteDbTemplate() {
		return pictureSqliteDbTemplate;
	}

	public void setPictureSqliteDbTemplate(
			DbTemplate<Picture> pictureSqliteDbTemplate) {
		this.pictureSqliteDbTemplate = pictureSqliteDbTemplate;
	}

	public String getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(String postedTime) {
		this.postedTime = postedTime;
	}

	public String getUploader() {
		try {
			return StringUtils.isNotBlank(uploader) ? URLDecoder.decode(URLDecoder.decode(uploader, "UTF-8"), "UTF-8") : "";
		} catch (UnsupportedEncodingException e) {}
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public boolean isOriginal() {
		return original;
	}

	public void setOriginal(boolean original) {
		this.original = original;
	}

	public boolean isSaveDirAsSubname() {
		return saveDirAsSubname;
	}

	public void setSaveDirAsSubname(boolean saveDirAsSubname) {
		this.saveDirAsSubname = saveDirAsSubname;
	}
	
	public String getRealSaveDirName(){
		if(saveDirAsSubname && subname != null){
			return FileUtil2.filterDir(StringUtils.isNotBlank(subname.trim()) ? subname.trim() : name);
		}
		return FileUtil2.filterDir(name);
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public boolean isSearched() {
		return searched;
	}
	public void setSearched(boolean searched) {
		this.searched = searched;
	}
	public String getSyncTime() {
		return syncTime;
	}
	public String getStoreSyncTime() {
		//如果与创建时间一致，则置空，节省数据库空间
		return syncTime == null || syncTime.equals(createTime) ? "" : syncTime;
	}
	public String getShowSyncTime() {
		return StringUtils.isNotBlank(syncTime) ? syncTime : (createTime != null ? createTime : "");
	}
	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}
	public String getOldurl() {
		return oldurl;
	}
	public void setOldurl(String oldurl) {
		this.oldurl = oldurl;
	}
	/**
	 * 添加或删除任务时调用，刷新每个标签拥有的任务数
	 * @param add
	 */
	public void flushTagsCount(boolean add){
		if(StringUtils.isNotBlank(tags)){
			String[] arr = tags.split(";");
			for(String tag : arr){
				tag = tag.replaceAll("\\+", " ");
				if(ComponentConst.allTaskCountMap.containsKey(tag)){
					ComponentConst.allTaskCountMap.put(tag, ComponentConst.allTaskCountMap.get(tag) + (add ? 1 : -1));
				}else{
					if(add) ComponentConst.allTaskCountMap.put(tag, 1);
				}
			}
		}
	}
}
