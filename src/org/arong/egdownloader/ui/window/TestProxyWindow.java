package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.spider.Spider;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 代理测试窗口
 * @author dipoo
 * @since 2016-03-23
 */
public class TestProxyWindow extends JDialog{

	private static final long serialVersionUID = 1922141062996395003L;
	/**
	 * http://ip-api.com/json
	 * https://ip.seeip.org/geoip
	 * https://ip.nf/me.json
	 * @param setting
	 */
	public TestProxyWindow(final Setting setting){
		this.setTitle("代理测试");
		this.setSize(600, 460);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		JLabel urlLabel = new AJLabel("测试地址:", Color.BLUE, 10, 15, 60, 30);
		final JTextField urlField = new AJTextField("http://ip-api.com/json", "", 70, 15, 435, 30);//http://www.ip111.cn/
		JLabel typeLabel = new AJLabel("编码类型:", Color.BLUE, 10, 55, 60, 30);
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton rb1 = new JRadioButton("UTF-8", true);
		rb1.setBounds(80, 55, 100, 30);
		final JRadioButton rb2 = new JRadioButton("GB2312");
		rb2.setBounds(200, 55, 100, 30);
		buttonGroup.add(rb1);
		buttonGroup.add(rb2);
		final AJLabel spendtime = new AJLabel("", Color.BLUE);
		spendtime.setBounds(350, 55, 200, 30);
		final JTextPane resultArea = new JTextPane();
		final EditorKit editorKit = new HTMLEditorKit();
		final EditorKit noeditorKit = new StyledEditorKit();
		//resultArea.setEditable(false);
		resultArea.setAutoscrolls(true);
		resultArea.setBorder(null);
		
		JScrollPane consolePane = new JScrollPane();
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(219,219,219)), "测试结果");
		consolePane.setBounds(10, 100, 568, 310);
		consolePane.setAutoscrolls(true);
		consolePane.setBorder(border);
		consolePane.setViewportView(resultArea);
		
		final JButton testBtn = new AJButton("执行测试", "", "", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window window, MouseEvent e) {
				final JButton source = (JButton) e.getSource();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new CommonSwingWorker(new Runnable() {
							public void run() {
								source.setEnabled(false);
								long t = System.currentTimeMillis();
								String url = urlField.getText();
								if("".equals(url)){
									JOptionPane.showMessageDialog(null, "地址不能为空");
									return;
								}
								String result = null;
								try{
									result = WebClient.getRequestUseJavaWithCookie(url, rb1.isSelected() ? rb1.getText() : rb2.getText(), null, 10 * 1000); 
									if(StringUtils.isNotBlank(result)){
										boolean html = true;String source = result;
										if(result.toLowerCase().contains("<body>")){
											source = Spider.getTextFromSource(result.toLowerCase(), "<body>", "</body>");
											source = filterImg(source);
										}else if(result.toLowerCase().contains("<body")){
											source = Spider.getTextFromSource(result.toLowerCase(), "<body", "</body>");
											source = Spider.substring(source, ">");
											source = filterImg(source);
										}else{
											html = false;
										}
										
										if(html){
											resultArea.setEditorKit(editorKit);
										}else{
											resultArea.setEditorKit(noeditorKit);
										}
										resultArea.setText(source);
									}else{
										resultArea.setText("[空]");
									}
								}catch(Exception e1){
									e1.printStackTrace();
									resultArea.setText(e1.getMessage());
								}finally{
									spendtime.setText("耗时：" + (System.currentTimeMillis() - t) + "ms");
									source.setEnabled(true);
									resultArea.select(resultArea.getDocument().getLength(), resultArea.getDocument().getLength());
								}
							}
						}).execute();
					}
				});
			}
		}), 515, 15, 60, 30);
		
		
		ComponentUtil.addComponents(getContentPane(), urlLabel, urlField, testBtn, typeLabel, rb1, rb2, spendtime, consolePane);
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
	
	public String filterImg(String str) {
		String reg = "<img[^>]*>";
		Pattern pat = Pattern.compile(reg);
		Matcher mat = pat.matcher(str);
		String s = mat.replaceAll("").trim();
		return s;
	}
}
