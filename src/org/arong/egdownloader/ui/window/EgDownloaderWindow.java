package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.version.Version;

/**
 * 主线程类
 * @author 阿荣
 * @since 2014-05-21
 */
public class EgDownloaderWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8904976570969033245L;
	
	JMenuBar jMenuBar;//菜单栏
	public JFrame toolsMenuWindow;
	public JDialog aboutMenuWindow;
	
	
	JButton newTaskBtn;//新建任务
	JButton deleteTasksBtn;//删除任务
	
	JTextArea statusTextarea;//状态显示
	
	public EgDownloaderWindow(){
		// 设置主窗口
		this.setTitle(Version.NAME);
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + "down.png")).getImage());
		this.getContentPane().setLayout(null);
		this.setSize(ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 设置菜单
		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, "cog.png", menuMouseListener);
		JMenu toolsMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, "tool.png", menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, "user.png", menuMouseListener);

		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, ComponentConst.CLIENT_WIDTH, 30, settingMenu, toolsMenu,
				aboutMenu);
		
		//构造操作按钮
		newTaskBtn = new AJButton("新建", "newTaskBtn", "add.gif", null, 5, 40, 55, 25);//新建
		deleteTasksBtn = new AJButton("删除", "deleteTasksBtn", "delete.gif", null, newTaskBtn.getX() + newTaskBtn.getWidth() + 10, 40, 55, 25);
		
		
		
		//状态栏
		statusTextarea = new AJTextArea("当前无任务", 0, ComponentConst.CLIENT_HEIGHT - 58, ComponentConst.CLIENT_WIDTH, 25);
		statusTextarea.setForeground(Color.BLACK);
		statusTextarea.setBackground(new Color(230,230,230));
		// 添加各个子组件
		ComponentUtil.addComponents(getContentPane(), jMenuBar, newTaskBtn, deleteTasksBtn, statusTextarea);
	}

	public void actionPerformed(ActionEvent e) {

	}
}
