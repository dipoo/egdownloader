package org.arong.egdownloader.ui.window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
/**
 * 漫画封面窗口
 * @author 阿荣
 * @since 2014-09-06
 */
public class CoverWindow extends JDialog {
	private static final long serialVersionUID = 6624222157904971813L;
	private JLabel coverLabel = null;
	public CoverWindow(Task task) {
		this.setTitle("漫画封面");
		this.getContentPane().setLayout(null);
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
		String path = ComponentConst.getSavePathPreffix() + task.getSaveDir() + "/cover.jpg";
		File cover = new File(path);
		//不存在封面
		if(cover == null || !cover.exists()){
			this.setSize(200, 80);
			coverLabel.setText("还没下载封面");
			coverLabel.setBounds(5, 5, 100, 30);
			coverLabel.setIcon(null);
		}else{
			ImageIcon icon = new ImageIcon(path);
			this.setSize(icon.getIconWidth() + 20, icon.getIconHeight() + 45);
			coverLabel.setBounds(5, 5, icon.getIconWidth(), icon.getIconHeight());
			coverLabel.setIcon(icon);
		}
		this.setLocationRelativeTo(null);
	}
}
