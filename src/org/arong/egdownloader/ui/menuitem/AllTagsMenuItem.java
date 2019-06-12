package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.window.AllTagsWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
/**
 * 所有标签菜单项
 * @author dipoo
 * @since 2019-06-11
 */
public class AllTagsMenuItem extends JMenuItem {
	
	public boolean istask;//是否为已建任务中所包含的标签

	public AllTagsMenuItem(String text, final EgDownloaderWindow mainWindow, boolean istask_){
		super(text);
		this.istask = istask_;
		this.setIcon(IconManager.getIcon("openpic"));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(mainWindow.allTagsWindow == null){
					mainWindow.allTagsWindow = new AllTagsWindow(mainWindow);
				}
				mainWindow.allTagsWindow.istask = istask;
				mainWindow.allTagsWindow.searchBtn.doClick();
			}
		});
	}
}
