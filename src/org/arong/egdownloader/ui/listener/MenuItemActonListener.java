package org.arong.egdownloader.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
/**
 * 用于监听菜单子项的鼠标监听器(没用，监听不到)
 * @author 阿荣
 * @since 2013-9-1
 */
public class MenuItemActonListener implements ActionListener{
	
	private JFrame mainWindow;
	
	private IMenuListenerTask task;
	
	public MenuItemActonListener(JFrame mainWindow, IMenuListenerTask task){
		this.mainWindow = mainWindow;
		this.task = task;
	}
	
	public void actionPerformed(ActionEvent e) {
		task.doWork(mainWindow, e);
	}
	

}
