package org.arong.egdownloader.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * 对ArrayList的调整类，主要添加属性监听
 * @author dipoo
 * @since 2015-07-25
 * @param <T>
 */
public class TaskList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 8354635507256958013L;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);//属性变化监听支持
	private Map<String, Integer> taskMap = new HashMap<String, Integer>();
	
	public Map<String, Integer> getTaskMap() {
		return taskMap;
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}

	public boolean add(T e) {
		changeSupport.firePropertyChange("", null, null);
		return super.add(e);
	}
	
	public boolean add(T e, String key) {
		taskMap.put(key, 0);
		return add(e);
	}

	public boolean remove(Object o) {
		changeSupport.firePropertyChange("", null, null);
		return super.remove(o);
	}
	
	public boolean remove(Object o, String key) {
		taskMap.remove(key);
		return remove(o);
	}

	public boolean addAll(Collection<? extends T> c) {
		changeSupport.firePropertyChange("", null, null);
		return super.addAll(c);
	}
	
	public boolean addAll(Collection<? extends T> c, Collection<String> keys) {
		if(keys != null){
			for (String key : keys) {
				taskMap.put(key, 0);
			}
		}
		return addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		changeSupport.firePropertyChange("", null, null);
		return super.removeAll(c);
	}
	public boolean removeAll(Collection<?> c, Collection<String> keys) {
		if(keys != null){
			for (String key : keys) {
				taskMap.remove(key);
			}
		}
		return removeAll(c);
	}

	public void add(int index, T element) {
		changeSupport.firePropertyChange("", null, null);
		super.add(index, element);
	}
	public void add(int index, T element, String key) {
		taskMap.put(key, 0);
		add(index, element);
	}
}
