package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SimpleSearchWindow;
/**
 * 简单搜索菜单项
 * @author dipoo
 * @since 2014-11-19
 */
public class SimpleSearchMenuItem extends JMenuItem {

	private static final long serialVersionUID = -833758755097678427L;
	
	public SimpleSearchMenuItem(String text, final EgDownloaderWindow mainWindow){
		super(text);
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(mainWindow.simpleSearchWindow == null){
					mainWindow.simpleSearchWindow = new SimpleSearchWindow(mainWindow);
				}
				SimpleSearchWindow ssw = (SimpleSearchWindow) mainWindow.simpleSearchWindow;
				ssw.setVisible(true);
			}
		});
		
	}
}
