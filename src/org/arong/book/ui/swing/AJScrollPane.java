package org.arong.book.ui.swing;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
/**
 * 封装JScrollPane,使构造函数可以设置边框标题,大小及位置<br>
 * 可以添加多个组件,默认使用虚线带标题边框
 * @author 阿荣
 * @since 2013-8-27
 */
public class AJScrollPane extends JScrollPane {

	private static final long serialVersionUID = 2812087034031177881L;
	/**
	 * 封装JScrollPane,使构造函数可以设置边框标题,大小及位置<br>
 * 		可以添加多个组件,默认使用虚线带标题边框
	 * @param borderTitle
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param components
	 */
	public AJScrollPane(String borderTitle, int x, int y, int width, int height, Component... components){
		this.setBounds(x, y, width, height);
		Border border = BorderFactory.createTitledBorder(borderTitle);
		this.setBorder(border);
		this.setAutoscrolls(true);
		for (Component component : components) {
			this.add(component);
		}
	}
	/**
	 * 可以设置坐标及大小<br>
	 * 添加多个组件，无边框，可滚动
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param components
	 */
	public AJScrollPane(int x, int y, int width, int height, Component... components){
		this.setBounds(x, y, width, height);
		this.setBorder(null);
		this.setAutoscrolls(true);
		for (Component component : components) {
			this.add(component);
		}
	}
}
