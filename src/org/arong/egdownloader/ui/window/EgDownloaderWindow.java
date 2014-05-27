package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.egdownloader.ui.work.listenerWork.DeleteTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.ListTaskWork;
import org.arong.egdownloader.version.Version;

/**
 * 主线程类
 * 
 * @author 阿荣
 * @since 2014-05-21
 */
public class EgDownloaderWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8904976570969033245L;
	
	JMenuBar jMenuBar;// 菜单栏
	public JFrame toolsMenuWindow;
	public JDialog aboutMenuWindow;
	public JDialog addFormWindow;

	public JPopupMenu tablePopupMenu;
	public JTable runningTable;
	JScrollPane tablePane;
	
	public Setting setting;
	public List<Task> tasks;

	public EgDownloaderWindow(Setting setting, List<Task> tasks) {
		
		//加载配置数据
		this.setting = setting;
		//加载任务列表
		this.tasks = tasks;
		
		// 设置主窗口
		this.setTitle(Version.NAME);
		this.setIconImage(new ImageIcon(getClass().getResource(
				ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("download"))).getImage());
		this.getContentPane().setLayout(null);
		this.setSize(ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 菜单
		JMenu newTaskMenu = new AJMenu(ComponentConst.ADD_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("add"),
				new OperaBtnMouseListener(this, MouseAction.CLICK,
						new IListenerTask() {
							public void doWork(Window mainWindow, MouseEvent e) {
								EgDownloaderWindow this_ = (EgDownloaderWindow) mainWindow;
								if (this_.addFormWindow == null) {
									this_.addFormWindow = new AddFormDialog(
											this_);
								} else {
									this_.addFormWindow.setVisible(true);
								}
								this_.setEnabled(false);
							}
						}));
		OperaBtnMouseListener deleteBtnMouseListener = new OperaBtnMouseListener(this, MouseAction.CLICK,new DeleteTaskWork());
		JMenu deleteTasksMenu = new AJMenu(ComponentConst.DELETE_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("delete"), deleteBtnMouseListener);

		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("setting"),
				menuMouseListener);
		JMenu toolsMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("tool"),
				menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("user"),
				menuMouseListener);
		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, ComponentConst.CLIENT_WIDTH, 30,
				newTaskMenu, deleteTasksMenu, settingMenu, toolsMenu, aboutMenu);

		// 正在下载table
		runningTable = new TaskingTable(5, 40, getWidth() - 20,
				(tasks == null ? 0 :tasks.size()) * 28, tasks, this);
		tablePane = new JScrollPane(runningTable);
		tablePane.setBounds(new Rectangle(5, 40, 620, 400));
		tablePane.getViewport().setBackground(Color.WHITE);
		
		AJMenu deletePopupMenuItem = new AJMenu(ComponentConst.POPUP_DETAIL_MENU_TEXT, "", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("tool"), new OperaBtnMouseListener(this, MouseAction.CLICK,new ListTaskWork()));
		//表格的右键菜单
		tablePopupMenu = new AJPopupMenu(deletePopupMenuItem);
		
		// 添加各个子组件
		ComponentUtil.addComponents(getContentPane(), jMenuBar, tablePane, tablePopupMenu);

		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {
			}

			public void windowGainedFocus(WindowEvent e) {
				EgDownloaderWindow window = (EgDownloaderWindow) e.getSource();
				if (window.addFormWindow != null) {
					window.addFormWindow.dispose();
				}
				if (window.toolsMenuWindow != null) {
					window.toolsMenuWindow.dispose();
				}
				if (window.aboutMenuWindow != null) {
					window.aboutMenuWindow.dispose();
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {

	}
}
