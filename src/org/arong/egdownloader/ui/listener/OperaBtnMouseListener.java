package org.arong.egdownloader.ui.listener;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 按钮鼠标操作监听类
 * @author 阿荣
 * @since 2014-05-22
 */
public class OperaBtnMouseListener implements MouseListener {
	private Window mainWindow;
	private IListenerTask task;
	public OperaBtnMouseListener(Window mainWindow, IListenerTask task){
		this.mainWindow = mainWindow;
		this.task = task;
	}
	public void mouseClicked(MouseEvent e) {
		task.doWork(mainWindow);
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
