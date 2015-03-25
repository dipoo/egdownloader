package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJTextPane;

/**
 * 【关于】菜单的窗口类
 * 
 * @author 阿荣
 * @since 2014-05-21
 * 
 */
public class AboutMenuWindow extends JDialog {

	private static final long serialVersionUID = -6501253363937575294L;

	private AJTextPane aboutTextPane;
	
	/**
	 * 加入参数mainWindow主要是使关于窗口始终在主窗口的中央弹出
	 * @param mainWindow
	 */
	public AboutMenuWindow(final JFrame mainWindow) {
		// 设置主窗口
		this.setSize(340, 250);
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("user"))).getImage());
		this.setTitle("about");
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(mainWindow);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.dispose();
			}
		});
		//添加鼠标活动监听器
		this.addMouseListener(new MouseAdapter() {
			// 当鼠标点击当前窗口时隐藏此窗口
			public void mouseClicked(MouseEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.dispose();
			}
		});

		aboutTextPane = new AJTextPane(ComponentConst.ABOUT_TEXTPANE_TEXT,
				Color.BLUE);
		this.getContentPane().add(aboutTextPane);
	}
}
