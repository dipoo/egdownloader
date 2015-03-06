package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

/**
 * 用于多行文本显示<br>
 * 封装JTextPane,使构造函数可以设置text值,文本颜色<br>
 * 默认外边距为10,20,20,10;文本不能编辑<br>
 * 默认支持html格式文本显示
 * 
 * @author 阿荣
 * @since 2013-8-25
 * 
 */
public class AJTextPane extends JTextPane {

	private static final long serialVersionUID = 5006884186865600388L;
	public AJTextPane(){
		super();
	}
	public AJTextPane(String text, Color color) {
		EditorKit editorKit = new HTMLEditorKit();
		this.setForeground(color);
		Border border = new EmptyBorder(10, 20, 20, 10);
		this.setBorder(border);
		this.setEditable(false);
		this.setEditorKit(editorKit);
		this.setText(text);
		//点击超链接打开浏览器事件
		this.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
					try {
						Desktop.getDesktop().browse(new URI(e.getURL().toString()));
					} catch (IOException e1) {
						try {
							Runtime.getRuntime().exec("cmd.exe /c start " + e.getURL().toString());
						} catch (IOException e2) {
							
						}
					} catch (URISyntaxException e1) {
						try {
							Runtime.getRuntime().exec("cmd.exe /c start " + e.getURL().toString());
						} catch (IOException e2) {
							
						}
					}
				}
			}
		});
	}
}
