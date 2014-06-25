package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.Setting;
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
		/* 基本配置页签 */
		JPanel basicPanel;
		JLabel saveDirLabel;
		public JTextField saveDirField;
		JLabel saveAsNameLabel;
		public JCheckBox saveAsNameBox;
		JLabel maxThreadLabel;
		public JTextField maxThreadField;
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
		
		JButton h_Btn;

		public SettingWindow(JFrame mainWindow) {
			super("配置");
			this.getContentPane().setLayout(null);
			this.setSize(640, 480);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			Setting setting = ((EgDownloaderWindow)mainWindow).setting;
			settingTabPanel.setBounds(20, 5, 600, 400);
			
			/* 基本配置 */
			basicPanel = new JPanel();
			basicPanel.setLayout(null);
			basicPanel.setBackground(Color.WHITE);
			saveDirLabel = new AJLabel("保存目录：", null, 25, 30, 100, 30);
			saveDirField = new AJTextField(setting.getDefaultSaveDir(), "", 125, 30, 360, 30);
			saveAsNameLabel = new AJLabel("以真实名称保存：", null, 25, 70, 100, 30);
			saveAsNameBox = new JCheckBox("2", true);
			saveAsNameBox.setBounds(125, 70, 30, 30);
			maxThreadLabel = new AJLabel("最多开启任务数：", null, 25, 110, 100, 30);
			maxThreadField = new AJTextField(setting.getMaxThread() + "", "", 125, 110, 100, 30);
			cookieLabel = new AJLabel("登录信息：", null, 25, 150, 100, 30);
			cookieField = new AJTextField(setting.getCookieInfo(), "", 125, 150, 360, 30);
			cookieButton = new AJButton("登录", "", "", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					
				}
			}), 500, 150, 60, 30);
			MouseListener basicBtnListener = new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
					SettingWindow settingWindow = (SettingWindow)mainWindow.settingWindow;
					if(! "".equals(settingWindow.saveDirField.getText())){
						mainWindow.setting.setDefaultSaveDir(settingWindow.saveDirField.getText());
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(null, "保存成功");
					}else{
						JOptionPane.showMessageDialog(null, "请填写保存目录");
					}
				}
			});
			basicBtn = new AJButton("保存", "", null, basicBtnListener, 270, 220, 60, 30);
		addComponentsJpanel(basicPanel, saveDirLabel, saveDirField,
				saveAsNameLabel, saveAsNameBox, maxThreadLabel, maxThreadField,
				cookieLabel, cookieField, cookieButton, basicBtn);
			/* HenTai@Home设置 */
			h_uriLabel = new AJLabel("URI:", null, 25, 30, 80, 30);
			h_uriTextField = new AJTextField(setting.getHentaiHome().getUri(), null, 105, 30, 400, 30);
			h_firstParameterNameLabel = new AJLabel("gid:", null, 25, 70, 80, 30);
			h_firstParameterNameTextField = new AJTextField(setting.getHentaiHome().getFirstParameterName(), null, 105, 70, 400, 30);
			h_secondParameterNameLabel = new AJLabel("t:", null, 25, 110, 80, 30);
			h_secondParameterNameTextField = new AJTextField(setting.getHentaiHome().getSecondParameterName(), null, 105, 110, 400, 30);
			h_totalPrefixLabel = new AJLabel("totalPrefix:", null, 25, 150, 80, 30);
			h_totalPrefixTextField = new AJTextField(setting.getTotalPrefix(), null, 105, 150, 120, 30);
			h_namePrefixLabel = new AJLabel("namePrefix:", null, 300, 150, 80, 30);
			h_namePrefixTextField = new AJTextField(setting.getNamePrefix(), null, 385, 150, 120, 30);
			h_fileListPrefixLabel = new AJLabel("filePrefix:", null, 25, 190, 80, 30);
			h_fileListPrefixTextField = new AJTextField(setting.getFileListPrefix(), null, 105, 190, 120, 30);
			h_fileListSuffixLabel = new AJLabel("fileSuffix:", null, 300, 190, 80, 30);
			h_fileListSuffixTextField = new AJTextField(setting.getFileListSuffix(), null, 385, 190, 120, 30);
			MouseListener h_BtnListener = new OperaBtnMouseListener(mainWindow, MouseAction.CLICK, new IListenerTask() {
				public void doWork(Window window, MouseEvent e) {
					EgDownloaderWindow mainWindow = (EgDownloaderWindow) window;
					SettingWindow settingWindow = (SettingWindow)mainWindow.settingWindow;
					if("".equals(settingWindow.h_uriTextField.getText())){
						JOptionPane.showMessageDialog(null, "请填写URI");
					}else if("".equals(settingWindow.h_firstParameterNameTextField.getText())){
						JOptionPane.showMessageDialog(null, "请填写gid");
					}else if("".equals(settingWindow.h_secondParameterNameTextField.getText())){
						JOptionPane.showMessageDialog(null, "请填写t");
					}else{
						mainWindow.setting.getHentaiHome().setUri(settingWindow.h_uriTextField.getText());
						mainWindow.setting.getHentaiHome().setFirstParameterName(settingWindow.h_firstParameterNameTextField.getText());
						mainWindow.setting.getHentaiHome().setSecondParameterName(settingWindow.h_secondParameterNameTextField.getText());
						mainWindow.settingDbTemplate.update(mainWindow.setting);//保存
						JOptionPane.showMessageDialog(null, "保存成功");
					}
					
				}
			});
			h_Btn = new AJButton("保存", "", null, h_BtnListener, 45, 280, 60, 30);
			henTaiHomePanel = new AJPanel(h_uriLabel, h_uriTextField,
				h_firstParameterNameLabel, h_firstParameterNameTextField,
				h_secondParameterNameLabel, h_secondParameterNameTextField,
				h_totalPrefixLabel, h_totalPrefixTextField, h_namePrefixLabel, 
				h_namePrefixTextField, h_fileListPrefixLabel, 
				h_fileListPrefixTextField, h_fileListSuffixLabel, 
				h_fileListSuffixTextField, h_Btn);
			/*下载设置*/
			d_pageCountLabel = new AJLabel("每页数目：", null, 25, 30, 100, 30);
			d_pageCountTextField = new AJTextField(setting.getPageCount() + "", "", 125, 30, 100, 30);
			d_pageParamLabel = new AJLabel("分页参数：", null, 250, 30, 100, 30);
			d_pageParamTextField = new AJTextField(setting.getPageParam() + "", "", 350, 30, 100, 30);
			d_sourcePrefixLabel = new AJLabel("sourcePrefix：", null, 25, 70, 100, 30);
			d_sourcePrefixTextField = new AJTextField(setting.getSourcePrefix() + "", "", 125, 70, 100, 30);
			d_sourceSuffixLabel = new AJLabel("sourceSuffix：", null, 250, 70, 100, 30);
			d_sourceSuffixTextField = new AJTextField(setting.getSourceSuffix() + "", "", 350, 70, 100, 30);
			d_showPicPrefixLabel = new AJLabel("showPicPrefix：", null, 25, 110, 100, 30);
			d_showPicPrefixTextField = new AJTextField(setting.getShowPicPrefix() + "", "", 125, 110, 100, 30);
			d_showPicSuffixLabel = new AJLabel("showPicSuffix：", null, 250, 110, 100, 30);
			d_showPicSuffixTextField = new AJTextField(setting.getShowPicSuffix() + "", "", 350, 110, 100, 30);
			
			d_realUrlPrefixLabel = new AJLabel("realUrlPrefix：", null, 25, 150, 100, 30);
			d_realUrlPrefixTextField = new AJTextField(setting.getRealUrlPrefix() + "", "", 125, 150, 100, 30);
			d_realUrlSuffixLabel = new AJLabel("realUrlSuffix：", null, 250, 150, 100, 30);
			d_realUrlSuffixTextField = new AJTextField(setting.getRealUrlSuffix() + "", "", 350, 150, 100, 30);
			
			
			downloadPanel = new AJPanel(d_pageCountLabel, d_pageCountTextField, d_pageParamLabel,
					d_pageParamTextField, d_sourcePrefixLabel, d_sourcePrefixTextField,
					d_sourceSuffixLabel, d_sourceSuffixTextField, d_showPicPrefixLabel,
					d_showPicPrefixTextField, d_showPicSuffixLabel, d_showPicSuffixTextField,
					d_realUrlPrefixLabel, d_realUrlPrefixTextField, d_realUrlSuffixLabel, d_realUrlSuffixTextField);
			
			settingTabPanel.add("基本配置", basicPanel);
			settingTabPanel.add("HenTai@Home设置", henTaiHomePanel);
			settingTabPanel.add("下载设置", downloadPanel);
			addComponents(settingTabPanel);
			this.setVisible(true);
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

