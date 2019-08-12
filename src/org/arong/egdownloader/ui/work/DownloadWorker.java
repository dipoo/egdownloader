package org.arong.egdownloader.ui.work;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.ServerException;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.DateUtil;
import org.arong.util.FileUtil2;
import org.arong.util.HtmlUtils;
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
	private InputStream is = null;//当前下载的图片输入流
	
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
		Tracker.println(getClass(), HtmlUtils.colorHtml(String.format("%s(%s-%s):开始下载", task.getDisplayName(), task.getStart(), task.getEnd()), "#e60"));
		List<Picture> pics = task.getPictures();
		
		Picture pic;
		is = null;
		File existNameFs = null;//判断是否有重复的文件名
		if(pics.size() != 0){
			int success = 0;//下载完成个数
			int requireNum = 0;//需要下载数（未下载数）
			for(int i = (task.getStart() < 1 ? 0 : task.getStart() - 1); i < task.getEnd() && i < pics.size(); i ++){
				pic = pics.get(i);
				if(pic.getUrl() != null && ! pic.isRunning() && !pic.isCompleted()){
					requireNum ++;
					long connectStart = System.currentTimeMillis();
					try{
						if(this.isCancelled())//是否暂停
							return null;
						//if(setting.isOpenScript()){
							pic.setRealUrl(ScriptParser.getdownloadUrl(task, pic.getUrl(), setting));
						/*}else{
							pic.setRealUrl(ParseEngine.getdownloadUrl(task.getName(), pic.getUrl(), setting));
						}*/
						
						if(StringUtils.isBlank(pic.getRealUrl())){
							exceptionNum ++;
							System.out.println(HtmlUtils.redColorHtml(String.format("%s:%s:获取图片下载地址为空", task.getDisplayName(), pic.getName())));
							continue;
						}
						if(this.isCancelled())//是否暂停
							return null; 
						Object[] streamAndLength =  null;
						connectStart = System.currentTimeMillis();
						if(pic.getRealUrl().contains("hentai.org")){
							streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(pic.getRealUrl(), setting.getCookieInfo(), 10 * 1000);
						}else{
							streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(pic.getRealUrl(), null);
						}
						
						if(this.isCancelled())//是否暂停
							return null;
						if(streamAndLength[0] == null){
							Tracker.println(HtmlUtils.redColorHtml(String.format("%s:%s-%s:图片流无效", task.getDisplayName(), pic.getName(), pic.getRealUrl())));
							pic.setRealUrl(null);
							exceptionNum ++;
							continue;
						}
						is = (InputStream) streamAndLength[0];
						int totalLength = (Integer) streamAndLength[1];
						
						int size = is.available();
						String name = pic.getName();
						//是否以真实名称保存，是的话则要判断是否重复并处理
						if(! pic.isSaveAsName()){
							if(name.indexOf(".") != -1){
								name = pic.getNum() + name.substring(name.lastIndexOf("."), name.length());
							}else{
								name = pic.getNum() + ".jpg";
							}
							existNameFs = new File(String.format("%s%s%s", task.getSaveDir(), File.separator, name));
						}else{
							existNameFs = new File(String.format("%s%s%s", task.getSaveDir(), File.separator, name));
							//已存在相同名称的文件
							while(existNameFs.exists()){
								name = String.format("%s_%s", name.substring(0, name.lastIndexOf(".")), name.substring(name.lastIndexOf("."), name.length()));
								existNameFs = new File(String.format("%s%s%s", task.getSaveDir(), File.separator, name));
							}
						}
						size = task.storeStream(existNameFs, is);//保存到目录
						if(pic.getRealUrl().contains("509.gif") || size == 28658 || size == 144 || size == 210 || size == 1009){
							pic.setRealUrl(null);
							//https://github.com/fffonion/xeHentai/blob/master/xeHentai/filters.py
							Tracker.println(HtmlUtils.redColorHtml(String.format("%s：%s：509", task.getDisplayName(), pic.getName())));
							delete(existNameFs);
							exceptionNum ++;
							continue;
						}else if(size == 925 || size < 1000){
							pic.setRealUrl(null);
							Tracker.println(HtmlUtils.redColorHtml(String.format("%s：%s：403", task.getDisplayName(), pic.getName())));
							delete(existNameFs);
							exceptionNum ++;
							continue;
						}else if(totalLength != size){
							//获取的流大小与http响应不一致则不算下载成功
							pic.setRealUrl(null);
							Tracker.println(HtmlUtils.redColorHtml(String.format("%s：%s(已下载%s)：下载不完整(原图大小%s)，耗时%s", task.getDisplayName(), pic.getName(), FileUtil2.showSizeStr((long)size), FileUtil2.showSizeStr((long)totalLength), formatSecend(System.currentTimeMillis() - connectStart))));
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
							pic.setPpi(String.format("%sx%s", sii.getWidth(), sii.getHeight()));
						} catch (Exception e) {
							e.printStackTrace();
							Tracker.println(HtmlUtils.redColorHtml(String.format("%s：%s：图片无法解析", task.getDisplayName(), pic.getName())));
							pic.setRealUrl(null);
							delete(existNameFs);
							exceptionNum ++;
							continue;
						}
						
						if(this.isCancelled()){//是否暂停
							//删除已经下载的文件
							delete(existNameFs);
							return null;
						}
						pic.setSize(size);//设置图片大小
						pic.setTime(DateUtil.YYYY_MM_DD_HH_MM_SS_FORMAT.format(new Date()));//下载完成时间
						pic.setCompleted(true);//设置为已下载完成
						task.setCurrent(task.getCurrent() + 1);//更新task的已下载数
						Tracker.println(DownloadWorker.class, HtmlUtils.greenColorHtml(String.format("%s：%s(%s, %s)：下载完成，耗时%s", task.getDisplayName(), pic.getName(), FileUtil2.showSizeStr((long)size), pic.getPpi(), formatSecend(System.currentTimeMillis() - connectStart))));
						if(mainWindow.tasks.get(mainWindow.runningTable.selectRowIndex) == task){
							//切换信息面板tab
							mainWindow.infoTabbedPane.flushTab(task);
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
						Tracker.println(HtmlUtils.redColorHtml(String.format("%s：%s-读取流超时，滞后重试，耗时%s", task.getDisplayName(), pic.getName(), formatSecend(System.currentTimeMillis() - connectStart))));
						//删除已经下载的文件
						delete(existNameFs);
						//继续下一个
						continue;
					}catch (ConnectTimeoutException e){
						exceptionNum ++;
						//碰到异常
						Tracker.println(HtmlUtils.redColorHtml(String.format("%s：s%s-连接超时，滞后重试，耗时%s", task.getDisplayName(), pic.getName(), formatSecend(System.currentTimeMillis() - connectStart))));
						//继续下一个
						continue;
					}catch (WebClientException e) {
						//碰到网络异常，任务暂停
						Tracker.println("当前网络异常，请检查网络设置是否正确");
						task.setStatus(TaskStatus.STOPED);
						table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
						//开始任务等待列表中的第一个任务
						table.startWaitingTask();
						e.printStackTrace();
						return null;
					}catch (ServerException e) {
						//碰到服务器异常，任务暂停
						Tracker.println(HtmlUtils.redColorHtml(String.format("%s，%s停止下载", e.getMessage(), task.getDisplayName())));
						task.setStatus(TaskStatus.STOPED);
						table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
						//开始任务等待列表中的第一个任务
						table.startWaitingTask();
						return null;
					}catch (Exception e){
						exceptionNum ++;
						//删除已经下载的文件
						delete(existNameFs);
						if(e instanceof SocketException && "Socket Closed".equals(e.getMessage())){//主动暂停任务引起的异常，可忽略
							
						}else{
							//碰到异常
							e.printStackTrace();
							Tracker.println(HtmlUtils.redColorHtml(String.format("%s：%s===%s", task.getDisplayName(), pic.getName(), e.getMessage())));
						}
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
					Tracker.println(DownloadWorker.class, HtmlUtils.greenColorHtml(String.format("【%s】：完成配置区间(%s-%s)下载。", task.getDisplayName(), task.getStart(), task.getEnd())));
					//设置任务状态为已暂停
					task.setStatus(TaskStatus.STOPED);
					table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
					//开始任务等待列表中的第一个任务
					table.startWaitingTask();
					return null;
				}
				if(exceptionNum >= requireNum){
					Tracker.println(DownloadWorker.class, HtmlUtils.redColorHtml(String.format("【%s】：配额不足或者下载异常，停止下载。", task.getDisplayName())));
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
				Tracker.println(DownloadWorker.class ,String.format("==%s==", HtmlUtils.greenColorHtml(String.format("【%s】已下载完毕", task.getDisplayName()))));
				task.setCompletedTime(DateUtil.YYYY_MM_DD_HH_MM_SS_FORMAT.format(new Date()));
				//更新任务到文件
				((EgDownloaderWindow)mainWindow).taskDbTemplate.update(task);
				table.setRunningNum(table.getRunningNum() - 1);//当前运行的任务数-1
				//开始任务等待列表中的第一个任务
				table.startWaitingTask();
			}
		}
		
		return null;
	}
	
	protected void done() {
		if(is != null){
			//中断正在下载的线程
			try {is.close();is = null;} catch (IOException e) {e.printStackTrace();}
		}
		super.done();
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
	private String formatSecend(long t){
		return String.format("%s秒", String.format("%.2f", (t / 1000f)));
	}
}
