package org.arong.egdownloader.db.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.CodeUtil;
import org.arong.util.Dom4jUtil2;
import org.arong.util.FileUtil2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
/**
 * 配置文件dom4j dbtemplate实现类
 * @author 阿荣
 * @since 2014-06-02
 */
public class SettingDom4jDbTemplate implements DbTemplate<Setting> {

	private static boolean locked;
	private static Document dom;
	
	static{
		updateDom();
	}
	public static void updateDom(){
		try {
			dom = Dom4jUtil2.getDOM(ComponentConst.SETTING_XML_DATA_PATH);
		} catch (DocumentException e) {
			FileUtil2.ifNotExistsThenCreate(ComponentConst.DATA_PATH);
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><settings></settings>";
			int length = 0; //每一次读取的长度
			char[] buffer = new char[2048]; //设缓冲最大值为2048字符
			//字符串转为字符流
			BufferedReader br = null;
			BufferedWriter bw = null;
			try {
				br = new BufferedReader(new StringReader(xml));
				bw = new BufferedWriter(new FileWriter(ComponentConst.SETTING_XML_DATA_PATH));
				while((length = br.read(buffer)) != -1){ //若读到的不是末尾
					bw.write(buffer, 0, length);
				}
				bw.flush();
				dom = Dom4jUtil2.getDOM(ComponentConst.SETTING_XML_DATA_PATH);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if(bw != null){
					try {
						bw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(br != null){
					try {
						br.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean store(Setting t) {
		while(locked){
			store(t);
		}
		locked = true;
		Element ele = setting2Element(t);
		Dom4jUtil2.appendElement(dom.getRootElement(), ele);
		try {
			Dom4jUtil2.writeDOM2XML(ComponentConst.SETTING_XML_DATA_PATH, dom);
			locked = false;
		} catch (Exception e) {
			locked = false;
			return false;
		}
		return true;
	}

	public boolean store(List<Setting> list) {
		return false;
	}

	public boolean update(Setting t) {
		if(t == null)
			return false;
		while(locked){
			update(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/settings/setting[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.appendElement(dom.getRootElement(), setting2Element(t));
				Dom4jUtil2.writeDOM2XML(ComponentConst.SETTING_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				locked = false;
				return false;
			}
		}
		return false;
	}
	
	public boolean update(List<Setting> t) {
		return false;
	}

	public boolean delete(Setting t) {
		return false;
	}

	public boolean delete(List<Setting> list) {
		return false;
	}

	public List<Setting> query() {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/settings/setting");
		if(nodes != null && nodes.size() > 0){
			List<Setting> settings = new ArrayList<Setting>();
			for (Node node : nodes) {
				settings.add(node2Setting(node));
			}
			return settings;
		}
		return null;
	}

	public List<Setting> query(Object id) {
		return null;
	}

	public List<Setting> query(String name, String value) {
		return null;
	}

	public Setting get(Object id) {
		return null;
	}

	public boolean exsits(String name, String value) {
		return false;
	}
	
	private Element setting2Element(Setting t) {
		Element ele = DocumentHelper.createElement("setting");
		ele.addAttribute("id", t.getId());
		ele.addAttribute("defaultSaveDir", t.getDefaultSaveDir());
		ele.addAttribute("cookieInfo", t.getCookieInfo());
		ele.addAttribute("saveAsName", t.isSaveAsName() + "");
		ele.addAttribute("showAsSubname", t.isShowAsSubname() + "");
		ele.addAttribute("autoDownload", t.isAutoDownload() + "");
		ele.addAttribute("downloadOriginal", t.isDownloadOriginal() + "");
		ele.addAttribute("saveDirAsSubname", t.isSaveDirAsSubname() + "");
		ele.addAttribute("debug", t.isDebug() + "");
		ele.addAttribute("useCoverReplaceDomain", t.isUseCoverReplaceDomain() + "");
		ele.addAttribute("tagsTranslate", t.isTagsTranslate() + "");
		ele.addAttribute("maxThread", t.getMaxThread() + "");
		ele.addAttribute("viewModel", t.getViewModel() + "");
		ele.addAttribute("siteModel", t.getSiteModel() + "");
		ele.addAttribute("searchViewModel", t.getSearchViewModel() + "");
		ele.addAttribute("skin", t.getSkin());
		
		ele.addAttribute("loginUrl", t.getLoginUrl());
		ele.addAttribute("tags", t.getTags());
		ele.addAttribute("favTags", t.getFavTags());
		
		ele.addAttribute("lastCreateTime", t.getLastCreateTime());
		ele.addAttribute("lastDownloadTime", t.getLastDownloadTime());
		ele.addAttribute("taskHostoryCount", t.getTaskHistoryCount() + "");
		ele.addAttribute("pictureHostoryCount", t.getPictureHistoryCount() + "");
		
		ele.addAttribute("openScript", t.isOpenScript() + "");
		ele.addAttribute("createTaskScriptPath", t.getCreateTaskScriptPath());
		ele.addAttribute("collectPictureScriptPath", t.getCollectPictureScriptPath());
		ele.addAttribute("downloadScriptPath", t.getDownloadScriptPath());
		ele.addAttribute("searchScriptPath", t.getSearchScriptPath());
		ele.addAttribute("searchScriptPath2", t.getSearchScriptPath2());
		
		ele.addAttribute("useProxy", t.isUseProxy() + "");
		ele.addAttribute("proxyType", t.getProxyType());
		ele.addAttribute("proxyIp", t.getProxyIp());
		ele.addAttribute("proxyPort", t.getProxyPort());
		ele.addAttribute("proxyUsername", t.getProxyUsername());
		ele.addAttribute("proxyPwd", CodeUtil.myEncode(t.getProxyPwd()));
		return ele;
	}
	private Setting node2Setting(Node node) {
		Element ele = (Element)node;
		Setting t = new Setting();
		t.setId(ele.attributeValue("id") == null ? t.getId() : ele.attributeValue("id"));
		t.setDefaultSaveDir(ele.attributeValue("defaultSaveDir") == null ? t.getDefaultSaveDir() : ele.attributeValue("defaultSaveDir"));
		t.setCookieInfo(ele.attributeValue("cookieInfo") == null ? t.getCookieInfo() : ele.attributeValue("cookieInfo"));
		t.setSaveAsName("true".equals(ele.attributeValue("saveAsName")));
		t.setAutoDownload("true".equals(ele.attributeValue("autoDownload")));
		t.setDownloadOriginal("true".equals(ele.attributeValue("downloadOriginal")));
		t.setSaveDirAsSubname("true".equals(ele.attributeValue("saveDirAsSubname")));
		t.setShowAsSubname(ele.attributeValue("showAsSubname") == null ? true : "true".equals(ele.attributeValue("showAsSubname")));
		t.setDebug("true".equals(ele.attributeValue("debug")));
		t.setUseCoverReplaceDomain("true".equals(ele.attributeValue("useCoverReplaceDomain")));
		t.setTagsTranslate("true".equals(ele.attributeValue("tagsTranslate")) ? true : "true".equals(ele.attributeValue("tagsTranslate")));
		t.setMaxThread(ele.attributeValue("maxThread") == null ? 0 : Integer.parseInt(ele.attributeValue("maxThread")));
		t.setViewModel(ele.attributeValue("viewModel") == null ? 1 : Integer.parseInt(ele.attributeValue("viewModel")));
		t.setSiteModel(ele.attributeValue("siteModel") == null ? 1 : Integer.parseInt(ele.attributeValue("siteModel")));
		t.setSearchViewModel(ele.attributeValue("searchViewModel") == null ? 1 : Integer.parseInt(ele.attributeValue("searchViewModel")));
		t.setLoginUrl(ele.attributeValue("loginUrl") == null ? t.getLoginUrl() : ele.attributeValue("loginUrl"));
		if(ele.attributeValue("tags") != null){
			t.setTags(ele.attributeValue("tags"));
		}
		if(ele.attributeValue("favTags") != null){
			t.setFavTags(ele.attributeValue("favTags"));
		}
		t.setSkin(ele.attributeValue("skin") == null ? t.getSkin() : ele.attributeValue("skin"));
		t.setTaskHistoryCount(ele.attributeValue("taskHostoryCount") == null ? 0 : Integer.parseInt(ele.attributeValue("taskHostoryCount")));
		t.setPictureHistoryCount(ele.attributeValue("pictureHostoryCount") == null ? 0 : Integer.parseInt(ele.attributeValue("pictureHostoryCount")));
		t.setLastCreateTime(ele.attributeValue("lastCreateTime"));
		t.setLastDownloadTime(ele.attributeValue("lastDownloadTime"));
		
		//t.setOpenScript("true".equals(ele.attributeValue("openScript")) ? true : false);
		t.setCreateTaskScriptPath(ele.attributeValue("createTaskScriptPath") == null ? t.getCreateTaskScriptPath() : ele.attributeValue("createTaskScriptPath"));
		t.setCollectPictureScriptPath(ele.attributeValue("collectPictureScriptPath") == null ? t.getCollectPictureScriptPath() : ele.attributeValue("collectPictureScriptPath"));
		t.setDownloadScriptPath(ele.attributeValue("downloadScriptPath") == null ? t.getDownloadScriptPath() : ele.attributeValue("downloadScriptPath"));
		t.setSearchScriptPath(ele.attributeValue("searchScriptPath") == null ? t.getSearchScriptPath() : ele.attributeValue("searchScriptPath"));
		t.setSearchScriptPath2(ele.attributeValue("searchScriptPath2") == null ? t.getSearchScriptPath2() : ele.attributeValue("searchScriptPath2"));
		
		t.setUseProxy("true".equals(ele.attributeValue("useProxy")) ? true : false);
		t.setProxyType(ele.attributeValue("proxyType"));
		t.setProxyIp(ele.attributeValue("proxyIp"));
		t.setProxyPort(ele.attributeValue("proxyPort"));
		t.setProxyUsername(ele.attributeValue("proxyUsername"));
		t.setProxyPwd(CodeUtil.myDecode(ele.attributeValue("proxyPwd")));
		return t;
	}
	@Override
	public boolean delete(String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
