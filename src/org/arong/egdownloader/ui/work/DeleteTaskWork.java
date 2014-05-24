package org.arong.egdownloader.ui.work;

import java.awt.Window;

import javax.swing.JOptionPane;

import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;

/**
 * 删除任务操作
 * @author 阿荣
 * @since 2014-05-24
 */
public class DeleteTaskWork implements IListenerTask {

	public void doWork(Window window) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int[] rows = table.getSelectedRows();
		if(rows.length == 0){
			JOptionPane.showMessageDialog(null, "请选择至少一个任务");
			return;
		}
		int option = JOptionPane.showConfirmDialog(null, "确定要删除" + (rows.length > 1 ? "这些" : "这个") + "任务吗");
		if(option == 0){
			for(int i = 0; i < rows.length; i ++){
				if(table.getTasks().size() >= (rows[i] - i)){
					table.getTasks().remove(rows[i] - i);
				}
				//操作数据库
			}
			table.clearSelection();//使之不选中任何行
			table.updateUI();//刷新表格
			mainWindow.setVisible(true);
		}
		
	}

}
