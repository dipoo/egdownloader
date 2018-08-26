package org.arong.egdownloader.ui.popmenu;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.util.FileUtil;

public class SearchWindowPopMenu extends JPopupMenu {
	public EgDownloaderWindow mainWindow;
	public SearchWindowPopMenu(EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		JMenuItem downItem = new AJMenuItem("创建任务", Color.BLACK,
				IconManager.getIcon("add"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						mainWindow.setEnabled(false);
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						if(mainWindow.creatingWindow != null && mainWindow.creatingWindow.isVisible()){
							mainWindow.creatingWindow.setVisible(true);
							mainWindow.creatingWindow.toFront();
						}else{
							if (mainWindow.addFormWindow == null) {
								mainWindow.addFormWindow = new AddFormDialog(mainWindow);
							}
							((AddFormDialog)mainWindow.addFormWindow).emptyField();
							((AddFormDialog)mainWindow.addFormWindow).setUrl(task.getUrl());
							mainWindow.addFormWindow.setVisible(true);
							mainWindow.addFormWindow.toFront();
						}
					}
				}));
		JMenuItem openPageItem = new AJMenuItem("打开网页", Color.BLACK, IconManager.getIcon("browse"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						openPage(task.getUrl());
					}
				}));
		JMenuItem openBtPageItem = new AJMenuItem("下载BT", Color.BLACK,
				IconManager.getIcon("download"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						if(task.getBtUrl() != null){
							openPage(task.getBtUrl());
						}else{
							JOptionPane.showMessageDialog(mainWindow.searchComicWindow, "该漫画没有可以下载的bt文件");
						}
					}
				}));
		JMenuItem searchTitleItem = new AJMenuItem("搜索标题", Color.BLACK, "",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						mainWindow.searchComicWindow.doSearch(task.getName());
					}
				}));
		JMenuItem searchAuthorItem = new AJMenuItem("搜索作者", Color.BLACK, "",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						if(task.getAuthor() != null){
							mainWindow.searchComicWindow.doSearch("tag:" + task.getAuthor());
						}
					}
				}));
		JMenuItem clearCoverItem = new AJMenuItem("清理封面", Color.BLACK, IconManager.getIcon("clear"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						String path = ComponentConst.CACHE_PATH + "/" + FileUtil.filterDir(task.getUrl());
						File coverFile = new File(path);
						if(coverFile.exists()){
							coverFile.delete();
							JOptionPane.showMessageDialog(mainWindow.searchComicWindow, "清理完成");
						}
					}
				}));
		this.add(downItem);
		this.add(openPageItem);
		this.add(openBtPageItem);
		this.add(searchTitleItem);
		this.add(searchAuthorItem);
		this.add(clearCoverItem);
	}
	
	public void openPage(String url){
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e1) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start " + url);
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "不支持此功能");
			}
		} catch (URISyntaxException e1) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start " + url);
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "不支持此功能");
			}
		}finally{
			//隐藏popupMenu
			this.setVisible(false);
		}
	}
}
