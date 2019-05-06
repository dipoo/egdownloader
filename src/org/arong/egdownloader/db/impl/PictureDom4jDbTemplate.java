package org.arong.egdownloader.db.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.Dom4jUtil2;
import org.arong.util.FileUtil2;
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
		updateDom();
	}
	
	public static void updateDom(){
		try {
			dom = Dom4jUtil2.getDOM(ComponentConst.PICTURE_XML_DATA_PATH);
		} catch (DocumentException e) {
			FileUtil2.ifNotExistsThenCreate(ComponentConst.DATA_PATH);
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
				dom = Dom4jUtil2.getDOM(ComponentConst.PICTURE_XML_DATA_PATH);
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

	public boolean store(Picture t) {
		if(t == null){
			return false;
		}
		while(locked){
			store(t);
		}
		locked = true;
		Element ele = picture2Element(t);
		Dom4jUtil2.appendElement(dom.getRootElement(), ele);
		try {
			Dom4jUtil2.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
			locked = false;
		} catch (Exception e) {
			locked = false;
			return false;
		}
		return true;
	}
	
	public boolean store(List<Picture> pics) {
		if(pics == null || pics.size() == 0){
			return false;
		}
		while(locked){
			store(pics);
		}
		locked = true;
		for (Picture t : pics) {
			Element ele = picture2Element(t);
			Dom4jUtil2.appendElement(dom.getRootElement(), ele);
		}
		try {
			Dom4jUtil2.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
			locked = false;
		} catch (Exception e) {
			locked = false;
			return false;
		}
		return true;
	}

	public boolean update(Picture t) {
		if(t == null){
			return false;
		}
		while(locked){
			update(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/pictures/picture[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.appendElement(dom.getRootElement(), picture2Element(t));
				Dom4jUtil2.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				locked = false;
				return false;
			}
		}
		locked = false;
		return false;
	}
	
	public boolean update(List<Picture> pics) {
		if(pics == null || pics.size() == 0){
			return false;
		}
		while(locked){
			update(pics);
		}
		locked = true;
		Node node = null;
		boolean update = false;
		for (Picture t : pics) {
			node = dom.selectSingleNode("/pictures/picture[@id='" + t.getId() + "']");
			if(node != null){
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.appendElement(dom.getRootElement(), picture2Element(t));
				update = true;
			}
		}
		if(update){
			try {
				Dom4jUtil2.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				locked = false;
				return false;
			}
		}
		locked = false;
		return false;
	}

	public boolean delete(Picture t) {
		if(t == null){
			return false;
		}
		while(locked){
			delete(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/pictures/picture[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				locked = false;
				return false;
			}
		}
		locked = false;
		return false;
	}
	
	public boolean delete(List<Picture> pics) {
		if(pics == null || pics.size() == 0){
			return false;
		}
		while(locked){
			delete(pics);
		}
		locked = true;
		Node node = null;
		boolean delete = false;
		for (Picture t : pics) {
			node = dom.selectSingleNode("/pictures/picture[@id='" + t.getId() + "']");
			if(node != null){
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				delete = true;
			}
		}
		if(delete){
			try {
				Dom4jUtil2.writeDOM2XML(ComponentConst.PICTURE_XML_DATA_PATH, dom);
				locked = false;
				return true;
			} catch (Exception e) {
				locked = false;
				return false;
			}
		}
		locked = false;
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
			//排序
			Collections.sort(pics, new Comparator<Picture>() {
				public int compare(Picture o1, Picture o2) {
					return o1.getNum().compareTo(o2.getNum());
				}
			});
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
//		ele.addAttribute("realUrl", t.getRealUrl());
		ele.addAttribute("size", t.getSize() + "");
		ele.addAttribute("time", t.getTime());
		ele.addAttribute("saveAsName", t.isSaveAsName() + "");
		ele.addAttribute("isCompleted", t.isCompleted() + "");
//		ele.addAttribute("isRunning", t.isRunning() + "");
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
//		pic.setRealUrl(ele.attributeValue("realUrl"));
		pic.setSize(ele.attributeValue("size") == null ? 1 : Integer.parseInt(ele.attributeValue("size")));
		pic.setTime(ele.attributeValue("time"));
		pic.setCompleted("true".equals(ele.attributeValue("isCompleted")) ? true : false);
//		pic.setRunning("true".equals(ele.attributeValue("isRunning")) ? true : false);
		pic.setSaveAsName("true".equals(ele.attributeValue("saveAsName")) ? true : false);
		return pic;
	}

	@Override
	public boolean delete(String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}

}
