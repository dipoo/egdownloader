package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.list.GroupList;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.window.form.AddGroupDialog;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.egdownloader.version.Version;
/**
 * 任务组窗口
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupWindow extends JFrame {

	private static final long serialVersionUID = 3500270648971377551L;
	
	public EgDownloaderWindow mainWindow;
	
	public GroupWindow(List<File> groups, final EgDownloaderWindow mainWindow){
		super(Version.NAME + "任务组列表");
		this.mainWindow = mainWindow;
		this.setSize(300, 400);
		this.setResizable(false);
		this.setIconImage(new ImageIcon(getClass().getResource(
				ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("group"))).getImage());
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(mainWindow == null ? EXIT_ON_CLOSE : DISPOSE_ON_CLOSE);
		JLabel tipLabel = new AJLabel("双击选择任务组", new Color(67,44,1), 15, 15, 100, 30);
		JButton addGroupBtn = new AJButton("新建", "", ComponentConst.SKIN_NUM
						+ ComponentConst.SKIN_ICON.get("add"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
							public void doWork(Window window, MouseEvent e) {
								new AddGroupDialog((GroupWindow) window, mainWindow);
							}
						}) , 215, 15, 62, 30);
		addGroupBtn.setUI(AJButton.blueBtnUi);
		JList list = new GroupList(groups, this, mainWindow);
		list.setSelectedIndex(0);
		JScrollPane listPane = new JScrollPane(list);
		listPane.setBounds(new Rectangle(10, 50, 270, 300));
		listPane.setAutoscrolls(true);
		listPane.getViewport().setBackground(new Color(254,254,254));
		ComponentUtil.addComponents(this.getContentPane(), tipLabel, addGroupBtn, listPane);
		
		this.setVisible(true);
	}
}
