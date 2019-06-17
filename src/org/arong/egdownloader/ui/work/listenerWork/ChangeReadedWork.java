package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.ActionEvent;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
/**
 * 更改阅读状态任务
 * @author 阿荣
 * @since 2014-09-18
 */
public class ChangeReadedWork implements IMenuListenerTask {

	public void doWork(Window window, ActionEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int index = table.getSelectedRow();
		Task task = table.getTasks().get(index);
		task.setReaded(! task.isReaded());
		mainWindow.taskDbTemplate.update(task);
		//隐藏tablePopupMenu
		mainWindow.tablePopupMenu.setVisible(false);
	}
}
