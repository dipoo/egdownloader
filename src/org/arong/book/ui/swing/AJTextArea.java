package org.arong.book.ui.swing;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * 用于多行文本显示<br>
 * 封装JTextArea<br>
 * 默认文本不能编辑,可以自动滚动,无边框<br>
 * 
 * @author 阿荣
 * @since 2013-8-27
 * 
 */
public class AJTextArea extends JTextArea {

	private static final long serialVersionUID = 5006884186865600388L;

	public AJTextArea() {
		this.setEditable(false);
		this.setAutoscrolls(true);
		this.setBorder(null);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
	}

	/**
	 * 可以设置坐标及大小，有边框，可编辑<br>
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AJTextArea(int x, int y, int width, int height, String borderTitle) {
		this.setAutoscrolls(true);
		Border border1 = BorderFactory.createLineBorder(Color.MAGENTA);
		Border border = BorderFactory.createTitledBorder(border1, borderTitle);
		this.setBorder(border);
		this.setBounds(x, y, width, height);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
	}
	
	/**
	 * 默认使用带标题的边框(实线)
	 * @param borderTitle
	 */
	public AJTextArea(String borderTitle, Color borderColor){
		this.setAutoscrolls(true);
		Border border1 = BorderFactory.createLineBorder(borderColor);
		Border border = BorderFactory.createTitledBorder(border1, borderTitle);
		this.setBorder(border);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
	}
}