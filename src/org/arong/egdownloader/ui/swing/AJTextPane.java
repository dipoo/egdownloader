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
		EditorKit editorKit = new HTMLEditorKit();
		Border border = new EmptyBorder(10, 20, 20, 10);
		this.setBorder(border);
		this.setEditable(false);
		this.setEditorKit(editorKit);
		
		/*final Font[] fList;
        //初始化Font列表
        String[] lstr = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fList = new Font[lstr.length];
		for (int i = 0; i < lstr.length; i++) {
			fList[i] = new Font(lstr[i], this.getFont().getStyle(), this.getFont().getSize());
		}
		this.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				JTextPane jtp = (JTextPane) e.getSource();
				String s = jtp.getText();
				Font f = jtp.getFont();
				if(f.canDisplayUpTo(s) != -1){//发现不能显示字体，则查找可使用的字体
					System.out.println(f.getFontName() + ":" + f.canDisplayUpTo(s));
					for(int i = 0; i < fList.length; i++){
						if(fList[i].canDisplayUpTo(s) == -1){
							jtp.setFont(fList[i]);
							break;
						}
					}
				}
			}
		});*/

		
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
}
