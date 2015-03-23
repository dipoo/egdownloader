package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextArea;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.FileUtil;
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
		/* 基本配置页签 */
		JPanel basicPanel;
		JLabel saveDirLabel;
		public JTextField saveDirField;
		JLabel saveAsNameLabel;
		public JCheckBox saveAsNameBox;
		JLabel autoDownloadLabel;
		public JCheckBox autoDownloadBox;
		JLabel maxThreadLabel;
		public JTextField maxThreadField;
		JLabel loginUrlLabel;
		public JTextField loginUrlField;
		JLabel cookieLabel;
		public JTextArea cookieArea;
		JButton cookieButton;
		
		JButton save_Btn;
		
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
		public JButton testBtn;
		public JTextPane scriptDocPanel;
		
		Color labelColor = new Color(65,145,65);
		Color bgColor = new Color(210,225,240);

		public SettingWindow(JFrame mainWindow) {
			super("配置");
			final Setting setting = ((EgDownloaderWindow)mainWindow).setting;
			this.setIconImage(new ImageIcon(getClass().getResource(
					ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM
					+ ComponentConst.SKIN_ICON.get("setting"))).getImage());
			this.getContentPane().setLayout(null);
			this.setSize(800, 480);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			
			settingTabPanel.setBounds(20, 0, 780, 450);
			
			/* 基本配置 */
			basicPanel = new JPanel();
			basicPanel.setLayout(null);  
			saveDirLabel = new AJLabel("保存目录：", labelColor, 25, 30, 100, 30);
			saveDirField = new AJTextField(setting.getDefaultSaveDir(), "", 125, 30, 360, 30);
			saveDirField.setEditable(false);
			saveDirField.setEnabled(false);
			
			final SettingWindow this_ = this;
			
			JButton openDirButton = new AJButton("打开", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("folder"), new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					try {
						String path = ComponentConst.getSavePathPreffix() + saveDirField.getText();
						File f = new File(path);
						FileUtil.ifNotExistsThenCreate(f);
						Desktop.getDesktop().open(f);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(this_, "文件夹已被删除");
					}
				}
			}), 500, 30, 60, 30);
			saveAsNameLabel = new AJLabel("以真实名称保存：", labelColor, 25, 70, 100, 30);
			saveAsNameBox = new JCheckBox("", setting.isSaveAsName());
			saveAsNameBox.setBounds(120, 70, 30, 30);
			autoDownloadLabel = new AJLabel("创建后自动下载：", labelColor, 360, 70, 100, 30);
			autoDownloadBox = new JCheckBox("", setting.isAutoDownload());
			autoDownloadBox.setBounds(460, 70, 30, 30);
			maxThreadLabel = new AJLabel("最多开启任务数：", labelColor, 25, 110, 100, 30);
			maxThreadField = new AJTextField(setting.getMaxThread() + "", "", 125, 110, 100, 30);
			loginUrlLabel = new AJLabel("登录地址：", labelColor, 25, 150, 100, 30);
			loginUrlField = new AJTextField(setting.getLoginUrl(), "", 125, 150, 360, 30);
			cookieLabel = new AJLabel("登录信息：", labelColor, 25, 190, 100, 30);
			cookieArea = new AJTextArea();
			cookieArea.setText(setting.getCookieInfo());
			cookieArea.setBounds(125, 190, 360, 150);
			cookieArea.setLineWrap(true);
			cookieArea.setBorder(BorderFactory.createEtchedBorder());
			cookieButton = new AJButton("登陆", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
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
			}), 500, 250, 60, 30);
		    addComponentsJpanel(basicPanel, saveDirLabel, saveDirField, openDirButton,
				saveAsNameLabel, saveAsNameBox, autoDownloadLabel,autoDownloadBox, maxThreadLabel, maxThreadField,
				loginUrlLabel, loginUrlField, cookieLabel, cookieArea,
				cookieButton);
		    
			/*脚本设置*/
			scriptPanel = new JPanel();
			scriptPanel.setLayout(null);
			createJsLabel = new AJLabel("创建任务脚本：", labelColor, 25, 30, 100, 30);
			createJsField = new AJTextField(setting.getCreateTaskScriptPath(), "", 125, 30, 360, 30);
			collectJsLabel = new AJLabel("收集图片脚本：", labelColor, 25, 70, 100, 30);
			collectJsField = new AJTextField(setting.getCollectPictureScriptPath(), "", 125, 70, 360, 30);
			downloadJsLabel = new AJLabel("下载任务脚本：", labelColor, 25, 110, 100, 30);
			downloadJsField = new AJTextField(setting.getDownloadScriptPath(), "", 125, 110, 360, 30);
			searchJsLabel = new AJLabel("搜索漫画脚本：", labelColor, 25, 150, 100, 30);
			searchJsField = new AJTextField(setting.getSearchScriptPath(), "", 125, 150, 360, 30);
			testBtn = new AJButton("脚本测试", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					String createScriptPath = settingWindow.createJsField.getText();
					String collectScriptPath = settingWindow.collectJsField.getText();
					String downloadScriptPath = settingWindow.downloadJsField.getText();
					
					if("".equals(createScriptPath.trim()) || "".equals(collectScriptPath.trim())
							|| "".equals(downloadScriptPath.trim())){
						JOptionPane.showMessageDialog(this_, "请填写完所有脚本路径！");
						return;
					}else{
						if(testScriptWindow == null){
							testScriptWindow = new TestScriptWindow(createScriptPath, collectScriptPath, downloadScriptPath, setting);
						}
						testScriptWindow.setVisible(true);
					}
				}
			}), 550, 90, 60, 30);
			scriptDocPanel = new AJTextPane(ComponentConst.SCRIPT_DESC_TEXT, labelColor);//Field(ComponentConst.SCRIPT_DESC_TEXT, "", 25, 200, 400, 100);
			scriptDocPanel.setBounds(20, 200, 650, 200);
			
			/*openScriptBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					boolean selected = openScriptBox.getSelectedObjects() == null ? false : true;
					showPathSettingPanel(selected);
				}
			});*/
			addComponentsJpanel(scriptPanel, createJsLabel,
					createJsField, collectJsLabel, collectJsField, downloadJsLabel, downloadJsField, searchJsLabel, searchJsField, testBtn, scriptDocPanel);
			
			settingTabPanel.add("基本配置", basicPanel);
			settingTabPanel.add("脚本配置", scriptPanel);
			
			
			
			save_Btn = new AJButton("保存", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("save"), new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					Setting setting = mainWindow.setting;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					int index = settingWindow.settingTabPanel.getSelectedIndex();
					//基本设置
					if(index == 0){
						String saveDir = settingWindow.saveDirField.getText();
						String maxThread = settingWindow.maxThreadField.getText();
						String loginUrl = settingWindow.loginUrlField.getText();
						boolean saveAsName = settingWindow.saveAsNameBox.getSelectedObjects() == null ? false : true;//是否选择了
						boolean autoDownload = settingWindow.autoDownloadBox.getSelectedObjects() == null ? false : true;
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
						}else if("".equals(loginUrl)){
							JOptionPane.showMessageDialog(this_, "请填写登录地址");
							return;
						}else{
							if("".equals(cookieInfo)){
								int result = JOptionPane.showConfirmDialog(this_, "登陆信息cookie不存在，确认要保存吗？");
								if(result != 0){//不保存
									return;
								}
							}
							setting.setDefaultSaveDir(saveDir);
							setting.setSaveAsName(saveAsName);
							setting.setAutoDownload(autoDownload);
							setting.setMaxThread(Integer.parseInt(maxThread));
							setting.setLoginUrl(loginUrl);
							setting.setCookieInfo(cookieInfo);
							mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
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
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(this_, "保存成功");
					}
				}
			}), 32, 250, 60, 30);
			
			
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

