package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
/**
 * 配置窗口
 * @author 阿荣
 * @since 2014-06-10
 */
public class SettingWindow extends JFrame implements ActionListener{
	
		private static final long serialVersionUID = -2290486210441887526L;

		JTabbedPane settingTabPanel = new JTabbedPane(JTabbedPane.TOP);
		/* 基本配置页签 */
		JPanel basicPanel;
		JLabel saveDirLabel;
		JTextField saveDirField;
		JButton basicBtn;

		public SettingWindow(JFrame mainWindow) {
			super("配置");
			this.getContentPane().setLayout(null);
			this.setSize(640, 480);
			this.setResizable(false);
			this.setLocationRelativeTo(null);

			settingTabPanel.setBounds(20, 5, 600, 460);
			/* 基本配置 */
			basicPanel = new JPanel();
			basicPanel.setLayout(null);
			basicPanel.setBackground(Color.WHITE);
			saveDirLabel = new AJLabel("保存目录：", null, 25, 30, 60, 30);
			saveDirField = new AJTextField("", 85, 30, 400, 30);
			saveDirField.setText(((EgDownloaderWindow)mainWindow).setting.getDefaultSaveDir());
			basicBtn = new AJButton("保存", "", null, this, 270, 220, 60, 30);
			addComponentsJpanel(basicPanel, saveDirLabel, saveDirField, basicBtn);
			settingTabPanel.add("基本配置", basicPanel);
			addComponents(settingTabPanel);
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
					return;
				}
				// 解密
				else if (ComponentConst.DECODE_BUTTON_NAME.equals(buttonName)) {
					
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

