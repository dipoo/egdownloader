package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.DeletingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.DeleteWorker;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;

/**
 * 删除任务操作
 * @author 阿荣
 * @since 2014-05-24
 */
public class DeleteTaskWork implements IListenerTask {

	public void doWork(Window window, MouseEvent e) {
		final EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		final TaskingTable table = (TaskingTable) mainWindow.runningTable;
		final int[] rows = table.getSelectedRows();
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				if(rows.length == 0){
					JOptionPane.showMessageDialog(mainWindow, "请选择至少一个任务");
					return;
				}
				int option = JOptionPane.showConfirmDialog(mainWindow, "确定要删除" + (rows.length > 1 ? "这" + rows.length + "个" : "这个【"  + table.getTasks().get(rows[0]).getDisplayName() + "】") + "任务吗");
				if(option == JOptionPane.OK_OPTION){
							//mainWindow.setEnabled(false);
							DeletingWindow w = (DeletingWindow) mainWindow.deletingWindow;
							if(w == null){
								mainWindow.deletingWindow = new DeletingWindow(mainWindow);
							}
							w = (DeletingWindow) mainWindow.deletingWindow;
							w.setData("    0/" + rows.length);
							w.setInfo("正在收集任务图片");
							int o = JOptionPane.showConfirmDialog(mainWindow, "是否删除下载的文件？");
							w.setVisible(true);
							//执行删除线程
							new DeleteWorker(mainWindow, table, w, rows, o == 0).execute();
						
				}
			}
		});
	}

}
