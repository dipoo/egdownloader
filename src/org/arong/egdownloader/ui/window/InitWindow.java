package org.arong.egdownloader.ui.window;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.version.Version;
/**
 * 程序初始化窗口
 * @author 阿荣
 * @since 2013-8-31
 */
public class InitWindow extends JFrame {

	private static final long serialVersionUID = -7316667195338580556L;
	
	private JLabel textLabel;
	
	public InitWindow(){
		super(Version.NAME + "初始化");
		this.setSize(300, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		textLabel = new AJLabel("加载采集站点信息",Color.BLUE,80,10,260,30);
		this.getContentPane().add(textLabel);
		this.setVisible(true);
		textLabel.setText("初始化主窗口");
		/*//加载采集站点信息
		try {
			Configuration.getSites();
		} catch (InvalidFileFormatException e) {
			JOptionPane.showMessageDialog(this, Configuration.INI_FILE_NAME
					+ "文件格式错误,无法完成初始化", "程序运行错误", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "程序运行错误",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, Configuration.INI_FILE_NAME
					+ "文件载入异常，无法完成初始化", "程序运行错误", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IniException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "程序运行错误：" + e.getMessage());
			System.exit(0);
		}*/
		
//		JFrame mainWindow = new MainWindow();
		JFrame egDownloaderWindow = new EgDownloaderWindow();
		textLabel.setText("初始化完成");
		this.setVisible(false);
		egDownloaderWindow.setVisible(true);
		//初始化控制台
	}
	public JLabel getTextLabel(){
		return textLabel;
	}
}
