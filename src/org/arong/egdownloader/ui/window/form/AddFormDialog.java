package org.arong.egdownloader.ui.window.form;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.AboutMenuWindow;

public class AddFormDialog extends JDialog {

	private static final long serialVersionUID = 6680144418171641216L;
	
	private JTextField urlField;
	private JFileChooser saveDirChooser;
	
	public AddFormDialog(final JFrame mainWindow){
		this.setTitle("新建任务");
		this.setSize(320, 240);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(mainWindow);
		
		ComponentUtil.addComponents(this.getContentPane());
		
		//添加窗口聚焦监听器
		this.addWindowFocusListener(new WindowFocusListener() {
			// 当失去活动状态的时候此窗口被隐藏
			public void windowLostFocus(WindowEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.setVisible(false);
			}
			public void windowGainedFocus(WindowEvent e) {}
		});
		//添加鼠标活动监听器
		this.addMouseListener(new MouseListener() {
			// 当鼠标点击当前窗口时隐藏此窗口
			public void mouseClicked(MouseEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.setVisible(false);
			}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
		});
	}

}
