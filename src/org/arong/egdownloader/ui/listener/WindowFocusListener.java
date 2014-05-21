package org.arong.egdownloader.ui.listener;

import java.awt.Window;
import java.awt.event.WindowEvent;

/**
 * 窗口焦点监听器<br>
 * 当窗口失去焦点,隐藏此窗口<br>
 * 
 * @author 阿荣
 * @since 2013-8-25
 * 
 */
public class WindowFocusListener implements java.awt.event.WindowFocusListener {
	private Window window;
	
	public WindowFocusListener(Window window){
		this.window = window;
	}
	
	public void windowGainedFocus(WindowEvent e) {
	}
	//窗口失去焦点，隐藏
	public void windowLostFocus(WindowEvent e) {
		window.setVisible(false);
	}

}
