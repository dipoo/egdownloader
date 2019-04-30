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
			//大于5M则另存为
			if(logfile.exists() && logfile.length() > 1024 * 1024 * 5){
				logfile.renameTo(new File(binPath + File.separator + "console.log." + sdf2.format(new Date())));
				logfile.delete();
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

	String message = null;
	// 重写write方法，这是什么模式？装饰？代理？
	public void write(byte[] buf, int off, int len) {
		filter(consolePanel);
		message = new String(buf, off, len);
		if(StringUtil.notBlank(message)){
			consolePanel.realtext.append("<b style='font-size:9px;font-family:微软雅黑;'><font style='color:#0000dd;'>")
			.append(sdf.format(new Date())).append("</font> ").append(message).append("</b><br/>");
			try {
				consolePanel.showLog();
				if(!consolePanel.locked){
					// 让光标置于最下方
					consolePanel.paintImmediately(consolePanel.getBounds());
					consolePanel.getTextPane().setCaretPosition(consolePanel.getTextPane().getStyledDocument().getLength()); 
					consolePanel.updateUI();
				}
			
				//写日志
				logfw.append(sdf.format(new Date()) + " " + message + "\n");
				logfw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 当控制台的字符过多，则截断
	 * @param consolePanel
	 */
	private void filter(ConsolePanel consolePanel){
		if(consolePanel.getTextPane().getText().length() > 200000){
			consolePanel.realtext = new StringBuffer();
			consolePanel.showLog();
		}
	}

}
