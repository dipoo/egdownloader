package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.MouseEvent;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.PictureTable;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.DetailWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;

public class ListTaskWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int index = table.getSelectedRow();
		if(mainWindow.detailWindow == null){
			mainWindow.detailWindow = new DetailWindow(new PictureTable(5, 40, ComponentConst.CLIENT_WIDTH - 20, 400, table.getTasks().get(index), mainWindow));
		}
		//隐藏tablePopupMenu
		mainWindow.tablePopupMenu.setVisible(false);
		mainWindow.detailWindow.setVisible(true);
		
	}

}
