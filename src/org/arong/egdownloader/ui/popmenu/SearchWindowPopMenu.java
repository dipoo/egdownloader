package org.arong.egdownloader.ui.popmenu;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.MergeWindow;
import org.arong.egdownloader.ui.window.SimpleSearchWindow;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.util.FileUtil2;

public class SearchWindowPopMenu extends JPopupMenu {
	public EgDownloaderWindow mainWindow;
	public JMenuItem openPictureItem;
	public JMenuItem downItem;
	public JMenuItem openBtPageItem;
	public JMenuItem showMergeItem;
	public SearchWindowPopMenu(EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		downItem = new AJMenuItem("创建任务", Color.BLACK,
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
		openPictureItem = new AJMenuItem("打开图片", Color.BLACK,
				IconManager.getIcon("openpic"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask searchTask = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						for(Task task : mainWindow.runningTable.getTasks()){
							if((task.getUrl() + "/").replaceAll("https", "http").startsWith(searchTask.getUrl().replaceAll("https", "http"))){
								if(task.getCurrent() > 0){
									List<Picture> pics = task.getPictures();
									for (Picture pic : pics) {
										if(pic.isCompleted()){
											try {
												String name = pic.getName();
												if(name.indexOf(".") != -1){
													name = pic.getNum() + name.substring(name.lastIndexOf("."), name.length());
												}else{
													name = pic.getNum() + ".jpg";
												}
												Desktop.getDesktop().open(new File(task.getSaveDir() + File.separator + name));
												break;
											} catch (Exception e1) {
												try {
													Desktop.getDesktop().open(new File(task.getSaveDir() + File.separator + pic.getName()));
													break;
												} catch (Exception e2) {
													JOptionPane.showMessageDialog(mainWindow, "图片不存在");
												}
											}
										}
									}
								}else{
									JOptionPane.showMessageDialog(mainWindow, "没有图片");
								}
							}
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
		openBtPageItem = new AJMenuItem("下载  BT", Color.BLACK,
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
		showMergeItem = new AJMenuItem("版本合并", Color.BLACK, "",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						if(mainWindow.searchComicWindow.mergeWindow == null){
							mainWindow.searchComicWindow.mergeWindow = new MergeWindow(mainWindow);
						}
						SearchTask st = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						mainWindow.searchComicWindow.mergeWindow.setSearchTask(st);
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						if(table.getTasks() == null || table.getTasks().size() == 0){
							return;
						}
						//以封面地址搜索
						if(mainWindow.simpleSearchWindow == null){
							mainWindow.simpleSearchWindow = new SimpleSearchWindow(mainWindow);
						}
						SimpleSearchWindow ssw = (SimpleSearchWindow) mainWindow.simpleSearchWindow;
						if(StringUtils.isNotBlank(st.getCoverUrl())){
							ssw.keyTextField.setText(String.format("cover:%s", st.getCoverUrl().replaceAll("-jpg_l.jpg", "").replaceAll("https:", "").replaceAll("http:", "")));
							ssw.searchBtn.doClick();
							mainWindow.setVisible(true);
							mainWindow.toFront();
						}
						Task t = table.getTasks().get(0);//选中第一行
						mainWindow.searchComicWindow.mergeWindow.setTask(t);
						mainWindow.searchComicWindow.mergeWindow.showMergeInfo();
						mainWindow.searchComicWindow.mergeWindow.setVisible(true);
						mainWindow.searchComicWindow.mergeWindow.toFront();
					}
				}));
		/*showTagsItem = new AJMenuItem("显示标签组", Color.BLACK, "",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						if(mainWindow.searchComicWindow.searchTagsWindow == null){
							mainWindow.searchComicWindow.searchTagsWindow = new SearchTagsWindow(mainWindow.searchComicWindow);
						}
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						mainWindow.searchComicWindow.searchTagsWindow.updateTaskTags(task);
						mainWindow.searchComicWindow.searchTagsWindow.setVisible(true);
						mainWindow.searchComicWindow.searchTagsWindow.toFront();
					}
				}));*/
		JMenuItem searchAuthorItem = new AJMenuItem("搜索作者", Color.BLACK, "",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						if(task.getAuthor() != null){
							mainWindow.searchComicWindow.doSearch(String.format("\"%s\"", task.getAuthor()));
						}
					}
				}));
		JMenuItem searchLocalAuthorItem = new AJMenuItem("本地搜作者", Color.BLACK, "",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						if(mainWindow.simpleSearchWindow == null){
							mainWindow.simpleSearchWindow = new SimpleSearchWindow(mainWindow);
						}
						SimpleSearchWindow ssw = (SimpleSearchWindow) mainWindow.simpleSearchWindow;
						if(task.getAuthor() != null){
							ssw.keyTextField.setText(task.getAuthor());
							ssw.searchBtn.doClick();
							mainWindow.setVisible(true);
							mainWindow.toFront();
						}
					}
				}));
		JMenuItem clearCoverItem = new AJMenuItem("清理封面", Color.BLACK, IconManager.getIcon("clear"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						SearchTask task = mainWindow.searchComicWindow.searchTasks.get(mainWindow.searchComicWindow.selectTaskIndex);
						String path = ComponentConst.CACHE_PATH + "/" + FileUtil2.filterDir(task.getUrl() + "/");
						File coverFile = new File(path);
						if(coverFile.exists()){
							coverFile.delete();
							JOptionPane.showMessageDialog(mainWindow.searchComicWindow, "清理完成");
						}
					}
				}));
		this.add(downItem);
		this.add(openPictureItem);
		this.add(openPageItem);
		this.add(openBtPageItem);
		/*this.add(showTagsItem);*/
		this.add(showMergeItem);
		this.add(searchAuthorItem);
		this.add(searchLocalAuthorItem);
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
