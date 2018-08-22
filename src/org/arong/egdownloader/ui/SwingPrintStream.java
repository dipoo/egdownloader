package org.arong.egdownloader.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

import org.arong.util.FileUtil;
import org.arong.utils.DateUtil;
import org.arong.utils.StringUtil;

/**
 * 用于将控制台信息即System.out.println打印的信息转移到Swing组件JtextArea上显示
 * 
 * @author 阿荣
 * @since 2013-8-18
 */
public class SwingPrintStream extends PrintStream {
	static BufferedWriter logfw = null;
	static{
		//获取日志文件
		File logfile = new File(FileUtil.getProjectPath() + File.separator + "console.log");
		try {
			//大于20M则另存为
			if(logfile.exists() && logfile.length() > 1024 * 1024 * 20){
				logfile.renameTo(new File(FileUtil.getProjectPath() + File.separator + "console.log." + DateUtil.getStringToday()));
			}
			logfw = new BufferedWriter(new FileWriter(logfile, true), 4096);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private JTextArea logTextArea;

	public SwingPrintStream(OutputStream out, JTextArea logTextArea)
			throws FileNotFoundException {
		super(out);
		System.setOut(this);
		this.logTextArea = logTextArea;
	}

	// 重写write方法，这是什么模式？装饰？代理？
	public void write(byte[] buf, int off, int len) {
		filter(logTextArea);
		final String message = new String(buf, off, len);
		if(StringUtil.notBlank(message)){
			logTextArea.insert("--" + message + "\n", 0);//logTextArea.append(message);
			// 让光标置于最上方
			logTextArea.setCaretPosition(0);
			logTextArea.paintImmediately(logTextArea.getBounds());
			try {
				//写日志
				logfw.append(DateUtil.getStringToday() + " " + message + "\n");
				logfw.flush();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * 当控制台的字符过多，则截断
	 * @param logTextArea
	 */
	private void filter(JTextArea logTextArea){
		String text = logTextArea.getText();
		if(text.length() > 20000){
			logTextArea.setText(text.substring(text.length() - 1000, text.length()));
		}
	}

}
