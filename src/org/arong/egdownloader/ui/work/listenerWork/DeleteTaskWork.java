package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;

/**
 * 删除任务操作
 * @author 阿荣
 * @since 2014-05-24
 */
public class DeleteTaskWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		TaskingTable table = (TaskingTable) mainWindow.runningTable;
		int[] rows = table.getSelectedRows();
		if(rows.length == 0){
			JOptionPane.showMessageDialog(null, "请选择至少一个任务");
			return;
		}
		int option = JOptionPane.showConfirmDialog(null, "确定要删除" + (rows.length > 1 ? "这些" : "这个") + "任务吗");
		if(option == 0){
			Task task;
			List<Picture> pics = new ArrayList<Picture>();
			List<Task> tasks = new ArrayList<Task>();
			for(int i = 0; i < rows.length; i ++){
				if(table.getTasks().size() >= (rows[i])){
					task = table.getTasks().get(rows[i]);
					tasks.add(task);
					System.out.println("删除：" + task.getName());
					if(task.pictures != null && task.pictures.size() > 0){
						pics.addAll(task.pictures);
					}
				}
				
			}
			//操作数据库
			if(tasks.size() > 0){
				if(pics.size() > 0){
					mainWindow.pictureDbTemplate.delete(pics);//删除图片信息
				}
				mainWindow.taskDbTemplate.delete(tasks);//删除任务
				//更新内存
				table.getTasks().removeAll(tasks);
			}
			table.clearSelection();//使之不选中任何行
			table.updateUI();//刷新表格
			if(table.getTasks().size() == 0){
				mainWindow.tablePane.setVisible(false);//将任务panel隐藏
				mainWindow.emptyTableTips.setVisible(true);//将控任务label显示
			}
			mainWindow.setVisible(true);
		}
		
	}

}
