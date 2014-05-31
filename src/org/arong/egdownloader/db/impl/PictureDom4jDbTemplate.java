package org.arong.egdownloader.db.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.Dom4jUtil;
import org.arong.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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
		} catch (DocumentException e) {
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
				dom = Dom4jUtil.getDOM(ComponentConst.PICTURE_XML_DATA_PATH);
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
		Node node = dom.selectSingleNode("/pictures/picture[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil.appendElement(dom.getRootElement(), picture2Element(t));
				Dom4jUtil.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				locked = false;
				return false;
			}
		}
		return false;
	}

	public boolean delete(Picture t) {
		while(locked){
			delete(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/pictures/picture[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				locked = false;
				return false;
			}
		}
		return false;
	}

	public List<Picture> query() {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/pictures/picture");
		if(nodes != null && nodes.size() > 0){
			List<Picture> pics = new ArrayList<Picture>();
			for (Node node : nodes) {
				pics.add(node2Picture(node));
			}
			return pics;
		}
		return null;
	}

	public List<Picture> query(Object id) {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/pictures/picture[@id='" + id.toString() + "']");
		if(nodes != null && nodes.size() > 0){
			List<Picture> pics = new ArrayList<Picture>();
			for (Node node : nodes) {
				pics.add(node2Picture(node));
			}
			return pics;
		}
		return null;
	}
	
	public List<Picture> query(String name, String value) {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/pictures/picture[@" + name + "='" + value + "']");
		List<Picture> pics = null;
		if(nodes != null && nodes.size() > 0){
			pics = new ArrayList<Picture>();
			for (Node node : nodes) {
				pics.add(node2Picture(node));
			}
		}
		return pics;
	}

	public Picture get(Object id) {
		Node node = dom.selectSingleNode("/pictures/picture[@id='" + id.toString() + "']");
		if(node != null){
			return node2Picture(node);
		}
		return null;
	}
	
	public boolean exsits(String name, String value) {
		Node node = dom.selectSingleNode("/pictures/picture[@" + name + "='" + value + "']");
		if(node != null){
			return true;
		}
		return false;
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
	
	public static Picture node2Picture(Node node){
		Element ele = (Element)node;
		Picture pic = new Picture();
		pic.setId(ele.attributeValue("id"));
		pic.setTid(ele.attributeValue("tid"));
		pic.setName(ele.attributeValue("name"));
		pic.setNum(ele.attributeValue("num"));
		pic.setUrl(ele.attributeValue("url"));
		pic.setRealUrl(ele.attributeValue("realUrl"));
		pic.setSize(ele.attributeValue("size") == null ? 1 : Integer.parseInt(ele.attributeValue("size")));
		pic.setTime(ele.attributeValue("time"));
		pic.setCompleted("true".equals(ele.attributeValue("isCompleted")) ? true : false);
		pic.setRunning("true".equals(ele.attributeValue("isRunning")) ? true : false);
		pic.setSaveAsName("true".equals(ele.attributeValue("saveAsName")) ? true : false);
		return pic;
	}

	public static void main(String[] args) {
		PictureDom4jDbTemplate picTemplate = new PictureDom4jDbTemplate();
		Picture pic = picTemplate.get("c834e3c9-7dbb-4cb6-818a-6939753b40bc");
		System.out.println(pic);
	}

}
