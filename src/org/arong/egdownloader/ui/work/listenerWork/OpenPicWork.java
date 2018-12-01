package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.menuitem.StartAllTaskMenuItem;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.util.Tracker;

public class OpenPicWork implements IMenuListenerTask{

	public void doWork(Window window, ActionEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		//如果正在重建，则不打开
		if(table.isRebuild()){
			Tracker.println(StartAllTaskMenuItem.class, "正在重建任务");
			return;
		}
		int index = table.getSelectedRow();
		Task task = table.getTasks().get(index);
		if(task.getCurrent() > 0){
			List<Picture> pics = task.getPictures();
			int c = 0;
			for (Picture pic : pics) {
				if(pic.isCompleted()){
					try {
						String name = pic.getName();
						if(name.indexOf(".") != -1){
							name = pic.getNum() + name.substring(name.lastIndexOf("."), name.length());
						}else{
							name = pic.getNum() + ".jpg";
						}
						Desktop.getDesktop().open(new File(task.getSaveDir() + File.separator + name));
						break;
					} catch (Exception e1) {
						try {
							Desktop.getDesktop().open(new File(task.getSaveDir() + File.separator + pic.getName()));
							break;
						} catch (Exception e2) {
							if(c < 1){
								c ++;
								JOptionPane.showMessageDialog(mainWindow, "图片不存在");
							}
						}
					}
				}
			}
		}else{
			JOptionPane.showMessageDialog(mainWindow, "没有图片");
		}
	}

}
