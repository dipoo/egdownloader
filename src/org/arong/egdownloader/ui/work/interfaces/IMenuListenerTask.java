package org.arong.egdownloader.ui.work.interfaces;

import java.awt.Window;

import javax.swing.event.MenuEvent;
/**
 * 监听菜单项任务接口
 * @author 阿荣
 * @since 2014-12-19
 */
public interface IMenuListenerTask {
	public void doWork(Window window, MenuEvent e);
}
