package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.Tracker;
/**
 * 更新所有未包含标签组任务的标签
 * @author dipoo
 * @since 2019-05-10
 */
public class UpdateAllNullTagsMenuItem extends JMenuItem {

	private static final long serialVersionUID = -2960067609351359632L;
	public UpdateAllNullTagsMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		this.setIcon(IconManager.getIcon(""));
		this.setForeground(new Color(0, 0, 85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//获取所有未完成、未开始的任务
				TaskingTable table = (TaskingTable) window.runningTable;
				//如果正在重建，则不下载
				if(table.isRebuild()){
					Tracker.println(UpdateAllNullTagsMenuItem.class, "正在重建任务");
					return;
				}
				Tracker.println(getClass(), "开始更新所有未包含标签组的任务");
				List<Task> tasks = table.getTasks();
				final List<Task> tasks2 = new ArrayList<Task>();
				for(int i = 0; i < tasks.size(); i ++){
					if(StringUtils.isBlank(tasks.get(i).getTags())){
						tasks2.add(tasks.get(i));
					}
				}
				Tracker.println(getClass(), String.format("共检测到%s个未包含标签组的任务", tasks2.size()));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new CommonSwingWorker(new Runnable() {
							public void run() {
								for(int i = 0; i < tasks2.size(); i ++){
									try {
										Task task = ScriptParser.getTaskByUrl(tasks2.get(i).getUrl(), window.setting);
										if(task != null &&StringUtils.isNotBlank(task.getTags())){
											tasks2.get(i).setTags(task.getTags());
											window.taskDbTemplate.update(tasks2.get(i));
										}
										Tracker.println(getClass(), String.format("已更新%s/%s个未包含标签组的任务", (i + 1), tasks2.size()));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								Tracker.println(getClass(), "【所有未包含标签组的任务已更新标签组】");
							}
						}).execute();
					}
				});
			}	
		});
	}
}
