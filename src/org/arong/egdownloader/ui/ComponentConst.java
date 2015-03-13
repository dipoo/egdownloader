package org.arong.egdownloader.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
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
	public static EgDownloaderWindow mainWindow;
	
	public final static int CLIENT_WIDTH = 1024;//主窗口宽
	
	public final static int CLIENT_HEIGHT = 680;//主窗口高
	
	public final static int MARGIN_X = 32;//组件之间水平相隔
	
	public final static int MARGIN_y = 32;//组件之间垂直间隔
	
	public static String groupName = "";//任务组名称
	
	public static String ROOT_DATA_PATH = "data";//数据根目录
	
	public static String DATA_PATH = "data";//数据目录
	
	public static String CACHE_PATH = "cache";//缓存目录
	
	public static String SETTING_XML_DATA_PATH = DATA_PATH + "/setting.xml";//配置文件保存路径
	
	public static String TASK_XML_DATA_PATH = DATA_PATH + "/task.xml";//任务数据保存路径
	
	public static String PICTURE_XML_DATA_PATH = DATA_PATH + "/picture.xml";//图片数据保存路径
	
	public final static String RESOURCES_PATH = "/resources/";
	
	public final static String ICON_PATH = RESOURCES_PATH + "icon/";
	
	public final static String[] TASK_TABLE_HEADER = {"", "名称", "数目(P)", "语言", "类别", "已下载", "状态"};
	
	public final static String[] PICTURE_TABLE_HEADER = {"序号", "名称", "大小", "状态", "地址", "下载时间"};
	
	public final static int MAX_TASK_PAGE = 14;//一页显示任务数
	
	public final static int SKIN_NUM = 1;//皮肤编号
	
	public final static Map<String, String> SKIN_ICON = new HashMap<String, String>();
	
	public static String docHtml;
	
	public static String countHtml;
	
	static{
		InputStream s = null;
		try {
			s = ComponentConst.class.getResourceAsStream(RESOURCES_PATH + "doc.html");
			docHtml = WebClient.read(s, 0);
		} finally{
			if(s != null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(docHtml == null)
				docHtml = "没有找到" + RESOURCES_PATH + "doc.html文件";
		}
		try {
			s = ComponentConst.class.getResourceAsStream(RESOURCES_PATH + "count.html");
			countHtml = WebClient.read(s, 0);
		} finally{
			if(s != null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(countHtml == null)
				countHtml = "没有找到" + RESOURCES_PATH + "count.html文件";
		}
		
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
		SKIN_ICON.put("empty", "_empty.png");
		SKIN_ICON.put("save", "_save.png");
		SKIN_ICON.put("opera", "_opera.png");
		SKIN_ICON.put("detail", "_detail.png");
		SKIN_ICON.put("copy", "_copy.png");
		SKIN_ICON.put("cut", "_cut.png");
		SKIN_ICON.put("paste", "_paste.png");
		SKIN_ICON.put("browse", "_browse.png");
		SKIN_ICON.put("check", "_check.png");
		SKIN_ICON.put("change", "_change.png");
		SKIN_ICON.put("reset", "_reset.png");
		SKIN_ICON.put("ok", "_ok.png");
		SKIN_ICON.put("start", "_start.png");
		SKIN_ICON.put("stop", "_stop.png");
		SKIN_ICON.put("count", "_count.png");
		SKIN_ICON.put("group", "_group.png");
		SKIN_ICON.put("clear", "_clear.png");
		SKIN_ICON.put("task", "_task.png");
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
	public final static String START_MENU_TEXT = "开始";
	public final static String STOP_MENU_TEXT = "暂停";
	public final static String DELETE_MENU_TEXT = "删除";
	public final static String TASKGROUP_MENU_TEXT = "任务组";
	public final static String SETTING_MENU_TEXT = "配置";
	public final static String OPERA_MENU_TEXT = "操作";
	public final static String SEARCH_MENU_TEXT = "搜索";
	public final static String CONSOLE_MENU_TEXT = "控制台";
	public final static String COUNT_MENU_TEXT = "统计";
	public final static String ABOUT_MENU_TEXT = "关于";
	public final static String DECODE_AND_ENCODE_TAB_TEXT = "加密/解密";
	
	public final static String POPUP_START_MENU_TEXT = "开始任务   ";
	public final static String POPUP_STOP_MENU_TEXT = "暂停任务   ";
	public final static String POPUP_DETAIL_MENU_TEXT = "详细信息   ";
	public final static String POPUP_COPYURL_MENU_TEXT = "复制地址   ";
	public final static String POPUP_OPENFOLDER_MENU_TEXT = "打开文件夹   ";
	public final static String POPUP_OPENWEBPAGE_MENU_TEXT = "浏览下载页   ";
	public final static String POPUP_DOWNLOADCOVER_MENU_TEXT = "下载封面   ";
	public final static String POPUP_CHECKRESET_MENU_TEXT = "查漏补缺   ";
	public final static String POPUP_CHANGEREADED_MENU_TEXT = "更改状态   ";
	public final static String POPUP_EDIT_MENU_TEXT = "编辑任务   ";
	public final static String POPUP_RESET_MENU_TEXT = "重置任务   ";
	public final static String POPUP_COMPLETED_MENU_TEXT = "完成任务   ";
	
	
	/*AboutMenuWindow内的组件*/
	public final static String ABOUT_TEXTPANE_TEXT ="<h3>" + Version.NAME +
			"</h3>作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者：<b>" +
			Version.AUTHOR + "</b><br>版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本：<b>" +
			Version.VERSION +"</b><br>更新时间：<b>" + Version.MODIFLIED +
			"</b><br>项目地址：<a href='https://github.com/dipoo/egdownloader'>https://github.com/dipoo/egdownloader</a>" + 
			"<br/><font color='blue'>有任何问题，请到项目托管地址发issue。</font>";
	public final static String SCRIPT_DESC_TEXT = "<div style='font-size:10px;color:#666;'>目前脚本解析系统只支持javascript脚本，共需要三个脚本文件，分别用于任务信息解析，图片列表解析和图片真实地址解析，" +
			"可以在上面指定具体的脚本路径。系统提供给js脚本中一个参数：<font color='red'>htmlSource</font>，代表网页源码（任务解析，图片列表解析的是主页面源码，真实地址解析的是图片浏览页面源码），你需要" +
			"返回各自所需的信息：<br>" + 
			"<b>任务信息解析</b>：返回一个JSON字符串，如：{\"name\":\"名称\",\"subname\":\"子标题\",\"type\":\"类别\",\"coverUrl\":\"封面地址\",\"total\":\"数目\"," +
			"\"size\":大小,\"language\":\"语言\"};<br>" + 
			"<b>图片列表解析</b>：返回一个JSON数组字符串，如[{\"name\":\"名称\",\"url\":\"浏览地址\"}];<br>" + 
			"<b>真实地址解析</b>：返回真实地址字符串。<br>" + 
			"约定：JSON字符中中key必须有引号，而且引号为双引号。具体请参看script目录下的自带脚本。</div>";
	
	public static void changeDataPath(String groupName){
		DATA_PATH = ROOT_DATA_PATH + "/" + groupName;
	}
	
	public static void changeDataXmlPath(){
		if(! "".equals(groupName)){
			SETTING_XML_DATA_PATH = DATA_PATH + "/data/setting.xml";//配置文件保存路径
			TASK_XML_DATA_PATH = DATA_PATH + "/data/task.xml";//任务数据保存路径
			PICTURE_XML_DATA_PATH = DATA_PATH + "/data/picture.xml";//图片数据保存路径
		}else{
			SETTING_XML_DATA_PATH = DATA_PATH + "/setting.xml";//配置文件保存路径
			TASK_XML_DATA_PATH = DATA_PATH + "/task.xml";//任务数据保存路径
			PICTURE_XML_DATA_PATH = DATA_PATH + "/picture.xml";//图片数据保存路径
		}
	}
	
	public static String getXmlDirPath(){
		if(! "".equals(groupName)){
			return DATA_PATH + "/data";
		}else{
			return DATA_PATH;
		}
	}

	public static String getSavePathPreffix(){
		if("".equals(groupName)){
			return "";
		}else{
			return ROOT_DATA_PATH + "/" + groupName + "/";
		}
	}
	
}
