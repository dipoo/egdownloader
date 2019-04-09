package org.arong.egdownloader.ui.work;

import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil;
/**
 * 搜索漫画缓存封面下载的线程
 * @author dipoo
 * @since 2015-03-15
 */
public class DownloadCacheCoverWorker extends SwingWorker<Void, Void>{
	private List<SearchTask> tasks;
	private EgDownloaderWindow mainWindow;
	public DownloadCacheCoverWorker(List<SearchTask> tasks, EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.tasks = tasks;
	}
	
	protected Void doInBackground() throws Exception {
		String localPath;
		File cover;
		if(tasks != null){
			for(int i = 0; i < tasks.size(); i ++){
				final SearchTask task = tasks.get(i);
				localPath = ComponentConst.CACHE_PATH + "/" + FileUtil.filterDir(task.getUrl());
				cover = new File(localPath);
				if(cover == null || !cover.exists()){
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {}
					new CommonSwingWorker(new Runnable() {
						public void run() {
							try{
								FileUtil.storeStream(ComponentConst.CACHE_PATH, FileUtil.filterDir(task.getUrl()),
										WebClient.getStreamUseJavaWithCookie(task.getCoverUrl(), mainWindow.setting.getCookieInfo()));
							}catch(Exception e){
								//最多下两次
								try{
									FileUtil.storeStream(ComponentConst.CACHE_PATH, FileUtil.filterDir(task.getUrl()),
											WebClient.getStreamUseJavaWithCookie(task.getCoverUrl(), mainWindow.setting.getCookieInfo()));
								}catch(Exception e1){
									
								}
							}
						}
					}).execute();
				}
			}
		}
		return null;
	}

}
