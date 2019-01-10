package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.util.FileUtil;
/**
 * 下载漫画封面（先检测是否存在，不存在才去下载）
 * @author 阿荣
 * @since 2014-09-07
 */
public class DownloadCoverWork implements IMenuListenerTask {

	public void doWork(Window window, ActionEvent e2) {
		final EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int index = table.getSelectedRow();
		final Task task = table.getTasks().get(index);
		File cover = new File(ComponentConst.getSavePathPreffix() + task.getSaveDir() + "/cover.jpg");
		//不存在封面则下载
		if(cover == null || !cover.exists()){
			new CommonSwingWorker(new Runnable() {
				public void run() {
					InputStream is;
					try {
						if(task.getCoverUrl() == null){
							ScriptParser.rebuildTask(task, mainWindow.setting);
						}
						
						//下载封面
						is =  WebClient.getStreamUseJavaWithCookie(task.getCoverUrl(), mainWindow.setting.getCookieInfo());//getStreamUseJava(task.getCoverUrl());
						int size = FileUtil.storeStream(ComponentConst.getSavePathPreffix() + task.getSaveDir(), "cover.jpg", is);//保存到目录
						if(size == 0){
							JOptionPane.showMessageDialog(mainWindow, "下载失败，地址错误或者地址不可访问");
						}else{
							if(mainWindow.coverWindow2 != null && mainWindow.coverWindow2.isVisible()){
								mainWindow.coverWindow2.dispose();
							}
							JOptionPane.showMessageDialog(mainWindow, "下载成功");
						}
					} catch (SocketTimeoutException e){
						JOptionPane.showMessageDialog(mainWindow, "读取文件超时，请检查网络后重试");
					} catch (ConnectTimeoutException e){
						JOptionPane.showMessageDialog(mainWindow, "连接超时，请检查网络后重试");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainWindow, e.getMessage());
					} finally{
						mainWindow.tablePopupMenu.setVisible(false);
					}
				}
			}).execute();
		}else{
			JOptionPane.showMessageDialog(mainWindow, "封面已存在");
			mainWindow.tablePopupMenu.setVisible(false);
		}
	}

}
