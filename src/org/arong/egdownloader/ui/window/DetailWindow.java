package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
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
	
	public DetailWindow(PictureTable pictureTable){
		this.pictureTable = pictureTable;
		this.setTitle("任务详细信息");
		this.setSize(800, 480);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		picturePane = new JScrollPane(pictureTable);
		picturePane.setBounds(new Rectangle(5, 40, ComponentConst.CLIENT_WIDTH - 20, 400));
		picturePane.getViewport().setBackground(new Color(254,254,254));
		ComponentUtil.addComponents(getContentPane(), picturePane);
	}

}
