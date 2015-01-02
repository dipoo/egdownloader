package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.ReBuildAllTaskWorker;
/**
 * 重建所有任务信息
 * @author dipoo
 * @since 2015-01-02
 */
public class ReBuildAllTaskMenuItem extends JMenuItem {

	private static final long serialVersionUID = -3444488661732450828L;
	public ReBuildAllTaskMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//询问是否重建任务
				int result = JOptionPane.showConfirmDialog(null, "重建任务需要暂停正在进行的任务，是否确定重建？");
				if(result == 0){//确定
					EgDownloaderWindow mainWindow = window;
					TaskingTable table = (TaskingTable) mainWindow.runningTable;
					//暂停所有任务
					table.stopAllTasks();
					new ReBuildAllTaskWorker(mainWindow).execute();
				}
			}
		});
	}
}
