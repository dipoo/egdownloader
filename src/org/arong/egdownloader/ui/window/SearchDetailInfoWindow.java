package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JWindow;

import org.arong.egdownloader.model.SearchTask;
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
		this.setSize(720, 180);
		this.setLocationRelativeTo(searchComicWindow);
		this.setVisible(false);
		
		taskTagsPanel = new TaskTagsPanel(searchComicWindow.mainWindow, this);
		taskTagsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 4));
		taskTagsPanel.searchTags = true;
		//taskTagsPanel.excludePanel.setVisible(false);
		
		ComponentUtil.addComponents(getContentPane(), taskTagsPanel);
		
		this.addWindowListener(new WindowAdapter() {
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
		final SearchDetailInfoWindow this_ = this;
		taskTagsPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//点击自身隐藏
				this_.setVisible(false);
			}
			
		});
	}
	/**
	 * 搜索窗口使用
	 */
	public void showDetail(SearchTask task, Point p){
		//重设高度
		this.setSize(this.getWidth(), 250);
		//渲染面板
		taskTagsPanel.showSearchTagGroup(task);
		this.setLocationRelativeTo(searchComicWindow);
		this.setLocation((int)p.getX(), (int)p.getY());
		this.setVisible(true);
		if(taskTagsPanel.getVerticalScrollBar().isVisible()){
			//重设高度
			this.setSize(this.getWidth(), this.getHeight() + 30);
		}
	}
}
