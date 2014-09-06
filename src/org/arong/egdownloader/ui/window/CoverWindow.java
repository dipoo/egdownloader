package org.arong.egdownloader.ui.window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
/**
 * 漫画封面窗口
 * @author 阿荣
 * @since 2014-09-06
 */
public class CoverWindow extends JDialog {
	private static final long serialVersionUID = 6624222157904971813L;
	private JLabel coverLabel = null;
	public CoverWindow(Task task) {
		ImageIcon icon = new ImageIcon(task.getSaveDir() + "/cover.jpg");
		this.setTitle("漫画封面");
		this.setSize(icon.getIconWidth() + 20, icon.getIconHeight() + 45);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		coverLabel = new JLabel(icon);
		coverLabel.setBounds(5, 5, icon.getIconWidth(), icon.getIconHeight());
		ComponentUtil.addComponents(getContentPane(),coverLabel);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				CoverWindow this_ = (CoverWindow) e.getSource();
				this_.dispose();
			}
			
		});
	}
}
