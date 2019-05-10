package org.arong.egdownloader.ui.window;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;

/**
 * 显示搜索漫画列表任务的标签组窗口
 * @author dipoo
 * @date 2019-05-10
 */
public class SearchTagsWindow extends JDialog {
	public SearchComicWindow searchComicWindow;
	public TaskTagsPanel taskTagsPanel;
	
	public void updateTaskTags(SearchTask task){
		this.setTitle("标签组-" + task.getName());
		taskTagsPanel.parseTaskAttribute(task.getTags(), false);
	}
	
	public SearchTagsWindow(SearchComicWindow searchComicWindow){
		
		this.searchComicWindow = searchComicWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH - 200, 300);
		this.setTitle("标签组");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(searchComicWindow);
		
		taskTagsPanel = new TaskTagsPanel(searchComicWindow.mainWindow);
		taskTagsPanel.searchTags = true;
		
		ComponentUtil.addComponents(getContentPane(), taskTagsPanel);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
	}
}
