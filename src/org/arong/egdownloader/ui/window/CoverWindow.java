package org.arong.egdownloader.ui.window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.work.listenerWork.DownloadCoverWork;
/**
 * 漫画封面窗口
 * @author 阿荣
 * @since 2014-09-06
 */
public class CoverWindow extends JDialog {
	private static final long serialVersionUID = 6624222157904971813L;
	private JLabel coverLabel = null;
	private JButton downBtn = null;
	private EgDownloaderWindow mainWindow;
	public CoverWindow(Task task, EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.setTitle("漫画封面");
		this.getContentPane().setLayout(null);
		this.setLocationRelativeTo(mainWindow);
		this.setResizable(false);
		coverLabel = new JLabel();
		showCover(task);
		ComponentUtil.addComponents(getContentPane(),coverLabel);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CoverWindow this_ = (CoverWindow) e.getSource();
				this_.dispose();
			}
		});
	}
	public void showCover(Task task){
		String path = task.getSaveDir() + "/cover.jpg";
		File cover = new File(path);
		//不存在封面
		if(cover == null || !cover.exists()){
			this.setSize(200, 80);
			coverLabel.setText("还没下载封面");
			coverLabel.setBounds(5, 5, 100, 30);
			coverLabel.setIcon(null);
			if(downBtn == null){
				downBtn = new AJButton("下载", "", new MenuItemActonListener(mainWindow, new DownloadCoverWork()), 120, 5, 60, 30);
				this.getContentPane().add(downBtn);
			}else{
				downBtn.setVisible(true);
			}
		}else{
			ImageIcon icon = new ImageIcon(path);
			if(icon.getIconWidth() == -1){
				this.setSize(200, 80);
				coverLabel.setText("封面文件格式错误");
				coverLabel.setBounds(50, 5, 150, 30);
				coverLabel.setIcon(null);
			}else{
				this.setSize(icon.getIconWidth() + 20, icon.getIconHeight() + 45);
				coverLabel.setBounds(5, 5, icon.getIconWidth(), icon.getIconHeight());
				coverLabel.setIcon(icon);
			}
			if(downBtn != null){
				downBtn.setVisible(false);
			}
		}
		this.setLocationRelativeTo(null);
	}
}
