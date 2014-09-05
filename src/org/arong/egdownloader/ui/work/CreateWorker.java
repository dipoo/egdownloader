package org.arong.egdownloader.ui.work;

import java.net.SocketTimeoutException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.ParseEngine;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.SpiderException;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
/**
 * 新建任务线程类
 * @author 阿荣
 * @since 2014-06-01
 */
public class CreateWorker extends SwingWorker<Void, Void>{
	
	private JFrame mainWindow;
	private String url;
	private String saveDir;
	public CreateWorker(String url, String saveDir, JFrame mainWindow){
		this.mainWindow = mainWindow;
		this.url = url;
		this.saveDir = saveDir;
	}
	
	protected Void doInBackground() throws Exception {
		EgDownloaderWindow window = (EgDownloaderWindow)mainWindow;
		AddFormDialog addFormWindow = ((AddFormDialog) window.addFormWindow);
		addFormWindow.dispose();
		window.creatingWindow.setVisible(true);//显示新建任务详细信息窗口
		Setting setting = window.setting;//获得配置信息
		Task task = null;
		try {
			task = ParseEngine.buildTask_new(url, saveDir, setting, window.creatingWindow);
			if(task != null){
				//保存到数据库
				window.pictureDbTemplate.store(task.getPictures());//保存图片信息
				window.taskDbTemplate.store(task);//保存任务
				//保存到内存
				TaskingTable taskTable = (TaskingTable)window.runningTable;
				taskTable.getTasks().add(0, task);//将任务添加到列表最前面
				addFormWindow.emptyField();//清空下载地址
				//关闭form,刷新table
				((CreatingWindow)(window.creatingWindow)).reset();
				window.creatingWindow.dispose();
				addFormWindow.dispose();
				window.tablePane.setVisible(true);//将表格panel显示出来
				window.emptyTableTips.setVisible(false);//将空任务label隐藏
				taskTable.updateUI();
				window.setEnabled(true);
				window.setVisible(true);
			}else{
				window.creatingWindow.dispose();
				JOptionPane.showMessageDialog(null, "创建异常");
			}
		} catch (SocketTimeoutException e){
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, "读取文件超时，请检查网络后重试");
		} catch (ConnectTimeoutException e){
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, "连接超时，请检查网络后重试");
		} catch (SpiderException e) {
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (WebClientException e) {
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
	}

}
