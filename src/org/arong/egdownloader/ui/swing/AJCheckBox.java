package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;
/**
 * 封装JCheckBox
 * @author dipoo
 * @since 2015-03-13
 */
public class AJCheckBox extends JCheckBox {

	private static final long serialVersionUID = 5733291772113186219L;
	
	public AJCheckBox(String text, Color color, Font font, boolean selected) {
		this.setText(text);
		this.setForeground(color);
		this.setFont(font);
		this.setSelected(selected);
	}
	public AJCheckBox(String text, Color color, boolean selected) {
		this.setText(text);
		this.setForeground(color);
		this.setSelected(selected);
	}

}
