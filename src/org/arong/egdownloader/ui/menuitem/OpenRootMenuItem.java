package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
import org.arong.util.Tracker;
/**
 * 打开根目录
 * @author dipoo
 * @since 2014-12-07
 */
public class OpenRootMenuItem extends JMenuItem {
	private static final long serialVersionUID = -3015271666943997829L;

	public OpenRootMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		this.setIcon(IconManager.getIcon("folder"));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					String binPath = FileUtil2.getAppPath(getClass());
					if(binPath.endsWith("bin")){
						binPath = binPath.substring(0, binPath.length() - 3);
					}else{
						String defaultSavePath = window.setting.getDefaultSaveDir();
						FileUtil2.ifNotExistsThenCreate(defaultSavePath);
						File f = new File(defaultSavePath);
						binPath = f.getAbsolutePath().replaceAll(defaultSavePath, "");
					}
					Desktop.getDesktop().open(new File(binPath));//去掉bin
				} catch (IOException e) {
					e.printStackTrace();
					Tracker.println("打开根目录出错");
				}
			}
		});
	}
}
