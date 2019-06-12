package org.arong.egdownloader.ui.work;

import java.io.InputStream;
import java.net.SocketTimeoutException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.util.FileUtil2;
import org.arong.util.HtmlUtils;
import org.arong.util.Tracker;
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
		final EgDownloaderWindow window = (EgDownloaderWindow)mainWindow;
		window.creatingWindow.setTitle("任务创建中");
		window.setEnabled(false);
		window.setVisible(true);
		AddFormDialog addFormWindow = ((AddFormDialog) window.addFormWindow);
		if(addFormWindow != null){
			addFormWindow.setVisible(false);
		}
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
				task = ScriptParser.buildTaskByJavaScript(task, setting, window.creatingWindow, false);
			}else{
				task = ScriptParser.buildTaskByJavaScript(task, setting, window.creatingWindow, false);
			}
			
			if(task != null){
				window.creatingWindow.setTitle("正在下载封面");
				//下载封面
				try{
					is = WebClient.getStreamUseJavaWithCookie(task.getDownloadCoverUrl(setting.isUseCoverReplaceDomain()), setting.getCookieInfo());
					FileUtil2.storeStream(task.getSaveDir(), "cover.jpg", is);//保存到目录
				} catch (SocketTimeoutException e){
					JOptionPane.showMessageDialog(null, "读取封面文件超时，请检查网络后重试");
				} catch (ConnectTimeoutException e){
					JOptionPane.showMessageDialog(null, "封面地址连接超时，请检查网络后重试");
				} 
				
				//设置最后创建时间
				setting.setLastCreateTime(task.getCreateTime());
				//设置历史任务总数
				setting.setTaskHistoryCount(setting.getTaskHistoryCount() + 1);
				//设置历史图片总数
				setting.setPictureHistoryCount(setting.getPictureHistoryCount() + task.getTotal());
				//保存到数据库
				window.creatingWindow.setTitle("正在保存数据");
				window.taskDbTemplate.store(task);//保存任务
				window.settingDbTemplate.update(setting);//保存配置
				//图片数目大于80则异步存储
				if(task.getTotal() > 80){
					new Thread(new Runnable() {
						public void run() {
							window.pictureDbTemplate.store(task.getPictures());//保存图片信息
							Tracker.println(HtmlUtils.greenColorHtml(task.getDisplayName() + "(" + task.getTotal() + ")：图片信息保存完成"));
						}
					}).start();
				}else{
					window.pictureDbTemplate.store(task.getPictures());//保存图片信息
				}
				
				//保存到内存
				final TaskingTable taskTable = (TaskingTable)window.runningTable;
				taskTable.getTasks().add(0, task);//将任务添加到列表最前面
				taskTable.propertyChange(task);//开始观察者模式，显示下载速度
				if(addFormWindow != null){
					addFormWindow.emptyField();//清空下载地址
					//关闭form,刷新table
					addFormWindow.dispose();
				}
				window.tablePane.setVisible(true);//将表格panel显示出来
				window.emptyPanel.setVisible(false);//将空任务label隐藏
				//是否开启下载
				if(setting.isAutoDownload()){
					taskTable.startTask(task);
				}
				task.setCreateWorker(null);
				if(window.viewModel == 2){
					window.taskImagePanel.init(taskTable.getTasks());
				}
				if(window.allTagsWindow != null && StringUtils.isNotBlank(task.getTags())){
					window.allTagsWindow.addTaskTags(task.getTags());
				}
				task.flushTagsCount(true);
			}else{
				JOptionPane.showMessageDialog(null, "创建异常");
				throw new RuntimeException("任务获取为空");
			}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "创建异常:" + e.getMessage());
			e.printStackTrace();
		}finally{
			((CreatingWindow)(window.creatingWindow)).reset();
			window.creatingWindow.dispose();
			if(addFormWindow != null){
				addFormWindow.dispose();
			}
			window.setEnabled(true);
			window.setVisible(true);
		}
		return null;
	}

}
