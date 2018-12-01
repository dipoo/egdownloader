package org.arong.egdownloader.ui.panel;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.arong.egdownloader.ui.menuitem.ClearConsoleMenuItem;
import org.arong.egdownloader.ui.menuitem.OpenLogMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;

public class ConsolePanel extends JScrollPane {
	
	private EgDownloaderWindow mainWindow;
	private JTextPane textPane;
	public JPopupMenu consolePopupMenu;
	
	public ConsolePanel(final EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textPane = new JTextPane();
		textPane.setBorder(null);
		textPane.setEditable(true);
		textPane.setAutoscrolls(true);
		this.setBorder(null);
		this.setViewportView(textPane);
		
		final JMenuItem clearItemPopup = new ClearConsoleMenuItem("清空控制台", mainWindow);
		final JMenuItem openLogItemPopup = new OpenLogMenuItem("打开日志文件", mainWindow);
		textPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//右键
				if(e.getButton() == MouseEvent.BUTTON3){
					if(consolePopupMenu == null){
						consolePopupMenu = new AJPopupMenu(clearItemPopup, openLogItemPopup);
					}
					consolePopupMenu.show((Component) e.getSource(), e.getPoint().x, e.getPoint().y);
				}
			}
		});
		
		textPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				
			}
		});
	}
	public JTextPane getTextPane() {
		return textPane;
	}
	public void setText(String string) {
		textPane.setText(string);
		//this.updateUI();
	}
}
