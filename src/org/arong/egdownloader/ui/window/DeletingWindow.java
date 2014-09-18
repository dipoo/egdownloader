package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
/**
 * 删除任务进度显示窗口
 * @author 阿荣
 * @since 2014-09-19
 */
public class DeletingWindow extends JDialog {

	private static final long serialVersionUID = -2544191890083257820L;
	
	public JFrame mainWindow;
	public JLabel dataLabel;
	public JLabel infoLabel;
	
	public DeletingWindow(JFrame window){
		this.mainWindow = window;
		this.setTitle("正在删除任务");
		this.setSize(400, 120);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(this.mainWindow);
		this.setBackground(Color.WHITE);
		
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				DeletingWindow window = (DeletingWindow) e.getSource();
				window.mainWindow.setEnabled(true);
				window.mainWindow.setVisible(true);
				window.dispose();
			}
			public void windowDeactivated(WindowEvent e) {
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
			}
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
		});
		
		dataLabel = new AJLabel("", Color.BLACK, 160, 10, 80, 30);
		infoLabel = new AJLabel("", Color.BLACK, 10, 50, 380, 30);
		ComponentUtil.addComponents(getContentPane(), dataLabel, infoLabel);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		//关闭事件
		if(e.getID() == WindowEvent.WINDOW_CLOSING){
			//do nothing
		}else{
			super.processWindowEvent(e);
		}
	}

	public void setData(String data){
		dataLabel.setText(data);
	}
	public void setInfo(String info){
		infoLabel.setText(info);
	}

}
