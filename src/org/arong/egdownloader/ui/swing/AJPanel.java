package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
/**
 * JPanel封装类
 * @author 阿荣
 * @since 2014-06-12
 */
public class AJPanel extends JPanel {

	private static final long serialVersionUID = -1284563325238310955L;

	public AJPanel(Component...components){
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		if(components.length > 0){
			for(int i= 0; i < components.length; i++){
				if(components[i] != null){
					this.add(components[i]);
				}
			}
		}
	}
}
