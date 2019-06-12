package org.arong.egdownloader.ui.listener;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.AboutMenuWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SettingWindow;

/**
 * 用于监听菜单的鼠标事件，主要是菜单单击事件<br>
 * 请带上主窗口的实例，这是为了使主菜单中的弹出窗口位于主窗口的中央
 * 
 * @author 阿荣
 * @since 2013-8-25
 * 
 */
public class MenuMouseListener implements MouseListener {
	private EgDownloaderWindow mainWindow;

	public MenuMouseListener() {

	}

	public MenuMouseListener(EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof Component) {
			Component menu = (Component) e.getSource();
			String menuName = menu.getName();
			// 如果点击的是工具菜单，则将关于窗口显示出来，倘若关于窗口为null就先实例化
			if (ComponentConst.SETTING_MENU_NAME.equals(menuName)) {
				if (mainWindow.settingWindow == null) {
					SettingWindow settingWindow = new SettingWindow(mainWindow);
					mainWindow.settingWindow = settingWindow;
				}
				mainWindow.settingWindow.setLocationRelativeTo(mainWindow);
				// 设置关于窗口置于最顶层
				mainWindow.settingWindow.setVisible(true);
				mainWindow.settingWindow.toFront();
			}
			// 如果点击的是关于菜单，则将关于窗口显示出来，倘若关于窗口为null就先实例化
			else if (ComponentConst.ABOUT_MENU_NAME.equals(menuName)) {
				if (mainWindow.aboutWindow == null) {
					AboutMenuWindow aboutWindow = new AboutMenuWindow(mainWindow);
					mainWindow.aboutWindow = aboutWindow;
				}
				mainWindow.aboutWindow.setLocationRelativeTo(mainWindow);
				// 设置关于窗口置于最顶层
				mainWindow.aboutWindow.setVisible(true);
				mainWindow.aboutWindow.toFront();
			}
		}

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

}
