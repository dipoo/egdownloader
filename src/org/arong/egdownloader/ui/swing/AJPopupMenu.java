package org.arong.egdownloader.ui.swing;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
/**
 * 封装右键菜单
 * @author 阿荣
 * @since 2014-05-26
 */
public class AJPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 5991402381842102636L;
	
	public AJPopupMenu(JMenuItem... menuItems){
		if(menuItems != null){
			for (JMenuItem jMenuItem : menuItems) {
				this.add(jMenuItem);
			}
		}
	}

	public void add(JMenuItem... menuItems){
		if(menuItems != null){
			for (JMenuItem jMenuItem : menuItems) {
				this.add(jMenuItem);
			}
		}
	}
}
