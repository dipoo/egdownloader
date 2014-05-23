package org.arong.egdownloader.ui.swing;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.arong.egdownloader.ui.ComponentConst;

/**
 * 封装JLabel，使构造函数可以设置文本，坐标，大小及字体颜色
 * 
 * @author 阿荣
 * @since 2012-8-25
 * 
 */
public class AJLabel extends JLabel {
	private static final long serialVersionUID = 4435841561097728806L;
	
	public AJLabel(String text, Color color) {
		super(text);
		this.setForeground(color);
	}
	/**
	 * 封装JLabel，使构造函数可以设置文本，坐标，大小及字体颜色
	 * 
	 * @param text
	 * @param color
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AJLabel(String text, Color color, int x, int y, int width, int height) {
		this(text, color);
		this.setBounds(x, y, width, height);
	}
	
	public AJLabel(String text, Color color, int align){
		this(text, color);
		this.setHorizontalAlignment(align);
	}
	
	public AJLabel(String text, String icon, Color color, int align){
		this(text, color, align);
		if(icon != null && !"".equals(icon.trim())){
			this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
		}
	}
}
