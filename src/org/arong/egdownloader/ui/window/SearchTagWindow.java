package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.plaf.ButtonUI;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.util.FileUtil;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

public class SearchTagWindow extends JDialog {

	private static final long serialVersionUID = 682782292634346851L;
	
	public SearchComicWindow searchComicWindow;
	public Properties tags;
	public JButton addBtn;
	public JSeparator separator;
	public JPanel addPanel;
	public JPanel tagPane;
	
	public SearchTagWindow(SearchComicWindow searchComicWindow){
		this.searchComicWindow = searchComicWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH - 200, ComponentConst.CLIENT_HEIGHT - 100);
		this.setTitle("选择标签");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(searchComicWindow);
		
		//加载标签定义文件
		tags = new Properties();
		try {
			tags.load(new FileReader(ComponentConst.TAG_FILE_PATH));
		} catch (FileNotFoundException e1) {
			try {
				FileUtil.storeStr2file("", ComponentConst.ROOT_DATA_PATH, ComponentConst.TAG_FILE_NAME);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		addBtn = new AJButton("添加标签", "",  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addPanel.setVisible(true);
				tagPane.setVisible(false);
			}
		}, (this.getWidth() - 60) / 2, 10, 60, 30);
		
		separator = new JSeparator(); 
		separator.setBounds(0, 45, this.getWidth(), 1);
		
		tagPane = new JPanel();
		tagPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		tagPane.setBounds(10, 50, this.getWidth() - 30, this.getHeight() - 80);
		updateTagScrollPane(tags);
		/**
		 * 表单
		 */
		addPanel = new JPanel();
		addPanel.setBounds((this.getWidth() - 300) / 2, (this.getHeight() - 130) / 2, 300, 130);
		addPanel.setVisible(false);
		addPanel.setLayout(null);
		addPanel.setBackground(Color.WHITE);
		addPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		JLabel nameLabel = new AJLabel("名(如:全彩)", Color.blue, 10, 10, 100, 30);
		final JTextField nameField = new AJTextField("", 120, 10, 150, 30);
		JLabel valueLabel = new AJLabel("值(如:fullcolor)", Color.blue, 10, 50, 100, 30);
		final JTextField valueField = new AJTextField("", 120, 50, 150, 30);
		final SearchTagWindow this_ = this;
		JButton saveBtn = new AJButton("保存", "",  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String name = nameField.getText();
				String value = valueField.getText();
				if("".equals(name.trim()) || "".equals(value.trim())){
					JOptionPane.showMessageDialog(this_, "名或值不能为空");
				}else{
					tags.put(name, value);
					//保存
					try {
						tags.store(new FileWriter(ComponentConst.TAG_FILE_PATH), "");
					} catch (IOException e) {
						e.printStackTrace();
					}
					updateTagScrollPane(tags);
					addPanel.setVisible(false);
					tagPane.setVisible(true);
				}
			}
		}, 80, 90, 60, 30);
		JButton cancelBtn = new AJButton("取消", "",  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addPanel.setVisible(false);
				tagPane.setVisible(true);
			}
		}, 150, 90, 60, 30);
		ComponentUtil.addComponents(addPanel, nameLabel, nameField, valueLabel, valueField, saveBtn, cancelBtn);
		
		
		
		ComponentUtil.addComponents(getContentPane(), addBtn, separator, tagPane);
		getContentPane().add(addPanel, 1);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
		//窗口大小变化监听
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Window window = (Window) e.getSource();
				if(addBtn != null){
					addBtn.setLocation((window.getWidth() - addBtn.getWidth()) / 2, addBtn.getY());
				}
				separator.setSize(window.getWidth(), 1);
				addPanel.setLocation((window.getWidth() - addPanel.getWidth()) / 2, (window.getHeight() - addPanel.getHeight()) / 2);
				tagPane.setSize(window.getWidth() - 30, window.getHeight() - 80);
			}
		});
		
	}
	
	public ButtonUI[] btnUis = {
			AJButton.blueBtnUi,
			AJButton.greenBtnUi,
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue),
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red)
	};
	
	public void updateTagScrollPane(final Properties tags){
		tagPane.removeAll();
		Set<String> keys = tags.stringPropertyNames();
		final SearchTagWindow this_ = this;
		for(final String key : keys){
			JButton b = new JButton(key);
			//随机颜色
			int r = (int) (Math.random() * 4);
			b.setUI(btnUis[r]);
			b.setForeground(Color.WHITE);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					this_.dispose();
					searchComicWindow.keyField.setText("tag:"+ tags.getProperty(key));
					searchComicWindow.searchBtn.doClick();
				}
			});
			tagPane.add(b);
		}
	}
}
