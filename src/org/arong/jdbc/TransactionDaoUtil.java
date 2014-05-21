package org.arong.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 带事务操作的数据库业务的工具类。<br />
 * 将数据映射为javabean对象/集合<br />
 * 将javabean对象持久化到数据库等
 * 
 * @Project
 * @author arong
 * @since 1.0.6
 */
public final class TransactionDaoUtil extends BasicDaoUtil {
	/**
	 * 执行一条sql语句<br>
	 * select count 则返回总记录数<br>
	 * select * 则返回行数<br>
	 * delete/update/insert则返回影响行数<br>
	 * 
	 * @param sql
	 * @param params
	 *            占位符对象数组
	 * @return
	 */
	public static int getCount(String sql, Object... params) {
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		int count = count(conn, sql, params);
		return count;
	}

	/**
	 * 执行普通不带占位符的sql语句批处理，这些sql语句可以为不同形式的语句
	 * 
	 * @param sqls
	 *            sql语句数组
	 * @since 1.0.5
	 */
	public static void executeBatch(String... sqls) {
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		executeBatch(conn, sqls);
	}

	/**
	 * 执行一个相同格式sql语句（select语句除外）的批处理，params的长度表示此批处理的任务个数
	 * 
	 * @param sql
	 *            批处理语句模版
	 * @param params
	 *            params 占位符数组，数组的每个元素对应一条sql任务
	 * @since 1.0.5
	 */
	public static void executeBatch(String sql, Object... params) {
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		executeBatch(conn, sql, params);
	}

	/**
	 * 简化的用于执行sql语句的方法，返回true表示执行成功，反之则失败<br>
	 * 如果sql不带占位符，参数params可以为null
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            sql语句占位符对应的对象数组
	 * @return
	 * @since 1.0.5
	 */
	public static boolean executeUpdate(String sql, Object... params) {
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		boolean f = execute(conn, sql, params);
		return f;
	}

	/**
	 * 将一个javabean对象持久化到/删除出数据库 可以执行insert/update/delete语句
	 * 
	 * @param sql
	 *            除了select之外的sql语句
	 * @param t
	 *            javabean对象
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static <T> boolean executeUpdate(T t, String sql) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		boolean f = execute(conn, sql, t);
		return f;
	}

	/**
	 * 执行一个数据库查询并将结果的第一条记录封装到一个指定<tt>javabean</tt>中<br />
	 * 
	 * @param sql
	 *            完整的不带占位符的select查询语句
	 * @param clazz
	 *            javabean字节码对象
	 * @return
	 */
	public static <T> T getBean(String sql, Class<T> clazz) {
		return getBean(sql, clazz, new Object[] {});
	}

	/**
	 * 执行一个数据库查询并将结果的第一条记录封装到一个指定<tt>javabean</tt>中<br />
	 * 
	 * @param sql
	 *            select查询语句
	 * @param params
	 *            sql语句中占位符所代表的对象数组
	 * @param clazz
	 *            javabean的字节码对象
	 * @return
	 */
	public static <T> T getBean(String sql, Class<T> clazz, Object... params) {
		// 获取数据库连接
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		// 获取
		T t = bean(conn, sql, clazz, params);
		return t;
	}

	/**
	 * 执行一个数据库查询并将结果封装为一个指定<b>javabean</b>的list集合。<br />
	 * 此方法要求数据库表的结构要与<b>javabean</b>中的成员变量一一对应。
	 * 
	 * @param sql
	 *            完整的不带占位符的select语句
	 * @param clazz
	 *            javabean的字节码对象
	 * @return
	 */
	public static <T> List<T> getBeanList(String sql, Class<T> clazz) {
		return getBeanList(sql, clazz, new Object[] {});
	}

	/**
	 * 执行一个数据库查询并将结果封装为一个指定<b>javabean</b>的list集合。<br />
	 * 此方法要求数据库表的结构要与<b>javabean</b>中的成员变量一一对应。
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            sql语句中占位符所代表的对象数组
	 * @param clazz
	 *            指定的javabean字节码对象
	 * @return
	 */
	public static <T> List<T> getBeanList(String sql, Class<T> clazz,
			Object... params) {
		// 获得一个数据库连接
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		// 获得集合
		List<T> list = listBean(conn, sql, clazz, params);
		// 返回集合
		return list;
	}

	/**
	 * 执行一个数据库查询并将结果集的第一条记录封装到一个Map<String, Object>中
	 * 
	 * @param sql
	 *            select语句,不能有占位符
	 * @return
	 */
	public static Map<String, Object> getMap(String sql) {
		return getMap(sql, new Object[] {});
	}

	/**
	 * 执行一个数据库查询并将结果集的第一条记录封装到一个Map<String, Object>中
	 * 
	 * @param sql
	 *            select语句
	 * @param params
	 *            占位符对象数组
	 * @return
	 */
	public static Map<String, Object> getMap(String sql, Object... params) {
		return getMapList(sql, params).get(0);
	}

	/**
	 * 执行一个数据库查询并将结果集封装到一个List<Map<String, Object>>集合中
	 * 
	 * @param sql
	 *            select语句,不能有占位符
	 * @return
	 */
	public static List<Map<String, Object>> getMapList(String sql) {
		return getMapList(sql, new Object[] {});
	}

	/**
	 * 执行一个数据库查询并将结果集封装到一个List<Map<String, Object>>集合中
	 * 
	 * @param sql
	 *            select语句
	 * @param params
	 *            占位符对象数组
	 * @return
	 */
	public static List<Map<String, Object>> getMapList(String sql,
			Object... params) {
		// 获得一个数据库连接
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		// 获得集合
		List<Map<String, Object>> list = listMap(conn, sql, params);
		// 返回集合
		return list;
	}

	/**
	 * 执行一个数据库查询并将结果集的第一条记录封装到一个Object[]中
	 * 
	 * @param sql
	 *            select语句,不能有占位符
	 * @return
	 */
	public static Object[] getArray(String sql) {
		return getArrayList(sql, new Object[] {}).get(0);
	}

	/**
	 * 执行一个数据库查询并将结果集的第一条记录封装到一个Object[]中
	 * 
	 * @param sql
	 *            select语句
	 * @param params
	 *            占位符对象数组
	 * @return
	 */
	public static Object[] getArray(String sql, Object... params) {
		return getArrayList(sql, params).get(0);
	}

	/**
	 * 执行一个数据库查询并将结果集封装到一个List<Object[]>集合中
	 * 
	 * @param sql
	 *            select语句,不能有占位符
	 * @return
	 */
	public static List<Object[]> getArrayList(String sql) {
		return getArrayList(sql, new Object[] {});
	}

	/**
	 * 执行一个数据库查询并将结果集封装到一个List<Object[]>集合中
	 * 
	 * @param sql
	 *            select语句
	 * @param params
	 *            占位符对象数组
	 * @return
	 */
	public static List<Object[]> getArrayList(String sql, Object... params) {
		// 获得一个数据库连接
		Connection conn = JdbcUtil.getConnectionInThreadLocal();
		// 获得集合
		List<Object[]> list = listArray(conn, sql, params);
		// 返回集合
		return list;
	}
}