package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.window.EgDownloaderWindow;
/**
 * 暂停所有任务
 * @author dipoo
 * @since 2014-12-07
 */
public class StopAllTaskMenuItem extends JMenuItem {

	private static final long serialVersionUID = 8033742031776192264L;
	public StopAllTaskMenuItem(String text, EgDownloaderWindow window){
		super(text);
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});
	}
}
