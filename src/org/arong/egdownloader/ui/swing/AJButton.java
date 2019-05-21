package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.util.Tracker;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * 封装JButton,使构造函数可以设置text值,name值,注册监听器,坐标,大小<br>
 * 默认使用手型光标,白色字体
 * 
 * @author 阿荣
 * @since 2013-8-25
 * 
 */
public class AJButton extends JButton {
	
	public static ButtonUI greenBtnUi = new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green);
	public static ButtonUI blueBtnUi = new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue);
	public static ButtonUI redBtnUi = new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red);
	public static ButtonUI lightBlueUi = new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue);

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
		if(text != null && !"".equals(text)){
			this.setToolTipText(text);
		}
		this.setBounds(x, y, width, height);
		this.setForeground(Color.WHITE);
		this.setFocusable(false);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setUI(greenBtnUi);
		// 设置为手型光标
		this.setCursor(CursorManager.getPointerCursor());
		if(actionListener != null)
			this.addActionListener(actionListener);
		this.addMouseListener(new MouseAdapter(){
			public void mouseExited(MouseEvent e) {
				AJButton btn = (AJButton) e.getSource();
				btn.setForeground(Color.WHITE);
			}
			public void mouseEntered(MouseEvent e) {
				AJButton btn = (AJButton) e.getSource();
				btn.setForeground(Color.BLACK);
			}
		});
	}
	
	public AJButton(String text, String name, String icon, MouseListener mouseListener,
			int x, int y, int width, int height) {
		this(text, name, null, x, y, width, height);
		if(mouseListener != null)
			this.addMouseListener(mouseListener);
		if(icon != null && !"".equals(icon)){
			try{
				this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
			}catch(Exception e){
				Tracker.println(getClass(), e.getMessage());
			}
		}
	}
	public AJButton(String text, ImageIcon icon, MouseListener mouseListener, 
			int x, int y, int width, int height) {
		this(text, "", null, x, y, width, height);
		if(mouseListener != null)
			this.addMouseListener(mouseListener);
		if(icon != null){
			try{
				this.setIcon(icon);
			}catch(Exception e){
				Tracker.println(getClass(), e.getMessage());
			}
		}
	}
	
	public AJButton(String text, ImageIcon icon, MouseListener mouseListener, boolean border) {
		super(text);
		this.setCursor(CursorManager.getPointerCursor());
		if(mouseListener != null)
			this.addMouseListener(mouseListener);
		if(icon != null){
			try{
				this.setIcon(icon);
			}catch(Exception e){
				Tracker.println(getClass(), e.getMessage());
			}
		}
		if(!border){
			this.setUI(new BasicButtonUI());
			final Border empty = BorderFactory.createEmptyBorder(0, 5, 0, 5);
			this.setBorder(empty);
			this.setFocusable(false);
			this.addMouseListener(new MouseAdapter() {
				Border vborder = new Border() {  
		            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {  
		                g.setColor(new Color(Integer.parseInt("2593D9", 16)));  
		                g.drawLine(x, y + height - 2, x + width, y + height - 2);
		            	g.drawLine(x, y + height - 1, x + width, y + height - 1);
		            }  
		  
		            public Insets getBorderInsets(Component c) {  
		                return new Insets(10, 5, 10, 5);  
		            }  
		  
		            public boolean isBorderOpaque() {  
		                return true;  
		            }  
		        };
			    public void mouseEntered(MouseEvent e) {  
			        if (isRolloverEnabled()) {  
			            setBorder(vborder);  
			        }  
			    }  
			  
			    public void mouseExited(MouseEvent e) {  
			        if (isRolloverEnabled()) {  
			            setBorder(empty);  
			        }  
			    } 
			});
		}
	}
	
	public AJButton(String text){
		this.setUI(greenBtnUi);
		this.setText(text);
		// 设置为手型光标
		this.setCursor(CursorManager.getPointerCursor());
		if(text != null && !"".equals(text)){
			this.setToolTipText(text);
		}
		this.setForeground(Color.WHITE);
		this.addMouseListener(new MouseAdapter(){
			public void mouseExited(MouseEvent e) {
				AJButton btn = (AJButton) e.getSource();
				btn.setForeground(Color.WHITE);
			}
			public void mouseEntered(MouseEvent e) {
				AJButton btn = (AJButton) e.getSource();
				btn.setForeground(Color.BLACK);
			}
		});
	}
	
	public AJButton(String text, String icon){
		this(text);
		if(icon != null && !"".equals(icon)){
			try{
				this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
			}catch(Exception e){
				Tracker.println(getClass(), e.getMessage());
			}
		}
	}
}
