package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.table.PictureTable;
/**
 * 详细任务面板
 * @author 阿荣
 * @since 2014-06-06
 */
public class DetailWindow extends JDialog {

	private static final long serialVersionUID = -4627145534664363270L;
	
	public PictureTable pictureTable;
	
	public JScrollPane picturePane;
	
	public JLabel taskNameLabel;
	public JLabel taskSubnameLabel;
	public JLabel taskLanguageLabel;
	public JLabel taskCreateLabel;
	public JLabel taskCompletedLabel;
	public JLabel taskSizeLabel;
	
	public DetailWindow(PictureTable pictureTable){
		this.pictureTable = pictureTable;
		this.setTitle("任务详细信息");
		this.setSize(ComponentConst.CLIENT_WIDTH, 600);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		JLabel l1 = new AJLabel("名称：", Color.BLUE, 5, 5, 40, 30);
		taskNameLabel = new AJLabel("", null, 60, 5, ComponentConst.CLIENT_WIDTH - 80, 30);
		JLabel l2 = new AJLabel("子标题：", Color.BLUE, 5, 5, 50, 70);
		taskSubnameLabel = new AJLabel("", null, 60, 5, ComponentConst.CLIENT_WIDTH - 80, 70);
		JLabel l3 = new AJLabel("漫画语言：", Color.BLUE, 5, 5, 60, 110);
		taskLanguageLabel = new AJLabel("", null, 80, 5, 150, 110);
		JLabel l4 = new AJLabel("创建时间：", Color.BLUE, 240, 5, 60, 110);
		taskCreateLabel = new AJLabel("", null, 310, 5, 150, 110);
		JLabel l5 = new AJLabel("完成时间：", Color.BLUE, 470, 5, 60, 110);
		taskCompletedLabel = new AJLabel("", null, 540, 5, 150, 110);
		JLabel l6 = new AJLabel("漫画大小：", Color.BLUE, 700, 5, 60, 110);
		taskSizeLabel = new AJLabel("", null, 770, 5, 150, 110);
		picturePane = new JScrollPane(pictureTable);
		picturePane.setBounds(new Rectangle(5, 80, ComponentConst.CLIENT_WIDTH - 20, 480));
		picturePane.getViewport().setBackground(new Color(254,254,254));
		ComponentUtil.addComponents(getContentPane(),l1, taskNameLabel, picturePane, l2, taskSubnameLabel,
				l3, l4, l5, l6, taskLanguageLabel, taskCreateLabel, taskCompletedLabel, taskSizeLabel);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				DetailWindow this_ = (DetailWindow) e.getSource();
				this_.dispose();
			}
			
		});
	}
	public void resetTile(String title){
		this.setTitle("任务详细信息(数目：" + title + "P)");
	}
}
