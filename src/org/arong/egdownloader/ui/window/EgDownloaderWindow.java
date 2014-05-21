package org.arong.egdownloader.ui.window;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.version.Version;

/**
 * 
 * @author 阿荣
 *
 */
public class EgDownloaderWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8904976570969033245L;
	
	JMenuBar jMenuBar;//菜单栏
	JButton newTaskBtn;//新建任务
	JButton deleteTasksBtn;//删除任务
	
	public EgDownloaderWindow(){
		// 设置主窗口
		this.setTitle(Version.NAME);
		this.setIconImage(new ImageIcon(EgDownloaderWindow.class.getResource("/resources/icon/eye.png")).getImage());
		this.getContentPane().setLayout(null);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 设置菜单
		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, menuMouseListener);
		JMenu toolsMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, menuMouseListener);

		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, 800, 30, settingMenu, toolsMenu,
				aboutMenu);
		
		//构造操作按钮组
		newTaskBtn = new AJButton("新建", "newTaskBtn", "add.gif", null, 5, 40, 55, 25);//新建
		deleteTasksBtn = new AJButton("删除", "deleteTasksBtn", "delete.gif", null, newTaskBtn.getX() + newTaskBtn.getWidth() + 10, 40, 55, 25);
		
		// 添加各个子组件
		addComponents(jMenuBar, newTaskBtn, deleteTasksBtn);
	}

	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * 添加组件到主容器
	 * 
	 * @param components
	 */
	private void addComponents(Component... components) {
		if (components != null) {
			for (Component comp : components) {
				this.getContentPane().add(comp);
			}
		}
	}
}
