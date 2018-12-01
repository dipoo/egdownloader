package org.arong.egdownloader.ui.swing;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.arong.egdownloader.ui.IconManager;
/**
 * 封装JTextField,使构造函数可以设置name值,大小及坐标(含右键菜单)
 * @author 阿荣
 * @since 2013-8-25
 *
 */
public class AJTextField extends JTextField {

	private static final long serialVersionUID = -1763143133193131228L;
	
	private JPopupMenu popupMenu;
	private Color color = new Color(0,0,85);
	//右键菜单：复制
	private AJMenuItem copyMenuItem = new AJMenuItem("复制", color, IconManager.getIcon("copy"), null);
	//右键菜单：剪切
	private AJMenuItem cutMenuItem = new AJMenuItem("剪切", color, IconManager.getIcon("cut"), null);
	//右键菜单：粘贴
	private AJMenuItem pasteMenuItem = new AJMenuItem("粘贴", color, IconManager.getIcon("paste"), null);
	//右键菜单：清空
	private AJMenuItem clearMenuItem = new AJMenuItem("清空", color, IconManager.getIcon("clear"), null);
	//右键菜单：全选
	private AJMenuItem selectAllMenuItem = new AJMenuItem("全选", color, null);
	
	public AJTextField(String name, int x, int y, int width, int height){
		this.setName(name);
		this.setBounds(x, y, width, height);
		final AJTextField this_ = this;
		
		copyMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectText = this_.getSelectedText();
				if(selectText != null && !"".equals(selectText)){
					StringSelection ss = new StringSelection(selectText);
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
				}
			}
		});
		
		cutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!this_.isEditable()){
					return;
				}
				String selectText = this_.getSelectedText();
				if(selectText != null && !"".equals(selectText)){
					StringSelection ss = new StringSelection(selectText);
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
					this_.setText(this_.getText().substring(0, this_.getSelectionStart()) + this_.getText().substring(this_.getSelectionEnd()));
				}
			}
		});
		
		pasteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!this_.isEditable()){
					return;
				}
				Transferable t = this_.getToolkit().getSystemClipboard().getContents(this);
				//判断内容是否为空，是否为字符串
				if(t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)){
					try {
						this_.setText(this_.getText() + t.getTransferData(DataFlavor.stringFlavor) + "");
					} catch (Exception e1) {
						System.out.println("粘贴出错");
					}
				}
			}
		});
		
		clearMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!this_.isEditable()){
					return;
				}
				this_.setText("");
			}
		});
		
		selectAllMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				this_.requestFocus();
				this_.selectAll();
			}
		});
		
		popupMenu = new AJPopupMenu(copyMenuItem, cutMenuItem, pasteMenuItem, clearMenuItem, selectAllMenuItem);
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				AJTextField field = (AJTextField)e.getSource();
				//获取点击的行数
				if(e.getButton() == MouseEvent.BUTTON3){
					String text = this_.getText();
					String selectText = this_.getSelectedText();
					//是否复制
					if(selectText == null || "".equals(selectText)){
						copyMenuItem.setEnabled(false);
						copyMenuItem.setForeground(Color.GRAY);
						cutMenuItem.setEnabled(false);
						cutMenuItem.setForeground(Color.GRAY);
					}else{
						copyMenuItem.setEnabled(true);
						copyMenuItem.setForeground(color);
						cutMenuItem.setEnabled(true);
						cutMenuItem.setForeground(color);
					}
					
					//是否粘贴
					Transferable t = this_.getToolkit().getSystemClipboard().getContents(this);
					//判断内容是否为空，是否为字符串
					if(t == null || !t.isDataFlavorSupported(DataFlavor.stringFlavor) || !this_.isEditable()){
						pasteMenuItem.setEnabled(false);
						pasteMenuItem.setForeground(Color.GRAY);
					}else{
						pasteMenuItem.setEnabled(true);
						pasteMenuItem.setForeground(color);
					}
					//是否清空/全选
					if("".equals(text) || !this_.isEditable()){
						clearMenuItem.setEnabled(false);
						clearMenuItem.setForeground(Color.GRAY);
						selectAllMenuItem.setEnabled(false);
						selectAllMenuItem.setForeground(Color.GRAY);
					}else{
						clearMenuItem.setEnabled(true);
						clearMenuItem.setForeground(color);
						selectAllMenuItem.setEnabled(true);
						selectAllMenuItem.setForeground(color);
					}
					
					popupMenu.show(field, e.getPoint().x, e.getPoint().y);
				}
			}
		});
		
	}
	public AJTextField(String text,String name, int x, int y, int width, int height){
		this(name, x, y, width, height);
		this.setText(text);
	}
	
}
