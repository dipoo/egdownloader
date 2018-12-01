package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Point;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.util.FileUtil;
/**
 * 搜索结果封面窗口
 * @author dipoo
 * @since 2015-03-13
 */
public class SearchCoverWindow extends JWindow {

	private static final long serialVersionUID = 6598148765450431311L;
	
	private SearchComicWindow comicWindow;
	
	private EgDownloaderWindow mainWindow;
	
	private JLabel coverLabel = null;
	
	private ImageIcon icon;
	
	public SearchCoverWindow(EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		coverLabel = new JLabel();
		coverLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.setLayout(null);
		this.getContentPane().add(coverLabel);
	}
	
	public SearchCoverWindow(SearchComicWindow comicWindow){
		this.comicWindow = comicWindow;
		coverLabel = new JLabel();
		coverLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.setLayout(null);
		this.getContentPane().add(coverLabel);
	}
	
	/**
	 * 主窗口使用
	 */
	public void showCover(Task task, Point p){
		String path = task.getSaveDir() + "/cover.jpg";
		File cover = new File(path);
		if(cover == null || !cover.exists()){
			this.setVisible(false);
		}else{
			icon = new ImageIcon(path);
			if(icon.getIconWidth() == -1){
				this.setVisible(false);
			}else{
				if(mainWindow.coverWindow2 != null && mainWindow.coverWindow2.isVisible()){
					mainWindow.coverWindow2.dispose();
				}
				this.setSize(icon.getIconWidth() + 4, icon.getIconHeight() + 4);
				coverLabel.setSize(icon.getIconWidth() + 4, icon.getIconHeight() + 4);
				coverLabel.setIcon(icon);
				this.setLocationRelativeTo(mainWindow);
				this.setLocation((int)p.getX(), (int)p.getY() - this.getHeight() / 2 + 50);
				this.setVisible(true);
			}
		}
	}
	
	/**
	 * 搜索窗口使用
	 */
	public void showCover(SearchTask task, Point p){
		//检测封面是否已下载http://exhentai.org/g/794884/2278359e3a
		String path = ComponentConst.CACHE_PATH + "/" + FileUtil.filterDir(task.getUrl());
		File cover = new File(path);
		if(cover == null || !cover.exists()){
			this.setSize(20, 20);
			coverLabel.setSize(20, 20);
			coverLabel.setIcon(IconManager.getIcon("loading"));
		}else{
			icon = new ImageIcon(path);
			if(icon.getIconWidth() == -1){
				this.setSize(20, 20);
				coverLabel.setSize(20, 20);
				coverLabel.setIcon(IconManager.getIcon("loading"));
			}else{
				this.setSize(icon.getIconWidth() + 4, icon.getIconHeight() + 4);
				coverLabel.setSize(icon.getIconWidth() + 4, icon.getIconHeight() + 4);
				icon.getImage().flush();//解决加载图片不完全问题
				coverLabel.setIcon(icon);
			}
		}
		this.setLocationRelativeTo(comicWindow);
		this.setLocation((int)p.getX(), (int)p.getY() - this.getHeight() / 2 - 50);
		this.setVisible(true);
	}
}
