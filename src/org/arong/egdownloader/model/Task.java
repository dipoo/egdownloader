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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.arong.egdownloader.ui.work.CreateWorker;
import org.arong.egdownloader.ui.work.DownloadWorker;
import org.arong.egdownloader.ui.work.ReCreateWorker;
import org.arong.util.FileUtil;
/**
 * 任务模型
 * @author 阿荣
 * @since 2014-05-22
 */
public class Task {
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);//属性变化监听支持
	private String id;//id
	private String url;//下载地址
	private String name;//名称
	private String subname;//子名称
	private String coverUrl;//封面路径
	private String saveDir;//保存目录
	private String language;//漫画语言
	private String type;//分类
	private String tag;//标签
	private boolean readed;//已读
	private String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//创建时间
	private String completedTime;//完成时间
	private int total;//图片总数
	private int current = 0;//已下载完成总数
	private String size;//总大小
	private TaskStatus status = TaskStatus.UNSTARTED;//是否已完成
	private int start = 1;//下载开始索引
	private int end = total;//下载结束索引
	private List<Picture> pictures;
	private DownloadWorker downloadWorker;//下载线程实例,不保存
	private ReCreateWorker reCreateWorker;//重新创建线程实例,不保存
	private CreateWorker createWorker;
	private String author;
	private Timer timer;//下载速度刷新定时器
	private TimerTask timerTask;
	private Long byteLength = 0L;
	private Long oldByteLength = 0L;
	private String downSpeed = "";//下载速度
	
	public String getDisplayName(){
		return subname == null || "".equals(subname) ? name : subname;
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
	public int storeStream(String path, String name, InputStream in) throws IOException{
		if(in == null){
			return 0;
		}
    	File dir = new File(path);
    	FileUtil.ifNotExistsThenCreate(dir);
    	BufferedInputStream bis = null;
    	BufferedOutputStream bos = null;
    	int size = 0;
    	try {
    		File fs = new File(path + File.separator + name);
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(new FileOutputStream(fs));
			byte[] buff = new byte[1024 * 10];
			int len = 0;
			while ((len = bis.read(buff)) != -1) {
				size += len;
				byteLength += len;
				FileUtil.byteLength += len; 
				bos.write(buff, 0, len);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
				+ ", language=" + language + ", coverUrl=" + coverUrl + "]";
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
					setDownSpeed(FileUtil.showSizeStr(length) + "/S");
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
}
