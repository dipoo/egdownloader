package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJPanel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 配置窗口
 * @author 阿荣
 * @since 2014-06-10
 */
public class SettingWindow extends JFrame{
	
		private static final long serialVersionUID = -2290486210441887526L;

		JTabbedPane settingTabPanel = new JTabbedPane(JTabbedPane.TOP);
		public LoginWindow loginWindow;
		/* 基本配置页签 */
		JPanel basicPanel;
		JLabel saveDirLabel;
		public JTextField saveDirField;
		JLabel saveAsNameLabel;
		public JCheckBox saveAsNameBox;
		JLabel maxThreadLabel;
		public JTextField maxThreadField;
		JLabel loginUrlLabel;
		public JTextField loginUrlField;
		JLabel cookieLabel;
		public JTextField cookieField;
		JButton cookieButton;
		JButton basicBtn;
		/* HenTai@Home设置 */
		JPanel henTaiHomePanel;
		JLabel h_uriLabel;
		public JTextField h_uriTextField;
		JLabel h_firstParameterNameLabel;
		public JTextField h_firstParameterNameTextField;
		JLabel h_secondParameterNameLabel;
		public JTextField h_secondParameterNameTextField;
		JLabel h_totalPrefixLabel;
		public JTextField h_totalPrefixTextField;
		JLabel h_namePrefixLabel;
		public JTextField h_namePrefixTextField;
		JLabel h_fileListPrefixLabel;
		public JTextField h_fileListPrefixTextField;
		JLabel h_fileListSuffixLabel;
		public JTextField h_fileListSuffixTextField;
		
		JButton h_Btn;
		/*下载设置*/
		JPanel downloadPanel;
		JLabel d_pageCountLabel;
		public JTextField d_pageCountTextField;
		JLabel d_pageParamLabel;
		public JTextField d_pageParamTextField;
		JLabel d_sourcePrefixLabel;
		public JTextField d_sourcePrefixTextField;
		JLabel d_sourceSuffixLabel;
		public JTextField d_sourceSuffixTextField;
		
		JLabel d_showPicPrefixLabel;
		public JTextField d_showPicPrefixTextField;
		JLabel d_showPicSuffixLabel;
		public JTextField d_showPicSuffixTextField;
		JLabel d_realUrlPrefixLabel;
		public JTextField d_realUrlPrefixTextField;
		JLabel d_realUrlSuffixLabel;
		public JTextField d_realUrlSuffixTextField;
		
		JButton d_Btn;

		public SettingWindow(JFrame mainWindow) {
			super("配置");
			this.getContentPane().setLayout(null);
			this.setSize(640, 450);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			Setting setting = ((EgDownloaderWindow)mainWindow).setting;
			settingTabPanel.setBounds(20, 5, 600, 400);
			Color labelColor = Color.BLUE;
			/* 基本配置 */
			basicPanel = new JPanel();
			basicPanel.setLayout(null);
			saveDirLabel = new AJLabel("保存目录：", labelColor, 25, 30, 100, 30);
			saveDirField = new AJTextField(setting.getDefaultSaveDir(), "", 125, 30, 360, 30);
			saveAsNameLabel = new AJLabel("以真实名称保存：", labelColor, 25, 70, 100, 30);
			saveAsNameBox = new JCheckBox("", setting.isSaveAsName());
			saveAsNameBox.setBounds(125, 70, 30, 30);
			maxThreadLabel = new AJLabel("最多开启任务数：", labelColor, 25, 110, 100, 30);
			maxThreadField = new AJTextField(setting.getMaxThread() + "", "", 125, 110, 100, 30);
			loginUrlLabel = new AJLabel("登录地址：", labelColor, 25, 150, 100, 30);
			loginUrlField = new AJTextField(setting.getLoginUrl(), "", 125, 150, 360, 30);
			cookieLabel = new AJLabel("登录信息：", labelColor, 25, 190, 100, 30);
			cookieField = new AJTextField(setting.getCookieInfo(), "", 125, 190, 360, 30);
			/*cookieButton = new AJButton("登录", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
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
			}), 500, 190, 60, 30);*/
			MouseListener basicBtnListener = new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
					SettingWindow settingWindow = (SettingWindow)mainWindow.settingWindow;
					String saveDir = settingWindow.saveDirField.getText();
					String maxThread = settingWindow.maxThreadField.getText();
					String loginUrl = settingWindow.loginUrlField.getText();
					boolean saveAsName = settingWindow.saveAsNameBox.getSelectedObjects() == null ? false : true;//是否选择了
					String cookieInfo = settingWindow.cookieField.getText();
					Pattern p = Pattern.compile("[0-9]");
					if("".equals(saveDir)){
						JOptionPane.showMessageDialog(null, "请填写保存目录");
						return;
					}else if("".equals(maxThread)){
						JOptionPane.showMessageDialog(null, "请填写最多开启任务数");
						return;
					}else if(!p.matcher(maxThread).matches()){
						JOptionPane.showMessageDialog(null, "最多开启任务数必须填写数字,或不能大于10");
						return;
					}else if("".equals(loginUrl)){
						JOptionPane.showMessageDialog(null, "请填写登录地址");
						return;
					}else{
						if("".equals(cookieInfo)){
							int result = JOptionPane.showConfirmDialog(null, "登陆信息cookie不存在，确认要保存吗？");
							if(result != 0){//不保存
								return;
							}
						}
						mainWindow.setting.setDefaultSaveDir(saveDir);
						mainWindow.setting.setSaveAsName(saveAsName);
						mainWindow.setting.setMaxThread(Integer.parseInt(maxThread));
						mainWindow.setting.setLoginUrl(loginUrl);
						mainWindow.setting.setCookieInfo(cookieInfo);
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(null, "保存成功");
					}
				}
			});
			basicBtn = new AJButton("保存", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("save"), basicBtnListener, 250, 250, 60, 30);
		addComponentsJpanel(basicPanel, saveDirLabel, saveDirField,
				saveAsNameLabel, saveAsNameBox, maxThreadLabel, maxThreadField,
				loginUrlLabel, loginUrlField, cookieLabel, cookieField,
				/*cookieButton,*/ basicBtn);
			/* HenTai@Home设置 */
			h_uriLabel = new AJLabel("URI:", labelColor, 25, 30, 80, 30);
			h_uriTextField = new AJTextField(setting.getHentaiHome().getUri(), null, 105, 30, 400, 30);
			h_firstParameterNameLabel = new AJLabel("gid:", labelColor, 25, 70, 80, 30);
			h_firstParameterNameTextField = new AJTextField(setting.getHentaiHome().getFirstParameterName(), null, 105, 70, 400, 30);
			h_secondParameterNameLabel = new AJLabel("t:", labelColor, 25, 110, 80, 30);
			h_secondParameterNameTextField = new AJTextField(setting.getHentaiHome().getSecondParameterName(), null, 105, 110, 400, 30);
			h_totalPrefixLabel = new AJLabel("totalPrefix:", labelColor, 25, 150, 80, 30);
			h_totalPrefixTextField = new AJTextField(setting.getTotalPrefix(), null, 105, 150, 120, 30);
			h_namePrefixLabel = new AJLabel("namePrefix:", labelColor, 300, 150, 80, 30);
			h_namePrefixTextField = new AJTextField(setting.getNamePrefix(), null, 385, 150, 120, 30);
			h_fileListPrefixLabel = new AJLabel("filePrefix:", labelColor, 25, 190, 80, 30);
			h_fileListPrefixTextField = new AJTextField(setting.getFileListPrefix(), null, 105, 190, 120, 30);
			h_fileListSuffixLabel = new AJLabel("fileSuffix:", labelColor, 300, 190, 80, 30);
			h_fileListSuffixTextField = new AJTextField(setting.getFileListSuffix(), null, 385, 190, 120, 30);
			MouseListener h_BtnListener = new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
					SettingWindow settingWindow = (SettingWindow)mainWindow.settingWindow;
					String uri = settingWindow.h_uriTextField.getText();
					String gid = settingWindow.h_firstParameterNameTextField.getText();
					String t = settingWindow.h_secondParameterNameTextField.getText();
					String totalPrefix = settingWindow.h_totalPrefixTextField.getText();
					String namePrefix = settingWindow.h_namePrefixTextField.getText();
					String fileListPrefix = settingWindow.h_fileListPrefixTextField.getText();
					String fileListSuffix = settingWindow.h_fileListSuffixTextField.getText();
					if("".equals(uri)){
						JOptionPane.showMessageDialog(null, "请填写URI");
						return;
					}else if("".equals(gid)){
						JOptionPane.showMessageDialog(null, "请填写gid");
						return;
					}else if("".equals(t)){
						JOptionPane.showMessageDialog(null, "请填写t");
						return;
					}else if("".equals(totalPrefix)){
						JOptionPane.showMessageDialog(null, "totalPrefix");
						return;
					}else if("".equals(namePrefix)){
						JOptionPane.showMessageDialog(null, "namePrefix");
						return;
					}else if("".equals(fileListPrefix)){
						JOptionPane.showMessageDialog(null, "fileListPrefix");
						return;
					}else if("".equals(fileListSuffix)){
						JOptionPane.showMessageDialog(null, "fileListSuffix");
						return;
					}else{
						mainWindow.setting.getHentaiHome().setUri(uri);
						mainWindow.setting.getHentaiHome().setFirstParameterName(gid);
						mainWindow.setting.getHentaiHome().setSecondParameterName(t);
						mainWindow.setting.setTotalPrefix(totalPrefix);
						mainWindow.setting.setNamePrefix(namePrefix);
						mainWindow.setting.setFileListPrefix(fileListPrefix);
						mainWindow.setting.setFileListSuffix(fileListSuffix);
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(null, "保存成功");
					}
				}
			});
			h_Btn = new AJButton("保存", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("save"), h_BtnListener, 250, 250, 60, 30);
			henTaiHomePanel = new AJPanel(h_uriLabel, h_uriTextField,
				h_firstParameterNameLabel, h_firstParameterNameTextField,
				h_secondParameterNameLabel, h_secondParameterNameTextField,
				h_totalPrefixLabel, h_totalPrefixTextField, h_namePrefixLabel, 
				h_namePrefixTextField, h_fileListPrefixLabel, 
				h_fileListPrefixTextField, h_fileListSuffixLabel, 
				h_fileListSuffixTextField, h_Btn);
			/*下载设置*/
			d_pageCountLabel = new AJLabel("每页数目：", labelColor, 25, 30, 100, 30);
			d_pageCountTextField = new AJTextField(setting.getPageCount() + "", "", 125, 30, 120, 30);
			d_pageParamLabel = new AJLabel("分页参数：", labelColor, 280, 30, 100, 30);
			d_pageParamTextField = new AJTextField(setting.getPageParam() + "", "", 380, 30, 120, 30);
			d_sourcePrefixLabel = new AJLabel("sourcePrefix：", labelColor, 25, 70, 100, 30);
			d_sourcePrefixTextField = new AJTextField(setting.getSourcePrefix() + "", "", 125, 70, 120, 30);
			d_sourceSuffixLabel = new AJLabel("sourceSuffix：", labelColor, 280, 70, 100, 30);
			d_sourceSuffixTextField = new AJTextField(setting.getSourceSuffix() + "", "", 380, 70, 120, 30);
			d_showPicPrefixLabel = new AJLabel("showPicPrefix：", labelColor, 25, 110, 100, 30);
			d_showPicPrefixTextField = new AJTextField(setting.getShowPicPrefix() + "", "", 125, 110, 120, 30);
			d_showPicSuffixLabel = new AJLabel("showPicSuffix：", labelColor, 280, 110, 100, 30);
			d_showPicSuffixTextField = new AJTextField(setting.getShowPicSuffix() + "", "", 380, 110, 120, 30);
			
			d_realUrlPrefixLabel = new AJLabel("realUrlPrefix：", labelColor, 25, 150, 100, 30);
			d_realUrlPrefixTextField = new AJTextField(setting.getRealUrlPrefix() + "", "", 125, 150, 120, 30);
			d_realUrlSuffixLabel = new AJLabel("realUrlSuffix：", labelColor, 280, 150, 100, 30);
			d_realUrlSuffixTextField = new AJTextField(setting.getRealUrlSuffix() + "", "", 380, 150, 120, 30);
			
			MouseListener d_BtnListener = new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
					SettingWindow settingWindow = (SettingWindow)mainWindow.settingWindow;
					String pageCount = settingWindow.d_pageCountTextField.getText();
					String pageParam = settingWindow.d_pageParamTextField.getText();
					String sourcePrefix = settingWindow.d_sourcePrefixTextField.getText();
					String sourceSuffix = settingWindow.d_sourceSuffixTextField.getText();
					String showPicPrefix = settingWindow.d_showPicPrefixTextField.getText();
					String showPicSuffix = settingWindow.d_showPicPrefixTextField.getText();
					String realUrlPrefix = settingWindow.d_realUrlPrefixTextField.getText();
					String realUrlSuffix = settingWindow.d_realUrlSuffixTextField.getText();
					Pattern p = Pattern.compile("[0-9]+");
					if("".equals(pageCount)){
						JOptionPane.showMessageDialog(null, "请填写每页数目");
						return;
					}else if(!p.matcher(pageCount).matches()){
						JOptionPane.showMessageDialog(null, "每页数目必须填写数字");
						return;
					}else if("".equals(pageParam)){
						JOptionPane.showMessageDialog(null, "请填写分页参数");
						return;
					}else if("".equals(sourcePrefix)){
						JOptionPane.showMessageDialog(null, "请填写sourcePrefix");
						return;
					}else if("".equals(sourceSuffix)){
						JOptionPane.showMessageDialog(null, "请填写sourceSuffix");
						return;
					}else if("".equals(showPicPrefix)){
						JOptionPane.showMessageDialog(null, "请填写showPicPrefix");
						return;
					}else if("".equals(showPicSuffix)){
						JOptionPane.showMessageDialog(null, "请填写showPicSuffix");
						return;
					}else if("".equals(realUrlPrefix)){
						JOptionPane.showMessageDialog(null, "请填写realUrlPrefix");
						return;
					}else if("".equals(realUrlSuffix)){
						JOptionPane.showMessageDialog(null, "请填写realUrlSuffix");
						return;
					}else{
						mainWindow.setting.setPageCount(Integer.parseInt(pageCount));
						mainWindow.setting.setPageParam(pageParam);
						mainWindow.setting.setSourcePrefix(sourcePrefix);
						mainWindow.setting.setSourceSuffix(sourceSuffix);
						mainWindow.setting.setShowPicPrefix(showPicPrefix);
						mainWindow.setting.setShowPicSuffix(showPicSuffix);
						mainWindow.setting.setRealUrlPrefix(realUrlPrefix);
						mainWindow.setting.setRealUrlSuffix(realUrlSuffix);
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(null, "保存成功");
					}
				}
			});
			
			d_Btn = new AJButton("保存", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("save"), d_BtnListener, 250, 250, 60, 30);
			
			downloadPanel = new AJPanel(d_pageCountLabel, d_pageCountTextField, d_pageParamLabel,
					d_pageParamTextField, d_sourcePrefixLabel, d_sourcePrefixTextField,
					d_sourceSuffixLabel, d_sourceSuffixTextField, d_showPicPrefixLabel,
					d_showPicPrefixTextField, d_showPicSuffixLabel, d_showPicSuffixTextField,
					d_realUrlPrefixLabel, d_realUrlPrefixTextField, d_realUrlSuffixLabel, 
					d_realUrlSuffixTextField, d_Btn);
			
			settingTabPanel.add("基本配置", basicPanel);
			settingTabPanel.add("HenTai@Home设置", henTaiHomePanel);
			settingTabPanel.add("下载设置", downloadPanel);
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

