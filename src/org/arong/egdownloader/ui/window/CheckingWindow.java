package org.arong.egdownloader.ui.window;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JLabel;

import org.arong.egdownloader.ui.swing.AJLabel;

/**
 * 查漏补缺任务的提示窗口
 * @author 阿荣
 * @since 2014-06-21
 */
public class CheckingWindow extends JDialog {

	private static final long serialVersionUID = -7059657779735889925L;
	public CheckingWindow(){
		this.setTitle("");
		this.setSize(200, 100);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		JLabel tips = new AJLabel("查漏补缺中……", Color.BLUE);
		getContentPane().add(tips);
	}

}
