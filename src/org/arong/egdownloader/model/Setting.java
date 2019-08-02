package org.arong.egdownloader.model;

import java.util.UUID;

import org.arong.egdownloader.ui.work.UpdateScriptWorker;

/**
 * 采集配置信息
 * @author 阿荣
 * @since 2014-05-25
 */
public class Setting {
	public final static String TAGSPLIT = "$￥";//标签分隔符
	
	private String id = UUID.randomUUID().toString();
	private String defaultSaveDir = "save";//默认保存路劲
	private boolean saveAsName = false;//是否以真实名称保存
	private boolean autoDownload;//创建任务后是否自动下载
	private boolean downloadOriginal = true;//下载原图
	private boolean saveDirAsSubname = false;//以子名称作为保存目录
	private int viewModel = 1;//默认显示模式
	private int siteModel = 1;//默认搜索网站（1：里站2：表站）
	private int searchViewModel = 1;//默认搜索显示模式
	private boolean showAsSubname = true;//以子名称展示
	private boolean tagsTranslate = true;//标签是否汉化显示
	private int maxThread = 5;
	private boolean debug = false;
	private boolean useCoverReplaceDomain = false;//是否使用替换域名
	private String favTags = "";
	private String cookieInfo = "igneous=4baadb8381b3bb5c20257b33b725e4ec93f51b4fe2ab7e97621c9fe260bbda7de47a44d6394b31783a0af329a20197c80d2ab687ccf0b667ca5c558ee1b9310b;ipb_member_id=1059070;ipb_pass_hash=e8e36f507753214279ee9df5d98c476c;";
	//private String cookieInfo2 = "igneous=4baadb8381b3bb5c20257b33b725e4ec93f51b4fe2ab7e97621c9fe260bbda7de47a44d6394b31783a0af329a20197c80d2ab687ccf0b667ca5c558ee1b9310b;ipb_member_id=1059070;ipb_pass_hash=e8e36f507753214279ee9df5d98c476c;s=1b0e7b0b3; sk=px6ms37kr0j3lf0m2fjtr1o8q2zu; lv=1544856920-1545910092;";
	
	private boolean openScript = true;//是否启用脚本
	private String createTaskScriptPath = "script/createTask.js";//创建任务脚本
	private String collectPictureScriptPath = "script/collectPicture.js";//收集图片脚本
	private String downloadScriptPath = "script/download.js";//任务下载脚本
	private String searchScriptPath = "script/search.js";//搜索漫画列表脚本
	private String searchScriptPath2 = "script/search2.js";//搜索漫画图文脚本
	private UpdateScriptWorker updateScriptWorker;//脚本更新器
	
	
	
	private String tags;//标签记忆，以$￥符号分割
	
	private int pageCount = 20;//一页最多图片数
	private String pageParam = "p";//切换分页的参数名
	
	private String loginUrl = "http://forums.e-hentai.org/index.php?act=Login&CODE=01";
	
	/**
	 * 统计
	 */
	private int taskHistoryCount = 0;//历史任务总数
	private int pictureHistoryCount = 0;//历史图片总数
	private String lastDownloadTime;//最后下载时间
	private String lastCreateTime;//最后创建时间
	
	/**
	 * 代理
	 */
	private boolean useProxy = false;//是否使用代理
	private String proxyType;//代理类型
	private String proxyIp;//代理ip
	private String proxyPort;//代理端口
	private String proxyUsername;//代理用户名
	private String proxyPwd;//代理密码
	
	/**
	 * 封面视图大小
	 */
	private int coverWidth = 350;//150;
	private int coverHeight = 480;//216;
	
	private String skin = "BeautyEye";//皮肤
	
	
	public String getPageParam() {
		return pageParam;
	}
	public void setPageParam(String pageParam) {
		this.pageParam = pageParam;
	}
	
	public String getCookieInfo() {
		return cookieInfo;
	}
	public void setCookieInfo(String cookieInfo) {
		this.cookieInfo = cookieInfo;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public String getDefaultSaveDir() {
		return defaultSaveDir;
	}
	public void setDefaultSaveDir(String defaultSaveDir) {
		this.defaultSaveDir = defaultSaveDir;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSaveAsName() {
		return saveAsName;
	}
	public void setSaveAsName(boolean saveAsName) {
		this.saveAsName = saveAsName;
	}
	public int getMaxThread() {
		return maxThread;
	}
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getLastDownloadTime() {
		return lastDownloadTime;
	}
	public void setLastDownloadTime(String lastDownloadTime) {
		this.lastDownloadTime = lastDownloadTime;
	}
	public String getLastCreateTime() {
		return lastCreateTime;
	}
	public void setLastCreateTime(String lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}
	public boolean isAutoDownload() {
		return autoDownload;
	}
	public void setAutoDownload(boolean autoDownload) {
		this.autoDownload = autoDownload;
	}
	
	public void setOpenScript(boolean openScript) {
		this.openScript = openScript;
	}
	public boolean isOpenScript() {
		return openScript;
	}
	public void setCreateTaskScriptPath(String createTaskScriptPath) {
		this.createTaskScriptPath = createTaskScriptPath;
	}
	public String getCreateTaskScriptPath() {
		return createTaskScriptPath;
	}
	public void setCollectPictureScriptPath(String collectPictureScriptPath) {
		this.collectPictureScriptPath = collectPictureScriptPath;
	}
	public String getCollectPictureScriptPath() {
		return collectPictureScriptPath;
	}
	public void setDownloadScriptPath(String downloadScriptPath) {
		this.downloadScriptPath = downloadScriptPath;
	}
	public String getDownloadScriptPath() {
		return downloadScriptPath;
	}
	public void setTaskHistoryCount(int taskHistoryCount) {
		this.taskHistoryCount = taskHistoryCount;
	}
	public int getTaskHistoryCount() {
		return taskHistoryCount;
	}
	public void setPictureHistoryCount(int pictureHistoryCount) {
		this.pictureHistoryCount = pictureHistoryCount;
	}
	public int getPictureHistoryCount() {
		return pictureHistoryCount;
	}
	public void setSearchScriptPath(String searchScriptPath) {
		this.searchScriptPath = searchScriptPath;
	}
	public String getSearchScriptPath() {
		return searchScriptPath;
	}
	public void setUpdateScriptWorker(UpdateScriptWorker updateScriptWorker) {
		this.updateScriptWorker = updateScriptWorker;
	}
	public UpdateScriptWorker getUpdateScriptWorker() {
		return updateScriptWorker;
	}
	public boolean isUseProxy() {
		return useProxy;
	}
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
	public String getProxyIp() {
		return proxyIp;
	}
	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getProxyUsername() {
		return proxyUsername;
	}
	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}
	public String getProxyPwd() {
		return proxyPwd;
	}
	public void setProxyPwd(String proxyPwd) {
		this.proxyPwd = proxyPwd;
	}
	public boolean isDownloadOriginal() {
		return downloadOriginal;
	}
	public void setDownloadOriginal(boolean downloadOriginal) {
		this.downloadOriginal = downloadOriginal;
	}
	public String getProxyType() {
		return proxyType;
	}
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}
	public boolean isSaveDirAsSubname() {
		return saveDirAsSubname;
	}
	public void setSaveDirAsSubname(boolean saveDirAsSubname) {
		this.saveDirAsSubname = saveDirAsSubname;
	}
	
	public int getViewModel() {
		return viewModel;
	}
	public void setViewModel(int viewModel) {
		this.viewModel = viewModel;
	}
	public int getCoverWidth() {
		return coverWidth;
	}
	public void setCoverWidth(int coverWidth) {
		this.coverWidth = coverWidth;
	}
	public int getCoverHeight() {
		return coverHeight;
	}
	public void setCoverHeight(int coverHeight) {
		this.coverHeight = coverHeight;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public int getSearchViewModel() {
		return searchViewModel;
	}
	public void setSearchViewModel(int searchViewModel) {
		this.searchViewModel = searchViewModel;
	}
	public String getSearchScriptPath2() {
		return searchScriptPath2;
	}
	public void setSearchScriptPath2(String searchScriptPath2) {
		this.searchScriptPath2 = searchScriptPath2;
	}
	
	public boolean isShowAsSubname() {
		return showAsSubname;
	}
	public void setShowAsSubname(boolean showAsSubname) {
		this.showAsSubname = showAsSubname;
	}
	
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public boolean isTagsTranslate() {
		return tagsTranslate;
	}
	public void setTagsTranslate(boolean tagsTranslate) {
		this.tagsTranslate = tagsTranslate;
	}
	public String getFavTags() {
		return favTags;
	}
	public void setFavTags(String favTags) {
		this.favTags = favTags;
	}
	public boolean isUseCoverReplaceDomain() {
		return useCoverReplaceDomain;
	}
	public void setUseCoverReplaceDomain(boolean useCoverReplaceDomain) {
		this.useCoverReplaceDomain = useCoverReplaceDomain;
	}
	public int getSiteModel() {
		return siteModel;
	}
	public void setSiteModel(int siteModel) {
		this.siteModel = siteModel;
	}
	
}
