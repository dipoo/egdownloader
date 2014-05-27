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
	private MouseAction action;
	public OperaBtnMouseListener(Window mainWindow, MouseAction action, IListenerTask task){
		this.mainWindow = mainWindow;
		this.task = task;
		this.action = action;
	}
	public void mouseClicked(MouseEvent e) {
		if(action == MouseAction.CLICK){
			task.doWork(mainWindow, e);
		}
	}

	public void mousePressed(MouseEvent e) {
		if(action == MouseAction.PRESS){
			task.doWork(mainWindow, e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(action == MouseAction.RELEASE){
			task.doWork(mainWindow, e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if(action == MouseAction.ENTER){
			task.doWork(mainWindow, e);
		}
	}

	public void mouseExited(MouseEvent e) {
		if(action == MouseAction.EXIT){
			task.doWork(mainWindow, e);
		}
	}
}
