package org.arong.book.ui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
/**
 * 用于监听菜单子项的鼠标监听器(没用，监听不到)
 * @author 阿荣
 * @since 2013-9-1
 */
public class MenuItemMouseListener implements MouseListener{
	
	private JFrame mainWindow;
	public MenuItemMouseListener(JFrame mainWindow){
		this.mainWindow = mainWindow;
	}
	public void mouseClicked(MouseEvent e) {
		System.out.println(mainWindow);
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
