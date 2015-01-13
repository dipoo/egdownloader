package org.arong.egdownloader.ui.work;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.ParseEngine;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.SpiderException;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.util.FileUtil;
/**
 * 新建任务线程类
 * @author 阿荣
 * @since 2014-06-01
 */
public class CreateWorker extends SwingWorker<Void, Void>{
	//http://exhentai.org/g/672439/1e00a4a2ec;http://exhentai.org/s/77ea7ec5bb/672439-1
	private JFrame mainWindow;
	private Task task;
	public CreateWorker(Task task, JFrame mainWindow){
		this.mainWindow = mainWindow;
		this.task = task;
	}
	
	protected Void doInBackground() throws Exception {
		/**
		 * 重新检测任务是否存在
		 */
		if(((EgDownloaderWindow)mainWindow).taskDbTemplate.exsits("url", task.getUrl())){
			return null;
		}
		EgDownloaderWindow window = (EgDownloaderWindow)mainWindow;
		window.setEnabled(false);
		window.setVisible(true);
		AddFormDialog addFormWindow = ((AddFormDialog) window.addFormWindow);
		addFormWindow.setVisible(false);
		window.creatingWindow.setVisible(true);//显示新建任务详细信息窗口
		Setting setting = window.setting;//获得配置信息
		InputStream is;
		try {
			if(setting.isOpenScript()){
				if("".equals(setting.getCreateTaskScriptPath())){
					JOptionPane.showMessageDialog(null, "创建任务脚本未指定");
					return null;
				}else if("".equals(setting.getCollectPictureScriptPath())){
					JOptionPane.showMessageDialog(null, "收集图片脚本未指定");
					return null;
				}
				task = ScriptParser.buildTaskByJavaScript(task, setting, window.creatingWindow);
			}else{
				task = ParseEngine.buildTask_new(task, setting, window.creatingWindow);
			}
			
			if(task != null){
				//下载封面
				is =  WebClient.getStreamUseJava(task.getCoverUrl());
				FileUtil.storeStream(ComponentConst.getSavePathPreffix() + task.getSaveDir(), "cover.jpg", is);//保存到目录
				
				//设置最后创建时间
				setting.setLastCreateTime(task.getCreateTime());
				//设置历史任务总数
				setting.setTaskHistoryCount(setting.getTaskHistoryCount() + 1);
				//设置历史图片总数
				setting.setPictureHistoryCount(setting.getPictureHistoryCount() + task.getTotal());
				//保存到数据库
				window.pictureDbTemplate.store(task.getPictures());//保存图片信息
				window.taskDbTemplate.store(task);//保存任务
				window.settingDbTemplate.update(setting);//保存配置
				
				//保存到内存
				TaskingTable taskTable = (TaskingTable)window.runningTable;
				taskTable.getTasks().add(0, task);//将任务添加到列表最前面
				addFormWindow.emptyField();//清空下载地址
				//关闭form,刷新table
				addFormWindow.dispose();
				window.tablePane.setVisible(true);//将表格panel显示出来
				window.emptyTableTips.setVisible(false);//将空任务label隐藏
				taskTable.updateUI();
				//是否开启下载
				if(setting.isAutoDownload()){
					taskTable.startTask(task);
				}
				task.setCreateWorker(null);
			}else{
				JOptionPane.showMessageDialog(null, "创建异常");
			}
		} catch (SocketTimeoutException e){
			JOptionPane.showMessageDialog(null, "读取文件超时，请检查网络后重试");
		} catch (ConnectTimeoutException e){
			JOptionPane.showMessageDialog(null, "连接超时，请检查网络后重试");
		} catch (SpiderException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (WebClientException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (ScriptException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		}finally{
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			addFormWindow.dispose();
			window.setEnabled(true);
			window.setVisible(true);
		}
		return null;
	}

}
