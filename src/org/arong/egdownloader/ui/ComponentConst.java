package org.arong.egdownloader.ui;

import java.util.HashMap;
import java.util.Map;

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
	
	public final static String SETTING_DATA_PATH = "data/setting.db";//配置文件保存路径
	
	public final static String TASK_DATA_PATH = "data/task.db";//任务数据保存路径
	
	public final static String RESOURCES_PATH = "/resources/";
	
	public final static String ICON_PATH = RESOURCES_PATH + "icon/";
	
	public final static String[] TASK_TABLE_HEADER = {"", "名称", "数目(P)", "已下载", "大小(M)", "状态"};
	
	public final static int MAX_TASK_PAGE = 14;//一页显示任务数
	
	public final static int SKIN_NUM = 1;//皮肤编号
	
	public final static Map<String, String> SKIN_ICON = new HashMap<String, String>();
	
	static{
		SKIN_ICON.put("add", "_add.png");
		SKIN_ICON.put("delete", "_delete.png");
		SKIN_ICON.put("setting", "_setting.png");
		SKIN_ICON.put("download", "_download.png");
		SKIN_ICON.put("select", "_select.png");
		SKIN_ICON.put("folder", "_folder.png");
		SKIN_ICON.put("size", "_size.png");
		SKIN_ICON.put("tool", "_tool.png");
		SKIN_ICON.put("picture", "_picture.png");
		SKIN_ICON.put("user", "_user.png");
	}
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
	public final static String DECODE_AND_ENCODE_MENUITEM_NAME = "docodeAndEncodeMenuItem";
	
	//组件的text值
	public final static String ADD_MENU_TEXT = "新建";
	public final static String DELETE_MENU_TEXT = "删除";
	public final static String SETTING_MENU_TEXT = "配置";
	public final static String TOOLS_MENU_TEXT = "工具";
	public final static String ABOUT_MENU_TEXT = "关于";
	public final static String DECODE_AND_ENCODE_TAB_TEXT = "加密/解密";
	
	public final static String POPUP_DETAIL_MENU_TEXT = "查看";
	
	
	/*AboutMenuWindow内的组件*/
	public final static String ABOUT_TEXTPANE_TEXT ="<h3>" + Version.NAME + "</h3>作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者：<b>" + Version.AUTHOR + "</b><br>版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本：<b>" + Version.VERSION +"</b><br>更新时间：<b>" + Version.MODIFLIED + "</b><br>官方网站：https://github.com/dipoo/egdownloader";
	
	/*DecodeAndEncodeWindow内的组件*/
	public final static String DECODE_BUTTON_NAME = "decodeButton";
	public final static String ENCODE_BUTTON_NAME = "encodeButton";
	public final static String SHOULU_URL_BUTTON_NAME = "shouluUrlButton";
	
	
	public final static String DECODE_AND_ENCODE_INPUT_BORDER_TEXT = "加密或解密的文本";
	public final static String DECODE_AND_ENCODE_OUTPUT_BORDER_TEXT = "加密或解密的结果";
	public final static String ENCODE_BUTTON_TEXT = "加密";
	public final static String DECODE_BUTTON_TEXT = "解密";
	
	public final static String TOOLS_WINDOW_TITLE = "工具";
}
