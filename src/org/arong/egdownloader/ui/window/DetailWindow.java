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
	
	public DetailWindow(PictureTable pictureTable){
		this.pictureTable = pictureTable;
		this.setTitle("任务详细信息");
		this.setSize(800, 480);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		JLabel l1 = new AJLabel("名称", Color.BLUE, 5, 5, 40, 30);
		taskNameLabel = new AJLabel("", null, 60, 5, ComponentConst.CLIENT_WIDTH - 80, 30);
		
		picturePane = new JScrollPane(pictureTable);
		picturePane.setBounds(new Rectangle(5, 40, ComponentConst.CLIENT_WIDTH - 20, 400));
		picturePane.getViewport().setBackground(new Color(254,254,254));
		ComponentUtil.addComponents(getContentPane(),l1, taskNameLabel, picturePane);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowDeactivated(WindowEvent e) {
				DetailWindow this_ = (DetailWindow) e.getSource();
				this_.dispose();
			}
			
		});
	}
	public void resetTile(String title){
		this.setTitle("任务详细信息(数目：" + title + "P)");
	}
}
