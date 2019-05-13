package org.arong.egdownloader.ui.window;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.util.FileUtil2;

/**
 * 显示搜索漫画详细信息窗口
 * @author dipoo
 * @date 2019-05-10
 */
public class SearchDetailInfoWindow extends JWindow {
	public SearchComicWindow searchComicWindow;
	public TaskTagsPanel taskTagsPanel;
	
	public SearchDetailInfoWindow(final SearchComicWindow searchComicWindow){
		
		this.searchComicWindow = searchComicWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH - 300, 300);
		this.setLocationRelativeTo(searchComicWindow);
		
		taskTagsPanel = new TaskTagsPanel(searchComicWindow.mainWindow);
		taskTagsPanel.searchTags = true;
		
		ComponentUtil.addComponents(getContentPane(), taskTagsPanel);
	}
	/**
	 * 搜索窗口使用
	 */
	public void showDetail(SearchTask task, Point p){
		taskTagsPanel.showMyFav = false;
		taskTagsPanel.parseTaskAttribute(task.getTags(), searchComicWindow.mainWindow.setting.isTagsTranslate());
		this.setLocationRelativeTo(searchComicWindow);
		this.setLocation((int)p.getX(), (int)p.getY() - this.getHeight() / 2 - 50);
		this.setVisible(true);
	}
}
