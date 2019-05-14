package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JWindow;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;

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
		this.setSize(ComponentConst.CLIENT_WIDTH - 400, 250);
		this.setLocationRelativeTo(searchComicWindow);
		
		taskTagsPanel = new TaskTagsPanel(searchComicWindow.mainWindow);
		taskTagsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		//taskTagsPanel.textPane.setBackground(Color.GRAY);
		taskTagsPanel.searchTags = true;
		
		ComponentUtil.addComponents(getContentPane(), taskTagsPanel);
	}
	/**
	 * 搜索窗口使用
	 */
	public void showDetail(SearchTask task, Point p){
		taskTagsPanel.showMyFav = false;
		taskTagsPanel.parseTaskAttribute(task);
		this.setLocationRelativeTo(searchComicWindow);
		this.setLocation((int)p.getX(), (int)p.getY() - this.getHeight() / 2 - 50);
		this.setVisible(true);
	}
}
