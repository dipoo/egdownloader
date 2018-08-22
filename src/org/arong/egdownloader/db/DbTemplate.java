package org.arong.egdownloader.db;

import java.util.List;
/**
 * 数据存取模板接口
 * @author 阿荣
 * @since 2014-05-29
 * @param <T>
 */
public interface DbTemplate<T> {
	public boolean store(T t);
	public boolean store(List<T> list);
	public boolean update(T t);
	public boolean update(List<T> t);
	public boolean delete(T t);
	public boolean delete(String name, String value);
	public boolean delete(List<T> list);
	public List<T> query();
	public List<T> query(Object id);
	public List<T> query(String name, String value);
	public T get(Object id);
	public boolean exsits(String name, String value);
}
