package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
/**
 * 重置所有任务状态进度窗口
 * @author dipoo
 * @since 2014-11-18
 */
public class ResetAllTaskWindow extends JDialog {

	private static final long serialVersionUID = 6439757790773454972L;
	
	public JFrame mainWindow;
	public JLabel infoLabel;
	List<Task> tasks;
	public ResetAllTaskWindow(JFrame window, List<Task> tasks) {
		this.mainWindow = window;
		this.tasks = tasks;
		this.setTitle("正在重置任务");
		this.setSize(400, 80);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(this.mainWindow);
		this.setBackground(Color.WHITE);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ResetAllTaskWindow window = (ResetAllTaskWindow) e.getSource();
				window.mainWindow.setEnabled(true);
				window.mainWindow.setVisible(true);
				window.dispose();
			}
			//窗体由激活状态变成非激活状态
			/*public void windowDeactivated(WindowEvent e) {
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				ResetAllTaskWindow window = (ResetAllTaskWindow) e.getSource();
				window.dispose();
			}*/
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
		});
		
		infoLabel = new AJLabel("", Color.BLACK, 120, 10, 150, 30);
		ComponentUtil.addComponents(getContentPane(), infoLabel);
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
	public void dispose() {
		mainWindow.setEnabled(true);
		mainWindow.setVisible(true);
		super.dispose();
	}
	public void setInfoLabel(String text) {
		infoLabel.setText(text + "/" + tasks.size());
	}

}
