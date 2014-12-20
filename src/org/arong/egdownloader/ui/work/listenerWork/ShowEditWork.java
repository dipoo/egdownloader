package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.ActionEvent;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EditWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
/**
 * 编辑任务信息窗口
 * @author 阿荣
 * @since 2014-09-18
 */
public class ShowEditWork implements IMenuListenerTask {

	public void doWork(Window window, ActionEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int index = table.getSelectedRow();
		Task task = table.getTasks().get(index);
		EditWindow ew = (EditWindow) mainWindow.editWindow;
		if(ew == null){
			mainWindow.editWindow = new EditWindow(mainWindow, task);
		}
		ew = (EditWindow) mainWindow.editWindow;
		ew.setTask(task);
		ew.initInfo();
		mainWindow.setEnabled(false);
		ew.setVisible(true);
		
	}

}
