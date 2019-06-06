package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
/**
 * 打开文件夹的操作任务类
 * @author 阿荣
 * @since 2014-06-05
 */
public class OpenFolderTaskWork implements IMenuListenerTask {

	public void doWork(Window window, ActionEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int rowIndex = table.getSelectedRow();
		Task task = table.getTasks().get(rowIndex);
		if(TaskStatus.UNCREATED != task.getStatus()){
			try {
				Desktop.getDesktop().open(new File(task.getSaveDir()));
			} catch (Exception e1) {
				//e1.printStackTrace();
				JOptionPane.showMessageDialog(mainWindow, "文件夹已被删除");
				mainWindow.tablePopupMenu.setVisible(false);
			}
		}else{
			JOptionPane.showMessageDialog(mainWindow, "文件夹还未生成");
		}
		mainWindow.tablePopupMenu.setVisible(false);
	}

}
