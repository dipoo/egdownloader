package org.arong.egdownloader.ui;

import java.awt.Font;

/**
 * 用于将所有组件的字体改变
 * @author 阿荣
 * @since 2013-8-18
 */
public class FontConst {
	public static String[] DEFAULT_FONT = new String[] { "Table.font",
		"TableHeader.font", "CheckBox.font", "Tree.font", "Viewport.font",
		"ProgressBar.font", "RadioButtonMenuItem.font",
		"ToolBar.font",
		"ColorChooser.font",
		"ToggleButton.font",
		"Panel.font",
		"Label.font",
		"MenuItem.font",
		"Button.font",
		"Menu.font",
		"TextField.font",
		"OptionPane.font",
		/*"TextArea.font"*,/
		"TableHeader.font"
		"MenuBar.font", 
		"PasswordField.font", "ScrollPane.font",
		/*"ToolTip.font",*/ "List.font", "EditorPane.font", "Table.font",
		"TabbedPane.font", "RadioButton.font", "CheckBoxMenuItem.font",
		"TextPane.font", "PopupMenu.font", "TitledBorder.font",
		"ComboBox.font" };
	// 调整默认字体
	/*for (int i = 0; i < FontConst.DEFAULT_FONT.length; i++) {
		UIManager.put(FontConst.DEFAULT_FONT[i], new Font("微软雅黑",
				Font.BOLD, 13));
	}*/
	
	public final static Font Comic_Sans_MS_BOLD_12 = new Font("Comic Sans MS", Font.BOLD, 12);
	public final static Font Comic_Sans_MS_BOLD_11 = new Font("Comic Sans MS", Font.BOLD, 11);
	public final static Font Georgia_BOLD_12 = new Font("Georgia", Font.BOLD, 12);
	public final static Font Songti_PLAIN_11 = new Font("宋体", Font.PLAIN, 11);
	public final static Font Songti_BOLD_12 = new Font("宋体", Font.BOLD, 12);
	public final static Font Songti_BOLD_13 = new Font("宋体", Font.BOLD, 13);
	public final static Font Microsoft_PLAIN_11 = new Font("微软雅黑", Font.PLAIN, 11);
	public final static Font Microsoft_BOLD_11 = new Font("微软雅黑", Font.BOLD, 11);
	public final static Font Microsoft_BOLD_12 = new Font("微软雅黑", Font.BOLD, 12);
	public final static Font Microsoft_BOLD_13 = new Font("微软雅黑", Font.BOLD, 13);
}
