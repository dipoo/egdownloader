package org.arong.egdownloader.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
	
	public static String DB_DATA_FILE_PATH = "data/data.db";//数据文件路径
	
	public static String DB_DATA_FILE_PATH_BAK = "data/data.db_bak";//数据文件备份路径
	
	public static String DATA_PATH = "data";//数据目录
	
	public static String CACHE_PATH = "cache";//缓存目录
	
	public static String SOURCE_PATH = "source/";//缓存目录
	
	public final static String TAG_FILE_NAME = "tags.properties";
	
	public final static String TAG_FILE_PATH = ROOT_DATA_PATH + "/tags.properties";
	
	public static String SETTING_XML_DATA_PATH = DATA_PATH + "/setting.xml";//配置文件保存路径
	
	public static String TASK_XML_DATA_PATH = DATA_PATH + "/task.xml";//任务数据保存路径(该文件已弃用)
	
	public static String PICTURE_XML_DATA_PATH = DATA_PATH + "/picture.xml";//图片数据保存路径(该文件已弃用)
	
	public final static String RESOURCES_PATH = "/resources/";
	
	public final static String ICON_PATH = RESOURCES_PATH + "icon/";
	
	public final static String TAGS_CN_FILE_PATH = "script/EhTagTranslator.wiki/database/";//标签汉化文件目录
	public static final String[] TAGS_CN_FILENAMES = new String[]{"artist.md", "character.md", "female.md", "group.md", "language.md", "male.md", "misc.md", "parody.md", "reclass.md", "rows.md"};
	public static final String TAGS_CN_FILENAMES_DOWNLOAD_URL_PREFFIX = "https://raw.githubusercontent.com/wiki/Mapaler/EhTagTranslator/database/";
	public static final String MISC = "misc"; 
	
	public static String EX_DOMAIN = "exhentai.org"; //域名
	public static String EX_REPLACE_COVER_DOMAIN = "ehgt.org"; //封面替换域名
	
	public final static String[] TASK_TABLE_HEADER = {"", "名称", "数目 /大小", "语言", "已下载", "状态"};
	
	public final static String[] PICTURE_TABLE_HEADER = {"序号", "名称", "存储名称", "大小", "分辨率", "状态", "地址", "下载时间", "查看"};
	
	public final static int MAX_TASK_PAGE = 14;//一页显示任务数
	
	public final static int SKIN_NUM = 1;//皮肤编号
	
	public final static Map<String, String> SKIN_ICON = new HashMap<String, String>();
	
	public static String taskinfoHtml;
	
	public static String countHtml;
	
	public static String localScriptVersion;//本地脚本版本号
	
	public static String remoteScriptVersion;//远程脚本版本号
	
	public static boolean scriptChange;//远程脚本是否更新
	
	public static String osname;//操作系统名称
	
	public static String EG_VERSION_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/egversion";
	public static String SCRIPT_CREATE_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/createTask.js";
	public static String SCRIPT_COLLECT_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/collectPicture.js";
	public static String SCRIPT_DOWNLOAD_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/download.js";
	public static String SCRIPT_SEARCH_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/search.js";
	public static String SCRIPT_SEARCH2_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/search2.js";
	public static String SCRIPT_VERSION_URL = "http://raw.githubusercontent.com/dipoo/egdownloader/master/script/version";
	
	public static Map<String, Integer> allTaskCountMap = new HashMap<String, Integer>();//已建任务标签对应的任务数
	
	public static Map<String, String> typeColorMap = new HashMap<String, String>();
	
	static{
		typeColorMap.put("DOUJINSHI", "<font color='#aa3032'>DOUJINSHI</font>");
		typeColorMap.put("MANGA", "<font color='yellow'>MANGA</font>");
		typeColorMap.put("ARTISTCG", "<font color='yellow'>ARTISTCG</font>");
		typeColorMap.put("ARTIST CG SETS", "<font color='yellow'>ARTISTCG</font>");
		typeColorMap.put("GAMECG", "<font color='#92c892'>GAMECG</font>");
		typeColorMap.put("GAME CG SETS", "<font color='#92c892'>GAMECG</font>");
		typeColorMap.put("WESTERN", "<font color='#e0ffc2'>WESTERN</font>");
		typeColorMap.put("NON-H", "<font color='#7ac7ff'>NON-H</font>");
		typeColorMap.put("IMAGESET", "<font color='#7e7eff'>IMAGESET</font>");
		typeColorMap.put("IMAGE SETS", "<font color='#7e7eff'>IMAGESET</font>");
		typeColorMap.put("COSPLAY", "<font color='#c0a7d3'>COSPLAY</font>");
		typeColorMap.put("ASIANPORN", "<font color='#f3acf3'>ASIANPORN</font>");
		typeColorMap.put("MISC", "<font color='#f4f4f4'>MISC</font>");
		typeColorMap.put("other", "<font color='#f4f4f4'>%s</font>");
		
		InputStream s = null;
		try {
			s = ComponentConst.class.getResourceAsStream(RESOURCES_PATH + "taskinfo.html");
			taskinfoHtml = WebClient.read(s, 0);
		} finally{
			if(s != null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(taskinfoHtml == null)
				taskinfoHtml = "没有找到" + RESOURCES_PATH + "taskinfo.html文件";
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
		
		Properties scriptProp = new Properties();
		try {
			scriptProp.load(new FileReader("script/version.properties"));
			if(scriptProp.get("eg_version_url") != null){
				EG_VERSION_URL = scriptProp.get("eg_version_url").toString();
			}
			if(scriptProp.get("script_create_url") != null){
				SCRIPT_CREATE_URL = scriptProp.get("script_create_url").toString();
			}
			if(scriptProp.get("script_collect_url") != null){
				SCRIPT_COLLECT_URL = scriptProp.get("script_collect_url").toString();
			}
			if(scriptProp.get("script_download_url") != null){
				SCRIPT_DOWNLOAD_URL = scriptProp.get("script_download_url").toString();
			}
			if(scriptProp.get("script_search_url") != null){
				SCRIPT_SEARCH_URL = scriptProp.get("script_search_url").toString();
			}
			if(scriptProp.get("script_version_url") != null){
				SCRIPT_VERSION_URL = scriptProp.get("script_version_url").toString();
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
		
		SKIN_ICON.put("add", "_add.png");
		SKIN_ICON.put("delete", "_delete.png");
		SKIN_ICON.put("setting", "_setting.png");
		SKIN_ICON.put("download", "_download.png");
		SKIN_ICON.put("select", "_select.png");
		SKIN_ICON.put("openpic", "_openpic.png");
		SKIN_ICON.put("folder", "_folder.png");
		SKIN_ICON.put("size", "_size.png");
		SKIN_ICON.put("tool", "_tool.png");
		SKIN_ICON.put("picture", "_picture.png");
		SKIN_ICON.put("user", "_user.png");
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
		SKIN_ICON.put("zip", "_zip.png");
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
	public final static String TASKGROUP_SEARCH_TEXT = "搜索";
	public final static String TASKGROUP_MENU_TEXT = "任务组";
	public final static String SETTING_MENU_TEXT = "配置";
	public final static String TAG_MENU_TEXT = "标签";
	public final static String OPERA_MENU_TEXT = "操作";
	public final static String SEARCH_MENU_TEXT = "搜索";
	public final static String CONSOLE_MENU_TEXT = "控制台";
	public final static String COUNT_MENU_TEXT = "统计";
	public final static String ABOUT_MENU_TEXT = "关于";
	public final static String DECODE_AND_ENCODE_TAB_TEXT = "加密/解密";
	
	public final static String POPUP_START_MENU_TEXT = " 开始任务   ";
	public final static String POPUP_JUMP_MENU_TEXT = " 任务插队   ";
	public final static String POPUP_OPENFILE_MENU_TEXT = " 打开图片   ";
	public final static String POPUP_STOP_MENU_TEXT = " 暂停任务   ";
	public final static String POPUP_DELETE_MENU_TEXT = " 删除任务   ";
	public final static String POPUP_DETAIL_MENU_TEXT = " 详细信息   ";
	public final static String POPUP_OPENFOLDER_MENU_TEXT = " 打开目录   ";
	public final static String POPUP_OPENWEBPAGE_MENU_TEXT = " 打开网页   ";
	public final static String POPUP_CHANGEREADED_MENU_TEXT = " 更改状态   ";
	public final static String POPUP_ZIP_MENU_TEXT = " 打包ZIP   ";
	public final static String POPUP_SEARCHAUTHOR_MENU_TEXT = " 在线搜作者";
	public final static String POPUP_LOCAL_SEARCHAUTHOR_MENU_TEXT = " 本地搜作者";
	public final static String POPUP_MORE_MENU_TEXT = "更多操作   ";
	public final static String POPUP_EDIT_MENU_TEXT = "编辑任务   ";
	public final static String POPUP_RESET_MENU_TEXT = "重置任务   ";
	public final static String POPUP_COMPLETED_MENU_TEXT = "置为完成   " ;
	public final static String POPUP_REBUILD_MENU_TEXT = "重建任务   ";
	public final static String POPUP_COPYURL_MENU_TEXT = "复制地址   ";
	public final static String POPUP_CHECKRESET_MENU_TEXT = "查漏补缺   ";
	public final static String POPUP_DOWNLOADCOVER_MENU_TEXT = "下载封面   ";
	
	/*AboutMenuWindow内的组件*/
	public final static String ABOUT_TEXTPANE_TEXT ="<h3>" + Version.NAME +
			"</h3>作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者：<b>" +
			Version.AUTHOR + "</b><br>版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本：<b>" +
			Version.VERSION + "." + Version.JARVERSION + "</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='checkVersion'><font color='red'>检查新版本</font><a/>" + 
			"&nbsp;/&nbsp;<a href='printSystemProperties'><font color='blue'>环境变量</font><a/><br>更新时间：<b>" + Version.MODIFLIED +
			"</b><br>项目地址：<a href='https://github.com/dipoo/egdownloader'>https://github.com/dipoo/egdownloader</a>" + 
			"<br/><font color='blue'>有任何问题，请到项目托管地址发issue。如果eh网页改版，请通知我更新脚本^_^</font>";
	public final static String SCRIPT_DESC_TEXT = "<div style='font-family:微软雅黑;font-size:10px;color:#666;'>目前脚本解析系统只支持javascript脚本，共需要四个脚本文件，分别用于任务信息解析，图片列表解析，图片真实地址解析和搜索漫画解析，" +
			"已经在上面指定了具体的脚本路径。系统提供给js脚本中一个参数：<font color='red'>htmlSource</font>，代表网页源码（任务解析，图片列表解析的是主页面源码，真实地址解析的是图片浏览页面源码，搜索漫画的是首页源码），你需要" +
			"返回各自所需的信息：<br>" + 
			"<b>任务信息解析</b>：返回一个JSON字符串，如：{\"name\":\"名称\",\"subname\":\"子标题\",\"type\":\"类别\",\"coverUrl\":\"封面地址\",\"total\":\"数目\"," +
			"\"size\":大小,\"language\":\"语言\"};<br>" + 
			"<b>图片列表解析</b>：返回一个JSON数组字符串，如[{\"name\":\"名称\",\"url\":\"浏览地址\"}];<br>" + 
			"<b>真实地址解析</b>：返回真实地址字符串。<br>" + 
			"<b>搜索漫畫解析</b>：返回一个带着分页信息和JSON数组的字符串，格式如1888,76###[{\"name\":\"名称\",\"url\":\"浏览地址\", \"coverUrl\":\"封面地址\", \"date\":\"发布时间\", \"type\":\"类型\", \"btUrl\":\"bt地址\", \"uploader\":\"上传者\"}];<br>" + 
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
	
}
