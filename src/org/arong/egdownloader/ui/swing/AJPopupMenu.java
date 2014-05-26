package org.arong.egdownloader.ui.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
/**
 * 封装右键菜单
 * @author 阿荣
 * @since 2014-05-26
 */
public class AJPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 5991402381842102636L;
	
	public AJPopupMenu(JMenuItem menuItem){
		this.add(menuItem);
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
				System.out.println(e.getSource());
			}
		});
	}

}
