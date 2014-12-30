package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.menuitem.OpenRootMenuItem;
import org.arong.egdownloader.ui.menuitem.ResetMenuItem;
import org.arong.egdownloader.ui.menuitem.SimpleSearchMenuItem;
import org.arong.egdownloader.ui.menuitem.StartAllTaskMenuItem;
import org.arong.egdownloader.ui.menuitem.StopAllTaskMenuItem;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.egdownloader.ui.work.listenerWork.ChangeReadedWork;
import org.arong.egdownloader.ui.work.listenerWork.CheckResetWork;
import org.arong.egdownloader.ui.work.listenerWork.ConsoleWork;
import org.arong.egdownloader.ui.work.listenerWork.DeleteTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.DownloadCoverWork;
import org.arong.egdownloader.ui.work.listenerWork.OpenFolderTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.OpenWebPageWork;
import org.arong.egdownloader.ui.work.listenerWork.ResetTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.ShowDetailWork;
import org.arong.egdownloader.ui.work.listenerWork.ShowEditWork;
import org.arong.egdownloader.ui.work.listenerWork.StartTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.StopTaskWork;
import org.arong.egdownloader.version.Version;

/**
 * 主线程类
 * 
 * @author 阿荣
 * @since 2014-05-21
 */
public class EgDownloaderWindow extends JFrame {

	private static final long serialVersionUID = 8904976570969033245L;
	
	JMenuBar jMenuBar;// 菜单栏
	public JFrame settingWindow;
	public JDialog aboutMenuWindow;
	public JDialog addFormWindow;
	public JDialog creatingWindow;
	public JDialog detailWindow;
	public JDialog checkingWindow;
	public JDialog coverWindow;
	public JDialog editWindow;
	public JDialog deletingWindow;
	public JDialog resetAllTaskWindow;
	public JDialog simpleSearchWindow;
	
	public JPopupMenu tablePopupMenu;
	public JTable runningTable;
	public JScrollPane tablePane;
	public JLabel emptyTableTips;
	public JScrollPane consolePane;
	
	public Setting setting;
	public List<Task> tasks;
	
	public DbTemplate<Task> taskDbTemplate;
	public DbTemplate<Picture> pictureDbTemplate;
	public DbTemplate<Setting> settingDbTemplate;

	public EgDownloaderWindow(Setting setting, List<Task> tasks, DbTemplate<Task> taskDbTemplate, DbTemplate<Picture> pictureDbTemplate, DbTemplate<Setting> settingDbTemplate) {
		this.taskDbTemplate = taskDbTemplate;
		this.pictureDbTemplate = pictureDbTemplate;
		this.settingDbTemplate = settingDbTemplate;
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
		this.setBackground(Color.WHITE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Color menuItemColor = new Color(0,0,85);
		// 菜单：新建
		JMenu newTaskMenu = new AJMenu(ComponentConst.ADD_MENU_TEXT,
				"", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("add"),
				new OperaBtnMouseListener(this, MouseAction.CLICK,
						new IListenerTask() {
							public void doWork(Window mainWindow, MouseEvent e) {
								EgDownloaderWindow this_ = (EgDownloaderWindow) mainWindow;
								this_.setEnabled(false);
								if(this_.creatingWindow != null && this_.creatingWindow.isVisible()){
									this_.creatingWindow.setVisible(true);
									this_.creatingWindow.toFront();
								}else{
									if (this_.addFormWindow == null) {
										this_.addFormWindow = new AddFormDialog(this_);
									}
									this_.addFormWindow.setVisible(true);
									this_.addFormWindow.toFront();
								}
								
							}
						}));
		// 菜单：开始
		JMenu startTasksMenu = new AJMenu(ComponentConst.START_MENU_TEXT,
				"", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("download"), new OperaBtnMouseListener(this, MouseAction.CLICK,new StartTaskWork()));
		// 菜单：暂停
		JMenu stopTasksMenu = new AJMenu(ComponentConst.STOP_MENU_TEXT,
				"", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("reset"), new OperaBtnMouseListener(this, MouseAction.CLICK,new StopTaskWork()));
		// 菜单：删除
		OperaBtnMouseListener deleteBtnMouseListener = new OperaBtnMouseListener(this, MouseAction.CLICK,new DeleteTaskWork());
		JMenu deleteTasksMenu = new AJMenu(ComponentConst.DELETE_MENU_TEXT,
				"", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("delete"), deleteBtnMouseListener);
		// 菜单：设置
		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("setting"),
				menuMouseListener);
		// 菜单：操作
		JMenu operaMenu = new AJMenu(ComponentConst.OPERA_MENU_TEXT,
				"", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("opera"), null);
		operaMenu.add(new StartAllTaskMenuItem("开始所有任务", this));
		operaMenu.add(new StopAllTaskMenuItem("暂停所有任务", this));
		operaMenu.add(new ResetMenuItem("重置所有任务", this));
		operaMenu.add(new SimpleSearchMenuItem("简单搜索", this));
		operaMenu.add(new OpenRootMenuItem("打开根目录", this));
		// 菜单：控制台
		JMenu consoleMenu = new AJMenu(ComponentConst.CONSOLE_MENU_TEXT,
				"", ComponentConst.SKIN_NUM
				+ ComponentConst.SKIN_ICON.get("select"),
				new OperaBtnMouseListener(this, MouseAction.CLICK,new ConsoleWork()));
		// 菜单：关于
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("user"),
				menuMouseListener);
		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, ComponentConst.CLIENT_WIDTH, 30,
				newTaskMenu, startTasksMenu, stopTasksMenu, deleteTasksMenu, settingMenu, operaMenu, consoleMenu, aboutMenu);
		
		// 正在下载table
		runningTable = new TaskingTable(5, 40, ComponentConst.CLIENT_WIDTH - 20,
				(tasks == null ? 0 :tasks.size()) * 28, tasks, this);
		tablePane = new JScrollPane(runningTable);
		tablePane.setBounds(new Rectangle(5, 40, ComponentConst.CLIENT_WIDTH - 20, 400));
		tablePane.getViewport().setBackground(new Color(254,254,254));
		//右键菜单：查看详细
		AJMenuItem detailPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_DETAIL_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("detail"),
				new MenuItemActonListener(this, new ShowDetailWork()));
		//右键菜单：复制网址
		AJMenuItem copyUrlPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_COPYURL_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("copy"),
				new MenuItemActonListener(this, new IMenuListenerTask() {
			public void doWork(Window window, ActionEvent e) {
				EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
				TaskingTable table = (TaskingTable) mainWindow.runningTable;
				int index = table.getSelectedRow();
				Task task = table.getTasks().get(index);
				StringSelection ss = new StringSelection(task.getUrl());
				table.getToolkit().getSystemClipboard().setContents(ss, ss);
				mainWindow.tablePopupMenu.setVisible(false);
			}
		}));
		//右键菜单：打开文件夹
		AJMenuItem openFolderPopupMenuItem = new AJMenuItem(ComponentConst.POPUP_OPENFOLDER_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("folder"),
				new MenuItemActonListener(this, new OpenFolderTaskWork()));
		//右键菜单：打开网页
		AJMenuItem openWebPageMenuItem = new AJMenuItem(ComponentConst.POPUP_OPENWEBPAGE_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("browse"),
				new MenuItemActonListener(this, new OpenWebPageWork()));
		//右键菜单：下载封面
		AJMenuItem downloadCoverMenuItem = new AJMenuItem(ComponentConst.POPUP_DOWNLOADCOVER_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("download"),
				new MenuItemActonListener(this, new DownloadCoverWork()));
		//右键菜单：查漏补缺
		AJMenuItem checkResetMenuItem = new AJMenuItem(ComponentConst.POPUP_CHECKRESET_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("check"),
				new MenuItemActonListener(this, new CheckResetWork()));
		//右键菜单：更改阅读状态
		AJMenuItem changeReadedMenuItem = new AJMenuItem(ComponentConst.POPUP_CHANGEREADED_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("change"),
				new MenuItemActonListener(this, new ChangeReadedWork()));
		//右键菜单：编辑任务信息
		AJMenuItem editMenuItem = new AJMenuItem(ComponentConst.POPUP_EDIT_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("save"),
				new MenuItemActonListener(this, new ShowEditWork()));
		//右键菜单：重置任务
		AJMenuItem resetMenuItem = new AJMenuItem(ComponentConst.POPUP_RESET_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("reset"),
				new MenuItemActonListener(this, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(task.getStatus() == TaskStatus.STARTED){
							JOptionPane.showMessageDialog(null, "正在下载中的任务不能重置！");
							return;
						}
						List<Task> tasks = new ArrayList<Task>();
						tasks.add(task);
						new ResetTaskWork(mainWindow, tasks, "重置后将无法还原，确定要重置【"
						+ ("".equals(task.getSubname()) ? task.getName() : task.getSubname()) +
						"】任务到初始状态吗？");
					}
				}));
		//右键菜单：完成任务
		AJMenuItem completedMenuItem = new AJMenuItem(ComponentConst.POPUP_COMPLETED_MENU_TEXT, menuItemColor,
				ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("ok"),
				new MenuItemActonListener(this, new IMenuListenerTask() {
					public void doWork(Window window, ActionEvent e) {
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						TaskingTable table = (TaskingTable) mainWindow.runningTable;
						int index = table.getSelectedRow();
						Task task = table.getTasks().get(index);
						if(task.getStatus() == TaskStatus.STARTED){
							JOptionPane.showMessageDialog(null, "正在下载中的任务不能执行此操作！");
							return;
						}
						//询问是否执行此操作
						int result = JOptionPane.showConfirmDialog(null, "此操作后将无法还原，确定要将【"
						+ ("".equals(task.getSubname()) ? task.getName() : task.getSubname()) +
						"】置为完成状态吗？");
						if(result == 0){//确定
							task.setCurrent(task.getTotal());
							task.setStatus(TaskStatus.COMPLETED);
							//保存数据
							mainWindow.taskDbTemplate.update(mainWindow.tasks);
							JOptionPane.showMessageDialog(null, "操作完成！");
						}
					}
				}));
		//表格的右键菜单
		tablePopupMenu = new AJPopupMenu(detailPopupMenuItem, openFolderPopupMenuItem,
				copyUrlPopupMenuItem, openWebPageMenuItem, downloadCoverMenuItem,
				checkResetMenuItem, changeReadedMenuItem, editMenuItem, resetMenuItem, completedMenuItem);
		emptyTableTips = new AJLabel("empty",  ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("empty"), new Color(227,93,81), JLabel.CENTER);
		emptyTableTips.setBounds(0, 160, ComponentConst.CLIENT_WIDTH, 100);
		emptyTableTips.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		// 添加各个子组件
		ComponentUtil.addComponents(getContentPane(), jMenuBar, tablePane, tablePopupMenu, emptyTableTips);
		if(tasks == null || tasks.size() == 0){
			tablePane.setVisible(false);
		}else{
			emptyTableTips.setVisible(false);
		}
		this.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				EgDownloaderWindow window = (EgDownloaderWindow) e.getSource();
				if (window.aboutMenuWindow != null) {
					window.aboutMenuWindow.dispose();
				}
				if(window.creatingWindow != null && window.creatingWindow.isVisible()){
					window.creatingWindow.requestFocus();
				}else if(window.addFormWindow != null && window.addFormWindow.isVisible()){
					window.addFormWindow.requestFocus();
				}else if(window.detailWindow != null && window.detailWindow.isVisible()){
					window.detailWindow.requestFocus();
				}else if(window.editWindow != null && window.editWindow.isVisible()){
					window.editWindow.requestFocus();
				}else if(window.deletingWindow != null && window.deletingWindow.isVisible()){
					window.deletingWindow.requestFocus();
				}else if(window.simpleSearchWindow != null && window.simpleSearchWindow.isVisible()){
					window.simpleSearchWindow.requestFocus();
				}
			}
		});
		//关闭监听
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				EgDownloaderWindow window = (EgDownloaderWindow) e.getSource();
				//保存数据
				window.taskDbTemplate.update(window.tasks);
				System.exit(0);
			}
		});
		
	}
}
