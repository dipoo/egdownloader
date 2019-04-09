package org.arong.egdownloader.ui.work;

import java.io.File;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.panel.PicturesInfoPanel;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil;
import org.arong.util.SimpleImageInfo;
import org.arong.util.Tracker;
/**
 * 下载单个图片线程类，执行耗时的下载任务
 * @author 阿荣
 * @since 2018-12-01
 */
public class DownloadSinglePicWorker extends SwingWorker<Void, Void>{
	
	private EgDownloaderWindow mainWindow;
	private Task task;
	private Picture pic;
	public DownloadSinglePicWorker(Task task, Picture pic, EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.task = task;
		this.pic = pic;
	}
	
	protected Void doInBackground() throws Exception {
		Tracker.println(task.getDisplayName() + ":" + pic.getName() + ":开始下载");
		Setting setting = mainWindow.setting;
		pic.setRealUrl(ScriptParser.getdownloadUrl(task, pic.getUrl(), setting));
		InputStream is;
		try{
			Object[] streamAndLength =  null;
			if(pic.getRealUrl().contains("exhentai.org")){
				streamAndLength =  WebClient.getStreamAndLengthUseJavaWithCookie(pic.getRealUrl(), setting.getCookieInfo());
			}else{
				streamAndLength =  WebClient.getStreamAndLengthUseJavaWithCookie(pic.getRealUrl(), null);
			}
			is = (InputStream) streamAndLength[0];
			int totalLength = (Integer) streamAndLength[1];
			
			if(is == null){
				pic.setRealUrl(null);
				Tracker.println(task.getDisplayName() + ":" + pic.getName() + ":图片流无效");
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
			}
			
			size = task.storeStream(ComponentConst.getSavePathPreffix() + task.getSaveDir(), name, is);//保存到目录
			if(size < 1000){
				pic.setRealUrl(null);
				Tracker.println(task.getDisplayName() + ":" + pic.getName() + ":403");
			}else if(size < 1010){
				pic.setRealUrl(null);
				Tracker.println(task.getDisplayName() + ":" + pic.getName() + ":509");
			}else if(totalLength - 1024 * 10 > size){
				//误差在10K以上则不算下载成功
				pic.setRealUrl(null);
				Tracker.println(task.getDisplayName() + ":" + pic.getName()+ "(" + FileUtil.showSizeStr((long)size) + "):下载不完整(" + FileUtil.showSizeStr((long)totalLength) + ")");
			}
			pic.setSize(size);//设置图片大小
			pic.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//下载完成时间
			if(!pic.isCompleted()){
				pic.setCompleted(true);//设置为已下载完成
				if(task.getCurrent() < task.getTotal()){
					task.setCurrent(task.getCurrent() + 1);//更新task的已下载数
					if(task.getCurrent() == task.getTotal()){
						task.setStatus(TaskStatus.COMPLETED);
					}else{
						task.setStatus(TaskStatus.STOPED);
					}
				}
			}
			try {
				SimpleImageInfo sii = new SimpleImageInfo(new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + File.separator + name));
				pic.setPpi(sii.getWidth() + "x" + sii.getHeight());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Tracker.println(DownloadSinglePicWorker.class ,task.getDisplayName() + ":" + pic.getName() + "(" + FileUtil.showSizeStr((long)size) + ", " + pic.getPpi() + ")下载完成。");
			
			if(mainWindow.infoTabbedPane.getSelectedIndex() == 2){
				PicturesInfoPanel infoPanel = (PicturesInfoPanel) mainWindow.infoTabbedPane.getComponent(2);
				infoPanel.showPictures(task);
			}
			
			//更新图片信息
			mainWindow.pictureDbTemplate.update(pic);
			//更新任务信息
			mainWindow.taskDbTemplate.update(task);
			//设置最后下载时间
			setting.setLastDownloadTime(pic.getTime());
		}catch (SocketTimeoutException e){
			//碰到异常
			Tracker.println(task.getDisplayName() + ":" + pic.getName() + "-读取流超时，滞后重试");
		}catch (ConnectTimeoutException e){
			//碰到异常
			Tracker.println(task.getDisplayName() + ":" + pic.getName() + "-连接超时，滞后重试");
		}catch (WebClientException e) {
			//碰到网络异常，任务暂停
			Tracker.println("当前无网络，请检查网络设置是否正确");
			return null;
		}catch (Exception e){
			//碰到异常
			e.printStackTrace();
		}
		return null;
	}
	
	public Task getTask() {
		return task;
	}
	
}
