package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.arong.egdownloader.ui.window.EgDownloaderWindow;
/**
 * 切换封面视图大小
 * @author dipoo
 * @since 2014-12-07
 */
public class ChangeViewSizeMenuItem extends JMenuItem {

	private static final long serialVersionUID = 8033742031776192264L;
	public ChangeViewSizeMenuItem(String text, final EgDownloaderWindow window, final int model){
		super(text);
		this.setForeground(new Color(0,0,85));
		final JMenuItem this_ = this;
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Component[] comps = this_.getParent().getComponents();
				for(Component c : comps){
					if(c instanceof JMenuItem){
						JMenuItem item = (JMenuItem)c;
						item.setText(item.getText().replace("√", ""));
					}
				}
				if(model == 2){
					window.setting.setCoverWidth(150);
					window.setting.setCoverHeight(216);
				}else if(model == 3){
					window.setting.setCoverWidth(100);
					window.setting.setCoverHeight(144);
				}else{
					window.setting.setCoverWidth(350);
					window.setting.setCoverHeight(480);
				}
				this_.setText(this_.getText() + "√");
				if(window.taskImagePanel != null){
					window.taskImagePanel.changeViewSize();
				}
				window.tablePane.repaint();
				window.tablePane.updateUI();
				window.repaint();
			}
		});
	}
}
