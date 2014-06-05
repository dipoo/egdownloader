package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.egdownloader.ui.work.listenerWork.DeleteTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.ListTaskWork;
import org.arong.egdownloader.ui.work.listenerWork.OpenFolderTaskWork;
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
	public JFrame toolsMenuWindow;
	public JDialog aboutMenuWindow;
	public JDialog addFormWindow;
	public JDialog creatingWindow;
	public JDialog detailWindow;
	
	public JPopupMenu tablePopupMenu;
	public JTable runningTable;
	public JScrollPane tablePane;
	public JLabel emptyTableTips;
	
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
								this_.setEnabled(false);
								if(this_.creatingWindow != null && this_.creatingWindow.isVisible()){
									this_.creatingWindow.setVisible(true);
									this_.creatingWindow.toFront();
								}else{
									if (this_.addFormWindow == null) {
										this_.addFormWindow = new AddFormDialog(
												this_);
									}
									this_.addFormWindow.setVisible(true);
									this_.addFormWindow.toFront();
								}
								
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
		JMenu searchMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("tool"),
				menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("user"),
				menuMouseListener);
		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, ComponentConst.CLIENT_WIDTH, 30,
				newTaskMenu, deleteTasksMenu, settingMenu, searchMenu, aboutMenu);
		
		// 正在下载table
		runningTable = new TaskingTable(5, 40, ComponentConst.CLIENT_WIDTH - 20,
				(tasks == null ? 0 :tasks.size()) * 28, tasks, this);
		tablePane = new JScrollPane(runningTable);
		tablePane.setBounds(new Rectangle(5, 40, ComponentConst.CLIENT_WIDTH - 20, 400));
		tablePane.getViewport().setBackground(new Color(254,254,254));
		AJMenu detailPopupMenuItem = new AJMenu(ComponentConst.POPUP_DETAIL_MENU_TEXT, "", null, new OperaBtnMouseListener(this, MouseAction.CLICK,new ListTaskWork()));
		AJMenu openFolderPopupMenuItem = new AJMenu(ComponentConst.POPUP_OPENFOLDER_MENU_TEXT, "", null, new OperaBtnMouseListener(this, MouseAction.CLICK,new OpenFolderTaskWork()));
		//表格的右键菜单
		tablePopupMenu = new AJPopupMenu(detailPopupMenuItem, openFolderPopupMenuItem);
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
