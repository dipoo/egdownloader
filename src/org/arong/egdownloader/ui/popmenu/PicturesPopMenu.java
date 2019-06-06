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

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.table.PictureTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.DownloadSinglePicWorker;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;

public class PicturesPopMenu extends JPopupMenu {
	public EgDownloaderWindow mainWindow;
	public JMenuItem openPictureItem;
	public JMenuItem downItem;
	public JMenuItem oldUrlItem;
	public PictureTable table;
	public PicturesPopMenu(EgDownloaderWindow mainWindow, PictureTable pictable){
		this.mainWindow = mainWindow;
		this.table = pictable;
		openPictureItem = new AJMenuItem("打开图片", Color.BLACK,
				IconManager.getIcon("openpic"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						Picture pic = table.getTask().getPictures().get(table.getSelectedRow());
						if(pic.isCompleted()){
							try {
								String name = pic.getName();
								if(name.indexOf(".") != -1){
									name = (pic.isSaveAsName() ? name.substring(0, name.lastIndexOf(".") - 1) : pic.getNum()) + name.substring(name.lastIndexOf("."), name.length());
								}else{
									name = (pic.isSaveAsName() ? name.substring(0, name.lastIndexOf(".") - 1) : pic.getNum()) + ".jpg";
								}
								Desktop.getDesktop().open(new File(table.getTask().getSaveDir() + File.separator + name));
							} catch (Exception e1) {
								try {
									Desktop.getDesktop().open(new File(table.getTask().getSaveDir() + File.separator + pic.getName()));
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "打开失败");
								}
							}
						}else{
							JOptionPane.showMessageDialog(null, "该图片还没有下载完成");
						}
					}
				}));
		downItem = new AJMenuItem("下载图片", Color.BLACK,
				IconManager.getIcon("download"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						if(table.getTask().getStatus() == TaskStatus.STARTED || table.getTask().getStatus() == TaskStatus.WAITING){
							JOptionPane.showMessageDialog(null, "任务正在运行，请先暂停");
							return;
						}
						Picture pic = table.getTask().getPictures().get(table.getSelectedRow());
						DownloadSinglePicWorker worker = new DownloadSinglePicWorker(table.getTask(), pic, mainWindow);
						table.getTask().setStatus(TaskStatus.STARTED);
						worker.execute();
					}
				}));
		JMenuItem resetStatusItem = new AJMenuItem("重置状态", Color.BLACK, IconManager.getIcon("change"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
						Picture pic = table.getTask().getPictures().get(table.getSelectedRow());
						pic.setCompleted(! pic.isCompleted());
						mainWindow.pictureDbTemplate.update(pic);
						table.updateUI();
					}
				}));
		JMenuItem openPageItem = new AJMenuItem("打开网页", Color.BLACK,
				IconManager.getIcon("browse"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						Picture pic = table.getTask().getPictures().get(table.getSelectedRow());
						openPage(pic.getUrl());
					}
				}));
		oldUrlItem = new AJMenuItem("旧版网页", Color.BLACK,
				IconManager.getIcon("browse"),
				new MenuItemActonListener(mainWindow, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						Picture pic = table.getTask().getPictures().get(table.getSelectedRow());
						openPage(pic.getOldurl());
					}
				}));
		
		this.add(openPictureItem);
		this.add(downItem);
		this.add(resetStatusItem);
		this.add(openPageItem);
		this.add(oldUrlItem);
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
