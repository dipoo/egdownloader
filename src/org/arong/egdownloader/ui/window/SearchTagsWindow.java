package org.arong.egdownloader.ui.window;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;

/**
 * 显示我收藏的标签
 * @author dipoo
 * @date 2019-05-10
 */
public class SearchTagsWindow extends JDialog {
	public SearchComicWindow searchComicWindow;
	public TaskTagsPanel taskTagsPanel;
	
	public SearchTagsWindow(final SearchComicWindow searchComicWindow){
		
		this.searchComicWindow = searchComicWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH - 200, 300);
		this.setTitle("标签组");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(searchComicWindow);
		
		taskTagsPanel = new TaskTagsPanel(searchComicWindow.mainWindow, this);
		taskTagsPanel.searchTags = true;
		
		ComponentUtil.addComponents(getContentPane(), taskTagsPanel);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window this_ = (Window) e.getSource();
				this_.dispose();
				searchComicWindow.toFront();
			}
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
	}
}
