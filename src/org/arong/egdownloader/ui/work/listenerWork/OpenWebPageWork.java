package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 打开任务下载网页任务
 * @author 阿荣
 * @since 2014-06-18
 */
public class OpenWebPageWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int index = table.getSelectedRow();
		Task task = table.getTasks().get(index);
		try {
			Desktop.getDesktop().browse(new URI(task.getUrl()));
		} catch (IOException e1) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start " + task.getUrl());
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "不支持此功能");
			}
		} catch (URISyntaxException e1) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start " + task.getUrl());
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "不支持此功能");
			}
		}finally{
			//隐藏tablePopupMenu
			mainWindow.tablePopupMenu.setVisible(false);
		}
	}

}
