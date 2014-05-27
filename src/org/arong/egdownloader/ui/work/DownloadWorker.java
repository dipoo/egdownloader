package org.arong.egdownloader.ui.work;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingWorker;

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
		List<Picture> pics = task.getPictures();
		Picture pic;
		Setting setting = ((EgDownloaderWindow)mainWindow).setting;
		JTable table = ((EgDownloaderWindow)mainWindow).runningTable;
		InputStream is;
		if(pics.size() != 0){
			for(int i = 0; i < pics.size(); i ++){
				pic = pics.get(i);
				if(pic.getUrl() != null && ! pic.isRunning() && !pic.isCompleted()){
					try{
						if(pic.getRealUrl() == null){
							pic.setRealUrl(ParseEngine.getdownloadUrl(pic.getUrl(), setting));
						}
						is =  WebClient.getStreamUseJava(pic.getRealUrl());
						int size = is.available();
						FileUtil.storeStream(task.getSaveDir() + "name", pic.getName(), is);//保存到目录
						pic.setSize(size);//设置图片大小
						pic.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//下载完成时间
						pic.setCompleted(true);//设置为已下载完成
						task.setCurrent(task.getCurrent() + 1);//更新task的已下载数
						//保存数据
						final String taskUrl = task.getUrl();
						Db4oTemplate.update(new Predicate<Task>() {
							public boolean match(Task task) {
								return task.getUrl().equals(taskUrl);//更新条件
							}
						}, task, ComponentConst.TASK_DATA_PATH);
						table.updateUI();
						Tracker.println(DownloadWorker.class ,task.getName() + ":" + pic.getName() + "下载完成");
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
			doInBackground();
		}else{
			//设置任务状态为已完成
			task.setStatus(TaskStatus.COMPLETED);
		}
		return null;
	}
	public Task getTask() {
		return task;
	}
}
