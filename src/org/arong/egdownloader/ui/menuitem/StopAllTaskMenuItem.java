package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.Tracker;
/**
 * 暂停所有任务
 * @author dipoo
 * @since 2014-12-07
 */
public class StopAllTaskMenuItem extends JMenuItem {

	private static final long serialVersionUID = 8033742031776192264L;
	public StopAllTaskMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//获取所有未完成、未开始的任务
				TaskingTable table = (TaskingTable) window.runningTable;
				List<Task> tasks = table.getTasks();
				Task task = null;
				for(int i = 0; i < tasks.size(); i ++){
					if(tasks.get(i).getStatus() == TaskStatus.STARTED
							|| tasks.get(i).getStatus() == TaskStatus.WAITING){
						task = tasks.get(i);
						task.setStatus(TaskStatus.STOPED);
						if(task.getDownloadWorker() != null){
							task.getDownloadWorker().cancel(true);
							task.setDownloadWorker(null);//swingworker不能复用，需要重新建立
							//更新任务数据
							window.taskDbTemplate.update(task);
						}
					}
				}
				//清空排队等待列表
				table.getWaitingTasks().clear();
				//清空正在下载数
				table.setRunningNum(0);
				//刷新表格
				table.updateUI();
				Tracker.println(getClass(), "【所有任务已暂停】");
			}
		});
	}
}
