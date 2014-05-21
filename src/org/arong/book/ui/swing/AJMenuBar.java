package org.arong.book.ui.swing;

import java.awt.Component;

import javax.swing.JMenuBar;
/**
 * 封装JMenuBar,使构造函数可以设置大小及位置<br>
 * 可以添加多个组件
 * @author 阿荣
 * @since 2013-8-27
 *
 */
public class AJMenuBar extends JMenuBar {

	private static final long serialVersionUID = 9023750560413748220L;
	
	public AJMenuBar(int x, int y, int width, int height, Component... components){
		this.setBounds(x, y, width, height);
		for (Component component : components) {
			this.add(component);
		}
	}
}
