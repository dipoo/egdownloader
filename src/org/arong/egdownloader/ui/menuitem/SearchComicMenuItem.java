package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
/**
 * 搜索绅士站漫画菜单项
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchComicMenuItem extends JMenuItem {

	private static final long serialVersionUID = -833758755097678427L;
	
	public SearchComicMenuItem(String text, final EgDownloaderWindow mainWindow){
		super(text);
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(mainWindow.searchComicWindow == null){
					mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
				}
				SearchComicWindow scw = (SearchComicWindow) mainWindow.searchComicWindow;
				scw.setVisible(true);
			}
		});
		
	}
}
