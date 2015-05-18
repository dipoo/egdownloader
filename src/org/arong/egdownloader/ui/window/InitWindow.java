package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.db.impl.PictureDom4jDbTemplate;
import org.arong.egdownloader.db.impl.SettingDom4jDbTemplate;
import org.arong.egdownloader.db.impl.TaskDom4jDbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.work.UpdateScriptWorker;
import org.arong.egdownloader.version.Version;
import org.arong.util.FileUtil;
/**
 * 程序初始化窗口
 * @author 阿荣
 * @since 2013-8-31
 */
public class InitWindow extends JWindow {

	private static final long serialVersionUID = -7316667195338580556L;
	
	public JLabel textLabel;
	
	public DbTemplate<Setting> settingDbTemplate;
	
	public DbTemplate<Task> taskDbTemplate;
	
	public DbTemplate<Picture> pictureDbTemplate;
	
	public Setting setting;
	
	public List<Task> tasks;
	
	public String scriptVersion;
	
	public InitWindow(){
		final ImageIcon icon = new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + "init.jpg"));
		this.setSize(icon.getIconWidth(), icon.getIconHeight());
//		this.getContentPane().setBackground(Color.decode("333"));
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		JPanel backPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {  
                Image img = icon.getImage();  
                g.drawImage(img, 0, 0, icon.getIconWidth(),  
                        icon.getIconHeight(), icon.getImageObserver());  
  
            }  
        };
        backPanel.setLayout(null);
		backPanel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		JLabel v = new AJLabel(Version.NAME + "v" + Version.VERSION, Color.WHITE, 0, 10, icon.getIconWidth(), 30);
		v.setHorizontalAlignment(JLabel.CENTER);
		textLabel = new AJLabel("程序初始化",Color.WHITE,0,100,icon.getIconWidth(),30);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		backPanel.add(v);
		backPanel.add(textLabel);
		this.getContentPane().add(backPanel);
		textLabel.setForeground(Color.WHITE);
		this.setVisible(true);
		this.toFront();
		textLabel.setText("读取配置数据");
		//检测数据目录是否存在,不存在则创建一个
		File data_path = new File(ComponentConst.getXmlDirPath());
		if(!data_path.exists()){
			data_path.mkdirs();
		}
		settingDbTemplate = new SettingDom4jDbTemplate();
		List<Setting> settings = settingDbTemplate.query();
		if(settings == null || settings.size() == 0){
			setting = new Setting();
			settingDbTemplate.store(setting);
		}else{
			setting = settings.get(0);
		}
		textLabel.setForeground(Color.WHITE);
		textLabel.setText("读取任务列表");
		taskDbTemplate = new TaskDom4jDbTemplate();
		pictureDbTemplate = new PictureDom4jDbTemplate();
		tasks = taskDbTemplate.query();
		if(tasks != null){
			//按照名称排序
			/*Collections.sort(tasks, new Comparator<Task>() {
				@Override
				public int compare(Task o1, Task o2) {
					return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
				}
			});*/
			int p_historyCount = 0;
			for (Task task : tasks) {
				task.setPictures(pictureDbTemplate.query("tid", task.getId()));
				p_historyCount += task.getTotal();
			}
			/**
			 * 为了兼容历史版本的db文件
			 */
			if(setting.getTaskHistoryCount() == 0){
				setting.setTaskHistoryCount(tasks.size());
			}
			if(setting.getPictureHistoryCount() == 0){
				setting.setPictureHistoryCount(p_historyCount);
			}
		}
		textLabel.setText("检测远程脚本");
		//检测脚本是否发生变化
		try {
			scriptVersion = WebClient.getRequestUseJava(ComponentConst.SCRIPT_VERSION_URL, null);
			//V.2015.03.26
			String currentVersion = FileUtil.getTextFromReader(new FileReader("script/version"));

			//版本返回信息需要以V.2字符串开头，否则可能获取的数据不正确，不做更新操作
			if(scriptVersion.startsWith("V.2") && scriptVersion != null && !currentVersion.equals(scriptVersion)){
				ComponentConst.removeScriptVersion = scriptVersion;
				ComponentConst.scriptChange = true;
				int r = JOptionPane.showConfirmDialog(null, "远程脚本发生变化，是否同步？");
				this.toFront();
				if(r == JOptionPane.YES_OPTION){
					new UpdateScriptWorker(this).execute();
				}else{
					startMain();
				}
			}else{
				startMain();
			}
		}catch (SocketTimeoutException e) {
			startMain();
		}catch (ConnectTimeoutException e) {
			startMain();
		} catch (FileNotFoundException e) {
			startMain();
		} catch (IOException e) {
			startMain();
		}
	}
	
	//启动主界面
	public void startMain(){
		if(ComponentConst.mainWindow != null){
			//暂停所有任务
			((TaskingTable)ComponentConst.mainWindow.runningTable).stopAllTasks();
			ComponentConst.mainWindow.changeTaskGroup(setting, tasks, taskDbTemplate, pictureDbTemplate, settingDbTemplate);
		}else{
			ComponentConst.mainWindow = new EgDownloaderWindow(setting, tasks, taskDbTemplate, pictureDbTemplate, settingDbTemplate);
		}
		textLabel.setText("初始化完成");
		ComponentConst.mainWindow.setVisible(true);
		this.dispose();//释放此窗口占用的资源，否则会消耗大量CPU
	}
}
