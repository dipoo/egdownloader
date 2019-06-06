package org.arong.egdownloader.ui.window;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
/**
 * 删除任务进度显示窗口
 * @author 阿荣
 * @since 2014-09-19
 */
public class DeletingWindow extends JDialog {

	private static final long serialVersionUID = -2544191890083257820L;
	
	public JFrame mainWindow;
	public JLabel dataLabel;
	public JLabel infoLabel;
	
	public DeletingWindow(JFrame window){
		this.mainWindow = window;
		this.setTitle("正在删除任务");
		this.setSize(400, 120);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(this.mainWindow);
		this.setBackground(Color.WHITE);
		
		dataLabel = new AJLabel("", Color.BLACK, 160, 10, 80, 30);
		infoLabel = new AJLabel("", Color.BLACK, 10, 50, 380, 30);
		ComponentUtil.addComponents(getContentPane(), dataLabel, infoLabel);
	}
	
	public void setData(String data){
		dataLabel.setText(data);
	}
	public void setInfo(String info){
		infoLabel.setText(info);
	}

}
