package org.arong.jdbc;

import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用反射及泛型处理数据库业务的工具类。<br />
 * 将数据映射为javabean对象/集合<br />
 * 将javabean对象持久化到数据库等
 * 
 * @author arong
 * @since 1.0.6 从DaoUtil类中抽取出来作为父类
 */
public class BasicDaoUtil {
	/**
	 * 执行insert/update/delete/select语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            sql语句
	 * @param params
	 *            sql语句中占位符对应的对象数组
	 * @return
	 * @since 1.0.5
	 */
	public static <T> boolean execute(Connection conn, String sql,
			Object... params) {
		// 空值判断
		nonull(new Object[] { conn, sql, params });
		PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql, params);
		try {
			// 如果为select语句(三目运算符效率高)
			return sql.trim().toLowerCase().startsWith("select") ? pstmt
					.executeQuery().next() : pstmt.executeUpdate() > 0;
			/*
			 * if (sql.trim().toLowerCase().startsWith("select")) { return
			 * pstmt.executeQuery().next(); } else { return
			 * pstmt.executeUpdate() > 0; }
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			JdbcUtil.close(pstmt);
		}
		return false;
	}

	/**
	 * 将一个javabean对象持久化到/删除出数据库
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            sql语句
	 * @param t
	 *            javabean对象
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static <T> boolean execute(Connection conn, String sql, T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// 空值判断
		nonull(new Object[] { conn, sql, t });
		PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql,
				beanToParams(t, getParamsfromSQL(sql)));
		try {
			return sql.trim().toLowerCase().startsWith("select") ? pstmt
					.executeQuery().next() : pstmt.executeUpdate() > 0;
			/*
			 * if (sql.trim().toLowerCase().startsWith("select")) { //
			 * 执行select语句 return pstmt.executeQuery().next(); } else { //
			 * 执行其他更新语句 return pstmt.executeUpdate() > 0; }
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			JdbcUtil.close(pstmt);
		}
		return false;
	}

	/**
	 * 执行一条sql语句，返回行数<br>
	 * select count 则返回总记录数<br>
	 * select * 则返回行数<br>
	 * delete/update/insert则返回影响行数
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int count(Connection conn, String sql, Object... params) {
		int count = 0;
		PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql, params);
		ResultSet rs = null;
		try {
			// 获取总记录数
			if (sql.trim().toLowerCase().replaceAll(" ", "")
					.startsWith("selectcount(")) {
				rs = pstmt.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
				}
			}
			// 获取记录条数
			else if (sql.trim().toLowerCase().startsWith("select")) {
				rs = pstmt.executeQuery();
				if (rs.next())
					count++;
			} else {
				count = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			JdbcUtil.close(rs, pstmt, null);
		}
		return count;
	}

	/**
	 * 执行一个数据库查询并将结果封装为一个指定<tt>javabean</tt>中<br />
	 * 此方法要求数据库表的结构要与<tt>javabean</tt>中的成员变量一一对应。<br />
	 * 此方法内部没有将传入的<tt>conn</tt>对象关闭。
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            select查询字符串
	 * @param clazz
	 *            javabean字节码对象
	 * @param params
	 *            sql语句中占位符代表的对象数组
	 * @return
	 */
	public static <T> T bean(Connection conn, String sql, Class<T> clazz,
			Object... params) {
		T bean = null;
		// 空值判断
		nonull(new Object[] { conn, sql, clazz });
		// 是否为select查询判断
		if (!sql.trim().toLowerCase().startsWith("select")) {
			throw new RuntimeException(
					"bean(Connection conn, String sql, Class<T> clazz):sql必须为select查询！");
		} else {
			// 获得预编译sql执行对象
			PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql, params);
			ResultSet rs = null;
			try {
				// 获得指定javabean中定义的成员方法数据
				Method[] methods = clazz.getDeclaredMethods();
				// 执行
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				T t = null;
				while (rs.next()) {
					// 实例化对象
					t = clazz.newInstance();
					// 将第一条记录封装到t对象中
					toBean(methods, rs, rsmd, t);
					// 将bean指向t
					bean = t;
					// 跳出循环（理论上应该只有一条记录，但要过滤不合理的sql语句）
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				JdbcUtil.close(rs, pstmt, null);
			}
		}
		return bean;
	}

	/**
	 * 执行一个数据库查询并将结果封装为一个指定<tt>javabean</tt>的list集合。<br />
	 * 此方法要求数据库表的结构要与<tt>javabean</tt>中的成员变量一一对应。<br />
	 * 此方法内部没有将传入的<tt>conn</tt>对象关闭。
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            select语句
	 * @param clazz
	 *            javabean的字节码对象
	 * @param params
	 *            sql语句中占位符代表的对象数组
	 * @return
	 */
	public static <T> List<T> listBean(Connection conn, String sql,
			Class<T> clazz, Object... params) {
		// 定义集合
		List<T> list = new ArrayList<T>();
		// 空值判断
		nonull(new Object[] { conn, sql, clazz });
		// 是否为select查询判断
		if (!sql.trim().toLowerCase().startsWith("select")) {
			throw new RuntimeException(
					"list(Connection conn, String sql, Class<T> clazz):sql必须为select查询！");
		} else {
			// 获得预编译sql执行对象
			PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql, params);
			ResultSet rs = null;
			try {
				// 获得指定javabean中定义的成员方法数据
				Method[] methods = clazz.getDeclaredMethods();
				// 执行
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				T t = null;
				// 如果有数据
				while (rs.next()) {
					// 实例化对象
					t = clazz.newInstance();
					// 将rs指向的记录封装到t对象
					toBean(methods, rs, rsmd, t);
					// 将存储这一条记录的javabean对象添加到list集合
					list.add(t);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				JdbcUtil.close(rs, pstmt, null);
			}
		}
		// 返回集合，当结果集为空时，此对象将没有数据
		return list;
	}

	/**
	 * 执行一个select查询，将结果集封装到一个List<map<String, Object>>集合中
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> listMap(Connection conn,
			String sql, Object... params) {
		// 定义集合
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 空值判断
		nonull(new Object[] { conn, sql });
		// 是否为select查询判断
		if (!sql.trim().toLowerCase().startsWith("select")) {
			throw new RuntimeException(
					"list(Connection conn, String sql, Class<T> clazz):sql必须为select查询！");
		} else {
			// 获得预编译sql执行对象
			PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql, params);
			ResultSet rs = null;
			try {
				// 执行
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				Map<String, Object> map = null;
				Integer count = rsmd.getColumnCount();
				// 如果有数据
				while (rs.next()) {
					// 创建map对象
					map = new HashMap<String, Object>();
					for (int i = 0; i < count; i++) {
						// 将rs指向的记录封装到map对象
						map.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));
					}
					// 将存储这一条记录的map对象添加到list集合
					list.add(map);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				JdbcUtil.close(rs, pstmt, null);
			}
		}
		// 返回集合，当结果集为空时，此对象将没有数据
		return list;
	}

	/**
	 * 执行一个select查询，将结果集封装到一个List<Object[]>集合中
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Object[]> listArray(Connection conn, String sql,
			Object... params) {
		// 定义集合
		List<Object[]> list = new ArrayList<Object[]>();
		// 空值判断
		nonull(new Object[] { conn, sql });
		// 是否为select查询判断
		if (!sql.trim().toLowerCase().startsWith("select")) {
			throw new RuntimeException(
					"list(Connection conn, String sql, Class<T> clazz):sql必须为select查询！");
		} else {
			// 获得预编译sql执行对象
			PreparedStatement pstmt = JdbcUtil.getPstmt(conn, sql, params);
			ResultSet rs = null;
			try {
				// 执行
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				Object[] array = null;
				Integer count = rsmd.getColumnCount();
				// 如果有数据
				while (rs.next()) {
					// 创建数组对象
					array = new Object[count];
					for (int i = 0; i < count; i++) {
						// 将rs指向的记录封装到array对象
						array[i] = rs.getObject(i + 1);
					}
					// 将存储这一条记录的array对象添加到list集合
					list.add(array);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				JdbcUtil.close(rs, pstmt, null);
			}
		}
		// 返回集合，当结果集为空时，此对象将没有数据
		return list;
	}

	/**
	 * 执行一个相同格式sql语句的批处理，params的长度表示此批处理的任务个数
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            批处理模版
	 * @param params
	 *            占位符数组，数组的每个元素对应一条sql任务
	 * @since 1.0.5
	 */
	public static void executeBatch(Connection conn, String sql,
			Object... params) {
		// 空值判断
		nonull(new Object[] { conn, sql, params });
		// 拦截select语句
		if (sql.trim().toLowerCase().startsWith("select")) {
			throw new RuntimeException("不能为select语句");
		} else {
			PreparedStatement pstmt = null;
			pstmt = JdbcUtil.getPstmt(conn, sql);
			Integer length = params.length;
			Integer childLength = 0;
			Object[] childs = null;
			try {
				for (int i = 0; i < length; i++) {
					// 设置
					// 当数组为二维数组
					if (params[i] instanceof Object[]) {
						childs = (Object[]) params[i];
						childLength = childs.length;
						for (int j = 0; j < childLength; j++) {
							JdbcUtil.pstmtSetValue(pstmt, childs[j], j + 1);
						}
					} else {
						JdbcUtil.pstmtSetValue(pstmt, params[i], 1);
					}
					// 添加批处理任务
					pstmt.addBatch();
					if (i % 15 == 0) {
						// 最多一次处理15条记录
						pstmt.executeBatch();
						// 清空
						pstmt.clearBatch();
					}
				}
				// 最后还需要提交一次，确保全部提交
				pstmt.executeBatch();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				JdbcUtil.close(pstmt);
			}
		}
	}

	/**
	 * 执行普通不带占位符的sql语句批处理，这些sql语句可以为不同形式的语句
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sqls
	 *            sql语句数组
	 * @since 1.0.5
	 */
	public static void executeBatch(Connection conn, String... sqls) {
		// 空值判断
		nonull(new Object[] { conn, sqls });
		Statement stmt = null;
		try {
			stmt = JdbcUtil.getStmt(conn);
			for (int i = 0; i < sqls.length; i++) {
				// 添加批处理任务
				stmt.addBatch(sqls[i]);
				if (i % 15 == 0) {
					// 最多一次处理15条记录
					stmt.executeBatch();
					// 清空
					stmt.clearBatch();
				}
			}
			// 最后还需要提交一次，确保全部提交
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			JdbcUtil.close(stmt);
		}
	}

	/**
	 * 将结果集光标指向的当前记录封装到指定的javabean对象
	 * 
	 * @param methods
	 *            javabean定义的方法数组
	 * @param rs
	 *            结果集
	 * @param rsmd
	 *            结果集元数据
	 * @param t
	 *            javabean对象实例
	 */
	protected static <T> void toBean(Method[] methods, ResultSet rs,
			ResultSetMetaData rsmd, T t) {
		// 定义变量存储成员变量名
		String name = null;
		// 定义变量存储成员方法参数类型
		String paramType = null;
		// 定义变量存储成员方法名
		String methodName = null;
		String columnName = null;

		try {
			// 记录长度
			Integer count = rsmd.getColumnCount();
			// 方法数量
			Integer methodNum = methods.length;
			for (int i = 0; i < count; i++) {
				columnName = Introspector.decapitalize(rsmd
						.getColumnName(i + 1));
				for (int j = 0; j < methodNum; j++) {
					// 获得成员方法名
					methodName = methods[j].getName();
					// 如果成员方法以set开头
					if (methodName.startsWith("set")) {
						// 通过Introspector.decapitalize(String str)方法取得标准属性名
						name = Introspector.decapitalize(methodName.substring(
								3, methodName.length()));
						if (name.equals(columnName)) {
							// 获得成员方法参数类型[注：set方法只有一个参数，所以取数组第一个]
							paramType = methods[j].getParameterTypes()[0]
									.getName();
							// 判断成员变量的类型并执行set方法将结果集中的数据传入t对象
							methodInvoked(methods[j], paramType, t, rs, name);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将javabean中指定名称的属性值放入一个字符串数组中
	 * 
	 * @param t
	 *            javabean对象
	 * @param names
	 *            指定属性名的数组
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Object[] beanToParams(T t, String[] names)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// 空值判断
		nonull(new Object[] { t, names });
		// 定义空集合
		List<Object> params = new ArrayList<Object>();
		// 获取t的字节码对象
		Class<T> clazz = (Class<T>) t.getClass();
		// 获取t对象中定义的方法数组
		Method[] methods = clazz.getDeclaredMethods();
		int len = (methods == null) ? 0 : methods.length;
		// 定义方法名变量
		String methodName = null;
		// 定义属性名变量
		String name = null;
		// 定义字段名
		String columnName = null;
		// 循环指定属性名数组

		Integer namesLength = names.length;
		for (int j = 0; j < namesLength; j++) {
			columnName = Introspector.decapitalize(names[j]);
			// 循环t对象方法数组
			for (int i = 0; i < len; i++) {
				// 获取方法名
				methodName = methods[i].getName();
				// 方法名以get开头，则为非布尔类型的属性
				if (methodName.startsWith("get") || methodName.startsWith("is")) {
					// 定义获取属性名需要从那个位置开始截取的索引
					int length = methodName.startsWith("get") ? 3 : 2;
					// 通过Introspector类的静态方法decapitalize获取每个get方法对应的属性名
					name = Introspector.decapitalize(methodName.substring(
							length, methodName.length()));
					if (name.equals(columnName)) {
						// 执行get方法，获得返回值
						Object value;
						// 执行get方法获取属性值
						value = methods[i].invoke(t);
						// 添加到集合中
						params.add(value);
						// 跳出当前循环
						break;

					}
				}
			}
		}
		return params.toArray();
	}

	/**
	 * (简单的crud语句)获取出sql语句中占位符对应的字段名，(str=?),返回数组 <br>
	 * 
	 * @param sql
	 *            update info set
	 *            Name=?,Address=?,Phone=?,Mphone=?,Qq=?,Email=?,Birthday=?
	 *            where Id=?
	 * @return ["Name","Address","Phone","Mphone","Qq","Email","Birthday","Id"]
	 * @since 1.0.5
	 */
	protected static String[] getParamsfromSQL(String sql) {
		// 空值判断
		nonull(new Object[] { sql });
		// 以问号对sql语句进行分组
		String[] array = sql.split("\\?");
		int len = (sql.trim().charAt(sql.length() - 1) != '?') ? array.length - 1
				: array.length;
		// 定义返回字符串数组
		String[] params = new String[len];
		// 分组字符串初始化
		String str = null;
		String str2 = null;

		String result = null;
		for (int i = 0; i < len; i++) {
			// 对分组字符串赋值
			str = array[i].trim().toLowerCase();
			// insert into user( name , pwd ) values(?,?);
			// 通过观察，insert语句的占位符名称全部在第一分组
			if (str.startsWith("insert") && str.contains(")")) {
				// 获取到[" name , pwd "]要去掉所有空格
				result = str.substring(str.indexOf('(') + 1, str.indexOf(')'))
						.replaceAll(" ", "");
				// result = "name,pwd";
				// 直接返回以逗号分隔的数组，结束方法
				return result.split(",");
			}
			// update user set name = ?,pwd=? where id=? and name=?;
			// delete from user where id=? and name=?;
			// select * from user where id=?;
			// select * from user where id=? and name=?;
			// 通过观察，select/update/delete语句的分组字符串可能包含where/逗号(,)/and等特殊字符。
			else {
				if (i < 1) {
					// -->[update user set name](把=号去掉并去掉两端空格)
					str2 = str.replace("=", "").trim();
					params[i] = str2.substring(str2.lastIndexOf(" ") + 1);
				} else {
					// 下面这些只要把特殊字符及=号去掉并去掉两端空格即可
					if (str.contains("where")) {
						params[i] = str.replace("where", "").replace("=", "")
								.trim();
					} else if (str.contains(",")) {
						params[i] = str.replace(",", "").replace("=", "")
								.trim();
					} else if (str.contains("and")) {
						params[i] = str.replace("and", "").replace("=", "")
								.trim();
					}
				}
			}
		}
		return params;
	}

	/**
	 * 从结果集中取出一条数据，<br />
	 * 根据对应JavaBean属性类型来执行javabean的set方法把数据放进t对象
	 * 
	 * @param method
	 *            set方法的Method对象
	 * @param paramType
	 *            参数类型名（完全限定名）
	 * @param t
	 *            javabean实例对象
	 * @param rs
	 *            数据库结果集
	 * @param name
	 *            javabean成员变量名
	 */
	protected static <T> void methodInvoked(Method method, String paramType,
			T t, ResultSet rs, String name) {
		try {
			if (rs.getObject(name) != null) {
				if (paramType.equals("java.lang.String"))
					method.invoke(t, rs.getString(name));
				else if (paramType.equals("int"))
					method.invoke(t, rs.getInt(name));
				else if (paramType.equals("java.sql.Date"))
					method.invoke(t, rs.getDate(name));
				else if (paramType.equals("java.util.Date"))
					method.invoke(t, rs.getDate(name));
				else if (paramType.equals("long"))
					method.invoke(t, rs.getLong(name));
				else if (paramType.equals("double"))
					method.invoke(t, rs.getDouble(name));
				else if (paramType.equals("float"))
					method.invoke(t, rs.getFloat(name));
				else if (paramType.equals("short"))
					method.invoke(t, rs.getShort(name));
				else if (paramType.equals("java.sql.Blob"))
					method.invoke(t, rs.getBlob(name));
				else if (paramType.equals("java.sql.Clob"))
					method.invoke(t, rs.getClob(name));
				else
					method.invoke(t, rs.getObject(name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对传入的对象数组成员分别进行空值判断
	 * 
	 * @param objs
	 */
	protected static <T> void nonull(Object[] objs) {
		// 空值判断
		int len = (objs == null) ? 0 : objs.length;
		for (int i = 0; i < len; i++) {
			nullParamException("参数" + i + 1, "参数", objs[i]);
		}
	}

	/**
	 * 方法参数为空异常
	 * 
	 * @param meodth
	 *            方法名
	 * @param paramName
	 *            参数名
	 * @param obj
	 *            对象
	 */
	protected static void nullParamException(String meodth, String paramName,
			Object obj) {
		if (obj == null) {
			System.out.println(meodth + ":" + paramName + "对象不能为空！");
			throw new RuntimeException(meodth + ":" + paramName + "对象不能为空！");
		}
	}
}