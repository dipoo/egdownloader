package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.db4o.Db4oTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.version.Version;

import com.db4o.query.Predicate;
/**
 * 程序初始化窗口
 * @author 阿荣
 * @since 2013-8-31
 */
public class InitWindow extends JFrame {

	private static final long serialVersionUID = -7316667195338580556L;
	
	private JLabel textLabel;
	
	@SuppressWarnings("serial")
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
		textLabel.setText("读取配置数据");
		//检测数据目录是否存在,不存在则创建一个
		File data_path = new File(ComponentConst.DATA_PATH);
		if(!data_path.exists()){
			data_path.mkdirs();
		}
		List<Setting> settings = Db4oTemplate.query(Setting.class, ComponentConst.SETTING_DATA_PATH);
		Setting setting = settings.size() > 0 ? settings.get(0) : new Setting();
		textLabel.setForeground(new Color(123,23,89));
		textLabel.setText("读取任务列表");
		List<Task> tasks = Db4oTemplate.query(Task.class, ComponentConst.TASK_DATA_PATH);
		for (final Task task : tasks) {
			task.pictures = Db4oTemplate.query(new Predicate<Picture>() {
				public boolean match(Picture pic) {
					return pic.getTid().equals(task.getId());
				}
			}, ComponentConst.PICTURE_DATA_PATH);
			System.out.println(task);
			System.out.println("");
		}
		JFrame egDownloaderWindow = new EgDownloaderWindow(setting, tasks);
		textLabel.setText("初始化完成");
		egDownloaderWindow.setVisible(true);
		this.dispose();//释放此窗口占用的资源，否则会消耗大量CPU
	}
	public JLabel getTextLabel(){
		return textLabel;
	}
}
