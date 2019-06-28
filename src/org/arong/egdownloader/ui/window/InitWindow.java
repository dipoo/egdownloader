package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.arong.egdownloader.db.AbstractSqlDbTemplate;
import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.db.impl.PictureDom4jDbTemplate;
import org.arong.egdownloader.db.impl.PictureSqliteDbTemplate;
import org.arong.egdownloader.db.impl.SettingDom4jDbTemplate;
import org.arong.egdownloader.db.impl.TaskDom4jDbTemplate;
import org.arong.egdownloader.db.impl.TaskSqliteDbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskList;
import org.arong.egdownloader.spider.Proxy;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.work.UpdateScriptWorker;
import org.arong.egdownloader.version.Version;
import org.arong.util.FileUtil2;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
/**
 * 程序初始化窗口
 * @author 阿荣
 * @since 2013-8-31
 */
public class InitWindow extends JWindow {

	private static final long serialVersionUID = -7316667195338580556L;
	
	public JLabel textLabel;
	
	public DbTemplate<Setting> settingDbTemplate;
	
	public AbstractSqlDbTemplate<Task> taskDbTemplate;
	
	public AbstractSqlDbTemplate<Picture> pictureDbTemplate;
	
	public Setting setting;
	
	public TaskList<Task> tasks;
	
	public String scriptVersion;
	
	public InitWindow(){
		try{
			ComponentConst.osname = System.getProperties().getProperty("os.name");
			if(ComponentConst.osname == null){
				ComponentConst.osname = "Linux";
			}
			
			final ImageIcon icon = IconManager.getIcon("init");
			this.setSize(icon.getIconWidth(), icon.getIconHeight());
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
			String text = String.format("<html>%s<span style='font-family:Consolas;font-size:12px;'>v%s.%s</span></html>", Version.NAME, Version.VERSION, Version.JARVERSION);
			JLabel v = new AJLabel(text, Color.WHITE, 0, 10, icon.getIconWidth(), 30);
			v.setHorizontalAlignment(JLabel.CENTER);
			v.setFont(FontConst.Microsoft_BOLD_13);
			textLabel = new AJLabel("程序初始化",Color.WHITE, 0, 100, icon.getIconWidth(), 30);
			textLabel.setFont(FontConst.Microsoft_BOLD_13);
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
			
			if("BeautyEye".equals(setting.getSkin())){
				//皮肤
				try {
					BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
					BeautyEyeLNFHelper.launchBeautyEyeLNF();
					UIManager.put("RootPane.setupButtonVisible", false);
				} catch (Exception e) {
				}
			}
			
			textLabel.setForeground(Color.WHITE);
			textLabel.setText("读取任务列表");
			taskDbTemplate = new TaskSqliteDbTemplate();//TaskDom4jDbTemplate();
			pictureDbTemplate = new PictureSqliteDbTemplate();//PictureDom4jDbTemplate();
			Task t = new Task();t.setGroupname(ComponentConst.groupName);t.setStatus(null);
			tasks = (TaskList<Task>) taskDbTemplate.query(t);
			if(tasks != null && tasks.size() > 0){
				int p_historyCount = 0;
				textLabel.setText("读取图片列表");
				for (int i = 0; i < tasks.size(); i ++) {
					tasks.get(i).setPictureSqliteDbTemplate(pictureDbTemplate);
					//预先读取前25条任务的图片列表
					if(i < 25){
						tasks.get(i).setPictures(pictureDbTemplate.query("tid", tasks.get(i).getId()));
					}
					p_historyCount += tasks.get(i).getTotal();
					//初始化任务标签对应的任务数
					tasks.get(i).flushTagsCount(true);
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
				textLabel.setText("数据文件备份");
				FileUtil2.copyFile(ComponentConst.DB_DATA_FILE_PATH, ComponentConst.DB_DATA_FILE_PATH_BAK);
			}else{
				//检测是否存在task.xml
				tasks = (TaskList<Task>)new TaskDom4jDbTemplate().query();
				if(tasks != null && tasks.size() > 0){
					textLabel.setText("导入任务");
					taskDbTemplate.store(tasks);
					int p_historyCount = 0;
					DbTemplate<Picture> pictemp = new PictureDom4jDbTemplate();
					for (int i = 0; i < tasks.size(); i ++) {
						textLabel.setText("导入图片" + i + "/" + tasks.size());
						tasks.get(i).setPictures(pictemp.query("tid", tasks.get(i).getId()));
						p_historyCount += tasks.get(i).getTotal();
						//保存
						pictureDbTemplate.store(tasks.get(i).getPictures());
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
			}
			//设置代理
			Proxy.init(setting.isUseProxy(), setting.getProxyType(), setting.getProxyIp(), 
					setting.getProxyPort(), setting.getProxyUsername(), setting.getProxyPwd());
			if(!setting.isDebug()){
				textLabel.setText("检测远程脚本");
				//检测脚本是否发生变化
				try {
					scriptVersion = WebClient.getRequestUseJava(ComponentConst.SCRIPT_VERSION_URL, null);
					//V.2015.03.26
					String currentVersion = FileUtil2.getTextFromReader(new FileReader("script/version"));
	
					//版本返回信息需要以V.2字符串开头，否则可能获取的数据不正确，不做更新操作
					if(scriptVersion.startsWith("V.2") && scriptVersion != null && !currentVersion.equals(scriptVersion)){
						ComponentConst.remoteScriptVersion = scriptVersion;
						ComponentConst.scriptChange = true;
						int r = JOptionPane.showConfirmDialog(null, "远程脚本发生变化，是否同步？");
						this.toFront();
						if(r == JOptionPane.OK_OPTION){
							new UpdateScriptWorker(this).execute();
						}else{
							startMain();
						}
					}else{
						startMain();
					}
				}catch (Exception e) {
					startMain();
					e.printStackTrace();
				}
			}else{
				startMain();
			}
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "运行报错：" + e.getMessage());
			JOptionPane.showMessageDialog(null, "自动退出");
			System.exit(0);
		}
	}
	
	//启动主界面
	public void startMain(){
		this.dispose();//释放此窗口占用的资源，否则会消耗大量CPU
		final InitWindow this_ = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(ComponentConst.mainWindow != null){
					//暂停所有任务
					((TaskingTable)ComponentConst.mainWindow.runningTable).stopAllTasks();
					ComponentConst.mainWindow.changeTaskGroup(setting, tasks, taskDbTemplate, pictureDbTemplate, settingDbTemplate);
				}else{
					ComponentConst.mainWindow = new EgDownloaderWindow(this_, setting, tasks, taskDbTemplate, pictureDbTemplate, settingDbTemplate);
				}
				textLabel.setText("初始化完成");
				ComponentConst.mainWindow.setVisible(true);
				ComponentConst.mainWindow.toFront();
			}
		});
		
	}
}
