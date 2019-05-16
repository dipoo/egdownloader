package org.arong.egdownloader.ui.work;

import java.io.File;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.panel.PicturesInfoPanel;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
import org.arong.util.SimpleImageInfo;
import org.arong.util.Tracker;
/**
 * 下载线程类，执行耗时的下载任务
 * @author 阿荣
 * @since 2014-05-25
 */
public class DownloadWorker extends SwingWorker<Void, Void>{
	
	private EgDownloaderWindow mainWindow;
	private Task task;
	private Setting setting;
	private int exceptionNum = 0;
	public DownloadWorker(Task task, EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.task = task;
		setting = ((EgDownloaderWindow)mainWindow).setting;
	}
	
	protected Void doInBackground() throws Exception {
		TaskingTable table = (TaskingTable) ((EgDownloaderWindow)mainWindow).runningTable;
		exceptionNum = 0;
		//设置任务状态为下载中
		//task.setStatus(TaskStatus.STARTED);
		Tracker.println(getClass(), "<font color='#e60'>" + task.getDisplayName() + "(" + task.getStart() + "-" + task.getEnd() + "):开始下载</font>");
		List<Picture> pics = task.getPictures();
		
		Picture pic;
		InputStream is = null;
		File existNameFs = null;//判断是否有重复的文件名
		if(pics.size() != 0){
			int success = 0;//下载完成个数
			int requireNum = 0;//需要下载数（未下载数）
			for(int i = (task.getStart() < 1 ? 0 : task.getStart() - 1); i < task.getEnd() && i < pics.size(); i ++){
				pic = pics.get(i);
				if(pic.getUrl() != null && ! pic.isRunning() && !pic.isCompleted()){
					requireNum ++;
					try{
						if(this.isCancelled())//是否暂停
							return null;
						if(setting.isOpenScript()){
							pic.setRealUrl(ScriptParser.getdownloadUrl(task, pic.getUrl(), setting));
						}else{
							//pic.setRealUrl(ParseEngine.getdownloadUrl(task.getName(), pic.getUrl(), setting));
						}
						
						if(StringUtils.isBlank(pic.getRealUrl())){
							exceptionNum ++;
							System.out.println("<font color='red'>" + task.getDisplayName() + ":" + pic.getName() + ":获取图片下载地址为空</font>");
							continue;
						}
						if(this.isCancelled())//是否暂停
							return null; 
						Object[] streamAndLength =  null;
						if(pic.getRealUrl().contains("exhentai.org")){
							streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(pic.getRealUrl(), setting.getCookieInfo(), 10 * 1000);
						}else{
							streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(pic.getRealUrl(), null);
						}
						is = (InputStream) streamAndLength[0];
						int totalLength = (Integer) streamAndLength[1];
						
						if(this.isCancelled())//是否暂停
							return null;
						if(is == null){
							Tracker.println("<font color='red'>" + task.getDisplayName() + ":" + pic.getName() + "-" + pic.getRealUrl() + ":图片流无效</font>");
							pic.setRealUrl(null);
							exceptionNum ++;
							continue;
						}
						int size = is.available();
						String name = pic.getName();
						//是否以真实名称保存，是的话则要判断是否重复并处理
						if(! pic.isSaveAsName()){
							if(name.indexOf(".") != -1){
								name = pic.getNum() + name.substring(name.lastIndexOf("."), name.length());
							}else{
								name = pic.getNum() + ".jpg";
							}
							existNameFs = new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + File.separator + name);
						}else{
							existNameFs = new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + File.separator + name);
							//已存在相同名称的文件
							while(existNameFs.exists()){
								name = name.substring(0, name.lastIndexOf(".")) + "_" + name.substring(name.lastIndexOf("."), name.length());
								existNameFs = new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + "/" + name);
							}
						}
						size = task.storeStream(existNameFs, is);//保存到目录
						if(size < 1000){
							pic.setRealUrl(null);
							Tracker.println(task.getDisplayName() + ":" + pic.getName() + ":403");
							delete(existNameFs);
							exceptionNum ++;
							continue;
						}else if(size < 1010 || pic.getRealUrl().contains("509.gif")){
							pic.setRealUrl(null);
							//https://github.com/fffonion/xeHentai/blob/master/xeHentai/filters.py
							Tracker.println(task.getDisplayName() + ":" + pic.getName() + ":509");
							delete(existNameFs);
							exceptionNum ++;
							continue;
						}else if(totalLength != size){
							//获取的流大小与http响应不一致则不算下载成功
							pic.setRealUrl(null);
							Tracker.println("<font color='red'>" + task.getDisplayName() + ":" + pic.getName()+ "(已下载" + FileUtil2.showSizeStr((long)size) + "):下载不完整(原图大小" + FileUtil2.showSizeStr((long)totalLength) + ")</font>");
							delete(existNameFs);
							exceptionNum ++;
							continue;
						}
						if(this.isCancelled()){//是否暂停
							//删除已经下载的文件
							delete(existNameFs);
							return null;
						}
						
						try {
							SimpleImageInfo sii = new SimpleImageInfo(existNameFs);
							pic.setPpi(sii.getWidth() + "x" + sii.getHeight());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(this.isCancelled()){//是否暂停
							//删除已经下载的文件
							delete(existNameFs);
							return null;
						}
						pic.setSize(size);//设置图片大小
						pic.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//下载完成时间
						pic.setCompleted(true);//设置为已下载完成
						task.setCurrent(task.getCurrent() + 1);//更新task的已下载数
						
						Tracker.println(DownloadWorker.class, "<font color='green'>" + task.getDisplayName() + ":" + pic.getName() + "(" + FileUtil2.showSizeStr((long)size) + ", " + pic.getPpi() + ")下载完成</font>");
						if(mainWindow.tasks.get(mainWindow.runningTable.selectRowIndex) == task){
							//刷新信息面板
							if(mainWindow.infoTabbedPane.getSelectedIndex() == 1){
								mainWindow.taskInfoPanel.parseTask(task, mainWindow.runningTable.selectRowIndex);
							}else if(mainWindow.infoTabbedPane.getSelectedIndex() == 3){
								PicturesInfoPanel infoPanel = (PicturesInfoPanel) mainWindow.infoTabbedPane.getComponent(3);
								infoPanel.pictureTable.updateUI();
							}
						}
						
						//更新图片信息
						((EgDownloaderWindow)mainWindow).pictureDbTemplate.update(pic);
						//更新任务信息
						((EgDownloaderWindow)mainWindow).taskDbTemplate.update(task);
						//设置最后下载时间
						setting.setLastDownloadTime(pic.getTime());
						success ++;
						continue;
					}catch (SocketTimeoutException e){
						exceptionNum ++;
						//碰到异常
						Tracker.println("<font color='red'>" + task.getDisplayName() + ":" + pic.getName() + "-读取流超时，滞后重试</font>");
						//删除已经下载的文件
						delete(existNameFs);
						//继续下一个
						continue;
					}catch (ConnectTimeoutException e){
						exceptionNum ++;
						//碰到异常
						Tracker.println("<font color='red'>" + task.getDisplayName() + ":" + pic.getName() + "-连接超时，滞后重试</font>");
						//继续下一个
						continue;
					}catch (WebClientException e) {
						//碰到网络异常，任务暂停
						Tracker.println("当前无网络，请检查网络设置是否正确");
						task.setStatus(TaskStatus.STOPED);
						table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
						//开始任务等待列表中的第一个任务
						table.startWaitingTask();
						return null;
					}catch (Exception e){
						exceptionNum ++;
						//碰到异常
						e.printStackTrace();
						Tracker.println("<font color='red'>" + task.getDisplayName() + ":" + pic.getName() + "===" + e.getMessage() + "</font>");
						//继续下一个
						continue;
					}finally{
						if(is != null){
							try{is.close();}catch(Exception e){}
						}
					}
				}
			}
			
			//整个过程下来，如果没有下载完成，则递归
			if(task.getCurrent() < pics.size()){
				if(this.isCancelled())//是否暂停
					return null;
				//是否达到下载区间要求,达到则暂停
				if(success == requireNum){
					Tracker.println(DownloadWorker.class, "<font color='green'>【" + task.getDisplayName() + "】:完成配置区间下载。</font>");
					//设置任务状态为已暂停
					task.setStatus(TaskStatus.STOPED);
					table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
					//开始任务等待列表中的第一个任务
					table.startWaitingTask();
					return null;
				}
				if(exceptionNum >= requireNum){
					Tracker.println(DownloadWorker.class, "<font color='red'>【" + task.getDisplayName() + "】:配额不足或者下载异常，停止下载。</font>");
					//设置任务状态为已暂停
					task.setStatus(TaskStatus.STOPED);
					table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
					//开始任务等待列表中的第一个任务
					table.startWaitingTask();
					return null;
				}
				doInBackground();
			}else{
				//设置任务状态为已完成
				task.setStatus(TaskStatus.COMPLETED);
				Tracker.println(DownloadWorker.class ,"==<font color='green'>【" + task.getDisplayName() + "】已下载完毕</font>==");
				task.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				//更新任务到文件
				((EgDownloaderWindow)mainWindow).taskDbTemplate.update(task);
				table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
				//开始任务等待列表中的第一个任务
				table.startWaitingTask();
			}
		}
		
		return null;
	}
	private void delete(File existNameFs){
		//是否以真实名称保存，是的话则要删除下载不完整的文件
		if(setting.isSaveAsName() && existNameFs != null && existNameFs.exists()){
			existNameFs.delete();
		}
	}
	public Task getTask() {
		return task;
	}
	
}
