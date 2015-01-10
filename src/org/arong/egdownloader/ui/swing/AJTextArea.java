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

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.arong.egdownloader.ui.ComponentConst;

/**
 * 用于多行文本显示<br>
 * 封装JTextArea<br>
 * 默认文本不能编辑,可以自动滚动,无边框<br>
 * 
 * @author 阿荣
 * @since 2013-8-27
 * 
 */
public class AJTextArea extends JTextArea {

	private static final long serialVersionUID = 5006884186865600388L;
	private JPopupMenu popupMenu;
	private Color color = new Color(0,0,85);
	//右键菜单：复制
	private AJMenuItem copyMenuItem = new AJMenuItem("复制", color, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("copy"), null);
	//右键菜单：剪切
	private AJMenuItem cutMenuItem = new AJMenuItem("剪切", color, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("cut"), null);
	//右键菜单：粘贴
	private AJMenuItem pasteMenuItem = new AJMenuItem("粘贴", color, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("paste"), null);
	//右键菜单：清空
	private AJMenuItem clearMenuItem = new AJMenuItem("清空", color, ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("clear"), null);
	//右键菜单：全选
	private AJMenuItem selectAllMenuItem = new AJMenuItem("全选", color, null);
	
	public AJTextArea() {
		this.setEditable(false);
		this.setAutoscrolls(true);
		this.setBorder(null);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
		popup(this);
	}

	/**
	 * 可以设置坐标及大小，有边框，可编辑<br>
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AJTextArea(int x, int y, int width, int height, String borderTitle) {
		this.setAutoscrolls(true);
		Border border1 = BorderFactory.createLineBorder(new Color(219,219,219));
		Border border = BorderFactory.createTitledBorder(border1, borderTitle);
		this.setBorder(border);
		this.setBounds(x, y, width, height);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
		popup(this);
	}
	
	/**
	 * 可以设置坐标及大小，有边框，可编辑<br>
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AJTextArea(String text, int x, int y, int width, int height) {
		this.setEditable(false);
		this.setAutoscrolls(true);
		Border border1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		this.setBorder(border1);
		this.setText(text);
		this.setBounds(x, y, width, height);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
		popup(this);
	}
	
	/**
	 * 默认使用带标题的边框(实线)
	 * @param borderTitle
	 */
	public AJTextArea(String borderTitle, Color borderColor){
		this.setAutoscrolls(true);
		Border border1 = BorderFactory.createLineBorder(borderColor);
		Border border = BorderFactory.createTitledBorder(border1, borderTitle);
		this.setBorder(border);
		// 下面这行代码是自动滚动的关键代码
		this.setLineWrap(true);
		popup(this);
	}
	
	public void popup(final AJTextArea this_){
		
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
					this_.setText(this_.getText().replaceAll(selectText, ""));
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
						this_.setText(t.getTransferData(DataFlavor.stringFlavor) + "");
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
				AJTextArea field = (AJTextArea)e.getSource();
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
}