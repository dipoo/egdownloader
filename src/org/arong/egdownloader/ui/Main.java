package org.arong.egdownloader.ui;

import java.awt.Font;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.arong.egdownloader.ui.window.GroupWindow;
import org.arong.egdownloader.ui.window.InitWindow;
import org.arong.egdownloader.version.Version;


/**
 * 启动程序的唯一入口
 * 
 * @author 阿荣
 * @since 2013-8-25
 */
public class Main {
	/** 端口号 */
	private static int iPort = 50009;

	public static void main(String[] args) {
		try{
			//删除windows计划任务
			Runtime.getRuntime().exec(String.format("cmd.exe /c schtasks /delete /tn %s /F", ComponentConst.RESTART_PLAN_TASK_NAME));
		}catch(Exception e){}
		
		
		Thread thread = null; // 启动服务器的线程
		try {
			// 连接服务器
			// 如果服务器未启动则抛异常
			(new Socket("localhost", iPort)).close();
			if(ComponentConst.mainWindow != null){
				ComponentConst.mainWindow.setVisible(true);
			}else{
				JOptionPane.showMessageDialog(null, "本程序不允许多开", Version.NAME, JOptionPane.ERROR_MESSAGE);
			}
			// 如果服务器已经启动则退出系统
			System.exit(0);
		} catch (Exception e) {
		}// 未做处理

		// 如果服务器未启动则在新的线程中启动服务器
		(thread = new Thread(new Server())).setDaemon(true);
		// 开始线程
		thread.start();

		// 调整默认字体
		for (int i = 0; i < FontConst.DEFAULT_FONT.length; i++)
			UIManager.put(FontConst.DEFAULT_FONT[i], new Font("微软雅黑",
					Font.BOLD, 12));
		File dataFile = new File(ComponentConst.ROOT_DATA_PATH);
		if (!dataFile.exists()) {
			dataFile.mkdirs();
			new InitWindow();
		} else {
			File[] files = dataFile.listFiles();
			List<File> groups = new ArrayList<File>();
			for (File file : files) {
				if (file.isDirectory()) {
					groups.add(file);
				}
			}
			if (groups.size() > 0) {
				new GroupWindow(groups, null);
			} else {
				new InitWindow();
			}
		}
		
		// 异步执行
		/*
		 * SwingUtilities.invokeLater(new Runnable() { public void run() { new
		 * InitWindow(); } });
		 */
	}

	/**
	 * 端口监听服务器端运行
	 * 
	 * @author hiswing
	 */
	static class Server implements Runnable {
		public final void run() {
			ServerSocket serversocket = null;

			// 查找没有占用的端口
			while (iPort < 60000) {
				try {
					serversocket = new ServerSocket(iPort);
				} catch (Exception ex) {
					iPort++;
				}
				break;
			}
			try {
				do {
					// 监听客户端是否有连接
					serversocket.accept();
					if(ComponentConst.mainWindow != null){
						ComponentConst.mainWindow.setVisible(true);
						// 窗口在任务栏闪动
						if (ComponentConst.mainWindow.getExtendedState() == 1) {
							ComponentConst.mainWindow.setExtendedState(0);
						}
						if (ComponentConst.mainWindow.getExtendedState() != 1) {
							ComponentConst.mainWindow.toFront();
							ComponentConst.mainWindow.requestFocus();
							ComponentConst.mainWindow.repaint();
						}
					}
				} while (true);
			} catch (Exception ex) {
				// 不做处理
			}
		}
	}
}