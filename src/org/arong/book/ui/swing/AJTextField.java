package org.arong.book.ui.swing;

import javax.swing.JTextField;
/**
 * 封装JTextField,使构造函数可以设置name值,大小及坐标
 * @author 阿荣
 * @since 2013-8-25
 *
 */
public class AJTextField extends JTextField {

	private static final long serialVersionUID = -1763143133193131228L;
	
	public AJTextField(String name, int x, int y, int width, int height){
		this.setName(name);
		this.setBounds(x, y, width, height);
	}
}
