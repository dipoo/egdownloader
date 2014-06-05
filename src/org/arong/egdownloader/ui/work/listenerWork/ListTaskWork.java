package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.MouseEvent;

import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;

public class ListTaskWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
//		int rowIndex = table.rowAtPoint(e.getPoint());
		System.out.println("查看");
		//隐藏tablePopupMenu
		mainWindow.tablePopupMenu.setVisible(false);
	}

}
