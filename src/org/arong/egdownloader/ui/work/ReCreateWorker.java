package org.arong.egdownloader.ui.work;

import java.io.InputStream;
import java.net.SocketTimeoutException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.spider.SpiderException;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
/**
 * 重新新建任务线程类
 * @author 阿荣
 * @since 2014-09-08
 */
public class ReCreateWorker extends SwingWorker<Void, Void>{
	
	private JFrame mainWindow;
	private Task task;
	public ReCreateWorker(Task task, JFrame mainWindow){
		this.mainWindow = mainWindow;
		this.task = task;
	}
	
	protected Void doInBackground() throws Exception {
		EgDownloaderWindow window = (EgDownloaderWindow)mainWindow;
		if(window.creatingWindow == null){
			window.creatingWindow = new CreatingWindow(mainWindow);
		}
		window.creatingWindow.setVisible(true);//显示新建任务详细信息窗口
		Setting setting = window.setting;//获得配置信息
		InputStream is = null;
		try {
			if(setting.isOpenScript()){
				task = ScriptParser.buildTaskByJavaScript(task, setting, window.creatingWindow, true);
			}else{
				//task = ParseEngine.buildTask_new(task, setting, window.creatingWindow);
			}
			
			if(task != null && task.getPictures() != null){
				if(task.getCoverUrl() == null){
					//下载封面
					is =  WebClient.getStreamUseJava(task.getCoverUrl());
					FileUtil2.storeStream(task.getSaveDir(), "cover.jpg", is);//保存到目录
				}
				
				//保存到数据库
				window.pictureDbTemplate.store(task.getPictures());//保存图片信息
				//关闭form
				((CreatingWindow)(window.creatingWindow)).reset();
				window.creatingWindow.dispose();
				window.tablePane.setVisible(true);//将表格panel显示出来
				window.setEnabled(true);
				window.setVisible(true);
				//下载
				TaskingTable table = (TaskingTable)window.runningTable;
				task.setStatus(TaskStatus.UNSTARTED);
				table.startTask(task);
			}else{
				window.creatingWindow.dispose();
				JOptionPane.showMessageDialog(null, "重建任务异常");
				//开始任务等待列表中的第一个任务
				window.runningTable.startWaitingTask();
				task.setStatus(TaskStatus.UNCREATED);
			}
		} catch (SocketTimeoutException e){
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, "读取文件超时，请检查网络后重试");
			task.setStatus(TaskStatus.UNCREATED);
			//开始任务等待列表中的第一个任务
			window.runningTable.startWaitingTask();
		} catch (ConnectTimeoutException e){
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, "连接超时，请检查网络后重试");
			task.setStatus(TaskStatus.UNCREATED);
			//开始任务等待列表中的第一个任务
			window.runningTable.startWaitingTask();
		} catch (SpiderException e) {
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, e.getMessage());
			task.setStatus(TaskStatus.UNCREATED);
			//开始任务等待列表中的第一个任务
			window.runningTable.startWaitingTask();
		} catch (WebClientException e) {
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, e.getMessage());
			task.setStatus(TaskStatus.UNCREATED);
			//开始任务等待列表中的第一个任务
			window.runningTable.startWaitingTask();
		} finally{
			if(is != null){
				is.close();
			}
		}
		return null;
	}

}
