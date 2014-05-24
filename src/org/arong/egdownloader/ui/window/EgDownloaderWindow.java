package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.table.TaskTableModel;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
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
	public JDialog addFormWindow;
	
	public JTable runningTable;
	JScrollPane tablePane;
	
	public EgDownloaderWindow(){
		// 设置主窗口
		this.setTitle(Version.NAME);
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("download"))).getImage());
		this.getContentPane().setLayout(null);
		this.setSize(ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 菜单
		JMenu newTaskMenu = new AJMenu(ComponentConst.ADD_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("add"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
					public void doWork(Window mainWindow) {
						EgDownloaderWindow this_ = (EgDownloaderWindow)mainWindow;
						((TaskTableModel)this_.runningTable.getModel()).getTasks().get(0).setCurrent(111);
						if(this_.addFormWindow == null){
							this_.addFormWindow = new AddFormDialog(this_);
						}else{
							if(!this_.addFormWindow.isVisible()){//如果是隐藏的，则清空文本
								((AddFormDialog)this_.addFormWindow).emptyField();
							}
							this_.addFormWindow.setVisible(true);
						}
						this_.setEnabled(false);
					}
				}));
		JMenu deleteTasksMenu = new AJMenu(ComponentConst.DELETE_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("delete"),  new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
					public void doWork(Window mainWindow) {
						EgDownloaderWindow this_ = (EgDownloaderWindow)mainWindow;
						TaskingTable table = (TaskingTable) this_.runningTable;
						table.getTasks().remove(table.getSelectedRow());
						table.updateUI();//刷新表格
					}
				}));
		
		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("setting"), menuMouseListener);
		JMenu toolsMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("tool"), menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("user"), menuMouseListener);
		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, ComponentConst.CLIENT_WIDTH, 30, newTaskMenu, deleteTasksMenu, settingMenu, toolsMenu,
				aboutMenu);
		
		
		
		List<Task> tasks = new ArrayList<Task>();
		Task t1 = new Task("http://www.hao123.com/1", "D:/eg");
		t1.setName("齐天大圣");
		t1.setTotal(4500);
		t1.setCurrent(5);
		t1.setSize(1345200);
		
		Task t2 = new Task("http://www.hao123.com/2", "D:/eg");
		t2.setName("盛世华景月天");
		t2.setTotal(123);
		t2.setCurrent(23);
		t2.setSize(5452299);
		
		tasks.add(t2);
		tasks.add(t1);tasks.add(t2);tasks.add(t2);tasks.add(t1);tasks.add(t2);tasks.add(t1);tasks.add(t1);
		tasks.add(t1);tasks.add(t2);tasks.add(t2);tasks.add(t1);tasks.add(t2);tasks.add(t1);//tasks.add(t1);
		//tasks.add(t1);tasks.add(t2);tasks.add(t2);tasks.add(t1);tasks.add(t2);tasks.add(t1);tasks.add(t1);
		//正在下载table
		runningTable = new TaskingTable(5, 40, getWidth() - 20, tasks.size() * 28, tasks);
		tablePane = new JScrollPane(runningTable);
		tablePane.setBounds(new Rectangle(5, 40, 620, 400));
		tablePane.getViewport().setBackground(Color.WHITE);
		// 添加各个子组件
		ComponentUtil.addComponents(getContentPane(), jMenuBar, tablePane);
		
		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {}
			public void windowGainedFocus(WindowEvent e) {
				EgDownloaderWindow window = (EgDownloaderWindow)e.getSource();
				if(window.addFormWindow != null){
					window.addFormWindow.setVisible(false);
				}
				if(window.toolsMenuWindow != null){
					window.toolsMenuWindow.setVisible(false);
				}
				if(window.aboutMenuWindow != null){
					window.aboutMenuWindow.setVisible(false);
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {

	}
}
