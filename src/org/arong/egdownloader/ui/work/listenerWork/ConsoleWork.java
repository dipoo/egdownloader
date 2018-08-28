package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.SwingPrintStream;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 打开/关闭控制台任务监听
 * @author 阿荣
 * @since 2014-06-22
 */
public class ConsoleWork implements IListenerTask {
	public JTextArea consoleTextArea;
	
	public void doWork(Window window, MouseEvent e) {
		/*final EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		if(mainWindow.consolePane == null){
			JTextArea jea = new JTextArea();
			jea.setEditable(false);
			jea.setAutoscrolls(true);
			jea.setLineWrap(true);
			jea.setBorder(null);
			jea.setFont(new Font("宋体", Font.PLAIN, 12));
			this.consoleTextArea = jea;
			JScrollPane consolePane = new JScrollPane(jea);
			TitledBorder border = BorderFactory.createTitledBorder(BorderUIResource.getEtchedBorderUIResource(), "控制台");
			consolePane.setBounds(5, 450, ComponentConst.CLIENT_WIDTH - 20, 190);
			consolePane.setAutoscrolls(true);
			consolePane.setBorder(border);
			consolePane.setVisible(false);
			
			mainWindow.getContentPane().add(consolePane);
			mainWindow.consolePane = consolePane;
			try {
				//将syso信息推送到控制台
				new SwingPrintStream(System.out, jea);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(window, "控制台初始化错误！");
			}
		}
		//如果控制台是显示的，则隐藏
		if(mainWindow.consolePane.isVisible()){
			mainWindow.consolePane.setVisible(false);
			//重设主窗口大小
			mainWindow.setSize(ComponentConst.CLIENT_WIDTH, mainWindow.getHeight() - 200);
		}
		//如果控制台是隐藏的，则显示
		else{
			mainWindow.consolePane.setVisible(true);
			//重设主窗口大小
			mainWindow.setSize(ComponentConst.CLIENT_WIDTH, mainWindow.getHeight() + 200);
		}
		mainWindow.setLocationRelativeTo(null);//使窗口居中
*/	}

}
