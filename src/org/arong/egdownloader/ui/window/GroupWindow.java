package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.list.GroupList;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.version.Version;
/**
 * 任务组窗口
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupWindow extends JFrame {

	private static final long serialVersionUID = 3500270648971377551L;
	
	public GroupWindow(List<File> groups){
		super(Version.NAME + "任务组列表");
		this.setSize(300, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel tipLabel = new AJLabel("请选择任务组", Color.BLUE, 100, 15, 100, 30);
		JList list = new GroupList(groups);
		JScrollPane listPane = new JScrollPane(list);
		listPane.setBounds(new Rectangle(10, 50, 270, 300));
		listPane.setAutoscrolls(true);
		listPane.getViewport().setBackground(new Color(254,254,254));
		ComponentUtil.addComponents(this.getContentPane(), tipLabel, listPane);
		this.setVisible(true);
	}
}
