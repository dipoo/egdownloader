package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 代理测试窗口
 * @author dipoo
 * @since 2016-03-23
 */
public class TestProxyWindow extends JDialog{

	private static final long serialVersionUID = 1922141062996395003L;
	
	public TestProxyWindow(final Setting setting){
		this.setTitle("代理测试");
		this.setSize(600, 460);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		JLabel urlLabel = new AJLabel("测试地址:", Color.BLUE, 10, 15, 60, 30);
		final JTextField urlField = new AJTextField("http://1212.ip138.com/ic.asp", "", 70, 15, 435, 30);
		JLabel typeLabel = new AJLabel("测试类型:", Color.BLUE, 10, 55, 60, 30);
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton rb1 = new JRadioButton("内置", true);
		rb1.setBounds(125, 55, 100, 30);
		final JRadioButton rb2 = new JRadioButton("HttpClient");
		rb2.setBounds(225, 55, 100, 30);
		buttonGroup.add(rb1);
		buttonGroup.add(rb2);
		final JTextArea resultArea = new AJTextArea();
		resultArea.setEditable(false);
		resultArea.setAutoscrolls(true);
		resultArea.setLineWrap(true);
		resultArea.setBorder(null);
		resultArea.setFont(new Font("宋体", Font.PLAIN, 12));
		resultArea.setForeground(new Color(63,127,95));
		
		JScrollPane consolePane = new JScrollPane(resultArea);
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(219,219,219)), "测试结果");
		consolePane.setBounds(10, 100, 568, 310);
		consolePane.setAutoscrolls(true);
		consolePane.setBorder(border);
		
		final JButton testBtn = new AJButton("执行测试", "", "", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window window, MouseEvent e) {
				String url = urlField.getText();
				if("".equals(url)){
					JOptionPane.showMessageDialog(null, "地址不能为空");
					return;
				}
				resultArea.setText("");
				String result = null;
				try{
					if(rb1.isSelected()){
						result = WebClient.getRequestUseJava(url, "gb2312");
					}
					if(rb2.isSelected()){
						result = WebClient.postRequest(url, "gb2312");
					}
					resultArea.setText(result);
				}catch(Exception e1){
					resultArea.setText(e1.getMessage());
				}
			}
		}), 515, 15, 60, 30);
		
		
		ComponentUtil.addComponents(getContentPane(), urlLabel, urlField, testBtn, typeLabel, rb1, rb2, consolePane);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				TestProxyWindow w = (TestProxyWindow)e.getSource();
				w.dispose();
			}
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				TestProxyWindow w = (TestProxyWindow)e.getSource();
				w.dispose();
			}
		});
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
