package org.arong.egdownloader.ui.swing;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.FontConst;
import org.jb2011.lnf.beautyeye.ch14_combox.BEComboBoxUI;

/**
 * 封装JcomboBox，使构造函数可以设置是否监听，组件name值，坐标，大小，选项最多显示个数（指不产生滚动条时）及下拉选项
 * 
 * @author 阿荣
 * @since 2013-8-25
 * 
 */
public class AJComboBox extends JComboBox {

	private static final long serialVersionUID = 4326625694595881489L;

	/**
	 * 封装JcomboBox，使构造函数可以设置是否监听，组件name值，坐标，大小，选项最多显示个数（指不产生滚动条时）及下拉选项
	 * 
	 * @param listen
	 * @param actionListener
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param maximumRowCount
	 * @param items
	 */
	public AJComboBox(boolean listen, ActionListener actionListener,
			String name, int x, int y, int width, int height,
			int maximumRowCount, String... items) {//此处的items参数不能为String[]，不然会显示异常，不知道为什么？
		super(items);
		this.setBounds(x, y, width, height);
		this.setName(name);
		BEComboBoxUI.createUI(this);
		this.setMaximumRowCount(maximumRowCount);
		this.setCursor(CursorManager.getPointerCursor());
		this.setFont(FontConst.Microsoft_BOLD_12);
		if (listen && actionListener != null) {
			this.addActionListener(actionListener);
		}
	}
}
