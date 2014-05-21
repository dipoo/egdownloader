package org.arong.egdownloader.ui;

import org.arong.egdownloader.version.Version;

/**
 * 定义各个window的title值<br>
 * 定义用于在监听器中标识swing组件对象的name值<br>
 * 定义各个组件的text值<br>
 * @author 阿荣
 * @since 2013-8-18
 *
 */
public final class ComponentConst {
	public final static int CLIENT_WIDTH = 640;//主窗口宽
	
	public final static int CLIENT_HEIGHT = 480;//主窗口高
	
	public final static int MARGIN_X = 32;//组件之间水平相隔
	
	public final static int MARGIN_y = 32;//组件之间垂直间隔
	
	public final static String RESOURCES_PATH = "/resources/";
	
	public final static String ICON_PATH = RESOURCES_PATH + "icon/";
	/*MainWindow内的组件*/
	//组件的name值
	
	
	//组件的text值
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*公共的*/
	public final static String QUERY_BUTTON_TEXT = "查询";
	/*MainWindow内的组件*/
	//组件的name值
	public final static String SETTING_MENU_NAME = "settingMenu";
	public final static String TOOLS_MENU_NAME = "toolsMenu";
	public final static String ABOUT_MENU_NAME = "aboutMenu";
	public final static String SHOULU_MENUITEM_NAME = "shouluMenuItem";
	public final static String ALEXA_MENUITEM_NAME = "alexaMenuItem";
	public final static String DECODE_AND_ENCODE_MENUITEM_NAME = "docodeAndEncodeMenuItem";
	public final static String SITENAME_COMBOBOX_NAME = "siteNameCombo";
	public final static String TYPENAME_COMBOBOX_NAME = "typeNameCombo";
	public final static String START_FIELD_NAME = "startField";
	public final static String end_FIELD_NAME = "endField";
	public final static String PAGEONE_BUTTON_NAME = "pageOneButton";
	public final static String ADDPAGE_BUTTON_NAME = "addPageButton";
	public final static String CLEAR_BUTTON_NAME = "clearButton";
	public final static String CHECK_BUTTON_NAME = "checkButton";
	public final static String COLLECT_BUTTON_NAME = "collectButton";
	public final static String CANCEL_BUTTON_NAME = "cancelButton";
	public final static String UPDATE_BUTTON_NAME = "updateButton";
	public final static String AUTHOR_BUTTON_NAME = "authorButton";
	
	//组件的text值
	public final static String SITENAME_LABEL_TEXT = "采集站点";
	public final static String TYPENAME_LABEL_TEXT = "书籍类型";
	public final static String START_LABEL_TEXT = "起始页码";
	public final static String END_LABEL_TEXT = "结束页码";
	public final static String PAGEONE_BUTTON_TEXT = "第一页";
	public final static String ADDPAGE_BUTTON_TEXT = "+1";
	public final static String CHECK_BUTTON_TEXT = "采前检测";
	public final static String COLLECT_BUTTON_TEXT = "开始采集";
	public final static String CLEAR_BUTTON_TEXT = "清屏";
	public final static String CANCEL_BUTTON_TEXT = "取消任务";
	public final static String UPDATE_BUTTON_TEXT = "更新站点";
	public final static String AUTHOR_BUTTON_TEXT = "生成作者";
	public final static String SETTING_MENU_TEXT = "配置";
	public final static String TOOLS_MENU_TEXT = "工具";
	public final static String ABOUT_MENU_TEXT = "关于";
	public final static String SHOULU_TAB_TEXT = "收录";
	public final static String ALEXA_TAB_TEXT = "排名";
	public final static String DECODE_AND_ENCODE_TAB_TEXT = "加密/解密";
	
	
	/*AboutMenuWindow内的组件*/
	public final static String ABOUT_TEXTPANE_TEXT ="<h2>" + Version.NAME + "</h2>作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者：<b>" + Version.AUTHOR + "</b><br>版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本：<b>" + Version.VERSION +"</b><br>更新时间：<b>" + Version.MODIFLIED + "</b><br>官方网站：<a href='http://www.shujixiazai.com/'>http://www.shujixiazai.com/</a><br>其他网站：<a href='http://dipoo.j1.fjjsp.net/'>http://dipoo.j1.fjjsp.net/</a>";
	
	/*DecodeAndEncodeWindow内的组件*/
	public final static String DECODE_BUTTON_NAME = "decodeButton";
	public final static String ENCODE_BUTTON_NAME = "encodeButton";
	public final static String SHOULU_URL_BUTTON_NAME = "shouluUrlButton";
	
	
	public final static String DECODE_AND_ENCODE_INPUT_BORDER_TEXT = "加密或解密的文本";
	public final static String DECODE_AND_ENCODE_OUTPUT_BORDER_TEXT = "加密或解密的结果";
	public final static String ENCODE_BUTTON_TEXT = "加密";
	public final static String DECODE_BUTTON_TEXT = "解密";
	public final static String SHOULU_URL_LABEL_TEXT = "网址";
	
	public final static String TOOLS_WINDOW_TITLE = "工具";
}
