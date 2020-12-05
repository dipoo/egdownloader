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
	private int runningCount = 0;
	public DownloadCacheCoverWorker(List<SearchTask> tasks, EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.tasks = tasks;
	}
	
	protected Void doInBackground() throws Exception {
		File cover;
		if(tasks != null){
			for(int i = 0; i < tasks.size(); i ++){
				final SearchTask task = tasks.get(i);
				cover = new File(task.getCoverCachePath());
				if(cover == null || !cover.exists()){
					try {
						while(runningCount > 2){
							Thread.sleep(2000);
						}
					} catch (InterruptedException e) {}
					runningCount ++;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							new CommonSwingWorker(new Runnable() {
								public void run() {
									InputStream is = null;
									try{
										Object[] streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(task.getDownloadCoverUrl(mainWindow.setting.isUseCoverReplaceDomain()), mainWindow.setting.getCookieInfo(), 20 * 1000);
										task.setCoverLength((Integer) streamAndLength[1]);
										is = (InputStream)streamAndLength[0];
										FileUtil2.storeStream(ComponentConst.CACHE_PATH, task.getCoverCacheFileName(), is);
										runningCount --;
									}catch(Exception e){
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e1) {}
										//最多下两次
										try{
											Object[] streamAndLength = WebClient.getStreamAndLengthUseJavaWithCookie(task.getDownloadCoverUrl(mainWindow.setting.isUseCoverReplaceDomain()), mainWindow.setting.getCookieInfo(), 20 * 1000);
											task.setCoverLength((Integer) streamAndLength[1]);
											is = (InputStream)streamAndLength[0];
											FileUtil2.storeStream(ComponentConst.CACHE_PATH, task.getCoverCacheFileName(), is);
										}catch(Exception e1){
											e1.printStackTrace();
											task.setCoverDownloadFail(true);
										}finally{
											runningCount --;
											if(is != null){
												try{is.close();}catch(Exception e1){}
											}
										}
										e.printStackTrace();
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
