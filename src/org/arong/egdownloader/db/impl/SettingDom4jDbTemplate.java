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
import org.arong.util.Dom4jUtil;
import org.arong.util.FileUtil;
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
		try {
			dom = Dom4jUtil.getDOM(ComponentConst.SETTING_XML_DATA_PATH);
		} catch (DocumentException e) {
			FileUtil.ifNotExistsThenCreate(ComponentConst.DATA_PATH);
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
				dom = Dom4jUtil.getDOM(ComponentConst.SETTING_XML_DATA_PATH);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
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
		Dom4jUtil.appendElement(dom.getRootElement(), ele);
		try {
			Dom4jUtil.writeDOM2XML(ComponentConst.SETTING_XML_DATA_PATH, dom);
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
		while(locked){
			update(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/settings/setting[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil.appendElement(dom.getRootElement(), setting2Element(t));
				Dom4jUtil.writeDOM2XML(ComponentConst.SETTING_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
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
		ele.addAttribute("maxThread", t.getMaxThread() + "");
		ele.addAttribute("gidPrefix", t.getGidPrefix());
		ele.addAttribute("hentaiHome.uri", t.getHentaiHome().getUri());
		ele.addAttribute("hentaiHome.firstParameterName", t.getHentaiHome().getFirstParameterName());
		ele.addAttribute("hentaiHome.secondParameterName", t.getHentaiHome().getSecondParameterName() + "");
		ele.addAttribute("totalPrefix", t.getTotalPrefix());
		ele.addAttribute("namePrefix", t.getNamePrefix());
		ele.addAttribute("fileListPrefix", t.getFileListPrefix());
		ele.addAttribute("fileListSuffix", t.getFileListSuffix());
		ele.addAttribute("fileListPrefix", t.getFileListPrefix());
		ele.addAttribute("pageCount", t.getPageCount() + "");
		ele.addAttribute("pageParam", t.getPageParam());
		ele.addAttribute("sourcePrefix", t.getSourcePrefix());
		ele.addAttribute("sourceSuffix", t.getSourceSuffix());
		ele.addAttribute("showPicPrefix", t.getShowPicPrefix());
		ele.addAttribute("showPicSuffix", t.getShowPicSuffix());
		ele.addAttribute("realUrlPrefix", t.getRealUrlPrefix());
		ele.addAttribute("realUrlSuffix", t.getRealUrlSuffix());
		ele.addAttribute("loginUrl", t.getLoginUrl());
		ele.addAttribute("tags", t.getTags());
		ele.addAttribute("lastCreateTime", t.getLastCreateTime());
		ele.addAttribute("lastDownloadTime", t.getLastDownloadTime());
		
		return ele;
	}
	private Setting node2Setting(Node node) {
		Element ele = (Element)node;
		Setting t = new Setting();
		t.setId(ele.attributeValue("id"));
		t.setDefaultSaveDir(ele.attributeValue("defaultSaveDir"));
		t.setCookieInfo(ele.attributeValue("cookieInfo"));
		t.setSaveAsName("true".equals(ele.attributeValue("saveAsName")) ? true : false);
		t.setGidPrefix(ele.attributeValue("gidPrefix"));
		t.setMaxThread(ele.attributeValue("maxThread") == null ? 0 : Integer.parseInt(ele.attributeValue("maxThread")));
		t.getHentaiHome().setUri(ele.attributeValue("hentaiHome.uri"));
		t.getHentaiHome().setFirstParameterName(ele.attributeValue("hentaiHome.firstParameterName"));
		t.getHentaiHome().setSecondParameterName(ele.attributeValue("hentaiHome.secondParameterName"));
		t.setTotalPrefix(ele.attributeValue("totalPrefix"));
		t.setNamePrefix(ele.attributeValue("namePrefix"));
		t.setFileListPrefix(ele.attributeValue("fileListPrefix"));
		t.setFileListSuffix(ele.attributeValue("fileListSuffix"));
		t.setFileListPrefix(ele.attributeValue("fileListPrefix"));
		t.setPageCount(ele.attributeValue("pageCount") == null ? 0 : Integer.parseInt(ele.attributeValue("pageCount")));
		t.setPageParam(ele.attributeValue("pageParam"));
		t.setSourcePrefix(ele.attributeValue("sourcePrefix"));
		t.setSourceSuffix(ele.attributeValue("sourceSuffix"));
		t.setShowPicPrefix(ele.attributeValue("showPicPrefix"));
		t.setShowPicSuffix(ele.attributeValue("showPicSuffix"));
		t.setRealUrlPrefix(ele.attributeValue("realUrlPrefix"));
		t.setRealUrlSuffix(ele.attributeValue("realUrlSuffix"));
		t.setLoginUrl(ele.attributeValue("loginUrl"));
		if(ele.attributeValue("tags") != null){
			t.setTags(ele.attributeValue("tags"));
		}
		t.setLastCreateTime(ele.attributeValue("lastCreateTime"));
		t.setLastDownloadTime(ele.attributeValue("lastDownloadTime"));
		return t;
	}
	
}
