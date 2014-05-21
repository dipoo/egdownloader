package org.arong.book.ui.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * 加密解密窗口
 * 
 * @author 阿荣
 * @since 2013-9-1
 * 
 */
public class DecodeAndEncodeWindow extends JDialog implements ActionListener {

	private static final long serialVersionUID = -879776726998086531L;

	JButton encodeButton;
	JButton decodeButton;
	JTextArea decodeAndEncodeInputArea;
	JTextArea decodeAndEncodeOutputArea;

	public DecodeAndEncodeWindow(JFrame mainWindow) {
		/*this.setSize(480, 640);
		// 相对于主窗口居中
		this.setLocationRelativeTo(mainWindow);
		// 不能最大化，不能改变大小
		this.setResizable(false);
		JLabel decodeAndEncodeInputLabel = new AJLabel(
				ComponentConst.DECODE_AND_ENCODE_INPUT_LABEL_TEXT, Color.BLUE,
				20, 20, 60, 30);
		decodeAndEncodeInputArea = new AJTextArea(80, 20, 380, 250,null);
		JLabel decodeAndEncodeOutputLabel = new AJLabel(
				ComponentConst.DECODE_AND_ENCODE_OUTPUT_LABEL_TEXT, Color.BLUE,
				20, 320, 60, 30);
		decodeAndEncodeOutputArea = new AJTextArea(80, 320, 380, 250,null);
		encodeButton = new AJButton(ComponentConst.ENCODE_BUTTON_TEXT,
				ComponentConst.ENCODE_BUTTON_NAME, this, 150, 400, 60, 30);
		decodeButton = new AJButton(ComponentConst.DECODE_BUTTON_TEXT,
				ComponentConst.DECODE_BUTTON_NAME, this, 330, 400, 60, 30);
		this.add(decodeAndEncodeInputLabel, decodeAndEncodeInputArea,
				decodeAndEncodeOutputLabel, decodeAndEncodeOutputArea,
				encodeButton, decodeButton);
		this.setVisible(true);*/
	}

	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * 添加组件
	 *//*
	private void add(Component... components) {
		for (int i = 0; i < components.length; i++) {
			this.add(components[i]);
		}
	}*/
}
