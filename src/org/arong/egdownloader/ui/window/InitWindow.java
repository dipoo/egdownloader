package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.db.impl.PictureDom4jDbTemplate;
import org.arong.egdownloader.db.impl.SettingDom4jDbTemplate;
import org.arong.egdownloader.db.impl.TaskDom4jDbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.version.Version;
/**
 * 程序初始化窗口
 * @author 阿荣
 * @since 2013-8-31
 */
public class InitWindow extends JFrame {

	private static final long serialVersionUID = -7316667195338580556L;
	
	private JLabel textLabel;
	
	public InitWindow(){
		super(Version.NAME + "初始化");
		this.setSize(300, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);//去掉标题
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		textLabel = new AJLabel("程序初始化",new Color(123,23,89),0,10,300,30);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		this.getContentPane().add(textLabel);
		this.setVisible(true);
		textLabel.setForeground(new Color(123,23,89));
		textLabel.setText("1、读取配置数据");
		//检测数据目录是否存在,不存在则创建一个
		File data_path = new File(ComponentConst.DATA_PATH);
		if(!data_path.exists()){
			data_path.mkdirs();
		}
		DbTemplate<Setting> settingDbTemplate = new SettingDom4jDbTemplate();
		List<Setting> settings = settingDbTemplate.query();
		Setting setting = settings != null && settings.size() > 0 ? settings.get(0) : new Setting();
		textLabel.setForeground(new Color(123,23,89));
		textLabel.setText("2、读取任务列表");
		DbTemplate<Task> taskDbTemplate = new TaskDom4jDbTemplate();
		DbTemplate<Picture> pictureDbTemplate = new PictureDom4jDbTemplate();
		List<Task> tasks = taskDbTemplate.query();
		if(tasks != null){
			for (Task task : tasks) {
				task.pictures = pictureDbTemplate.query("tid", task.getId());
			}
		}
		JFrame egDownloaderWindow = new EgDownloaderWindow(setting, tasks, taskDbTemplate, pictureDbTemplate, settingDbTemplate);
		textLabel.setText("初始化完成");
		egDownloaderWindow.setVisible(true);
		this.dispose();//释放此窗口占用的资源，否则会消耗大量CPU
	}
	public JLabel getTextLabel(){
		return textLabel;
	}
}
