package org.arong.egdownloader.ui.window;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.commons.codec.binary.Base64;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.listener.WindowMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.util.CodeUtil;

public class ToolsMenuWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = -2290486210441887526L;

	JTabbedPane toolTabPanel = new JTabbedPane(JTabbedPane.TOP);
	/* 加密/解密页签 */
	JPanel decodeAndEncodePanel;
	JTextArea decodeAndEncodeInputArea;
	JTextArea decodeAndEncodeOutputArea;
	JButton encodeButton;
	JButton decodeButton;

	/* 收录页签 
	JPanel shouLuPanel;
	JLabel shouLuUrlLabel;
	JTextField shouLuUrlTextField;
	JButton shouLuUrlButton;
	JTable shouLuTable;*/

	public ToolsMenuWindow(JFrame mainWindow) {
		super(ComponentConst.TOOLS_WINDOW_TITLE);
		this.getContentPane().setLayout(null);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		// 添加窗口聚焦监听器
		// this.addWindowFocusListener(new WindowFocusListener(this));
		// 添加鼠标活动监听器
		this.addMouseListener(new WindowMouseListener(this));

		toolTabPanel.setBounds(0, 20, 800, 600);
		/* 加密解密页签 */
		decodeAndEncodePanel = new JPanel();
		decodeAndEncodePanel.setLayout(null);
		decodeAndEncodeInputArea = new AJTextArea(20, 20, 750, 170,
				ComponentConst.DECODE_AND_ENCODE_INPUT_BORDER_TEXT);
		decodeAndEncodeOutputArea = new AJTextArea(20, 210, 750, 170,
				ComponentConst.DECODE_AND_ENCODE_OUTPUT_BORDER_TEXT);
		encodeButton = new AJButton(ComponentConst.ENCODE_BUTTON_TEXT,
				ComponentConst.ENCODE_BUTTON_NAME, this, 330, 400, 60, 30);
		decodeButton = new AJButton(ComponentConst.DECODE_BUTTON_TEXT,
				ComponentConst.DECODE_BUTTON_NAME, this, 410, 400, 60, 30);
		addComponentsJpanel(decodeAndEncodePanel, decodeAndEncodeInputArea,
				decodeAndEncodeOutputArea, encodeButton);
		/*
		shouLuPanel = new JPanel();
		shouLuPanel.setLayout(null);
		shouLuUrlLabel = new AJLabel(ComponentConst.SHOULU_URL_LABEL_TEXT,
				Color.BLUE, 20, 20, 40, 30);
		shouLuUrlTextField = new AJTextField(null, 60, 20, 610, 30);
		shouLuUrlButton = new AJButton(ComponentConst.QUERY_BUTTON_TEXT,
				ComponentConst.SHOULU_URL_BUTTON_NAME, this, 690, 20, 80, 30);
		shouLuTable = new JTable(3, 4);
		shouLuTable.setRowHeight(140);
		shouLuTable.setBorder(BorderFactory.createEtchedBorder());
		shouLuTable.setBounds(20, 70, 750, 420);
		// 关闭表格列的自动调整功能。
		shouLuTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// 单选
		shouLuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shouLuTable.setSelectionBackground(Color.WHITE);
		shouLuTable.setSelectionForeground(Color.WHITE);
		addComponentsJpanel(shouLuPanel, shouLuUrlLabel, shouLuUrlTextField,
				shouLuUrlButton, shouLuTable);

		toolTabPanel.add(ComponentConst.SHOULU_TAB_TEXT, shouLuPanel);*/
		toolTabPanel.add(ComponentConst.DECODE_AND_ENCODE_TAB_TEXT,
				decodeAndEncodePanel);
		addComponents(toolTabPanel);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof JButton) {
			JButton button = (JButton) source;
			String buttonName = button.getName();
			/* 加密解密 */
			// 加密
			if (ComponentConst.ENCODE_BUTTON_NAME.equals(buttonName)) {
				String inputString = decodeAndEncodeInputArea.getText();
				if (inputString == null || "".equals(inputString.trim())) {
					JOptionPane.showMessageDialog(this, "请输入需要加密的文本");
					decodeAndEncodeInputArea.requestFocus();
					return;
				}
				String str1 = CodeUtil.myEncode(inputString);
				byte[] b1 = Base64.encodeBase64(str1.getBytes());
				decodeAndEncodeOutputArea.setText(new String(b1));
				return;
			}
			// 解密
			else if (ComponentConst.DECODE_BUTTON_NAME.equals(buttonName)) {
				String inputString = decodeAndEncodeInputArea.getText();
				if (inputString == null || "".equals(inputString.trim())) {
					JOptionPane.showMessageDialog(this, "请输入需要解密的文本");
					decodeAndEncodeInputArea.requestFocus();
					return;
				}
				try {
					byte[] b2 = Base64.decodeBase64(inputString.getBytes());
					String str = CodeUtil.myDecode(new String(b2));
					decodeAndEncodeOutputArea.setText(str);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, "输入的文本不符合规范，无法解密");
					decodeAndEncodeInputArea.selectAll();
					return;
				}
				return;
			}
		}
	}

	/**
	 * 添加组件
	 */
	private void addComponents(Component... components) {
		for (int i = 0; i < components.length; i++) {
			this.getContentPane().add(components[i]);
		}
	}

	/**
	 * 为panel组件添加其他子组件
	 * 
	 * @param panel
	 * @param components
	 */
	private void addComponentsJpanel(JPanel panel, Component... components) {
		for (int i = 0; i < components.length; i++) {
			panel.add(components[i]);
		}
	}

}
