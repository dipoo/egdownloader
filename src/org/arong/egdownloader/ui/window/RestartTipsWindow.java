package org.arong.egdownloader.ui.window;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JWindow;

import org.arong.egdownloader.ui.swing.AJLabel;
/**
 * 重启提示窗口
 * @author dipoo
 * @since 2019-10-11
 */
public class RestartTipsWindow extends JWindow {
	private static final long serialVersionUID = 8239859673602520213L;
	public JLabel textLabel;
	
	public RestartTipsWindow(){
		this.setSize(200, 60);
		this.getContentPane().setBackground(Color.BLACK); 
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		textLabel = new AJLabel("正在重启中，请耐心等候", Color.WHITE);
		textLabel.setBackground(Color.BLACK);
		textLabel.setBounds(30, 15, 190, 30);
		this.getContentPane().add(textLabel);
		this.setVisible(true);
		this.toFront();
		this.setAlwaysOnTop(true);
	}
}
