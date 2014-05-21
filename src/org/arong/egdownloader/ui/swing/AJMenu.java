package org.arong.egdownloader.ui.swing;

import java.awt.Component;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
/**
 * 封装JMenu,使构造函数可以设置text值,name值,鼠标监听器,大小及位置
 * @author 阿荣
 * @since 2013-8-27
 *
 */
public class AJMenu extends JMenu {

	private static final long serialVersionUID = 4174281127843821355L;
	
	public AJMenu(String text, String name, MouseListener listener, int x, int y, int width, int height){
		super(text);
		this.setBounds(x, y, width, height);
		this.setName(name);
		this.setCursor(CursorManager.getPointerCursor());
		this.addMouseListener(listener);
	}
	public AJMenu(String text, String name, MouseListener listener){
		super(text);
		this.setName(name);
		this.setCursor(CursorManager.getPointerCursor());
		this.addMouseListener(listener);
	}
	public AJMenu(String text, String name, String icon, MouseListener listener){
		this(text, name, listener);
		//设置图标
		this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
	}
	/**
	 * 用于带有下拉项的菜单,这样的菜单没有添加鼠标监听,而是由其子项来各自监听<br>
	 * 默认每个子项间由分割线
	 * @param text
	 * @param name
	 * @param components
	 */
	public AJMenu(String text, String name, Component... components){
		super(text);
		this.setName(name);
		this.setCursor(CursorManager.getPointerCursor());
		for (int i = 0; i < components.length; i++) {
			this.add(components[i]);
			if(i != components.length - 1){
				this.addSeparator();
			}
		}
	}
}
