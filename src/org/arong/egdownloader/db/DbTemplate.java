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
	public boolean update(T t);
	public boolean delete(T t);
	public List<T> query();
	public List<T> query(Object id);
	public T get(Object id);
	public boolean exsits(String name, String value);
}
