package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;

/**
 * 封装JButton,使构造函数可以设置text值,name值,注册监听器,坐标,大小<br>
 * 默认使用手型光标,白色字体
 * 
 * @author 阿荣
 * @since 2013-8-25
 * 
 */
public class AJButton extends JButton {

	private static final long serialVersionUID = 1876134017404282134L;

	/**
	 * 装JButton,使构造函数可以设置text值,name值,注册监听器,坐标,大小<br>
	 * 默认使用手型光标
	 * 
	 * @param text
	 * @param name
	 * @param actionListener
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AJButton(String text, String name, ActionListener actionListener,
			int x, int y, int width, int height) {
		super(text);
		this.setName(name);
		this.setToolTipText(text);
		this.setBounds(x, y, width, height);
		this.setForeground(Color.GRAY);
		this.setFocusable(false);
		this.setMargin(new Insets(0, 0, 0, 0));
		// 设置为手型光标
		this.setCursor(CursorManager.getPointerCursor());
		if(actionListener != null)
			this.addActionListener(actionListener);
	}
	
	public AJButton(String text, String name, String icon, ActionListener actionListener,
			int x, int y, int width, int height) {
		this(text, name, actionListener, x, y, width, height);
		if(icon != null)
			this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
	}
	public AJButton(String text, String name, String icon, MouseListener mouseListener,
			int x, int y, int width, int height) {
		this(text, name, null, x, y, width, height);
		if(mouseListener != null)
			this.addMouseListener(mouseListener);
		if(icon != null)
			this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
	}
}
