package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 脚本测试窗口
 * @author dipoo
 * @since 2015-01-09
 */
public class TestScriptWindow extends JDialog{

	private static final long serialVersionUID = 1922141062996395003L;
	
	public TestScriptWindow(final String createScript, final String collectScript, final String downloadScript, final String searchScript, final Setting setting){
		this.setTitle("脚本测试");
		this.setSize(600, 460);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		JLabel urlLabel = new AJLabel("地址:", Color.BLUE, 10, 15, 40, 30);
		final JTextField urlField = new AJTextField("http://exhentai.org/g/757931/9db0ba4a6a", "", 60, 15, 445, 30);
		JLabel typeLabel = new AJLabel("输出类型:", Color.BLUE, 10, 55, 60, 30);
		JLabel crLabel = new AJLabel("任务", Color.BLACK, 100, 55, 25, 30);
		final JCheckBox createCb = new JCheckBox("", true);
		createCb.setBounds(130, 55, 30, 30);
		JLabel clLabel = new AJLabel("图片列表", Color.BLACK, 165, 55, 70, 30);
		final JCheckBox collectCb = new JCheckBox("", true);
		collectCb.setBounds(240, 55, 30, 30);
		JLabel doLabel = new AJLabel("真实地址", Color.BLACK, 280, 55, 70, 30);
		final JCheckBox downloadCb = new JCheckBox("", true);
		downloadCb.setBounds(360, 55, 30, 30);
		JLabel seLabel = new AJLabel("搜索列表", Color.BLACK, 400, 55, 70, 30);
		final JCheckBox searchCb = new JCheckBox("", true);
		searchCb.setBounds(420, 55, 30, 30);
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
		
		final JButton testBtn = new AJButton("测试脚本", "", "", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window window, MouseEvent e) {
				final String url = urlField.getText();
				if("".equals(url)){
					JOptionPane.showMessageDialog(null, "地址不能为空");
					return;
				}
				final Setting s = new Setting();
				s.setCreateTaskScriptPath(createScript);
				s.setCollectPictureScriptPath(collectScript);
				s.setDownloadScriptPath(downloadScript);
				s.setSearchScriptPath(searchScript);
				s.setCookieInfo(setting.getCookieInfo());
				resultArea.setText("");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new CommonSwingWorker(new Runnable() {
							public void run() {
								ScriptParser.testScript(url, resultArea, s, createCb.getSelectedObjects() != null,
										collectCb.getSelectedObjects() != null, downloadCb.getSelectedObjects() != null, 
										searchCb.getSelectedObjects() != null);
							}
						}).execute();
					}
				});
			}
		}), 515, 15, 60, 30);
		
		
		ComponentUtil.addComponents(getContentPane(), urlLabel, urlField, testBtn, typeLabel, crLabel, createCb, 
				clLabel, collectCb, doLabel, downloadCb, seLabel, searchCb, consolePane);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				TestScriptWindow w = (TestScriptWindow)e.getSource();
				w.dispose();
			}
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				TestScriptWindow w = (TestScriptWindow)e.getSource();
				w.dispose();
			}
		});
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
