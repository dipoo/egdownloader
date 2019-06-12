package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
	public AJLabel() {
	}
	public AJLabel(String text, Color color) {
		super(text);
		if(color != null)
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
			try{
				this.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + icon)));
			}catch (Exception e) {
				
			}
		}
	}
	
	public AJLabel(String text, ImageIcon icon, Color color, int align){
		this(text, color, align);
		if(icon != null){
			try{
				this.setIcon(icon);
			}catch (Exception e) {
				
			}
		}
	}
	
	public AJLabel(String text, Color color, Font font, int align){
		this(text, color);
		this.setFont(font);
		this.setHorizontalAlignment(align);
	}
	
	private ImageIcon image;
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(image != null){
        	Graphics2D g2 = (Graphics2D)g;
            g2.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        	g2.getRenderingHints().put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        	g2.getRenderingHints().put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int w = getWidth();
            int h = getHeight();
            int iw = image.getIconWidth();
            int ih = image.getIconHeight();
            double xScale = (double)w / iw;
            double yScale = (double)h / ih;
            double scale = Math.min(xScale, yScale);    // scale to fit
            int width = (int)(scale * iw);
            int height = (int)(scale * ih);
            int x = (w - width) / 2;
            int y = (h - height) / 2;
            g2.drawImage(image.getImage(), x, y, width, height, this);
            /*count ++;
            if(count > 3) {
            	count = 0;
            	imageRendered = true;
            }*/
        }
	}
	public ImageIcon getImage() {
		return image;
	}
	public void setImage(ImageIcon image) {
		this.image = image;
	}
}
