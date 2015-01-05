package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.Tracker;
/**
 * 登录窗口
 * @author 阿荣
 * @since 2014-06-27
 */
public class LoginWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	public JTextField userTextField;
	JPasswordField pwdPasswordField;
	
	public LoginWindow(EgDownloaderWindow mainWindow){
		this.setTitle("登录");
		this.setSize(300, 200);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		JLabel userLabel = new AJLabel("用户名：", Color.BLUE, 10, 20, 60, 30);
		userTextField = new AJTextField("", 60, 20, 200, 30);
		JLabel pwdLabel = new AJLabel("密   码：", Color.BLUE, 10, 60, 60, 30);
		pwdPasswordField = new JPasswordField();
		pwdPasswordField.setBounds(60, 60, 200, 30);
		JButton loginButton = new AJButton("登录", "", null, new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window window, MouseEvent e) {
				EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
				SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
				String user = settingWindow.loginWindow.userTextField.getText();
				String pwd = String.valueOf(settingWindow.loginWindow.pwdPasswordField.getPassword());
				if("".equals(user)){
					JOptionPane.showMessageDialog(null, "请填写用户名");
					return;
				}else if("".equals(pwd)){
					JOptionPane.showMessageDialog(null, "请填写密码");
					return;
				}else{
					try {
						if("".equals(settingWindow.loginUrlField.getText())){
							JOptionPane.showMessageDialog(null, "请填写登录地址");
							return;
						}
						Map<String, String> params = new HashMap<String, String>();
						params.put("referer", "http://forums.e-hentai.org/index.php?");
						params.put("b", "");
						params.put("bt", "");
						params.put("UserName", user);
						params.put("PassWord", pwd);//
						params.put("CookieDate", "1");
//						String cookieInfo = WebClient.getCookieByPostWithoutCookie(settingWindow.loginUrlField.getText(),
//								"UTF-8", params); 
						String url = WebClient.getUrlStr(settingWindow.loginUrlField.getText() + "&", params);
//						System.out.println(url);
						String cookieInfo = WebClient.getCookieUseJava(url, "UTF-8");
						if("".equals(cookieInfo)){
							JOptionPane.showMessageDialog(null, "登录失败");
							return;
						}else{
							settingWindow.cookieArea.setText(cookieInfo);
							settingWindow.loginWindow.dispose();
							settingWindow.setVisible(true);
						}
					} catch (Exception e1) {
						Tracker.println(LoginWindow.class, e1.getMessage());
						JOptionPane.showMessageDialog(null, "登录失败");
						return;
					}
				}
			}
		}), 120, 100, 60, 30);
		ComponentUtil.addComponents(getContentPane(), userLabel, userTextField, pwdLabel,
				pwdPasswordField, loginButton);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				LoginWindow this_ = (LoginWindow) e.getSource();
				this_.dispose();
			}
		});
	}
	
}
