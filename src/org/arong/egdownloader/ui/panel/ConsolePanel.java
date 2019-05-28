package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.arong.egdownloader.ui.menuitem.ClearConsoleMenuItem;
import org.arong.egdownloader.ui.menuitem.OpenLogMenuItem;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.HtmlUtils;

public class ConsolePanel extends JScrollPane {
	
	private AJTextPane textPane;
	public JPopupMenu consolePopupMenu;
	public boolean locked;//是否锁屏
	
	public ConsolePanel(final EgDownloaderWindow mainWindow) {
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.setBorder(null);
		this.setAutoscrolls(false);
		textPane = new AJTextPane(true);
		textPane.setBorder(null);
		textPane.setEditable(false);
		textPane.setAutoscrolls(true);
		
		this.setViewportView(textPane);
		
		final JMenuItem clearItemPopup = new ClearConsoleMenuItem("清空控制台", mainWindow);
		final JMenuItem lockItemPopup = new AJMenuItem("", new Color(0, 0, 85), new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.consolePanel.locked = !mainWindow.consolePanel.locked;
			}
		});
		final JMenuItem openLogItemPopup = new OpenLogMenuItem("打开日志文件", mainWindow);
		textPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//右键
				if(e.getButton() == MouseEvent.BUTTON3){
					if(consolePopupMenu == null){
						consolePopupMenu = new AJPopupMenu(clearItemPopup, lockItemPopup, openLogItemPopup);
					}
					if(mainWindow.consolePanel.locked){
						lockItemPopup.setText("开启自动滚屏");
					}else{
						lockItemPopup.setText("锁定自动滚屏");
					}
					consolePopupMenu.show((Component) e.getSource(), e.getPoint().x, e.getPoint().y);
				}
			}
		});
	}
	public AJTextPane getTextPane() {
		return textPane;
	}

	public void showLog(String msg) {
		try{
			textPane.appendBHtml("<b style=\"font-size:9px;font-family:微软雅黑\">" + HtmlUtils.filterEmoji2SegoeUISymbolFont(msg) + "</b>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
