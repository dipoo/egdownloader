package org.arong.egdownloader.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.arong.egdownloader.db.impl.DbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.Dom4jUtil;
import org.arong.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
/**
 * 图片存取的dom4j实现
 * @author 阿荣
 * @since 2014-05-29
 */
public class PictureDom4jDbTemplate implements DbTemplate<Picture> {
	/**
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <pictures>
	 * 	<picture id="" tid="" num="" name="" url="" realUrl="" size="" time="" saveAsName="" isCompleted="" isRunning=""/>
	 * </pictures>
	 */
	private static boolean locked;
	private static Document dom;
	
	static{
		try {
			dom = Dom4jUtil.getDOM(ComponentConst.PICTURE_XML_DATA_PATH);
		} catch (FileNotFoundException e) {
			FileUtil.ifNotExistsThenCreate(ComponentConst.DATA_PATH);
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><pictures></pictures>";
			int length = 0; //每一次读取的长度
			char[] buffer = new char[2048]; //设缓冲最大值为2048字符
			//字符串转为字符流
			BufferedReader br = null;
			BufferedWriter bw = null;
			try {
				br = new BufferedReader(new StringReader(xml));
				bw = new BufferedWriter(new FileWriter(ComponentConst.PICTURE_XML_DATA_PATH));
				while((length = br.read(buffer)) != -1){ //若读到的不是末尾
					bw.write(buffer, 0, length);
				}
				bw.flush();
			} catch (IOException e1) {
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

	public boolean store(Picture t) {
		while(locked){
			store(t);
		}
		locked = true;
		Element ele = picture2Element(t);
		Dom4jUtil.appendElement(dom.getRootElement(), ele);
		try {
			Dom4jUtil.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
			locked = false;
		} catch (Exception e) {
			locked = false;
			return false;
		}
		return true;
	}

	public boolean update(Picture t) {
		while(locked){
			update(t);
		}
		locked = true;
//		dom.getRootElement().createXPath("");
		return true;
	}

	public boolean delete(Picture t) {
		return false;
	}

	public List<Picture> query() {
		return null;
	}

	public List<Picture> query(Object id) {
		return null;
	}

	public Picture get(Object id) {
		Node node = dom.selectSingleNode("/pictures/picture[id=" + id.toString() + "]");
		return null;
	}
	
	public static Element picture2Element(Picture t){
		Element ele = DocumentHelper.createElement("picture");
		ele.addAttribute("id", t.getId());
		ele.addAttribute("tid", t.getTid());
		ele.addAttribute("num", t.getNum());
		ele.addAttribute("name", t.getName());
		ele.addAttribute("url", t.getUrl());
		ele.addAttribute("realUrl", t.getRealUrl());
		ele.addAttribute("size", t.getSize() + "");
		ele.addAttribute("time", t.getTime());
		ele.addAttribute("saveAsName", t.isSaveAsName() + "");
		ele.addAttribute("isCompleted", t.isCompleted() + "");
		ele.addAttribute("isRunning", t.isRunning() + "");
		return ele;
	}
	
	public static Picture Node2Picture(Node node){
		Picture pic = new Picture();
		return pic;
	}
}
