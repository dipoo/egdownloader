package org.arong.egdownloader.ui.popmenu;

import java.awt.Color;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.menuitem.StartAllTaskMenuItem;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SimpleSearchWindow;
import org.arong.egdownloader.ui.window.ZiptingWindow;
import org.arong.egdownloader.ui.work.ZIPWorker;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.egdownloader.ui.work.listenerWork.ChangeReadedWork;
import org.arong.egdownloader.ui.work.listenerWork.CheckResetWork;
import org.arong.egdownloader.ui.work.listenerWork.DownloadCoverWork;
import org.arong.egdownloader.ui.work.listenerWork.OpenFolderTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.OpenPicWork;
import org.arong.egdownloader.ui.work.listenerWork.OpenWebPageWork;
import org.arong.egdownloader.ui.work.listenerWork.ResetTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.ShowDetailWork;
import org.arong.egdownloader.ui.work.listenerWork.ShowEditWork;
import org.arong.util.Tracker;
/**
 * 主窗口右键菜单
 *
 */
public class MainPopupMenu extends AJPopupMenu{
	public MainPopupMenu(EgDownloaderWindow mainWindow){
		Color menuItemColor = new Color(0,0,85);
		//右键菜单：开始
		AJMenuItem startPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_START_MENU_TEXT, menuItemColor,
				IconManager.getIcon("start"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						//如果正在重建，则不下载
						if(table.isRebuild()){
							Tracker.println(StartAllTaskMenuItem.class, "正在重建任务");
							return;
						}
						int index = table.getSelectedRow();
						table.startTask(table.getTasks().get(index));
					}
		}));
		//右键菜单：暂停
		AJMenuItem stopPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_STOP_MENU_TEXT, menuItemColor,
				IconManager.getIcon("stop"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						//如果正在重建，则不执行
						if(table.isRebuild()){
							Tracker.println(StartAllTaskMenuItem.class, "正在重建任务");
							return;
						}
						int index = table.getSelectedRow();
						table.stopTask(table.getTasks().get(index));
					}
		}));
		//右键菜单：查看详细
		/*AJMenuItem detailPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_DETAIL_MENU_TEXT, menuItemColor,
				IconManager.getIcon("detail"),
				new MenuItemActonListener(mainWindow, new ShowDetailWork()));*/
		//右键菜单：复制网址
		AJMenuItem copyUrlPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_COPYURL_MENU_TEXT, menuItemColor,
				IconManager.getIcon("copy"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
			public void doWork(Window window, ActionEvent e) {
				EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
				TaskingTable table = (TaskingTable) mainWindow.runningTable;
				int index = table.getSelectedRow();
				Task task = table.getTasks().get(index);
				StringSelection ss = new StringSelection(task.getUrl());
				table.getToolkit().getSystemClipboard().setContents(ss, ss);
				mainWindow.tablePopupMenu.setVisible(false);
			}
		}));
		//右键菜单：打开图片
		AJMenuItem openPicPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_OPENFILE_MENU_TEXT, menuItemColor,
				IconManager.getIcon("openpic"),
				new MenuItemActonListener(mainWindow, new OpenPicWork()));
		//右键菜单：打开文件夹
		AJMenuItem openFolderPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_OPENFOLDER_MENU_TEXT, menuItemColor,
				IconManager.getIcon("folder"),
				new MenuItemActonListener(mainWindow, new OpenFolderTaskWork()));
		//右键菜单：打开网页
		AJMenuItem openWebPageMenuItem = new AJMenuItem(ComponentConst.POPUP_OPENWEBPAGE_MENU_TEXT, menuItemColor,
				IconManager.getIcon("browse"),
				new MenuItemActonListener(mainWindow, new OpenWebPageWork()));
		//右键菜单：下载封面
		AJMenuItem downloadCoverMenuItem = new AJMenuItem(ComponentConst.POPUP_DOWNLOADCOVER_MENU_TEXT, menuItemColor,
				IconManager.getIcon("download"),
				new MenuItemActonListener(mainWindow, new DownloadCoverWork()));
		//右键菜单：查漏补缺
		AJMenuItem checkResetMenuItem = new AJMenuItem(ComponentConst.POPUP_CHECKRESET_MENU_TEXT, menuItemColor,
				IconManager.getIcon("check"),
				new MenuItemActonListener(mainWindow, new CheckResetWork()));
		//右键菜单：更改阅读状态
		AJMenuItem changeReadedMenuItem = new AJMenuItem(ComponentConst.POPUP_CHANGEREADED_MENU_TEXT, menuItemColor,
				IconManager.getIcon("change"),
				new MenuItemActonListener(mainWindow, new ChangeReadedWork()));
		//右键菜单：打包ZIP
		AJMenuItem zipMenuItem = new AJMenuItem(ComponentConst.POPUP_ZIP_MENU_TEXT, menuItemColor,
				IconManager.getIcon("zip"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						
						if(mainWindow.zipWindow == null){
							mainWindow.zipWindow = new ZiptingWindow(mainWindow);
						}
						mainWindow.tablePopupMenu.setVisible(false);
						ZIPWorker zipWordker = new ZIPWorker(mainWindow, table, (ZiptingWindow)mainWindow.zipWindow);
						zipWordker.execute();
					}
				}));
		//右键菜单：在线搜索作者
		AJMenuItem searchAuthorMenuItem = new AJMenuItem(ComponentConst.POPUP_SEARCHAUTHOR_MENU_TEXT, menuItemColor,
				"",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(mainWindow.searchComicWindow == null){
							mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
						}
						SearchComicWindow scw = mainWindow.searchComicWindow;
						if(task.getAuthor() != null){
							scw.doSearch(task.getAuthor());
							scw.setVisible(true);
						}
					}
				}));
		//右键菜单：本地搜索作者
		AJMenuItem searchLocalAuthorMenuItem = new AJMenuItem(ComponentConst.POPUP_LOCAL_SEARCHAUTHOR_MENU_TEXT, menuItemColor,
				"",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(mainWindow.simpleSearchWindow == null){
							mainWindow.simpleSearchWindow = new SimpleSearchWindow(mainWindow);
						}
						SimpleSearchWindow ssw = (SimpleSearchWindow) mainWindow.simpleSearchWindow;
						if(task.getAuthor() != null){
							ssw.keyTextField.setText(task.getAuthor());
							ssw.searchBtn.doClick();
						}
					}
				}));
		//右键菜单：编辑任务信息
		AJMenuItem editMenuItem = new AJMenuItem(ComponentConst.POPUP_EDIT_MENU_TEXT, menuItemColor,
				IconManager.getIcon("save"),
				new MenuItemActonListener(mainWindow, new ShowEditWork()));
		//右键菜单：重置任务
		AJMenuItem resetMenuItem = new AJMenuItem(ComponentConst.POPUP_RESET_MENU_TEXT, menuItemColor,
				IconManager.getIcon("reset"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(task.getStatus() == TaskStatus.STARTED){
							JOptionPane.showMessageDialog(mainWindow, "正在下载中的任务不能重置！");
							return;
						}
						List<Task> tasks = new ArrayList<Task>();
						tasks.add(task);
						new ResetTaskWork(mainWindow, tasks, "重置后将无法还原，确定要重置【"
						+ ("".equals(task.getSubname()) ? task.getName() : task.getSubname()) +
						"】任务到初始状态吗？");
					}
				}));
		//右键菜单：完成任务
		AJMenuItem completedMenuItem = new AJMenuItem(ComponentConst.POPUP_COMPLETED_MENU_TEXT, menuItemColor,
				IconManager.getIcon("ok"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(task.getStatus() == TaskStatus.STARTED){
							JOptionPane.showMessageDialog(mainWindow, "正在下载中的任务不能执行此操作！");
							return;
						}
						//询问是否执行此操作
						int result = JOptionPane.showConfirmDialog(mainWindow, "此操作后将无法还原，确定要将【"
						+ ("".equals(task.getSubname()) ? task.getName() : task.getSubname()) +
						"】置为完成状态吗？");
						if(result == JOptionPane.OK_OPTION){//确定
							task.setCurrent(task.getTotal());
							task.setStatus(TaskStatus.COMPLETED);
							//保存数据
							mainWindow.taskDbTemplate.update(mainWindow.tasks);
							JOptionPane.showMessageDialog(mainWindow, "操作完成！");
						}
					}
				}));
		//右键菜单：重建任务
		AJMenuItem rebuildMenuItem = new AJMenuItem(ComponentConst.POPUP_REBUILD_MENU_TEXT, menuItemColor,
				"",
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(task.getStatus() == TaskStatus.STARTED){
							JOptionPane.showMessageDialog(mainWindow, "正在下载中的任务不能执行此操作！");
							return;
						}
						//询问是否执行此操作
						int result = JOptionPane.showConfirmDialog(mainWindow, "此操作后将无法还原，确定要重建【"
						+ ("".equals(task.getSubname()) ? task.getName() : task.getSubname()) +
						"】这个任务吗？");
						if(result == JOptionPane.OK_OPTION){//确定
							try {
								ScriptParser.rebuildTask(task, mainWindow.setting);
								//保存数据
								mainWindow.taskDbTemplate.update(task);
								JOptionPane.showMessageDialog(mainWindow, "操作完成！");
							}catch (Exception e1) {
								JOptionPane.showMessageDialog(mainWindow, "操作失败！" + e1.getMessage());
							}
						}
					}
				}));
		AJMenu moreMenu = new AJMenu(ComponentConst.POPUP_MORE_MENU_TEXT, "", editMenuItem, resetMenuItem, completedMenuItem, rebuildMenuItem, copyUrlPopupMenuItem,
				checkResetMenuItem, downloadCoverMenuItem);
		moreMenu.setForeground(menuItemColor);
		this.add(startPopupMenuItem, stopPopupMenuItem/*, detailPopupMenuItem*/, openPicPopupMenuItem, openFolderPopupMenuItem,
				 openWebPageMenuItem, changeReadedMenuItem, zipMenuItem, searchAuthorMenuItem, searchLocalAuthorMenuItem, moreMenu);
	}
}
