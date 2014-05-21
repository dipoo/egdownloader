package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.arong.egdownloader.ini.IniPropertyValidator;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.SwingPrintStream;
import org.arong.egdownloader.ui.listener.MenuMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJMenu;
import org.arong.egdownloader.ui.swing.AJMenuBar;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.CollectWork;
import org.arong.egdownloader.ui.work.UpdateSiteWork;
import org.arong.egdownloader.version.Version;

/**
 * 采集程序主界面
 * 
 * @author 阿荣
 * @since 2013-8-18
 */
public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = -929178969404921993L;
	JMenuBar jMenuBar;
	public JFrame toolsMenuWindow;
	public JDialog aboutMenuWindow;
	JLabel siteLabel;
	JLabel typeLabel;
	JLabel startLabel;
	JLabel endLabel;
	JComboBox siteNameCombo;
	JComboBox typeNameCombo;
	ComboBoxModel comboBoxModel;
	JTextField endField;
	JTextField startField;
	JButton pageOneButton;
	JButton addPageButton;
	JButton checkButton;
	JButton collectButton;
	JButton clearButton;
	JButton cancelButton;
	JButton updateButton;
	JButton authorButton;
	JTextArea logTextArea = new JTextArea();
	JScrollPane scrollPanel = new JScrollPane(logTextArea);

	CollectWork collectWork;
	UpdateSiteWork updateSiteWork;

	public MainWindow() {
		// 初始化二
		try {
			// 初始化控制台,将打印详细输送到logTextArea组件上
			new SwingPrintStream(System.out, logTextArea);
		} catch (FileNotFoundException e2) {
			JOptionPane.showMessageDialog(this, "控制台初始化错误！");
			return;
		}
		// 设置主窗口
		this.setTitle(Version.NAME);
		this.getContentPane().setLayout(null);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 设置菜单
		MouseListener menuMouseListener = new MenuMouseListener(this);
		JMenu settingMenu = new AJMenu(ComponentConst.SETTING_MENU_TEXT,
				ComponentConst.SETTING_MENU_NAME, menuMouseListener);
		JMenu toolsMenu = new AJMenu(ComponentConst.TOOLS_MENU_TEXT,
				ComponentConst.TOOLS_MENU_NAME, menuMouseListener);
		JMenu aboutMenu = new AJMenu(ComponentConst.ABOUT_MENU_TEXT,
				ComponentConst.ABOUT_MENU_NAME, menuMouseListener);

		// 构造菜单栏并添加菜单
		jMenuBar = new AJMenuBar(0, 0, 800, 30, settingMenu, toolsMenu,
				aboutMenu);

		siteLabel = new AJLabel(ComponentConst.SITENAME_LABEL_TEXT, Color.BLUE,
				20, 40, 60, 30);
		typeLabel = new AJLabel(ComponentConst.TYPENAME_LABEL_TEXT, Color.BLUE,
				220, 40, 60, 30);

		startLabel = new AJLabel(ComponentConst.START_LABEL_TEXT, Color.BLUE,
				420, 40, 60, 30);
		// 设置起始页文本框位置及大小
		startField = new AJTextField(null, 480, 40, 50, 30);

		endLabel = new AJLabel(ComponentConst.END_LABEL_TEXT, Color.BLUE, 540,
				40, 60, 30);
		// 设置结束页文本框位置及大小
		endField = new AJTextField(null, 600, 40, 50, 30);
		// 第一页按钮
		pageOneButton = new AJButton(ComponentConst.PAGEONE_BUTTON_TEXT,
				ComponentConst.PAGEONE_BUTTON_NAME, this, 660, 40, 60, 30);
		// 加一页按钮
		addPageButton = new AJButton(ComponentConst.ADDPAGE_BUTTON_TEXT,
				ComponentConst.ADDPAGE_BUTTON_NAME, this, 730, 40, 40, 30);
		// 清屏按钮
		clearButton = new AJButton(ComponentConst.CLEAR_BUTTON_TEXT,
				ComponentConst.CLEAR_BUTTON_NAME, this, 20, 120, 80, 30);
		// 检测按钮
		checkButton = new AJButton(ComponentConst.CHECK_BUTTON_TEXT,
				ComponentConst.CHECK_BUTTON_NAME, this, 120, 120, 80, 30);
		// 采集按钮
		collectButton = new AJButton(ComponentConst.COLLECT_BUTTON_TEXT,
				ComponentConst.COLLECT_BUTTON_NAME, this, 220, 120, 80, 30);
		// 取消任务按钮
		cancelButton = new AJButton(ComponentConst.CANCEL_BUTTON_TEXT,
				ComponentConst.CANCEL_BUTTON_NAME, this, 320, 120, 80, 30);
		//更新站点首页按钮
		updateButton = new AJButton(ComponentConst.UPDATE_BUTTON_TEXT,
				ComponentConst.UPDATE_BUTTON_NAME, this, 420, 120, 80, 30);
		//生成作者及添加作品按钮
		authorButton = new AJButton(ComponentConst.AUTHOR_BUTTON_TEXT,
				ComponentConst.AUTHOR_BUTTON_NAME, this, 520, 120, 80, 30);
		logTextArea.setEditable(false);
		logTextArea.setAutoscrolls(true);
		logTextArea.setLineWrap(true);
		logTextArea.setBorder(null);
		TitledBorder border = BorderFactory.createTitledBorder("超级无敌跟踪台");
		scrollPanel.setBounds(20, 160, 750, 380);
		scrollPanel.setAutoscrolls(true);
		scrollPanel.setBorder(border);
		// 设置页码值
		setPageFieldText();

		// 添加各个子组件
		addComponents(jMenuBar, siteLabel, siteNameCombo, typeLabel,
				typeNameCombo, startLabel, startField, endLabel, endField,
				pageOneButton, addPageButton, clearButton, checkButton,
				collectButton, cancelButton, updateButton, authorButton, scrollPanel);
	}

	// 事件处理
	public void actionPerformed(ActionEvent event) {
		Object o = event.getSource();
		if (o instanceof JComboBox) {
			JComboBox combo = (JComboBox) o;
			if (ComponentConst.SITENAME_COMBOBOX_NAME.equals(combo.getName())) {
				// 获取选择站点的索引，索引从0开始
				int index = combo.getSelectedIndex();
				typeNameCombo.setModel(comboBoxModel);
			} else if (ComponentConst.TYPENAME_COMBOBOX_NAME.equals(combo
					.getName())) {

			}
			// 重新设置页码值
			setPageFieldText();
		} else if (o instanceof JButton) {
			JButton btn = (JButton) o;
			// 点击了第一页按钮
			if (ComponentConst.PAGEONE_BUTTON_NAME.equals(btn.getName())) {
				startField.setText("1");
				endField.setText("1");
				return;// 返回，不做下面的事情了
			}
			// 点击了加一页按钮
			if (ComponentConst.ADDPAGE_BUTTON_NAME.equals(btn.getName())) {
				// 先检查页码的合法性或合理性
				if (!pageValidator()) {
					return;
				}
				// 获取结束页码的值
				String endPage = endField.getText();
				int end = Integer.parseInt(endPage);
				startField.setText((end + 1) + "");
				endField.setText((end + 1) + "");
				return;// 返回，不做下面的事情了
			}
			// 点击了清屏按钮，清理文本
			if (ComponentConst.CLEAR_BUTTON_NAME.equals(btn.getName())) {
				logTextArea.setText("");
				return;// 返回，不做下面的事情了
			}
			// 取消任务按钮
			if (ComponentConst.CANCEL_BUTTON_NAME.equals(btn.getName())) {
				if (collectWork != null && !collectWork.isDone()) {
					collectWork.cancel(true);
				} else {
					JOptionPane.showMessageDialog(this, "当前没有任务");
				}
				return;
			}
			// 拦截
			if (collectWork != null && !collectWork.isDone()) {
				JOptionPane.showMessageDialog(this, "任务还没有结束");
				return;
			}
			if (updateSiteWork != null && !updateSiteWork.isDone()) {
				JOptionPane.showMessageDialog(this, "请等待网站更新完成");
				return;
			}
			// 更新
			if (ComponentConst.UPDATE_BUTTON_NAME.equals(btn.getName())) {
				updateSiteWork = new UpdateSiteWork(this);
				updateSiteWork.execute();
				return;
			}

			/* 采集或者检测 */

			Boolean intoDao = false;
			// 当点击了采集按钮，才开启入库模式
			if (ComponentConst.COLLECT_BUTTON_NAME.equals(btn.getName())) {
				intoDao = true;
			}
			// 先验证页码是否合法或者合理
			if (!pageValidator()) {
				return;
			}
			// 获取两个页码表单域的值
			String startPage = startField.getText();
			String endPage = endField.getText();
			int start = Integer.parseInt(startPage);
			int end = Integer.parseInt(endPage);
			int siteIndex = siteNameCombo.getSelectedIndex();
			int typeIndex = typeNameCombo.getSelectedIndex();
			/*ISiteEngine siteEngine = null;
			SiteInfo siteInfo = sites.get(siteIndex);
			siteInfo.setChangable(typeIndex, start, end, intoDao);
			if ("搜娱电子书".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new SooYuuEngine(siteInfo);
			} else if ("爱奇电子书".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new AiQiEngine(siteInfo);
			} else if ("久久电子书".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new JiuJiuEngine(siteInfo);
			} else if ("半亩方塘".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new BanTangEngine(siteInfo);
			} else if ("落吧书屋".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new Luo8Engine(siteInfo);
			} else if ("TXT456".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new Txt456Engine(siteInfo);
			} else if ("奇书网".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new QiShuEngine(siteInfo);
			} else if ("书香电子书".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new ShuXiangEngine(siteInfo);
			} else if ("爱电子书吧".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new IBook8Engine(siteInfo);
			} else if ("哈18".equals(siteNameCombo.getSelectedItem())) {
				siteEngine = new Ha18Engine(siteInfo);
			}
			if (siteEngine == null) {
				JOptionPane.showMessageDialog(this,
						siteNameCombo.getSelectedItem() + "还没有可以处理的引擎实例");
				return;
			}
			// 开启业务线程
			collectWork = new CollectWork(siteEngine, this);*/
			collectWork.execute();
		}
	}

	/**
	 * 根据所选的采集站点和小说类型动态为起始页码和结束页码赋值，默认二者是一样的
	 */
	private void setPageFieldText() {
		// 获取选择站点的索引，索引从0开始
		int siteIndex = siteNameCombo.getSelectedIndex();
		// 获取选择类型的索引，索引从0开始
		int typeIndex = typeNameCombo.getSelectedIndex();
	}

	/**
	 * 添加组件到主容器
	 * 
	 * @param components
	 */
	private void addComponents(Component... components) {
		if (components != null) {
			for (Component comp : components) {
				this.getContentPane().add(comp);
			}
		}
	}

	private boolean pageValidator() {
		// 获取两个页码表单域的值
		String startPage = startField.getText();
		String endPage = endField.getText();
		/* 验证页码 */
		if (IniPropertyValidator.invalidPositiveInteger(startPage)) {
			JOptionPane.showMessageDialog(this, "起始页码：请输入正整数。");
			// 选中页码值
			startField.selectAll();
			// 光标定位到输入框
			startField.requestFocusInWindow();
			return false;
		}
		if (IniPropertyValidator.invalidPositiveInteger(endPage)) {
			JOptionPane.showMessageDialog(this, "结束页码：请输入正整数。");
			endField.selectAll();
			endField.requestFocusInWindow();
			return false;
		}
		int start = Integer.parseInt(startPage);
		int end = Integer.parseInt(endPage);
		if (start > end) {
			JOptionPane.showMessageDialog(this, "起始页码不能大于结束页码");
			// 选中页码值
			startField.selectAll();
			// 光标定位到输入框
			startField.requestFocusInWindow();
			return false;
		}/* 验证页码结束 */
		return true;
	}
}
