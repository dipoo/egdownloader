package org.arong.egdownloader.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.arong.egdownloader.ui.panel.ConsolePanel;
import org.arong.util.DateUtil;
import org.arong.util.FileUtil2;
import org.arong.utils.StringUtil;

/**
 * 用于将控制台信息即System.out.println打印的信息转移到Swing组件JtextArea上显示
 * 
 * @author 阿荣
 * @since 2013-8-18
 */
public class SwingPrintStream extends PrintStream {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	static {
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}
	static BufferedWriter logfw = null;
	static{
		String binPath = FileUtil2.getProjectPath();
		if(binPath.endsWith("bin")){
			binPath = binPath.substring(0, binPath.length() - 3);
		}else{
			FileUtil2.ifNotExistsThenCreate("");
			File f = new File("");
			binPath = f.getAbsolutePath();
		}
		//获取日志文件
		File logfile = new File(binPath + File.separator + "console.log");
		try {
			//大于20M则另存为
			if(logfile.exists() && logfile.length() > 1024 * 1024 * 20){
				logfile.renameTo(new File(FileUtil2.getProjectPath() + File.separator + "console.log." + sdf2.format(new Date())));
			}
			logfw = new BufferedWriter(new FileWriter(logfile, true), 4096);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private ConsolePanel consolePanel;

	public SwingPrintStream(OutputStream out, ConsolePanel consolePanel)
			throws FileNotFoundException {
		super(out);
		System.setOut(this);
		this.consolePanel = consolePanel;
	}

	// 重写write方法，这是什么模式？装饰？代理？
	public void write(byte[] buf, int off, int len) {
		filter(consolePanel);
		String message = new String(buf, off, len);
		if(StringUtil.notBlank(message)){
			//consolePanel.insert("--" + message + "\n", 0);//consolePanel.append(message);
			message = (message.length() > 8 && !":".equals(message.substring(2, 3)) ? sdf.format(new Date()) : sdf2.format(new Date())) + " " + message + "\n";
			try {
				consolePanel.setText(consolePanel.getTextPane().getText() + message);
				if(!consolePanel.locked){
					// 让光标置于最下方
					//consolePanel.paintImmediately(consolePanel.getBounds());
					consolePanel.getTextPane().setCaretPosition(consolePanel.getTextPane().getStyledDocument().getLength()); 
					consolePanel.updateUI();
				}
			
				//写日志
				logfw.append(message);
				logfw.flush();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * 当控制台的字符过多，则截断
	 * @param consolePanel
	 */
	private void filter(ConsolePanel consolePanel){
		String text = consolePanel.getTextPane().getText();
		if(text.length() > 200000){
			consolePanel.setText("");
		}
	}

}
