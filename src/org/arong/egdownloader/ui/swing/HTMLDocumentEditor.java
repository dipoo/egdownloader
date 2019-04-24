package org.arong.egdownloader.ui.swing;

/* HTMLDocumentEditor.java 
 * @author: Charles Bell 
 * @version: May 27, 2002 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/** HTML文件文档编辑器 */
public class HTMLDocumentEditor extends JFrame implements ActionListener {
	/** 声明一个网页文档对象变量 */
	private HTMLDocument document;
	/** 创建一个文本编辑板 */
	private JTextPane textPane = new JTextPane();
	private boolean debug = false;
	/** 声明一个文件对象变量 */
	private File currentFile;

	/** 侦听在当前文档上的编辑器 */
	protected UndoableEditListener undoHandler = new UndoHandler();

	/** 添加撤消管理器 */
	protected UndoManager undo = new UndoManager();

	/** 添加撤消侦听器 */
	private UndoAction undoAction = new UndoAction();
	/** 添加恢复侦听器 */
	private RedoAction redoAction = new RedoAction();

	/** 添加剪切侦听器 */
	private Action cutAction = new DefaultEditorKit.CutAction();
	/** 添加复制侦听器 */
	private Action copyAction = new DefaultEditorKit.CopyAction();
	/** 添加粘贴侦听器 */
	private Action pasteAction = new DefaultEditorKit.PasteAction();

	/** 添加加粗侦听器 */
	private Action boldAction = new StyledEditorKit.BoldAction();
	/** 添加加下划线侦听器 */
	private Action underlineAction = new StyledEditorKit.UnderlineAction();
	/** 添加倾斜侦听器 */
	private Action italicAction = new StyledEditorKit.ItalicAction();

	private Action insertBreakAction = new DefaultEditorKit.InsertBreakAction();
	private HTMLEditorKit.InsertHTMLTextAction unorderedListAction = new HTMLEditorKit.InsertHTMLTextAction(
			"Bullets", "<ul><li> </li></ul>", HTML.Tag.P, HTML.Tag.UL);
	private HTMLEditorKit.InsertHTMLTextAction bulletAction = new HTMLEditorKit.InsertHTMLTextAction(
			"Bullets", "<li> </li>", HTML.Tag.UL, HTML.Tag.LI);

	/** 构造方法 */
	public HTMLDocumentEditor() {
		/** 设置主窗体标题 */
		super("HTMLDocumentEditor");
		HTMLEditorKit editorKit = new HTMLEditorKit();
		/** 创建默认文档指向网页引用document */
		document = (HTMLDocument) editorKit.createDefaultDocument();

		// 强制SWINGSET实现跨平台，不改变风格
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
			// 如果你想用系统的界面风格替代，请注释掉上一行代码，而取消下一行代码的注释：
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {
			// 产生异常，则显示错误消息：加载L&F错误
			System.err.println("Error loading L&F: " + exc);
		}

		// 调用初始化方法
		init();
	}

	/** 主方法，起动程序 */
	public static void main(String[] args) {
		// 创建一个类的实例，即创建一个网页编辑器
		HTMLDocumentEditor editor = new HTMLDocumentEditor();
	}

	/** 初始化各组件的方法 */
	public void init() {
		// 调用自定义继承WindowListener的侦听器FrameListener，给主窗体添加WindowListener
		addWindowListener(new FrameListener());

		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		JMenu fileMenu = new JMenu("File"); // 文件
		JMenu editMenu = new JMenu("Edit"); // 编辑
		JMenu colorMenu = new JMenu("Color"); // 颜色
		JMenu fontMenu = new JMenu("Font"); // 字体
		JMenu styleMenu = new JMenu("Style"); // 样式
		JMenu alignMenu = new JMenu("Align"); // 对齐
		JMenu helpMenu = new JMenu("Help"); // 帮助

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(colorMenu);
		menuBar.add(fontMenu);
		menuBar.add(styleMenu);
		menuBar.add(alignMenu);
		menuBar.add(helpMenu);

		JMenuItem newItem = new JMenuItem("New", new ImageIcon(
				"whatsnew-bang.gif")); // 新建
		JMenuItem openItem = new JMenuItem("Open", new ImageIcon("open.gif")); // 打开
		JMenuItem saveItem = new JMenuItem("Save", new ImageIcon("save.gif")); // 保存
		JMenuItem saveAsItem = new JMenuItem("Save As"); // 另存
		JMenuItem exitItem = new JMenuItem("Exit", new ImageIcon("exit.gif")); // 退出

		newItem.addActionListener(this);
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		saveAsItem.addActionListener(this);
		exitItem.addActionListener(this);

		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(exitItem);

		// 给菜单项添加侦听器
		JMenuItem undoItem = new JMenuItem(undoAction); // 撤消
		JMenuItem redoItem = new JMenuItem(redoAction); // 恢复
		JMenuItem cutItem = new JMenuItem(cutAction); // 剪切
		JMenuItem copyItem = new JMenuItem(copyAction); // 复制
		JMenuItem pasteItem = new JMenuItem(pasteAction); // 粘贴
		JMenuItem clearItem = new JMenuItem("Clear"); // 清除
		JMenuItem selectAllItem = new JMenuItem("Select All"); // 全选
		JMenuItem insertBreaKItem = new JMenuItem(insertBreakAction);
		JMenuItem unorderedListItem = new JMenuItem(unorderedListAction);
		JMenuItem bulletItem = new JMenuItem(bulletAction); // 项目符号

		cutItem.setText("Cut");
		copyItem.setText("Copy");
		pasteItem.setText("Paste");
		insertBreaKItem.setText("Break");
		cutItem.setIcon(new ImageIcon("cut.gif"));
		copyItem.setIcon(new ImageIcon("copy.gif"));
		pasteItem.setIcon(new ImageIcon("paste.gif"));
		insertBreaKItem.setIcon(new ImageIcon("break.gif"));
		unorderedListItem.setIcon(new ImageIcon("bullets.gif"));

		clearItem.addActionListener(this);
		selectAllItem.addActionListener(this);

		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(clearItem);
		editMenu.add(selectAllItem);
		editMenu.add(insertBreaKItem);
		editMenu.add(unorderedListItem);
		editMenu.add(bulletItem);

		JMenuItem redTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Red", Color.red));
		JMenuItem orangeTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Orange", Color.orange));
		JMenuItem yellowTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Yellow", Color.yellow));
		JMenuItem greenTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Green", Color.green));
		JMenuItem blueTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Blue", Color.blue));
		JMenuItem cyanTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Cyan", Color.cyan));
		JMenuItem magentaTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Magenta", Color.magenta));
		JMenuItem blackTextItem = new JMenuItem(
				new StyledEditorKit.ForegroundAction("Black", Color.black));

		redTextItem.setIcon(new ImageIcon("red.gif"));
		orangeTextItem.setIcon(new ImageIcon("orange.gif"));
		yellowTextItem.setIcon(new ImageIcon("yellow.gif"));
		greenTextItem.setIcon(new ImageIcon("green.gif"));
		blueTextItem.setIcon(new ImageIcon("blue.gif"));
		cyanTextItem.setIcon(new ImageIcon("cyan.gif"));
		magentaTextItem.setIcon(new ImageIcon("magenta.gif"));
		blackTextItem.setIcon(new ImageIcon("black.gif"));

		colorMenu.add(redTextItem);
		colorMenu.add(orangeTextItem);
		colorMenu.add(yellowTextItem);
		colorMenu.add(greenTextItem);
		colorMenu.add(blueTextItem);
		colorMenu.add(cyanTextItem);
		colorMenu.add(magentaTextItem);
		colorMenu.add(blackTextItem);

		JMenu fontTypeMenu = new JMenu("Font Type");
		fontMenu.add(fontTypeMenu);

		String[] fontTypes = { "SansSerif", "Serif", "Monospaced", "Dialog",
				"DialogInput" };
		for (int i = 0; i < fontTypes.length; i++) {
			if (debug)
				System.out.println(fontTypes[i]);
			JMenuItem nextTypeItem = new JMenuItem(fontTypes[i]);
			nextTypeItem.setAction(new StyledEditorKit.FontFamilyAction(
					fontTypes[i], fontTypes[i]));
			fontTypeMenu.add(nextTypeItem);
		}

		JMenu fontSizeMenu = new JMenu("Font Size");
		fontMenu.add(fontSizeMenu);

		int[] fontSizes = { 6, 8, 10, 12, 14, 16, 20, 24, 32, 36, 48, 72 };
		for (int i = 0; i < fontSizes.length; i++) {
			if (debug)
				System.out.println(fontSizes[i]);
			JMenuItem nextSizeItem = new JMenuItem(String.valueOf(fontSizes[i]));
			nextSizeItem.setAction(new StyledEditorKit.FontSizeAction(String
					.valueOf(fontSizes[i]), fontSizes[i]));
			fontSizeMenu.add(nextSizeItem);
		}

		JMenuItem boldMenuItem = new JMenuItem(boldAction);
		JMenuItem underlineMenuItem = new JMenuItem(underlineAction);
		JMenuItem italicMenuItem = new JMenuItem(italicAction);

		boldMenuItem.setText("Bold");
		underlineMenuItem.setText("Underline");
		italicMenuItem.setText("Italic");

		boldMenuItem.setIcon(new ImageIcon("bold.gif"));
		underlineMenuItem.setIcon(new ImageIcon("underline.gif"));
		italicMenuItem.setIcon(new ImageIcon("italic.gif"));

		styleMenu.add(boldMenuItem);
		styleMenu.add(underlineMenuItem);
		styleMenu.add(italicMenuItem);

		JMenuItem subscriptMenuItem = new JMenuItem(new SubscriptAction());
		JMenuItem superscriptMenuItem = new JMenuItem(new SuperscriptAction());
		JMenuItem strikeThroughMenuItem = new JMenuItem(
				new StrikeThroughAction());

		subscriptMenuItem.setText("Subscript");
		superscriptMenuItem.setText("Superscript");
		strikeThroughMenuItem.setText("StrikeThrough");

		subscriptMenuItem.setIcon(new ImageIcon("subscript.gif"));
		superscriptMenuItem.setIcon(new ImageIcon("superscript.gif"));
		strikeThroughMenuItem.setIcon(new ImageIcon("strikethough.gif"));

		styleMenu.add(subscriptMenuItem);
		styleMenu.add(superscriptMenuItem);
		styleMenu.add(strikeThroughMenuItem);

		JMenuItem leftAlignMenuItem = new JMenuItem(
				new StyledEditorKit.AlignmentAction("Left Align",
						StyleConstants.ALIGN_LEFT));
		JMenuItem centerMenuItem = new JMenuItem(
				new StyledEditorKit.AlignmentAction("Center",
						StyleConstants.ALIGN_CENTER));
		JMenuItem rightAlignMenuItem = new JMenuItem(
				new StyledEditorKit.AlignmentAction("Right Align",
						StyleConstants.ALIGN_RIGHT));

		leftAlignMenuItem.setText("Left Align");
		centerMenuItem.setText("Center");
		rightAlignMenuItem.setText("Right Align");

		leftAlignMenuItem.setIcon(new ImageIcon("left.gif"));
		centerMenuItem.setIcon(new ImageIcon("center.gif"));
		rightAlignMenuItem.setIcon(new ImageIcon("right.gif"));

		alignMenu.add(leftAlignMenuItem);
		alignMenu.add(centerMenuItem);
		alignMenu.add(rightAlignMenuItem);

		JMenuItem helpItem = new JMenuItem("帮助");
		helpItem.addActionListener(this);
		helpMenu.add(helpItem);

		JMenuItem shortcutsItem = new JMenuItem("Keyboard Shortcuts");
		shortcutsItem.addActionListener(this);
		helpMenu.add(shortcutsItem);

		JMenuItem aboutItem = new JMenuItem("About QuantumHyperSpace");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);

		JPanel editorControlPanel = new JPanel();
		// editorControlPanel.setLayout(new GridLayout(3,3));
		editorControlPanel.setLayout(new FlowLayout());

		/* 按钮 */
		JButton cutButton = new JButton(cutAction); // 创建“剪切”按钮，添加剪切侦听
		JButton copyButton = new JButton(copyAction); // 创建“复制”按钮，添加复制侦
		JButton pasteButton = new JButton(pasteAction); // 创建“粘贴”按钮，添加粘贴侦

		JButton boldButton = new JButton(boldAction); // 创建“加粗”按钮，添加加粗侦听
		JButton underlineButton = new JButton(underlineAction); // 创建“下划线”按钮，添加下划线侦听
		JButton italicButton = new JButton(italicAction); // 创建“斜体”按钮，添加斜体侦听

		// JButton insertButton = new JButton(insertAction);
		// JButton insertBreakButton = new JButton(insertBreakAction);
		// JButton tabButton = new JButton(tabAction);

		cutButton.setText("Cut");
		copyButton.setText("Copy");
		pasteButton.setText("Paste");

		boldButton.setText("Bold");
		underlineButton.setText("Underline");
		italicButton.setText("Italic");

		// insertButton.setText("Insert");
		// insertBreakButton.setText("Insert Break");
		// tabButton.setText("Tab");

		cutButton.setIcon(new ImageIcon("cut.gif"));
		copyButton.setIcon(new ImageIcon("copy.gif"));
		pasteButton.setIcon(new ImageIcon("paste.gif"));

		boldButton.setIcon(new ImageIcon("bold.gif"));
		underlineButton.setIcon(new ImageIcon("underline.gif"));
		italicButton.setIcon(new ImageIcon("italic.gif"));

		editorControlPanel.add(cutButton);
		editorControlPanel.add(copyButton);
		editorControlPanel.add(pasteButton);

		editorControlPanel.add(boldButton);
		editorControlPanel.add(underlineButton);
		editorControlPanel.add(italicButton);

		// editorControlPanel.add(insertButton);
		// editorControlPanel.add(insertBreakButton);
		// editorControlPanel.add(tabButton);

		JButton subscriptButton = new JButton(new SubscriptAction());
		JButton superscriptButton = new JButton(new SuperscriptAction());
		JButton strikeThroughButton = new JButton(new StrikeThroughAction());

		subscriptButton.setIcon(new ImageIcon("subscript.gif"));
		superscriptButton.setIcon(new ImageIcon("superscript.gif"));
		strikeThroughButton.setIcon(new ImageIcon("strikethough.gif"));

		JPanel specialPanel = new JPanel();
		specialPanel.setLayout(new FlowLayout());

		specialPanel.add(subscriptButton);
		specialPanel.add(superscriptButton);
		specialPanel.add(strikeThroughButton);

		// JButton leftAlignButton = new JButton(new AlignLeftAction());
		// JButton centerButton = new JButton(new CenterAction());
		// JButton rightAlignButton = new JButton(new AlignRightAction());

		JButton leftAlignButton = new JButton(
				new StyledEditorKit.AlignmentAction("Left Align",
						StyleConstants.ALIGN_LEFT));
		JButton centerButton = new JButton(new StyledEditorKit.AlignmentAction(
				"Center", StyleConstants.ALIGN_CENTER));
		JButton rightAlignButton = new JButton(
				new StyledEditorKit.AlignmentAction("Right Align",
						StyleConstants.ALIGN_RIGHT));
		JButton colorButton = new JButton(new StyledEditorKit.AlignmentAction(
				"Right Align", StyleConstants.ALIGN_RIGHT));

		leftAlignButton.setIcon(new ImageIcon("left.gif"));
		centerButton.setIcon(new ImageIcon("center.gif"));
		rightAlignButton.setIcon(new ImageIcon("right.gif"));
		colorButton.setIcon(new ImageIcon("color.gif"));

		leftAlignButton.setText("Left Align");
		centerButton.setText("Center");
		rightAlignButton.setText("Right Align");

		JPanel alignPanel = new JPanel();
		alignPanel.setLayout(new FlowLayout());
		alignPanel.add(leftAlignButton);
		alignPanel.add(centerButton);
		alignPanel.add(rightAlignButton);

		document.addUndoableEditListener(undoHandler);
		resetUndoManager();

		textPane = new JTextPane(document);
		textPane.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(textPane);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scrollPaneSize = new Dimension(5 * screenSize.width / 8,
				5 * screenSize.height / 8);
		scrollPane.setPreferredSize(scrollPaneSize);

		// 创建工具栏面板，并设置面板布局管理器，添加子面板
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new BorderLayout());
		toolPanel.add(editorControlPanel, BorderLayout.NORTH);
		toolPanel.add(specialPanel, BorderLayout.CENTER);
		toolPanel.add(alignPanel, BorderLayout.SOUTH);

		// 向主窗体添加菜单栏
		getContentPane().add(menuBar, BorderLayout.NORTH);
		// 向主窗体添加工具栏
		getContentPane().add(toolPanel, BorderLayout.CENTER);
		// 向主窗体添加滚动面板
		getContentPane().add(scrollPane, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		startNewDocument();
		show();
	}

	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		if (debug) {
			int modifier = ae.getModifiers();
			long when = ae.getWhen();
			String parameter = ae.paramString();
			System.out.println("actionCommand: " + actionCommand);
			System.out.println("modifier: " + modifier);
			System.out.println("when: " + when);
			System.out.println("parameter: " + parameter);
		}
		if (actionCommand.compareTo("New") == 0) {
			startNewDocument();
		} else if (actionCommand.compareTo("Open") == 0) {
			openDocument();
		} else if (actionCommand.compareTo("Save") == 0) {
			saveDocument();
		} else if (actionCommand.compareTo("Save As") == 0) {
			saveDocumentAs();
		} else if (actionCommand.compareTo("Exit") == 0) {
			exit();
		} else if (actionCommand.compareTo("Clear") == 0) {
			clear();
		} else if (actionCommand.compareTo("Select All") == 0) {
			selectAll();
		} else if (actionCommand.compareTo("帮助") == 0) {
			help();
		} else if (actionCommand.compareTo("Keyboard Shortcuts") == 0) {
			showShortcuts();
		} else if (actionCommand.compareTo("About QuantumHyperSpace") == 0) {
			aboutQuantumHyperSpace();
		}
	}

	public void startNewDocument() {
		Document oldDoc = textPane.getDocument();
		if (oldDoc != null)
			oldDoc.removeUndoableEditListener(undoHandler);
		HTMLEditorKit editorKit = new HTMLEditorKit();
		document = (HTMLDocument) editorKit.createDefaultDocument();
		textPane.setDocument(document);
		currentFile = null;
		setTitle("HTMLDocumentEditor");
		textPane.getDocument().addUndoableEditListener(undoHandler);
		resetUndoManager();
	}

	public void openDocument() {
		try {
			File current = new File(".");
			JFileChooser chooser = new JFileChooser(current);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new HTMLFileFilter());
			int approval = chooser.showSaveDialog(this);
			if (approval == JFileChooser.APPROVE_OPTION) {
				currentFile = chooser.getSelectedFile();
				setTitle(currentFile.getName());
				FileReader fr = new FileReader(currentFile);
				Document oldDoc = textPane.getDocument();
				if (oldDoc != null)
					oldDoc.removeUndoableEditListener(undoHandler);
				HTMLEditorKit editorKit = new HTMLEditorKit();
				document = (HTMLDocument) editorKit.createDefaultDocument();
				editorKit.read(fr, document, 0);
				document.addUndoableEditListener(undoHandler);
				textPane.setDocument(document);
				resetUndoManager();
			}
		} catch (BadLocationException ble) {
			System.err.println("BadLocationException: " + ble.getMessage());
		} catch (FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public void saveDocument() {
		if (currentFile != null) {
			try {
				FileWriter fw = new FileWriter(currentFile);
				fw.write(textPane.getText());
				fw.close();
			} catch (FileNotFoundException fnfe) {
				System.err.println("FileNotFoundException: "
						+ fnfe.getMessage());
			} catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
		} else {
			saveDocumentAs();
		}
	}

	public void saveDocumentAs() {
		try {
			File current = new File(".");
			JFileChooser chooser = new JFileChooser(current);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new HTMLFileFilter());
			int approval = chooser.showSaveDialog(this);
			if (approval == JFileChooser.APPROVE_OPTION) {
				File newFile = chooser.getSelectedFile();
				if (newFile.exists()) {
					String message = newFile.getAbsolutePath()
							+ " already exists. /n"
							+ "Do you want to replace it?";
					if (JOptionPane.showConfirmDialog(this, message) == JOptionPane.YES_OPTION) {
						currentFile = newFile;
						setTitle(currentFile.getName());
						FileWriter fw = new FileWriter(currentFile);
						fw.write(textPane.getText());
						fw.close();
						if (debug)
							System.out.println("Saved "
									+ currentFile.getAbsolutePath());
					}
				} else {
					currentFile = new File(newFile.getAbsolutePath());
					setTitle(currentFile.getName());
					FileWriter fw = new FileWriter(currentFile);
					fw.write(textPane.getText());
					fw.close();
					if (debug)
						System.out.println("Saved "
								+ currentFile.getAbsolutePath());
				}
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public void exit() {
		String exitMessage = "Are you sure you want to exit?";
		if (JOptionPane.showConfirmDialog(this, exitMessage) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/** 调用startNewDocument()方法，清除当前文本，开始一个新文档 */
	public void clear() {
		startNewDocument();
	}

	/** 调用JTextPane的全选方法 */
	public void selectAll() {
		textPane.selectAll();
	}

	/** 利用消息框显示帮助信息 */
	public void help() {
		JOptionPane.showMessageDialog(this, "DocumentEditor.java/n"
				+ "Author: Charles Bell/n" + "Version: May 25, 2002/n"
				+ "http://www.quantumhyperspace.com/n"
				+ "QuantumHyperSpace Programming Services");
	}

	/** 利用消息框显示快捷键 */
	public void showShortcuts() {
		String shortcuts = "Navigate in | Tab/n" + "Navigate out | Ctrl+Tab/n"
				+ "Navigate out backwards | Shift+Ctrl+Tab/n"
				+ "Move up/down a line | Up/Down Arrown/n"
				+ "Move left/right a component or char | Left/Right Arrow/n"
				+ "Move up/down one vertical block | PgUp/PgDn/n"
				+ "Move to start/end of line | Home/End/n"
				+ "Move to previous/next word | Ctrl+Left/Right Arrow/n"
				+ "Move to start/end of data | Ctrl+Home/End/n"
				+ "Move left/right one block | Ctrl+PgUp/PgDn/n"
				+ "Select All | Ctrl+A/n"
				+ "Extend selection up one line | Shift+Up Arrow/n"
				+ "Extend selection down one line | Shift+Down Arrow/n"
				+ "Extend selection to beginning of line | Shift+Home/n"
				+ "Extend selection to end of line | Shift+End/n"
				+ "Extend selection to beginning of data | Ctrl+Shift+Home/n"
				+ "Extend selection to end of data | Ctrl+Shift+End/n"
				+ "Extend selection left | Shift+Right Arrow/n"
				+ "Extend selection right | Shift+Right Arrow/n"
				+ "Extend selection up one vertical block | Shift+PgUp/n"
				+ "Extend selection down one vertical block | Shift+PgDn/n"
				+ "Extend selection left one block | Ctrl+Shift+PgUp/n"
				+ "Extend selection right one block | Ctrl+Shift+PgDn/n"
				+ "Extend selection left one word | Ctrl+Shift+Left Arrow/n"
				+ "Extend selection right one word | Ctrl+Shift+Right Arrow/n";
		JOptionPane.showMessageDialog(this, shortcuts);
	}

	public void aboutQuantumHyperSpace() {
		JOptionPane.showMessageDialog(this,
				"QuantumHyperSpace Programming Services/n"
						+ "http://www.quantumhyperspace.com/n"
						+ "email: support@quantumhyperspace.com/n" + " or /n"
						+ "email: charles@quantumhyperspace.com/n",
				"QuantumHyperSpace", JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon("quantumhyperspace.gif"));
	}

	/** 内部类：自定义继承WindowListener的侦听器FrameListener */
	class FrameListener extends WindowAdapter {
		/** 处理点击窗体关闭按钮事件，实现程序的关闭停止 */
		public void windowClosing(WindowEvent we) {
			exit();
		}
	}

	class SubscriptAction extends StyledEditorKit.StyledTextAction {

		public SubscriptAction() {
			super(StyleConstants.Subscript.toString());
		}

		public void actionPerformed(ActionEvent ae) {
			JEditorPane editor = getEditor(ae);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean subscript = (StyleConstants.isSubscript(attr)) ? false
						: true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSubscript(sas, subscript);
				setCharacterAttributes(editor, sas, false);
			}
		}
	}

	class SuperscriptAction extends StyledEditorKit.StyledTextAction {

		public SuperscriptAction() {
			super(StyleConstants.Superscript.toString());
		}

		public void actionPerformed(ActionEvent ae) {
			JEditorPane editor = getEditor(ae);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean superscript = (StyleConstants.isSuperscript(attr)) ? false
						: true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSuperscript(sas, superscript);
				setCharacterAttributes(editor, sas, false);
			}
		}
	}

	class StrikeThroughAction extends StyledEditorKit.StyledTextAction {
		public StrikeThroughAction() {
			super(StyleConstants.StrikeThrough.toString());
		}

		public void actionPerformed(ActionEvent ae) {
			JEditorPane editor = getEditor(ae);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean strikeThrough = (StyleConstants.isStrikeThrough(attr)) ? false
						: true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setStrikeThrough(sas, strikeThrough);
				setCharacterAttributes(editor, sas, false);
			}
		}
	}

	class HTMLFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File f) {
			return ((f.isDirectory()) || (f.getName().toLowerCase()
					.indexOf(".htm") > 0));
		}

		public String getDescription() {
			return "html";
		}
	}

	protected void resetUndoManager() {
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
	}

	class UndoHandler implements UndoableEditListener {
		/**
		 * Messaged when the Document has created an edit, the edit is added to
		 * <code>undo</code>, an instance of UndoManager.
		 */
		public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}

	class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if (undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	class RedoAction extends AbstractAction {

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				System.err.println("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if (undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
}
