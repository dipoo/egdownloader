package org.arong.book.ui.window;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.arong.book.ui.ComponentConst;
import org.arong.book.ui.swing.AJTextPane;

/**
 * 【关于】菜单的窗口类
 * 
 * @author 阿荣
 * @since 2013-8-25
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
		//System.out.println("AboutMenuWindow实例化");
		// 设置主窗口
		this.setSize(320, 240);
		// this.setBackground(Color.GREEN);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(mainWindow);
		//添加窗口聚焦监听器
		this.addWindowFocusListener(new WindowFocusListener() {
			// 当失去活动状态的时候此窗口被隐藏
			public void windowLostFocus(WindowEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.setVisible(false);
			}
			public void windowGainedFocus(WindowEvent e) {}
		});
		//添加鼠标活动监听器
		this.addMouseListener(new MouseListener() {
			// 当鼠标点击当前窗口时隐藏此窗口
			public void mouseClicked(MouseEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.setVisible(false);
			}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
		});

		aboutTextPane = new AJTextPane(ComponentConst.ABOUT_TEXTPANE_TEXT,
				Color.BLUE);
		this.getContentPane().add(aboutTextPane);
	}
}
