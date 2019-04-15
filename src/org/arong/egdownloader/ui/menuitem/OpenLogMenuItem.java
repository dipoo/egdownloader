package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
import org.arong.util.Tracker;
/**
 * 打开日志文件
 * @author dipoo
 * @since 2018-08-28
 */
public class OpenLogMenuItem extends JMenuItem {

	private static final long serialVersionUID = -2951780178305327150L;
	
	public OpenLogMenuItem(String text, final EgDownloaderWindow mainWindow){
		super(text);
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					String binPath = FileUtil2.getAppPath(getClass());
					if(binPath.endsWith("bin")){
						binPath = binPath.substring(0, binPath.length() - 3);
					}else{
						String defaultSavePath = mainWindow.setting.getDefaultSaveDir();
						FileUtil2.ifNotExistsThenCreate(defaultSavePath);
						File f = new File(defaultSavePath);
						binPath = f.getAbsolutePath().replaceAll(defaultSavePath, "");
					}
					String logfile = binPath + File.separator + "console.log";
					System.out.println("打开日志文件" + logfile);
					Desktop.getDesktop().open(new File(logfile));//去掉bin
				} catch (IOException e) {
					e.printStackTrace();
					Tracker.println("打开日志文件出错");
				}
			}
		});
	}
}
