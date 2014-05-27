package org.arong.egdownloader.ui.work;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.db4o.Db4oTemplate;
import org.arong.egdownloader.model.ParseEngine;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil;
import org.arong.util.Tracker;

import com.db4o.query.Predicate;
/**
 * 下载线程类，执行耗时的下载任务
 * @author 阿荣
 * @since 2014-05-25
 */
public class DownloadWorker extends SwingWorker<Void, Void>{
	
	private JFrame mainWindow;
	private Task task;
	
	public DownloadWorker(Task task, JFrame mainWindow){
		this.mainWindow = mainWindow;
		this.task = task;
	}
	
	protected Void doInBackground() throws Exception {
		//设置任务状态为下载中
		task.setStatus(TaskStatus.STARTED);
		List<Picture> pics = task.pictures;
		Picture pic;
		Setting setting = ((EgDownloaderWindow)mainWindow).setting;
		JTable table = ((EgDownloaderWindow)mainWindow).runningTable;
		InputStream is;
		final String taskId = task.getId();
		if(pics.size() != 0){
			for(int i = 0; i < pics.size(); i ++){
				pic = pics.get(i);
				if(pic.getUrl() != null && ! pic.isRunning() && !pic.isCompleted()){
					try{
						if(this.isCancelled())//是否暂停
							return null;
						if(pic.getRealUrl() == null){
							pic.setRealUrl(ParseEngine.getdownloadUrl(pic.getUrl(), setting));
						}
						if(this.isCancelled())//是否暂停
							return null;
						is =  WebClient.getStreamUseJava(pic.getRealUrl());
						if(this.isCancelled())//是否暂停
							return null;
						int size = is.available();
						FileUtil.storeStream(task.getSaveDir() + "name", pic.getName(), is);//保存到目录
						if(this.isCancelled())//是否暂停
							return null;
						//Picture [id=41b2c042-7560-422b-a521-e76b56720a77, num=01, name=P213_.jpg, url=http://exhentai.org/s/b0f5fe0e5c/698928-1, realUrl=http://36.233.48.163:8888/h/b0f5fe0e5c10d164456ed3f2000d8b0ef258ab5d-1385766-1279-1850-jpg/keystamp=1401206100-f2b9d0361c/P213_.jpg, size=0, time=null, saveAsName=true, isCompleted=false, isRunning=false]
						pic.setSize(size);//设置图片大小
						pic.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//下载完成时间
						pic.setCompleted(true);//设置为已下载完成
						task.setCurrent(task.getCurrent() + 1);//更新task的已下载数
						//保存数据
						if(this.isCancelled())//是否暂停
							return null;
						final String picId = pic.getId();
						Db4oTemplate.update(new Predicate<Picture>() {
							public boolean match(Picture pic_) {
								return pic_.getId().equals(picId);//更新条件
							}
						}, pic, ComponentConst.TASK_DATA_PATH);
						
						table.updateUI();
						Tracker.println(DownloadWorker.class ,task.getName() + ":" + pic.getName() + "下载完成");
					}catch (SocketTimeoutException e){
						//碰到异常
						System.out.println("读取流超时，重试");
						//继续下一个
						continue;
					}catch (ConnectTimeoutException e){
						//碰到异常
						System.out.println("连接超时，重试");
						//继续下一个
						continue;
					}catch (Exception e){
						//碰到异常
						System.out.println(e.getMessage());
						//继续下一个
						continue;
					}
				}
			}
		}
		//整个过程下来，如果没有下载完成，则递归
		if(task.getCurrent() < pics.size()){
			if(this.isCancelled())//是否暂停
				return null;
			doInBackground();
		}else{
			//设置任务状态为已完成
			task.setStatus(TaskStatus.COMPLETED);
			Db4oTemplate.update(new Predicate<Task>() {
				public boolean match(Task task_) {
					return task_.getId().equals(taskId);//更新条件
				}
			}, task, ComponentConst.TASK_DATA_PATH);
		}
		return null;
	}
	public Task getTask() {
		return task;
	}
}
