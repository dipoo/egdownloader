package org.arong.egdownloader.db.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskList;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.Dom4jUtil2;
import org.arong.util.FileUtil2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
/**
 *  下载任务存取的dom4j实现
 * @author 阿荣
 * @since 2014-05-29
 */
public class TaskDom4jDbTemplate implements DbTemplate<Task> {
	/**
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <tasks>
	 * 	<task id="" url="" name="" saveDir="" createTime="" completedTime="" total="" current="" size="" status=""/>
	 * </tasks>
	 */
	private static boolean locked;
	private static Document dom;
	
	static{
		updateDom();
	}
	
	public static void updateDom(){
		try {
			dom = Dom4jUtil2.getDOM(ComponentConst.TASK_XML_DATA_PATH);
		} catch (DocumentException e) {
			FileUtil2.ifNotExistsThenCreate(ComponentConst.DATA_PATH);
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tasks></tasks>";
			int length = 0; //每一次读取的长度
			char[] buffer = new char[2048]; //设缓冲最大值为2048字符
			//字符串转为字符流
			BufferedReader br = null;
			BufferedWriter bw = null;
			try {
				br = new BufferedReader(new StringReader(xml));
				bw = new BufferedWriter(new FileWriter(ComponentConst.TASK_XML_DATA_PATH));
				while((length = br.read(buffer)) != -1){ //若读到的不是末尾
					bw.write(buffer, 0, length);
				}
				bw.flush();
				dom = Dom4jUtil2.getDOM(ComponentConst.TASK_XML_DATA_PATH);
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
	
	public boolean store(Task t) {
		if(t == null){
			return false;
		}
		while(locked){
			store(t);
		}
		locked = true;
		Element ele = task2Element(t);
		Dom4jUtil2.appendElement(dom.getRootElement(), ele);
		try {
			Dom4jUtil2.writeDOM2XML(ComponentConst.TASK_XML_DATA_PATH, dom);
			locked = false;
		} catch (Exception e) {
			locked = false;
			return false;
		}
		return true;
	}
	
	public boolean store(List<Task> tasks) {
		if(tasks == null || tasks.size() == 0){
			return false;
		}
		while(locked){
			store(tasks);
		}
		locked = true;
		for (Task t : tasks) {
			Element ele = task2Element(t);
			Dom4jUtil2.appendElement(dom.getRootElement(), ele);
		}
		
		try {
			Dom4jUtil2.writeDOM2XML(ComponentConst.TASK_XML_DATA_PATH, dom);
			locked = false;
		} catch (Exception e) {
			locked = false;
			return false;
		}
		return true;
	}

	public boolean update(Task t) {
		if(t == null){
			return false;
		}
		while(locked){
			update(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/tasks/task[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.appendElement(dom.getRootElement(), task2Element(t));
				Dom4jUtil2.writeDOM2XML(ComponentConst.TASK_XML_DATA_PATH, dom);
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
	
	public boolean update(List<Task> tasks) {
		if(tasks == null || tasks.size() == 0){
			return false;
		}
		while(locked){
			update(tasks);
		}
		locked = true;
		Node node = null;
		boolean update = false;
		for (Task t : tasks) {
			node = dom.selectSingleNode("/tasks/task[@id='" + t.getId() + "']");
			if(node != null){
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.appendElement(dom.getRootElement(), task2Element(t));
				update = true;
			}
		}
		if(update){
			try {
				Dom4jUtil2.writeDOM2XML(ComponentConst.TASK_XML_DATA_PATH, dom);
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

	public boolean delete(Task t) {
		while(locked){
			delete(t);
		}
		locked = true;
		Node node = dom.selectSingleNode("/tasks/task[@id='" + t.getId() + "']");
		if(node != null){
			try {
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				Dom4jUtil2.writeDOM2XML(ComponentConst.TASK_XML_DATA_PATH, dom);
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
	
	public boolean delete(List<Task> tasks) {
		if(tasks == null || tasks.size() == 0){
			return false;
		}
		while(locked){
			delete(tasks);
		}
		locked = true;
		Node node = null;
		boolean delete = false;
		for (Task t : tasks) {
			node = dom.selectSingleNode("/tasks/task[@id='" + t.getId() + "']");
			if(node != null){
				Dom4jUtil2.deleteElement(dom.getRootElement(), (Element)node);
				delete = true;
			}
		}
		if(delete){
			try {
				Dom4jUtil2.writeDOM2XML(ComponentConst.TASK_XML_DATA_PATH, dom);
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

	public List<Task> query() {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/tasks/task");
		if(nodes != null && nodes.size() > 0){
			TaskList<Task> tasks = new TaskList<Task>();
			for (Node node : nodes) {
				tasks.add(node2Task(node));
			}
			//按照创建日期进行降序排序
			Collections.sort(tasks, new Comparator<Task>() {
				private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				public int compare(Task o1, Task o2) {
					try {
						if(sdf.parse(o1.getCreateTime()).after(sdf.parse(o2.getCreateTime()))){
							return -1;
						}else{
							return 1;
						}
					} catch (ParseException e) {
						return 0;
					}
				}
			});
			return tasks;
		}
		return null;
	}

	public List<Task> query(Object id) {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/tasks/task[@id='" + id.toString() + "']");
		if(nodes != null && nodes.size() > 0){
			List<Task> tasks = new ArrayList<Task>();
			for (Node node : nodes) {
				tasks.add(node2Task(node));
			}
			return tasks;
		}
		return null;
	}
	
	public List<Task> query(String name, String value) {
		@SuppressWarnings("unchecked")
		List<Node> nodes = dom.selectNodes("/tasks/task[@" + name + "='" + value + "']");
		List<Task> tasks = null;
		if(nodes != null && nodes.size() > 0){
			tasks = new ArrayList<Task>();
			for (Node node : nodes) {
				tasks.add(node2Task(node));
			}
		}
		return tasks;
	}

	public Task get(Object id) {
		Node node = dom.selectSingleNode("/tasks/task[@id='" + id.toString() + "']");
		if(node != null){
			return node2Task(node);
		}
		return null;
	}

	public boolean exsits(String name, String value) {
		Node node = dom.selectSingleNode("/tasks/task[@" + name + "='" + value + "']");
		if(node != null){
			return true;
		}
		return false;
	}
	
	private static Element task2Element(Task t){
		Element ele = DocumentHelper.createElement("task");
		ele.addAttribute("id", t.getId());
		ele.addAttribute("url", t.getUrl());
		ele.addAttribute("name", t.getName());
		ele.addAttribute("subname", t.getSubname());
		ele.addAttribute("coverUrl", t.getCoverUrl());
		ele.addAttribute("language", t.getLanguage());
		ele.addAttribute("type", t.getType());
		ele.addAttribute("saveDir", t.getSaveDir());
		ele.addAttribute("tag", t.getTag());
		ele.addAttribute("readed", t.isReaded() + "");
		ele.addAttribute("createTime", t.getCreateTime());
		ele.addAttribute("completedTime", t.getCompletedTime());
		ele.addAttribute("total", t.getTotal() + "");
		ele.addAttribute("current", t.getCurrent() + "");
		ele.addAttribute("size", t.getSize());
		ele.addAttribute("status", t.getStatus().getStatus() + "");
		ele.addAttribute("start", t.getStart() + "");
		ele.addAttribute("end", t.getEnd() + "");
		return ele;
	}
	
	private static Task node2Task(Node node){
		Element ele = (Element)node;
		Task task = new Task();
		task.setId(ele.attributeValue("id"));
		task.setUrl(ele.attributeValue("url"));
		task.setName(ele.attributeValue("name"));
		task.setSubname(ele.attributeValue("subname") == null ? "" : ele.attributeValue("subname"));
		task.setCoverUrl(ele.attributeValue("coverUrl"));
		task.setLanguage(ele.attributeValue("language") == null ? "" : ele.attributeValue("language"));
		task.setType(ele.attributeValue("type") == null ? "" : ele.attributeValue("type"));
		task.setSaveDir(ele.attributeValue("saveDir"));
		task.setTag(ele.attributeValue("tag") == null ? "一般" : ele.attributeValue("tag"));
		task.setReaded("true".equals(ele.attributeValue("readed")) ? true : false);
		task.setCreateTime(ele.attributeValue("createTime"));
		task.setCompletedTime(ele.attributeValue("completedTime"));
		task.setTotal(ele.attributeValue("total") == null ? 0 : Integer.parseInt(ele.attributeValue("total")));
		task.setCurrent(ele.attributeValue("current") == null ? 0 : Integer.parseInt(ele.attributeValue("current")));
		task.setSize(ele.attributeValue("size") == null ? "" : ele.attributeValue("size"));
		//如果任务为下载中，则加载到内存中设置为已暂停，由用户手动开启任务
		TaskStatus ts = TaskStatus.parseTaskStatus(ele.attributeValue("status"));
		if(ts == TaskStatus.STARTED){
			ts = TaskStatus.STOPED;
		}
		task.setStatus(ts);
		task.setStart(ele.attributeValue("start") == null ? 1 : Integer.parseInt(ele.attributeValue("start")));
		task.setEnd(ele.attributeValue("end") == null ? task.getTotal() : Integer.parseInt(ele.attributeValue("end")));
		return task;
	}

	@Override
	public boolean delete(String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}
}
