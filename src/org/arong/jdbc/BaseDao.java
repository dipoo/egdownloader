package org.arong.jdbc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 将javabean与数据库的数据交互的通用方法抽取出来作为抽象父类。<br>
 * 具体的javabean dao类可以继承此类以获得更快捷的方法。
 * 
 * @author Administrator
 * 
 * @param <T>
 * @since 1.0.8
 */
public abstract class BaseDao<T> {
	// 表名
	private String tableName;
	// javabean字节码对象
	private Class<?> clazz;

	public BaseDao() {
		init();
	}
	
	public BaseDao(String tableName) {
		init();
		this.tableName = tableName;
	}

	private void init() {
		// 初始化
		Class<?> Localclazz = this.getClass();
		// 获得泛型父类
		Type type = Localclazz.getGenericSuperclass();
		// 转换
		ParameterizedType pt = (ParameterizedType) type;
		// 获得实际类型
		Type[] types = pt.getActualTypeArguments();
		// 赋值
		this.clazz = (Class<?>) types[0];
		// 赋值，表名要求与类名一致
		this.tableName = this.clazz.getSimpleName();
	}

	/**
	 * 根据id从数据库获取数据并封装到一个javabean对象<br>
	 * 要求表中具有id字段<br>
	 * 
	 * @param id
	 * @return
	 */
	public T getById(Integer id) {
		String sql = "select * from " + this.tableName + " where id = ?";
		@SuppressWarnings("unchecked")
		T t = (T) GenericDaoUtil.getBean(sql, this.clazz, id);
		return t;
	}

	/**
	 * 根据指定的字段名及字段值从数据库获取数据并封装到一个javabean对象<br>
	 * 如果记录有多条，则取第一条
	 * 
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public T getByField(String fieldName, Object obj) {
		String sql = "select * from " + this.tableName + " where " + fieldName
				+ " = ?";
		@SuppressWarnings("unchecked")
		T t = (T) GenericDaoUtil.getBean(sql, this.clazz, obj);
		return t;
	}

	/**
	 * 获取表中所有记录并封装成javabean集合
	 * 
	 * @return
	 */
	public List<T> getBeanListAll() {
		String sql = "select * from " + this.tableName;
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) GenericDaoUtil.getBeanList(sql, this.clazz);
		return list;
	}

	/**
	 * 用于获取分页数据<br>
	 * mysql数据库才能用这个方法
	 * 
	 * @param m
	 *            记录从哪里开始
	 * @param n
	 *            取多少条记录
	 * @return
	 */
	public List<T> getBeanListByPage(Integer m, Integer n) {
		String sql = "select * from " + this.tableName + " limit ?,?";
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) GenericDaoUtil.getBeanList(sql, this.clazz, m,
				n);
		return list;
	}

	/**
	 * 获取表中所有记录并封装到map集合中
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getMapListAll() {
		String sql = "select * from " + this.tableName;
		List<Map<String, Object>> list = (List<Map<String, Object>>) GenericDaoUtil
				.getMapList(sql);
		return list;
	}

	/**
	 * 用于获取分页数据<br>
	 * mysql数据库才能用这个方法
	 * 
	 * @param m
	 *            记录从哪里开始
	 * @param n
	 *            取多少条记录
	 * @return
	 */
	public List<Map<String, Object>> getMapListByPage(Integer m, Integer n) {
		String sql = "select * from " + this.tableName + " limit ?,?";
		List<Map<String, Object>> list = (List<Map<String, Object>>) GenericDaoUtil
				.getMapList(sql, m, n);
		return list;
	}
	/**
	 * 根据id删除一条记录<br>
	 * 表中要有id字段
	 * @param id id字段值
	 * @return
	 */
	public boolean deleteById(Integer id){
		String sql = "delete from " + this.tableName + " where id = ?";
		return GenericDaoUtil.executeUpdate(sql, id);
	} 
	/**
	 * 根据字段名及字段值删除一条记录<br>
	 * 如果有多条记录，则删除第一条
	 * @param field 字段名
	 * @param obj 字段值
	 * @return
	 */
	public boolean deleteByField(String field, Object obj){
		String sql = "delete from " + this.tableName + " where " + field + " = ?";
		return GenericDaoUtil.executeUpdate(sql, obj);
	}
	/**
	 * 根据多个id进行批处理删除
	 * @param ids
	 */
	public void deleteBatchById(Integer... ids){
		String sql = "delete from " + this.tableName + " where id = ?";
		GenericDaoUtil.executeBatch(sql, (Object[])ids);
	}
	/**
	 * 根据某个字段及多个字段值进行批处理删除
	 * @param field 字段名
	 * @param objs 
	 */
	public void deleteBatchByField(String field, Object... objs){
		String sql = "delete from " + this.tableName + " where " + field + " = ?";
		GenericDaoUtil.executeBatch(sql, objs);
	}
	/**
	 * 根据某个字段查询是否存在记录
	 * @param field 字段名
	 * @param obj 字段值
	 * @return
	 */
	public boolean exist(String field, Object obj){
		String sql = "select * from " + this.tableName + " where " + field + " = ?";;
		return GenericDaoUtil.executeUpdate(sql, obj);
	}
}