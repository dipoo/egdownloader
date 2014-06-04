package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJProgressBar;
/**
 * 新建任务细节显示窗口
 * @author 阿荣
 * @since 2014-06-01
 */
public class CreatingWindow extends JDialog {

	private static final long serialVersionUID = -2544191890083257820L;
	
	public JFrame mainWindow;
	
	public JProgressBar bar;

	public JLabel totalLabel;
	
	public JLabel nameLabel;
	
	public CreatingWindow(JFrame window){
		this.mainWindow = window;
		this.setTitle("任务创建中");
		this.setSize(400, 100);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(this.mainWindow);
		this.setBackground(Color.WHITE);
		
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CreatingWindow window = (CreatingWindow) e.getSource();
				window.mainWindow.setEnabled(true);
				window.dispose();
			}
		});
		
		nameLabel = new AJLabel("名称：", Color.BLACK, 10, 5, 380, 20);
		nameLabel.setVisible(false);
		totalLabel = new AJLabel("图片：", Color.BLACK, 10, 25, 100, 20);
		totalLabel.setVisible(false);
		bar = new AJProgressBar(100, 25, 200, 20, 0, 100);
		bar.setStringPainted(true);
		ComponentUtil.addComponents(getContentPane(), nameLabel, totalLabel, bar);
	}
	
	public void reset(){
		nameLabel.setText("名称：");
		totalLabel.setText("图片：");
		nameLabel.setVisible(false);
		totalLabel.setVisible(false);
		bar.setValue(0);
	}

}
