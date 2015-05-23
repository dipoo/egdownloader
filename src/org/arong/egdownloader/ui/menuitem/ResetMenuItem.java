package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.listenerWork.ResetTaskWork;
/**
 * 重置任务菜单项
 * @author dipoo
 * @since 2014-11-18
 */
public class ResetMenuItem extends JMenuItem{

	private static final long serialVersionUID = -1668277828521783250L;
	
	public ResetMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		this.setIcon(IconManager.getIcon("reset"));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				EgDownloaderWindow mainWindow = window;
				TaskingTable table = (TaskingTable) mainWindow.runningTable;
				List<Task> tasks = table.getTasks();
				new ResetTaskWork(window, tasks, "重置后将无法还原，确定要重置所有任务到初始状态吗？(正在下载的任务不能重置)");
			}
		});
	}
}
