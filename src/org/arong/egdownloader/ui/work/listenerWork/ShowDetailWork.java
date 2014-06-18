package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.PictureTable;
import org.arong.egdownloader.ui.table.PictureTableModel;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.DetailWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 下载任务详细信息窗口
 * @author 阿荣
 * @since 2014-06-18
 */
public class ShowDetailWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int index = table.getSelectedRow();
		Task task = table.getTasks().get(index);
		//当任务不为下载中，可以对图片进行排序
		if(task.getStatus() != TaskStatus.STARTED){
			sortPictures(task.getPictures());
		}
		DetailWindow dw = (DetailWindow) mainWindow.detailWindow;
		if(dw == null){
			mainWindow.detailWindow = new DetailWindow(new PictureTable(5, 40, ComponentConst.CLIENT_WIDTH - 20, 400,task , mainWindow));
		}else{
			((PictureTableModel)dw.pictureTable.getModel()).setPictures(task.getPictures());
		}
		dw = (DetailWindow) mainWindow.detailWindow;
		dw.resetTile(task.getTotal() + "");
		dw.taskNameLabel.setText(task.getName());
		dw.pictureTable.updateUI();
		//隐藏tablePopupMenu
		mainWindow.tablePopupMenu.setVisible(false);
		mainWindow.detailWindow.setVisible(true);
	}
	private void sortPictures(List<Picture> pics){
		if(pics != null)
			Collections.sort(pics, new Comparator<Picture>() {
				public int compare(Picture o1, Picture o2) {
					if(Integer.parseInt(o1.getNum()) > Integer.parseInt(o2.getNum()))
						return 1;
					else return -1;
				}
			});
	}

}
