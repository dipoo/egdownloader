package org.arong.egdownloader.ui.work;

import java.io.File;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil;
import org.arong.util.Tracker;
/**
 * 下载线程类，执行耗时的下载任务
 * @author 阿荣
 * @since 2014-05-25
 */
public class DownloadWorker2 extends SwingWorker<Void, Void>{
	
	private JFrame mainWindow;
	private Task task;
	private Setting setting;
	TaskingTable table;
	private int exceptionNum = 0;
	private int success = 0;//下载完成个数
	private int requireNum = 0;//需要下载数（未下载数）
	private int maxthread = 5;
	private int running = 0;
	public DownloadWorker2(Task task, JFrame mainWindow){
		this.mainWindow = mainWindow;
		this.task = task;
		setting = ((EgDownloaderWindow)mainWindow).setting;
		table = (TaskingTable) ((EgDownloaderWindow)mainWindow).runningTable;
	}
	
	protected Void doInBackground() throws Exception {
		//设置任务状态为下载中
		task.setStatus(TaskStatus.STARTED);
		Tracker.println(getClass(), task.getName() + "(" + task.getStart() + "-" + task.getEnd() + "):开始下载");
		List<Picture> pics = task.getPictures();
		
		if(pics.size() != 0){
			for(int i = (task.getStart() < 1 ? 0 : task.getStart() - 1); i < task.getEnd() && i < pics.size(); i ++){
				Picture pic = pics.get(i);
				if(pic.getUrl() != null && ! pic.isRunning() && !pic.isCompleted()){
					if(this.isCancelled())//是否暂停
						return null;
					if(running < maxthread){
						DownloadPictureWorker2 worker = new DownloadPictureWorker2(pic);
						running ++;
						worker.execute();
						Boolean result = worker.get();
						if(result != null && result){
							
						}else if(result == null){
							return null;//暂停
						}
					}else{
						break;
					}
				}
			}
			
			//整个过程下来，如果没有下载完成，则递归
			if(task.getCurrent() < pics.size()){
				if(this.isCancelled())//是否暂停
					return null;
				//是否达到下载区间要求,达到则暂停
				if(success == requireNum){
					Tracker.println(DownloadWorker.class, "【" + task.getName() + ":完成配置区间下载。】");
					//设置任务状态为已暂停
					task.setStatus(TaskStatus.STOPED);
					table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
					//开始任务等待列表中的第一个任务
					table.startWaitingTask();
					table.updateUI();
					return null;
				}
				if(exceptionNum >= requireNum){
					Tracker.println(DownloadWorker.class, "【" + task.getName() + ":配额不足或者下载异常，停止下载。】");
					//设置任务状态为已暂停
					task.setStatus(TaskStatus.STOPED);
					table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
					//开始任务等待列表中的第一个任务
					table.startWaitingTask();
					table.updateUI();
					return null;
				}
				doInBackground();
			}else{
				//设置任务状态为已完成
				task.setStatus(TaskStatus.COMPLETED);
				Tracker.println(DownloadWorker.class ,"【" + task.getName() + "已下载完毕。】");
				task.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				//更新任务到文件
				((EgDownloaderWindow)mainWindow).taskDbTemplate.update(task);
				table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
				//开始任务等待列表中的第一个任务
				table.startWaitingTask();
				table.updateUI();
			}
		}
		
		return null;
	}
	/**
	 * 返回真则下载完成，返回false则下载没完成，返回null，则暂停
	 * @return Boolean
	 */
	private Boolean download(Picture pic){
		requireNum ++;
		try{
			if(this.isCancelled())//是否暂停
				return null;
			if(setting.isOpenScript()){
				pic.setRealUrl(ScriptParser.getdownloadUrl(task.getName(), pic.getUrl(), setting));
			}else{
				//pic.setRealUrl(ParseEngine.getdownloadUrl(task.getName(), pic.getUrl(), setting));
			}
			
			if(pic.getRealUrl() == null){
				exceptionNum ++;
				return false;
			}
			if(this.isCancelled())//是否暂停
				return null;
			InputStream is =  WebClient.getStreamUseJava(pic.getRealUrl());
			if(this.isCancelled())//是否暂停
				return null;
			int size = is.available();
			if(size < 1000){
				pic.setRealUrl(null);
				Tracker.println(task.getName() + ":" + pic.getName() + ":403");
				is.close();
				exceptionNum ++;
				return false;
			}else if(size < 1010){
				pic.setRealUrl(null);
				Tracker.println(task.getName() + ":" + pic.getName() + ":509");
				is.close();
				exceptionNum ++;
				return false;
			}
			String name = pic.getName();
			//是否以真实名称保存，是的话则要判断是否重复并处理
			if(! setting.isSaveAsName()){
				if(name.indexOf(".") != -1){
					name = pic.getNum() + name.substring(name.lastIndexOf("."), name.length());
				}
			}else{
				File existNameFs = new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + "/" + name);
				//已存在相同名称的文件
				while(existNameFs.exists()){
					name = name.substring(0, name.lastIndexOf(".")) + "_" + name.substring(name.lastIndexOf("."), name.length());
					existNameFs = new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + "/" + name);
				}
				existNameFs = null;
			}
			size = FileUtil.storeStream(ComponentConst.getSavePathPreffix() + task.getSaveDir(), name, is);//保存到目录
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
			//更新图片信息
			((EgDownloaderWindow)mainWindow).pictureDbTemplate.update(pic);
			//设置最后下载时间
			setting.setLastDownloadTime(pic.getTime());
			Tracker.println(DownloadWorker.class ,task.getName() + ":" + pic.getName() + "下载完成。");
			success ++;
			running --;
			table.updateUI();//完成
			return true;
		}catch (SocketTimeoutException e){
			//碰到异常
			Tracker.println(task.getName() + ":" + pic.getName() + "-读取流超时，滞后重试");
			//继续下一个
			return false;
		}catch (ConnectTimeoutException e){
			//碰到异常
			Tracker.println(task.getName() + ":" + pic.getName() + "-连接超时，滞后重试");
			//继续下一个
			return false;
		}catch (Exception e){
			//碰到异常
			Tracker.println(task.getName() + ":" + pic.getName() + e.getMessage());
			//继续下一个
			return false;
		}
	}
	
	public Task getTask() {
		return task;
	}
	
	/**
	 * 下载图片的线程类,用于开启任务内的多线程下载
	 * @author dipoo
	 * @since 2015-01-09
	 */
	public class DownloadPictureWorker2 extends SwingWorker<Boolean, Boolean> {
		Picture pic;
		public DownloadPictureWorker2(Picture pic){
			this.pic = pic;
		}
		
		protected Boolean doInBackground() throws Exception {
			return download(pic);
		}

	}
	
}
