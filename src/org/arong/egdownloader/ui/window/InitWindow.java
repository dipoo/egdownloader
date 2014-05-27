package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.db4o.Db4oTemplate;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.version.Version;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;
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
		textLabel = new AJLabel("程序初始化",Color.BLUE,0,10,300,30);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		this.getContentPane().add(textLabel);
		this.setVisible(true);
		textLabel.setForeground(Color.BLUE);
		//打开db4o级联
		EmbeddedConfiguration conf=Db4oEmbedded.newConfiguration();
		conf.common().objectClass("org.arong.egdownloader.model.Task").cascadeOnUpdate(true);
		conf.common().objectClass("org.arong.egdownloader.model.Task").cascadeOnDelete(true);
		textLabel.setText("读取配置数据");
		List<Setting> settings = Db4oTemplate.query(Setting.class, ComponentConst.SETTING_DATA_PATH);
		Setting setting = settings.size() > 0 ? settings.get(0) : new Setting();
		textLabel.setForeground(new Color(123,23,89));
		textLabel.setText("读取任务列表");
		List<Task> tasks = Db4oTemplate.query(Task.class, ComponentConst.TASK_DATA_PATH);
		JFrame egDownloaderWindow = new EgDownloaderWindow(setting, tasks);
		textLabel.setText("初始化完成");
		egDownloaderWindow.setVisible(true);
		this.dispose();//释放此窗口占用的资源，否则会消耗大量CPU
	}
	public JLabel getTextLabel(){
		return textLabel;
	}
}
