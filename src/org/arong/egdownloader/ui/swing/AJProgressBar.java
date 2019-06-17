package org.arong.egdownloader.ui.swing;

import javax.swing.JProgressBar;
/**
 * 进度条封装，实现进度条+文本显示
 * @author Administrator
 *
 */
public class AJProgressBar extends JProgressBar {

	private static final long serialVersionUID = 8982790542840735278L;
	public AJProgressBar(int x, int y, int width, int height, int min, int max){
		super(min, max);
		this.setBounds(x, y, width, height);
	}
	public AJProgressBar(int min, int max){
		super(min, max);
	}
	
}
