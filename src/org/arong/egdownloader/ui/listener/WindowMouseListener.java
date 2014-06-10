package org.arong.egdownloader.ui.listener;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * 窗口鼠标监听器<br>
 * 当鼠标单击窗口,隐藏此窗口<br>
 * 
 * @author 阿荣
 * @since 2013-9-1
 * 
 */
public class WindowMouseListener implements MouseListener {
	
	private Window window;
	public WindowMouseListener(Window window){
		this.window = window;
	}
	//鼠标单击,释放此窗口
	public void mouseClicked(MouseEvent e) {
		//this.window.setVisible(false);
		this.window.dispose();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
