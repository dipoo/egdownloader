package org.arong.egdownloader.ui;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * 用于将控制台信息即System.out.println打印的信息转移到Swing组件JtextArea上显示
 * 
 * @author 阿荣
 * @since 2013-8-18
 */
public class SwingPrintStream extends PrintStream {
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
		logTextArea.append(message);
		// 让光标置于最下方
		logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
		logTextArea.paintImmediately(logTextArea.getBounds());
	}
	
	/**
	 * 当控制台的字符过多，则截断
	 * @param logTextArea
	 */
	private void filter(JTextArea logTextArea){
		String text = logTextArea.getText();
		if(text.length() > 3000){
			logTextArea.setText(text.substring(text.length() - 3000, text.length()));
		}
	}

}
