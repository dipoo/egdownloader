package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.spider.Proxy;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.work.UpdateScriptWorker;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.FileUtil2;
/**
 * 配置窗口
 * @author 阿荣
 * @since 2014-06-10
 */
public class SettingWindow extends JFrame{
	
		private static final long serialVersionUID = -2290486210441887526L;

		JTabbedPane settingTabPanel = new JTabbedPane(JTabbedPane.LEFT);
		public LoginWindow loginWindow;
		public TestScriptWindow testScriptWindow;
		public TestProxyWindow testProxyWindow;
		/* 基本配置页签 */
		JPanel basicPanel;
		JLabel saveDirLabel;
		public JTextField saveDirField;
		private JFileChooser saveDirChooser;
		JButton browseDirButton;
		JButton openDirButton;
		JLabel saveAsNameLabel;
		public JCheckBox saveAsNameBox;
		JLabel autoDownloadLabel;
		public JCheckBox autoDownloadBox;
		JLabel downloadOriginalLabel;
		public JCheckBox downloadOriginalBox;
		JLabel showAsSubnameLabel;
		public JCheckBox showAsSubnameBox;
		JLabel tagTranslateLabel;
		public JCheckBox tagTranslateBox;
		JLabel maxThreadLabel;
		public JCheckBox saveDirAsSubnameBox;
		JLabel saveDirAsSubnameLabel;
		public JTextField maxThreadField;
		JLabel loginUrlLabel;
		public JTextField loginUrlField;
		JLabel cookieLabel;
		public JTextArea cookieArea;
		JButton cookieButton;
		
		JButton save_Btn;
		boolean click_save_Btn;
		
		/* 脚本设置 */
		JPanel scriptPanel;
		public JLabel createJsLabel;
		public JTextField createJsField;
		public JLabel collectJsLabel;
		public JTextField collectJsField;
		public JLabel downloadJsLabel;
		public JTextField downloadJsField;
		public JLabel searchJsLabel;
		public JTextField searchJsField;
		public JButton openScriptDirBtn;
		public JButton testBtn;
		public JButton updateBtn;
		public JLabel loadingLabel;
		public JTextPane scriptDocPanel;
		
		/* 代理设置 */
		JPanel proxyPanel;
		public JLabel proxyLabel;
		public ButtonGroup proxyButtonGroup;
		public JRadioButton noRadioButton;
		public JRadioButton yesRadioButton;
		public JLabel proxyTypeLabel;
		public ButtonGroup proxyTypeButtonGroup;
		public JRadioButton httpRadioButton;
		public JRadioButton socksRadioButton;
		public JRadioButton ieRadioButton;
		public JLabel proxyIpLabel;
		public JTextField proxyIpField;
		public JLabel proxyPortLabel;
		public JTextField proxyPortField;
		public JLabel proxyUsernameLabel;
		public JTextField proxyUsernameField;
		public JLabel proxyPwdLabel;
		public JPasswordField  proxyPwdField;
		public JButton proxyTestBtn;
		/*private JLabel proxyTipLabel;*/
		
		Color labelColor = new Color(65,145,65);
		Color bgColor = new Color(210,225,240);
		
		

		public void dispose() {
			super.dispose();
			if(loginWindow != null && loginWindow.isVisible()){
				loginWindow.dispose();
			}
			if(testScriptWindow != null && testScriptWindow.isVisible()){
				testScriptWindow.dispose();
			}
		}

		public SettingWindow(JFrame mainWindow) {
			super("配置");
			final Setting setting = ((EgDownloaderWindow)mainWindow).setting;
			this.setIconImage(IconManager.getIcon("setting").getImage());
			this.getContentPane().setLayout(null);
			this.setSize(800, 480);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			
			settingTabPanel.setBounds(20, 0, 780, 450);
			
			/* 基本配置 */
			basicPanel = new JPanel();
			basicPanel.setLayout(null);
			saveDirChooser = new JFileChooser("/");
			saveDirChooser.setDialogTitle("选择保存目录");//选择框标题
			saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
			saveDirLabel = new AJLabel("保存目录：", labelColor, 25, 30, 100, 30);
			saveDirField = new AJTextField(setting.getDefaultSaveDir(), "", 125, 30, 360, 30);
			saveDirField.setEditable(false);
			saveDirField.setEnabled(false);
			
			final SettingWindow this_ = this;
			browseDirButton = new AJButton("浏览", IconManager.getIcon("select"), new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					int result = this_.saveDirChooser.showOpenDialog(this_);
					File file = null;  
	                if(result == JFileChooser.APPROVE_OPTION) {  
	                    file = this_.saveDirChooser.getSelectedFile();  
	                    if(!file.isDirectory()) {  
	                        JOptionPane.showMessageDialog(this_, "你选择的目录不存在");
	                        return ;
	                    }  
	                    String path = file.getAbsolutePath();
	                    this_.saveDirField.setText(path);
	                }
				}
			}), 500, 30, 60, 30);
			openDirButton = new AJButton("打开", IconManager.getIcon("folder"), new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					try {
						String path = saveDirField.getText();
						File f = new File(path);
						FileUtil2.ifNotExistsThenCreate(f);
						Desktop.getDesktop().open(f);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(this_, "文件夹已被删除");
					}
				}
			}), 570, 30, 60, 30);
			saveAsNameLabel = new AJLabel("以真实名称保存：", labelColor, 25, 70, 100, 30);
			saveAsNameBox = new JCheckBox("", setting.isSaveAsName());
			saveAsNameBox.setBounds(118, 70, 30, 30);
			autoDownloadLabel = new AJLabel("创建后自动下载：", labelColor, 200, 70, 100, 30);
			autoDownloadBox = new JCheckBox("", setting.isAutoDownload());
			autoDownloadBox.setBounds(290, 70, 30, 30);
			downloadOriginalLabel = new AJLabel("下载原图：", labelColor, 400, 70, 100, 30);
			downloadOriginalBox = new JCheckBox("", setting.isDownloadOriginal());
			downloadOriginalBox.setBounds(460, 70, 30, 30);
			saveDirAsSubnameLabel = new AJLabel("子标题作为目录：", labelColor, 25, 110, 100, 30);
			saveDirAsSubnameBox = new JCheckBox("", setting.isSaveDirAsSubname());
			saveDirAsSubnameBox.setBounds(118, 110, 30, 30);
			showAsSubnameLabel = new AJLabel("列表子标题展示：", labelColor, 200, 110, 100, 30);
			showAsSubnameBox = new JCheckBox("", setting.isShowAsSubname());
			showAsSubnameBox.setBounds(290, 110, 30, 30);
			tagTranslateLabel = new AJLabel("标签汉化：", labelColor, 400, 110, 100, 30);
			tagTranslateBox = new JCheckBox("", setting.isTagsTranslate());
			tagTranslateBox.setBounds(460, 110, 30, 30);
			
			maxThreadLabel = new AJLabel("最多开启任务数：", labelColor, 25, 150, 100, 30);
			maxThreadField = new AJTextField(setting.getMaxThread() + "", "", 125, 150, 60, 30);
			/*loginUrlLabel = new AJLabel("登录地址：", labelColor, 25, 150, 100, 30);
			loginUrlField = new AJTextField(setting.getLoginUrl(), "", 125, 150, 360, 30);*/
			cookieLabel = new AJLabel("Cookie：", labelColor, 25, 190, 100, 30);
			cookieLabel.setToolTipText("用于用户认证");
			cookieArea = new AJTextArea();
			cookieArea.setText(setting.getCookieInfo());
			cookieArea.setBounds(125, 190, 360, 150);
			cookieArea.setLineWrap(true);
			cookieArea.setEditable(true);
			cookieArea.setBorder(BorderFactory.createEtchedBorder());
			
			/*cookieButton = new AJButton("登陆", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					if(settingWindow.loginWindow == null){
						settingWindow.loginWindow = new LoginWindow(mainWindow);
						settingWindow.loginWindow.setVisible(true);
					}else{
						settingWindow.loginWindow.setVisible(true);
					}
				}
			}), 500, 250, 60, 30);*/
			JButton resumeButton = new AJButton("还原", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					settingWindow.cookieArea.setText(new Setting().getCookieInfo());
				}
			}), 500, 250, 60, 30);
		    addComponentsJpanel(basicPanel, saveDirLabel, saveDirField, browseDirButton, openDirButton,
				saveAsNameLabel, saveAsNameBox, tagTranslateLabel, tagTranslateBox, autoDownloadLabel, autoDownloadBox, downloadOriginalLabel,
				downloadOriginalBox, showAsSubnameLabel, showAsSubnameBox, maxThreadLabel, maxThreadField,
				saveDirAsSubnameLabel,saveDirAsSubnameBox,
				/*loginUrlLabel, loginUrlField,*/ cookieLabel, cookieArea,
				/*cookieButton, */resumeButton);
		    
			/*脚本设置*/
			scriptPanel = new JPanel();
			scriptPanel.setLayout(null);
			createJsLabel = new AJLabel("创建任务脚本：", labelColor, 25, 30, 100, 30);
			createJsField = new AJTextField(setting.getCreateTaskScriptPath(), "", 125, 30, 360, 30);
			createJsField.setEditable(false);
			collectJsLabel = new AJLabel("收集图片脚本：", labelColor, 25, 70, 100, 30);
			collectJsField = new AJTextField(setting.getCollectPictureScriptPath(), "", 125, 70, 360, 30);
			collectJsField.setEditable(false);
			downloadJsLabel = new AJLabel("下载任务脚本：", labelColor, 25, 110, 100, 30);
			downloadJsField = new AJTextField(setting.getDownloadScriptPath(), "", 125, 110, 360, 30);
			downloadJsField.setEditable(false);
			searchJsLabel = new AJLabel("搜索漫画脚本：", labelColor, 25, 150, 100, 30);
			searchJsField = new AJTextField(setting.getSearchScriptPath(), "", 125, 150, 360, 30);
			searchJsField.setEditable(false);
			openScriptDirBtn = new AJButton("脚本目录", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					try {
						String path = "script";
						File f = new File(path);
						FileUtil2.ifNotExistsThenCreate(f);
						Desktop.getDesktop().open(f);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(this_, "文件夹已被删除");
					}
				}
			}), 500, 50, 60, 30);
			
			testBtn = new AJButton("脚本测试", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					String createScriptPath = settingWindow.createJsField.getText();
					String collectScriptPath = settingWindow.collectJsField.getText();
					String downloadScriptPath = settingWindow.downloadJsField.getText();
					String searchScriptPath = settingWindow.searchJsField.getText();
					
					if("".equals(createScriptPath.trim()) || "".equals(collectScriptPath.trim())
							|| "".equals(downloadScriptPath.trim())){
						JOptionPane.showMessageDialog(this_, "请填写完所有脚本路径！");
						return;
					}else{
						if(testScriptWindow == null){
							testScriptWindow = new TestScriptWindow(createScriptPath, collectScriptPath, downloadScriptPath, searchScriptPath, setting);
						}
						testScriptWindow.setVisible(true);
					}
				}
			}), 500, 90, 60, 30);
			updateBtn = new AJButton("同步脚本", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					if(ComponentConst.scriptChange){
						EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
						if(mainWindow.setting.getUpdateScriptWorker() == null){
							mainWindow.setting.setUpdateScriptWorker(new UpdateScriptWorker(mainWindow));
						}
						updateBtn.setEnabled(false);
						mainWindow.setting.getUpdateScriptWorker().execute();
					}else{
						JOptionPane.showMessageDialog(this_, "远程脚本未更新或者更新不可用！");
					}
				}
			}), 500, 130, 60, 30);
			loadingLabel = new AJLabel("", "loading.gif", null, JLabel.LEFT);//
			loadingLabel.setBounds(620, 110, 30, 30);
			loadingLabel.setVisible(false);
			scriptDocPanel = new AJTextPane(ComponentConst.SCRIPT_DESC_TEXT, labelColor);
			scriptDocPanel.setBounds(20, 200, 650, 250);
			
			addComponentsJpanel(scriptPanel, createJsLabel,
					createJsField, collectJsLabel, collectJsField, downloadJsLabel, downloadJsField, searchJsLabel, searchJsField, openScriptDirBtn, testBtn, updateBtn, loadingLabel, scriptDocPanel);
			
			/* 代理配置 */
			proxyPanel = new JPanel();
			proxyPanel.setLayout(null);
			proxyButtonGroup = new ButtonGroup();
			proxyLabel = new AJLabel("是否使用代理：", labelColor, 25, 30, 100, 30);
			noRadioButton = new JRadioButton("不使用", !setting.isUseProxy());
			noRadioButton.setBounds(125, 30, 80, 30);
			yesRadioButton = new JRadioButton("使用", setting.isUseProxy());
			yesRadioButton.setBounds(230, 30, 80, 30);
			proxyButtonGroup.add(noRadioButton);
			proxyButtonGroup.add(yesRadioButton);
			proxyTypeButtonGroup = new ButtonGroup();
			proxyTypeLabel = new AJLabel("代理方式：", labelColor, 25, 70, 100, 30);
			httpRadioButton = new JRadioButton("HTTP(S)", "http".equals(setting.getProxyType()));
			httpRadioButton.setBounds(125, 70, 100, 30);
			proxyTypeButtonGroup.add(httpRadioButton);
			socksRadioButton = new JRadioButton("SOCKS", "socks".equals(setting.getProxyType()));
			socksRadioButton.setBounds(230, 70, 80, 30);
			proxyTypeButtonGroup.add(socksRadioButton);
			ieRadioButton = new JRadioButton("使用IE配置", "ie".equals(setting.getProxyType()));
			ieRadioButton.setBounds(315, 70, 120, 30);
			proxyTypeButtonGroup.add(ieRadioButton);
			proxyIpLabel = new AJLabel("服务器：", labelColor, 25, 110, 100, 30);
			proxyIpField = new AJTextField(setting.getProxyIp(), "", 130, 110, 200, 30);
			proxyPortLabel = new AJLabel("端口：", labelColor, 350, 110, 100, 30);
			proxyPortField = new AJTextField(setting.getProxyPort(), "", 405, 110, 80, 30);
			proxyUsernameLabel = new AJLabel("用户名：", labelColor, 25, 150, 100, 30);
			proxyUsernameField = new AJTextField(setting.getProxyUsername(), "", 130, 150, 200, 30);
			proxyPwdLabel = new AJLabel("密码：", labelColor, 25, 190, 100, 30);
			proxyPwdField = new JPasswordField(setting.getProxyPwd());
			proxyPwdField.setBounds(130, 190, 200, 30);
			proxyTestBtn = new AJButton("测试", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					if(testProxyWindow == null){
						testProxyWindow = new TestProxyWindow(setting);
					}
					//点击保存
					click_save_Btn = true;
					for(MouseListener ml : save_Btn.getMouseListeners()){
						ml.mouseClicked(new MouseEvent(save_Btn, 1, System.currentTimeMillis(), 1, 1, 1, 1, false, 1));
					}
					
					testProxyWindow.setVisible(true);
				}
			}), 125, 230, 60, 30);
			//proxyTipLabel =  new AJLabel("提示：测试前请先保存当前配置", Color.BLUE, 200, 230, 300, 30);
			
			addComponentsJpanel(proxyPanel, proxyLabel, noRadioButton, yesRadioButton, proxyTypeLabel, proxyIpLabel, httpRadioButton,
					socksRadioButton/*, ieRadioButton*/, proxyIpField, proxyPortLabel, proxyPortField,
					proxyUsernameLabel, proxyUsernameField, proxyPwdLabel, proxyPwdField, proxyTestBtn/*, proxyTipLabel*/);
			
			settingTabPanel.add("基本配置", basicPanel);
			settingTabPanel.add("脚本配置", scriptPanel);
			settingTabPanel.add("代理配置", proxyPanel);
			
			
			
			
			save_Btn = new AJButton("保存", IconManager.getIcon("save"), new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					Setting setting = mainWindow.setting;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					int index = settingWindow.settingTabPanel.getSelectedIndex();
					//基本设置
					if(index == 0){
						String saveDir = settingWindow.saveDirField.getText();
						String maxThread = settingWindow.maxThreadField.getText();
						/*String loginUrl = settingWindow.loginUrlField.getText();*/
						boolean saveAsName = settingWindow.saveAsNameBox.getSelectedObjects() == null ? false : true;//是否选择了
						boolean autoDownload = settingWindow.autoDownloadBox.getSelectedObjects() == null ? false : true;
						boolean downloadOriginal = settingWindow.downloadOriginalBox.getSelectedObjects() == null ? false : true;
						boolean saveDirAsSubname = saveDirAsSubnameBox.isSelected();
						boolean showAsSubname = showAsSubnameBox.isSelected();
						boolean tagTranslate = tagTranslateBox.isSelected();
						String cookieInfo = settingWindow.cookieArea.getText();
						Pattern p = Pattern.compile("[0-9]");
						if("".equals(saveDir)){
							JOptionPane.showMessageDialog(this_, "请填写保存目录");
							return;
						}else if("".equals(maxThread)){
							JOptionPane.showMessageDialog(this_, "请填写最多开启任务数");
							return;
						}else if(!p.matcher(maxThread).matches()){
							JOptionPane.showMessageDialog(this_, "最多开启任务数必须填写数字,或不能大于10");
							return;
						}/*else if("".equals(loginUrl)){
							JOptionPane.showMessageDialog(this_, "请填写登录地址");
							return;
						}*/else{
							if("".equals(cookieInfo)){
								int result = JOptionPane.showConfirmDialog(this_, "登陆信息cookie不存在，确认要保存吗？");
								if(result != JOptionPane.OK_OPTION){//不保存
									return;
								}
							}
							setting.setDefaultSaveDir(saveDir);
							setting.setSaveAsName(saveAsName);
							setting.setAutoDownload(autoDownload);
							setting.setDownloadOriginal(downloadOriginal);
							setting.setSaveDirAsSubname(saveDirAsSubname);
							setting.setShowAsSubname(showAsSubname);
							setting.setTagsTranslate(tagTranslate);
							setting.setMaxThread(Integer.parseInt(maxThread));
							/*setting.setLoginUrl(loginUrl);*/
							setting.setCookieInfo(cookieInfo);
							mainWindow.settingDbTemplate.update(setting);//保存
							JOptionPane.showMessageDialog(this_, "保存成功");
						}
					}
					//脚本设置
					else if(index == 1){
						String createScriptPath = settingWindow.createJsField.getText();
						String collectScriptPath = settingWindow.collectJsField.getText();
						String downloadScriptPath = settingWindow.downloadJsField.getText();
						String searchScriptPath = settingWindow.searchJsField.getText();
						
						setting.setCreateTaskScriptPath(createScriptPath);
						setting.setCollectPictureScriptPath(collectScriptPath);
						setting.setDownloadScriptPath(downloadScriptPath);
						setting.setSearchScriptPath(searchScriptPath);
						mainWindow.settingDbTemplate.update(setting);//保存
						JOptionPane.showMessageDialog(this_, "保存成功");
					}
					//代理设置
					else if(index == 2){
						boolean useProxy = settingWindow.yesRadioButton.isSelected() ? true : false;
						String proxyType = "http";
						if(socksRadioButton.isSelected()){
							proxyType = "socks";
						}else if(ieRadioButton.isSelected()){
							proxyType = "ie";
						}
						String proxyIp = settingWindow.proxyIpField.getText();
						String proxyPort = settingWindow.proxyPortField.getText();
						String proxyUsername = settingWindow.proxyUsernameField.getText();
						String proxyPwd = String.valueOf(settingWindow.proxyPwdField.getPassword());
						
						setting.setUseProxy(useProxy);
						setting.setProxyType(proxyType);
						setting.setProxyIp(proxyIp);
						setting.setProxyPort(proxyPort);
						setting.setProxyUsername(proxyUsername);
						setting.setProxyPwd(proxyPwd);
						
						Proxy.init(useProxy, proxyType, proxyIp, proxyPort, proxyUsername, proxyPwd);
						mainWindow.settingDbTemplate.update(setting);//保存
						if(! click_save_Btn){
							JOptionPane.showMessageDialog(this_, "保存成功");
						}else{
							click_save_Btn = false;
						}	
					}
				}
			}), 32, 200, 60, 30);
			
			
			this.getContentPane().add(save_Btn, -1);
			addComponents(settingTabPanel);
			
			this.setVisible(true);
			
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					SettingWindow this_ = (SettingWindow) e.getSource();
					this_.dispose();
				}
			});
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

