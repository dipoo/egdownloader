package org.arong.egdownloader.ui.work;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
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
				localPath = ComponentConst.CACHE_PATH + "/" + FileUtil2.filterDir(task.getUrl());
				cover = new File(localPath);
				if(cover == null || !cover.exists()){
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							new CommonSwingWorker(new Runnable() {
								public void run() {
									InputStream is = null;
									try{
										Object[] streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(task.getCoverUrl(), mainWindow.setting.getCookieInfo(), 20 * 1000);
										task.setCoverLength((Integer) streamAndLength[1]);
										is = (InputStream)streamAndLength[0];
										FileUtil2.storeStream(ComponentConst.CACHE_PATH, FileUtil2.filterDir(task.getUrl()), is);
									}catch(Exception e){
										//最多下两次
										try{
											Object[] streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(task.getCoverUrl(), mainWindow.setting.getCookieInfo(), 20 * 1000);
											task.setCoverLength((Integer) streamAndLength[1]);
											is = (InputStream)streamAndLength[0];
											FileUtil2.storeStream(ComponentConst.CACHE_PATH, FileUtil2.filterDir(task.getUrl()), is);
										}catch(Exception e1){
											
										}finally{
											if(is != null){
												try{is.close();}catch(Exception e1){}
											}
										}
									}finally{
										if(is != null){
											try{is.close();}catch(Exception e){}
										}
									}
								}
							}).execute();
						}
					});
				}
			}
		}
		return null;
	}

}
