package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJProgressBar;
/**
 * 打包zip细节显示窗口
 * @author dipoo
 * @since 2016-03-30
 */
public class ZiptingWindow extends JDialog {

	private static final long serialVersionUID = -2544191890083257820L;
	
	public JFrame mainWindow;
	public JProgressBar bar;
	public JLabel totalLabel;
	public JLabel nameLabel;
	
	public ZiptingWindow(JFrame window){
		this.mainWindow = window;
		this.setTitle("正在打包为ZIP文件");
		this.setSize(400, 150);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(this.mainWindow);
		this.setBackground(Color.WHITE);
		
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ZiptingWindow window = (ZiptingWindow) e.getSource();
				window.dispose();
			}
			//窗体由激活状态变成非激活状态
			/*public void windowDeactivated(WindowEvent e) {
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				CreatingWindow window = (CreatingWindow) e.getSource();
				window.dispose();
			}*/
			public void windowActivated(WindowEvent e) {
				//mainWindow.setEnabled(false);
			}
		});
		
		nameLabel = new AJLabel("名称：", Color.BLACK, 10, 5, 380, 20);
		totalLabel = new AJLabel("数目：", Color.BLACK, 10, 35, 100, 20);
		bar = new AJProgressBar(40, 65, 310, 20, 0, 100);
		bar.setStringPainted(true);
		ComponentUtil.addComponents(getContentPane(), nameLabel,
				totalLabel, bar);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		/*//关闭事件
		if(e.getID() == WindowEvent.WINDOW_CLOSING){
			//do nothing
		}else{
			super.processWindowEvent(e);
		}*/
		super.processWindowEvent(e);
	}

}
