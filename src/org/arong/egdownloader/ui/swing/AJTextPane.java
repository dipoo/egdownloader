package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
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
	
	private HTMLEditorKit htmlEditorKit;
	private HTMLDocument htmlDoc;
	private Component com;
	public AJTextPane(){
		this(true);
	}
	public AJTextPane(boolean html){
		htmlEditorKit = new HTMLEditorKit();
		htmlDoc = (HTMLDocument) htmlEditorKit.createDefaultDocument();
		Border border = new EmptyBorder(10, 20, 20, 10);
		this.setBorder(border);
		this.setEditorKit(htmlEditorKit);
		this.setContentType("text/html");
		this.setDocument(htmlDoc);
		this.setEditable(false);
		
		//点击超链接打开浏览器事件
		this.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if(e.getURL() == null || "#".equals(e.getURL().toString()))
						return;
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
	public AJTextPane(String text, Color color) {
		this();
		if(color != null)
			this.setForeground(color);
		this.setText(text);
	}
	
	public void appendBHtml(String html){
		if(htmlEditorKit != null && htmlDoc != null){
			try {
				htmlEditorKit.insertHTML(htmlDoc, htmlDoc.getLength(), html, 0, 0, HTML.Tag.B);
				htmlEditorKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<br/>", 0, 0, HTML.Tag.BR);
			}catch (Exception e) {
				this.setText(this.getText() + "======exception:" + e.getMessage());
			}
		}else{
			this.setText(this.getText() + html);
		}
	}
	
	public void clear(){
		this.setText(null);
	}
	public HTMLEditorKit getHtmlEditorKit() {
		return htmlEditorKit;
	}
	public HTMLDocument getHtmlDoc() {
		return htmlDoc;
	}
	public Component getCom() {
		return com;
	}
	public void setCom(Component com) {
		this.com = com;
	}
}
