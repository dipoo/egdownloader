package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.Tracker;
/**
 * 开始所有任务
 * @author dipoo
 * @since 2014-12-07
 */
public class StartAllTaskMenuItem extends JMenuItem {

	private static final long serialVersionUID = -2960067609351359632L;
	public StartAllTaskMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("start"))));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//获取所有未完成、未开始的任务
				TaskingTable table = (TaskingTable) window.runningTable;
				//如果正在重建，则不下载
				if(table.isRebuild()){
					Tracker.println(StartAllTaskMenuItem.class, "正在重建任务");
					return;
				}
				List<Task> tasks = table.getTasks();
				for(int i = 0; i < tasks.size(); i ++){
					if(tasks.get(i).getStatus() == TaskStatus.UNSTARTED
							|| tasks.get(i).getStatus() == TaskStatus.UNCREATED 
							|| tasks.get(i).getStatus() == TaskStatus.STOPED){
						//开启任务
						table.startTask(tasks.get(i));
					}
				}
				//刷新表格
				table.updateUI();
				Tracker.println(getClass(), "【所有任务已开启】");
			}
		});
	}
}
