package org.arong.egdownloader.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
/**
 * 对ArrayList的调整类，主要添加属性监听
 * @author dipoo
 * @since 2015-07-25
 */
public class TaskList<T> extends ArrayList<T> {
	private static final long serialVersionUID = 8354635507256958013L;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);//属性变化监听支持
	private Map<String, Integer> taskUrlMap = new HashMap<String, Integer>();//用于通过地址检查任务是否已存在
	//http://exhentai.org/t/33/9d【/339df975fe】9d2fdff9b097db4c220341baf463ad-1431193-1428-2018-png_250.jpg
	private StringBuilder taskTokens = new StringBuilder();//用于通过任务地址与封面地址检查任务是否存在已添加的旧版，格式如：/339df975fe_{gid}/228ssdsdsa_{gid}
	public static final int COVER_TOKEN_SUB_LENGTH = 20;
	public Map<String, Integer> getTaskUrlMap() {
		return taskUrlMap;
	}
	public StringBuilder getTokenTokens() {
		return taskTokens;
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}
	
	private void addCoverToken(Task task){
		if(StringUtils.isNotBlank(task.getUrl()) && StringUtils.isNotBlank(task.getCoverUrl())){
			String[] taskarr = task.getUrl().split("/g/");
			String[] coverarr = task.getCoverUrl().split("-");
			taskTokens.append(coverarr[0].substring(coverarr[0].lastIndexOf("/"), coverarr[0].lastIndexOf("/") + COVER_TOKEN_SUB_LENGTH))
					.append(taskarr[1].substring(0, taskarr[1].indexOf("/")));
		}
	}
	private void removeCoverToken(Task task){
		if(StringUtils.isNotBlank(task.getUrl()) && StringUtils.isNotBlank(task.getCoverUrl())){
			String[] taskarr = task.getUrl().split("/g/");
			String[] coverarr = task.getCoverUrl().split("-");
			String token = coverarr[0].substring(coverarr[0].lastIndexOf("/"), coverarr[0].lastIndexOf("/") + COVER_TOKEN_SUB_LENGTH) + taskarr[1].substring(0, taskarr[1].indexOf("/"));
			if(taskTokens.indexOf(token) != -1){
				taskTokens.replace(taskTokens.indexOf(token), taskTokens.indexOf(token) + token.length(), "");
			}
		}
	}

	public boolean add(T e) {
		if(e instanceof Task){
			Task t = (Task)e;
			taskUrlMap.put(getCacheKey(t.getUrl()), 0);
			addCoverToken(t);
		}
		changeSupport.firePropertyChange("", null, null);
		return super.add(e);
	}
	
	public boolean remove(Object o) {
		if(o instanceof Task){
			Task t = (Task)o;
			taskUrlMap.remove(getCacheKey(t.getUrl()));
			removeCoverToken(t);
		}
		changeSupport.firePropertyChange("", null, null);
		return super.remove(o);
	}
	
	public boolean addAll(Collection<? extends T> c) {
		if(c != null && c.size() > 0 && c.iterator().next() instanceof Task){
			Iterator<? extends T> it = c.iterator();
			while(it.hasNext()){
				T o = it.next();
				if(o instanceof Task){
					Task t = (Task)o;
					taskUrlMap.put(getCacheKey(t.getUrl()), 0);
					addCoverToken(t);
				}
			}
		}
		changeSupport.firePropertyChange("", null, null);
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if(c != null && c.size() > 0 && c.iterator().next() instanceof Task){
			Iterator<? extends T> it = c.iterator();
			while(it.hasNext()){
				T o = it.next();
				if(o instanceof Task){
					Task t = (Task)o;
					taskUrlMap.put(getCacheKey(t.getUrl()), 0);
					addCoverToken(t);
				}
			}
		}
		changeSupport.firePropertyChange("", null, null);
		return super.addAll(index, c);
	}
	public boolean removeAll(Collection<?> c) {
		if(c != null && c.size() > 0 && c.iterator().next() instanceof Task){
			Iterator<?> it = c.iterator();
			while(it.hasNext()){
				Object o = it.next();
				if(o instanceof Task){
					Task t = (Task)o;
					taskUrlMap.remove(getCacheKey(t.getUrl()));
					removeCoverToken(t);
				}
			}
		}
		changeSupport.firePropertyChange("", null, null);
		return super.removeAll(c);
	}

	public void add(int index, T e) {
		if(e instanceof Task){
			Task t = (Task)e;
			taskUrlMap.put(getCacheKey(t.getUrl()), 0);
			addCoverToken(t);
		}
		changeSupport.firePropertyChange("", null, null);
		super.add(index, e);
	}
	
	public static String getCacheKey(String url){
		if(StringUtils.isNotBlank(url)){
			return url.replaceAll("https://", "http://").replaceAll("exhentai.org", "").replaceAll("e-hentai.org", "");
		}
		return url;
	}
}
