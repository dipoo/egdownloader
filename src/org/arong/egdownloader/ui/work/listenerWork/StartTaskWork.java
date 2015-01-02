package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import org.arong.egdownloader.ui.menuitem.StartAllTaskMenuItem;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.Tracker;
/**
 * 开始任务操作
 * @author dipoo
 * @since 2014-12-30
 */
public class StartTaskWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		//如果正在重建，则不下载
		if(table.isRebuild()){
			Tracker.println(StartAllTaskMenuItem.class, "正在重建任务");
			return;
		}
		int[] rows = table.getSelectedRows();
		if(rows.length == 0){
			JOptionPane.showMessageDialog(null, "请选择至少一个任务");
			return;
		}
		for(int i = 0; i < rows.length; i++){
			table.startTask(table.getTasks().get(rows[i]));
		}
	}
}
