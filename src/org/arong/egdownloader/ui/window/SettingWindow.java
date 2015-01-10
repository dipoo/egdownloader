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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJPanel;
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
		
		/* 引擎设置 */
		JPanel enginePanel;
		JLabel nameLabel;
		public JTextField nameTextFieldPrefix;//名称前缀
		public JTextField nameTextFieldSuffix;//名称后缀
		JLabel subnameLabel;
		public JTextField  subnameTextFieldPrefix;
		public JTextField  subnameTextFieldSuffix;
		JLabel coverLabel;
		public JTextField  coverTextFieldPrefix;
		public JTextField  coverTextFieldSuffix;
		JLabel typeLabel;
		public JTextField  typeTextFieldPrefix;
		public JTextField  typeTextFieldSuffix;
		JLabel totalSizeLabel;
		public JTextField  totalSizeTextFieldPrefix;
		public JTextField  totalSizeTextFieldSuffix;
		JLabel languageLabel;
		public JTextField languageTextFieldPrefix;
		public JTextField languageTextFieldSuffix;
		JLabel interceptLabel;
		public JTextField interceptTextFieldPrefix;
		public JTextField interceptTextFieldSuffix;
		JLabel showUrlLabel;
		public JTextField showUrlTextFieldPrefix;
		public JTextField showUrlTextFieldSuffix;
		JLabel picNameLabel;
		public JTextField picNameTextFieldPrefix;
		public JTextField picNameTextFieldSuffix;
		JLabel realUrlLabel;
		public JTextField realUrlTextField1;
		public JTextField realUrlTextField2;
		JButton save_Btn;
		
		/* 脚本设置 */
		JPanel scriptPanel;
		JLabel openScriptLabel;
		public JCheckBox openScriptBox;
		public JLabel createJsLabel;
		public JTextField createJsField;
		public JLabel collectJsLabel;
		public JTextField collectJsField;
		public JLabel downloadJsLabel;
		public JTextField downloadJsField;
		public JButton testBtn;
		public JTextPane scriptDocPanel;
		
		Color labelColor = new Color(65,145,65);
		Color bgColor = new Color(210,225,240);
		
		public void showPathSettingPanel(boolean b){
			if(b){
				createJsLabel.setVisible(true);
				createJsField.setVisible(true);
				collectJsLabel.setVisible(true);
				collectJsField.setVisible(true);
				downloadJsLabel.setVisible(true);
				downloadJsField.setVisible(true);
				testBtn.setVisible(true);
				scriptDocPanel.setVisible(true);
			}else{
				createJsLabel.setVisible(false);
				createJsField.setVisible(false);
				collectJsLabel.setVisible(false);
				collectJsField.setVisible(false);
				downloadJsLabel.setVisible(false);
				downloadJsField.setVisible(false);
				testBtn.setVisible(false);
				scriptDocPanel.setVisible(false);
			}
		}

		public SettingWindow(JFrame mainWindow) {
			super("配置");
			final Setting setting = ((EgDownloaderWindow)mainWindow).setting;
			
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
			JButton openDirButton = new AJButton("打开", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("folder"), new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					try {
						String path = ComponentConst.getSavePathPreffix() + saveDirField.getText();
						File f = new File(path);
						FileUtil.ifNotExistsThenCreate(f);
						Desktop.getDesktop().open(f);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "文件夹已被删除");
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
		    
		    /* 引擎配置 */
			enginePanel = new JPanel();
			enginePanel.setLayout(null);
			nameLabel = new AJLabel("名称前后缀：", labelColor, 25, 30, 100, 30);
			nameTextFieldPrefix = new AJTextField(setting.getTask_name()[0], "", 125, 30, 240, 30);
			nameTextFieldSuffix = new AJTextField(setting.getTask_name()[1], "", 385, 30, 240, 30);
			subnameLabel = new AJLabel("子名称前后缀：", labelColor, 25, 70, 100, 30);
			subnameTextFieldPrefix = new AJTextField(setting.getTask_subname()[0] + "", "", 125, 70, 240, 30);
			subnameTextFieldSuffix = new AJTextField(setting.getTask_subname()[1] + "", "", 385, 70, 240, 30);
			typeLabel = new AJLabel("类别前后缀：", labelColor, 25, 110, 100, 30);
			typeTextFieldPrefix = new AJTextField(setting.getTask_type()[0] + "", "", 125, 110, 240, 30);
			typeTextFieldSuffix = new AJTextField(setting.getTask_type()[1] + "", "", 385, 110, 240, 30);
			coverLabel = new AJLabel("封面前后缀：", labelColor, 25, 150, 100, 30);
			coverTextFieldPrefix = new AJTextField(setting.getTask_coverUrl()[0] + "", "", 125, 150, 240, 30);
			coverTextFieldSuffix = new AJTextField(setting.getTask_coverUrl()[1] + "", "", 385, 150, 240, 30);
			totalSizeLabel = new AJLabel("数目大小前后缀：", labelColor, 25, 190, 100, 30);
			totalSizeTextFieldPrefix = new AJTextField(setting.getTask_total_size()[0] + "", "", 125, 190, 240, 30);
			totalSizeTextFieldSuffix = new AJTextField(setting.getTask_total_size()[1] + "", "", 385, 190, 240, 30);
			languageLabel = new AJLabel("语言前后缀：", labelColor, 25, 230, 100, 30);
			languageTextFieldPrefix = new AJTextField(setting.getTask_language()[0] + "", "", 125, 230, 240, 30);
			languageTextFieldSuffix = new AJTextField(setting.getTask_language()[1] + "", "", 385, 230, 240, 30);
			interceptLabel = new AJLabel("截取列表前后缀：", labelColor, 25, 270, 100, 30);
			interceptTextFieldPrefix = new AJTextField(setting.getPicture_intercept()[0] + "", "", 125, 270, 240, 30);
			interceptTextFieldSuffix = new AJTextField(setting.getPicture_intercept()[1] + "", "", 385, 270, 240, 30);
			showUrlLabel = new AJLabel("图片地址前后缀：", labelColor, 25, 310, 100, 30);
			showUrlTextFieldPrefix = new AJTextField(setting.getPicture_showUrl()[0] + "", "", 125, 310, 240, 30);
			showUrlTextFieldSuffix = new AJTextField(setting.getPicture_showUrl()[1] + "", "", 385, 310, 240, 30);
			picNameLabel = new AJLabel("图片名称前后缀：", labelColor, 25, 350, 100, 30);
			picNameTextFieldPrefix = new AJTextField(setting.getPicture_name()[0] + "", "", 125, 350, 240, 30);
			picNameTextFieldSuffix = new AJTextField(setting.getPicture_name()[1] + "", "", 385, 350, 240, 30);
			realUrlLabel = new AJLabel("真实地址前后缀：", labelColor, 25, 390, 100, 30);
			realUrlTextField1 = new AJTextField(setting.getPicture_realUrl()[0] + "", "", 125, 390, 240, 30);
			realUrlTextField2 = new AJTextField(setting.getPicture_realUrl()[1] + "", "", 385, 390, 240, 30);
			
			addComponentsJpanel(enginePanel, nameLabel, nameTextFieldPrefix, nameTextFieldSuffix, subnameLabel, 
					subnameTextFieldPrefix, subnameTextFieldSuffix, typeLabel, typeTextFieldPrefix, 
					typeTextFieldSuffix, coverLabel, coverTextFieldPrefix, coverTextFieldSuffix, 
					totalSizeLabel, totalSizeTextFieldPrefix, totalSizeTextFieldSuffix,
					languageLabel, languageTextFieldPrefix, languageTextFieldSuffix, 
					interceptLabel, interceptTextFieldPrefix, interceptTextFieldSuffix,
					showUrlLabel, showUrlTextFieldPrefix, showUrlTextFieldSuffix, 
					picNameLabel, picNameTextFieldPrefix, picNameTextFieldSuffix,
					realUrlLabel, realUrlTextField1, realUrlTextField2);
			/*脚本设置*/
			scriptPanel = new JPanel();
			scriptPanel.setLayout(null);
			openScriptLabel = new AJLabel("是否开启脚本：", labelColor, 25, 30, 90, 30);
			openScriptBox = new JCheckBox("", setting.isOpenScript());
			openScriptBox.setBounds(118, 30, 30, 30);
			createJsLabel = new AJLabel("创建任务脚本：", labelColor, 25, 70, 100, 30);
			createJsField = new AJTextField(setting.getCreateTaskScriptPath(), "", 125, 70, 360, 30);
			collectJsLabel = new AJLabel("收集图片脚本：", labelColor, 25, 110, 100, 30);
			collectJsField = new AJTextField(setting.getCollectPictureScriptPath(), "", 125, 110, 360, 30);
			downloadJsLabel = new AJLabel("下载任务脚本：", labelColor, 25, 150, 100, 30);
			downloadJsField = new AJTextField(setting.getDownloadScriptPath(), "", 125, 150, 360, 30);
			testBtn = new AJButton("脚本测试", "", "", new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
					SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
					String createScriptPath = settingWindow.createJsField.getText();
					String collectScriptPath = settingWindow.collectJsField.getText();
					String downloadScriptPath = settingWindow.downloadJsField.getText();
					
					if("".equals(createScriptPath.trim()) || "".equals(collectScriptPath.trim())
							|| "".equals(downloadScriptPath.trim())){
						JOptionPane.showMessageDialog(null, "请填写完所有脚本路径！");
						return;
					}else{
						if(testScriptWindow == null){
							testScriptWindow = new TestScriptWindow(createScriptPath, collectScriptPath, downloadScriptPath, setting);
						}
						testScriptWindow.setVisible(true);
					}
				}
			}), 500, 110, 60, 30);
			scriptDocPanel = new AJTextPane(ComponentConst.SCRIPT_DESC_TEXT, labelColor);//Field(ComponentConst.SCRIPT_DESC_TEXT, "", 25, 200, 400, 100);
			scriptDocPanel.setBounds(20, 200, 650, 200);
			
			openScriptBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					boolean selected = openScriptBox.getSelectedObjects() == null ? false : true;
					showPathSettingPanel(selected);
				}
			});
			showPathSettingPanel(setting.isOpenScript());
			addComponentsJpanel(scriptPanel, openScriptLabel, openScriptBox, createJsLabel,
					createJsField, collectJsLabel, collectJsField, downloadJsLabel, downloadJsField, testBtn, scriptDocPanel);
			
			/* HenTai@Home设置 */
			/*h_uriLabel = new AJLabel("URI:", labelColor, 25, 30, 80, 30);
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
			henTaiHomePanel = new AJPanel(h_uriLabel, h_uriTextField,
				h_firstParameterNameLabel, h_firstParameterNameTextField,
				h_secondParameterNameLabel, h_secondParameterNameTextField,
				h_totalPrefixLabel, h_totalPrefixTextField, h_namePrefixLabel, 
				h_namePrefixTextField, h_fileListPrefixLabel, 
				h_fileListPrefixTextField, h_fileListSuffixLabel, 
				h_fileListSuffixTextField);*/
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
			
			downloadPanel = new AJPanel(d_pageCountLabel, d_pageCountTextField, d_pageParamLabel,
					d_pageParamTextField, d_sourcePrefixLabel, d_sourcePrefixTextField,
					d_sourceSuffixLabel, d_sourceSuffixTextField, d_showPicPrefixLabel,
					d_showPicPrefixTextField, d_showPicSuffixLabel, d_showPicSuffixTextField,
					d_realUrlPrefixLabel, d_realUrlPrefixTextField, d_realUrlSuffixLabel, 
					d_realUrlSuffixTextField);
			
			JPanel descPanel = new JPanel();
			descPanel.setLayout(null);
			JTextPane descTextPanel = new AJTextPane(ComponentConst.docHtml, Color.BLUE);
			descTextPanel.setBounds(0, 0, 680, 480);
			descPanel.add(descTextPanel);
			settingTabPanel.add("基本配置", basicPanel);
			settingTabPanel.add("引擎配置", enginePanel);
			settingTabPanel.add("配置说明", descPanel);
			settingTabPanel.add("脚本配置", scriptPanel);
			//settingTabPanel.add("HenTai@Home设置", henTaiHomePanel);
			settingTabPanel.add("下载设置", downloadPanel);
			
			
			
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
							setting.setDefaultSaveDir(saveDir);
							setting.setSaveAsName(saveAsName);
							setting.setAutoDownload(autoDownload);
							setting.setMaxThread(Integer.parseInt(maxThread));
							setting.setLoginUrl(loginUrl);
							setting.setCookieInfo(cookieInfo);
							mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
							JOptionPane.showMessageDialog(null, "保存成功");
						}
					}
					//引擎设置
					else if(index == 1){
						String name_1 = nameTextFieldPrefix.getText();
						String name_2 = nameTextFieldSuffix.getText();
						String subname_1 = subnameTextFieldPrefix.getText();
						String subname_2 = subnameTextFieldSuffix.getText();
						String type_1 = typeTextFieldPrefix.getText();
						String type_2 = typeTextFieldSuffix.getText();
						String cover_1 = coverTextFieldPrefix.getText();
						String cover_2 = coverTextFieldSuffix.getText();
						String totalSize_1 = totalSizeTextFieldPrefix.getText();
						String totalSize_2 = totalSizeTextFieldSuffix.getText();
						String language_1 = languageTextFieldPrefix.getText();
						String language_2 = languageTextFieldSuffix.getText();
						String intercept_1 = interceptTextFieldPrefix.getText();
						String intercept_2 = interceptTextFieldSuffix.getText();
						String showUrl_1 = showUrlTextFieldPrefix.getText();
						String showUrl_2 = showUrlTextFieldSuffix.getText();
						String picName_1 = picNameTextFieldPrefix.getText();
						String picName_2 = picNameTextFieldSuffix.getText();
						String realUrl_1 = realUrlTextField1.getText();
						String realUrl_2 = realUrlTextField2.getText();
						if("".equals(name_1) || "".equals(name_2) || "".equals(subname_1) ||
								 "".equals(subname_2) || "".equals(type_1) || "".equals(type_2) ||
								 "".equals(cover_1) || "".equals(cover_2) || "".equals(totalSize_1) ||
								 "".equals(totalSize_2) || "".equals(language_1) || "".equals(language_2) ||
								 "".equals(intercept_1) || "".equals(intercept_2) || "".equals(showUrl_1) ||
								 "".equals(showUrl_2) || "".equals(picName_1) || "".equals(picName_2) ||
								 "".equals(realUrl_1) || "".equals(realUrl_2)){
							JOptionPane.showMessageDialog(null, "请填写完整！");
							return;
						}else{
							setting.getTask_name()[0] = name_1;
							setting.getTask_name()[1] = name_2;
							setting.getTask_subname()[0] = subname_1;
							setting.getTask_subname()[1] = subname_2;
							setting.getTask_type()[0] = type_1;
							setting.getTask_type()[1] = type_2;
							setting.getTask_coverUrl()[0] = cover_1;
							setting.getTask_coverUrl()[1] = cover_2;
							setting.getTask_total_size()[0] = totalSize_1;
							setting.getTask_total_size()[1] = totalSize_2;
							setting.getTask_language()[0] = language_1;
							setting.getTask_language()[1] = language_2;
							setting.getPicture_intercept()[0] = intercept_1;
							setting.getPicture_intercept()[1] = intercept_2;
							setting.getPicture_name()[0] = picName_1;
							setting.getPicture_name()[1] = picName_2;
							setting.getPicture_showUrl()[0] = showUrl_1;
							setting.getPicture_showUrl()[1] = showUrl_2;
							setting.getPicture_realUrl()[0] = realUrl_1;
							setting.getPicture_realUrl()[1] = realUrl_2;
							mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
							JOptionPane.showMessageDialog(null, "保存成功");
						}
					}
					//脚本设置
					else if(index == 3){
						boolean openScript = settingWindow.openScriptBox.getSelectedObjects() == null ? false : true;//是否选择了
						String createScriptPath = settingWindow.createJsField.getText();
						String collectScriptPath = settingWindow.collectJsField.getText();
						String downloadScriptPath = settingWindow.downloadJsField.getText();
						
						if(openScript && ("".equals(createScriptPath.trim()) || "".equals(collectScriptPath.trim())
								|| "".equals(downloadScriptPath.trim()))){
							JOptionPane.showMessageDialog(null, "请填写完所有脚本路径！");
							return;
						}
						setting.setOpenScript(openScript);
						setting.setCreateTaskScriptPath(createScriptPath);
						setting.setCollectPictureScriptPath(collectScriptPath);
						setting.setDownloadScriptPath(downloadScriptPath);
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(null, "保存成功");
					}
					//hentai@home设置
					/*else if(index == 5){
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
							setting.getHentaiHome().setUri(uri);
							setting.getHentaiHome().setFirstParameterName(gid);
							setting.getHentaiHome().setSecondParameterName(t);
							setting.setTotalPrefix(totalPrefix);
							setting.setNamePrefix(namePrefix);
							setting.setFileListPrefix(fileListPrefix);
							setting.setFileListSuffix(fileListSuffix);
							mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
							JOptionPane.showMessageDialog(null, "保存成功");
						}
					}*/
					//下载设置
					else if(index == 4){
						String pageCount = settingWindow.d_pageCountTextField.getText();
						String pageParam = settingWindow.d_pageParamTextField.getText();
						String sourcePrefix = settingWindow.d_sourcePrefixTextField.getText();
						String sourceSuffix = settingWindow.d_sourceSuffixTextField.getText();
						String showPicPrefix = settingWindow.d_showPicPrefixTextField.getText();
						String showPicSuffix = settingWindow.d_showPicSuffixTextField.getText();
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
							setting.setPageCount(Integer.parseInt(pageCount));
							setting.setPageParam(pageParam);
							setting.setSourcePrefix(sourcePrefix);
							setting.setSourceSuffix(sourceSuffix);
							setting.setShowPicPrefix(showPicPrefix);
							setting.setShowPicSuffix(showPicSuffix);
							setting.setRealUrlPrefix(realUrlPrefix);
							setting.setRealUrlSuffix(realUrlSuffix);
							mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
							JOptionPane.showMessageDialog(null, "保存成功");
						}
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

