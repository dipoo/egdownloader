package org.arong.util.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.arong.util.FileUtil2;

/**
 * 获取数据库连接、sql执行对象等的工具类
 * 
 * @author arong
 * @since 1.0.0
 */

public final class JdbcUtil {
	/**
	 * 配置信息的集合，在静态代码块中初始化
	 */
	private static Properties prop;
	/**
	 * 连接池对象
	 */
	private static DataSource dataSource;

	public static final String CONFIG_FILE_NAME = "arong-db.properties";
	public static final String CONFIG_DRIVER_KEY = "driver";
	public static final String CONFIG_URL_KEY = "url";
	public static final String CONFIG_USER_KEY = "user";
	public static final String CONFIG_PASSWORD_KEY = "password";
	public static final String CONFIG_USEDATASOURCE_KEY = "useDataSource";
	public static final String CONFIG_MAXACTIVE_KEY = "maxActive";
	public static final String CONFIG_MAXIDLE_KEY = "maxIdle";
	public static final String CONFIG_MINIDLE_KEY = "minIdle";
	public static final String CONFIG_INITIALSIZE_KEY = "initialSize";
	public static final String CONFIG_MAXWAIT_KEY = "maxWait";
	public static final String CONFIG_DATASOURCE_KEY = "dataSource";

	/**
	 * 静态代码块,加载配置文件,配置文件放在【项目/WEB-INF/classes】目录下<br>
	 * 如果
	 */
	static {
		try {
			prop = new Properties();
			File conf = new File(JdbcUtil.class.getResource("/" + CONFIG_FILE_NAME).getPath());
			InputStream is = null;
			if(! conf.exists()){
				String[] _p_ = new String[3]; _p_[0] = conf.getAbsolutePath();
				conf = new File(FileUtil2.getProjectPath() + "/" + CONFIG_FILE_NAME);
				if(! conf.exists()){
					_p_[1] = conf.getAbsolutePath();
					conf = new File(FileUtil2.getAppPath(JdbcUtil.class) + "/" + CONFIG_FILE_NAME);
					if(! conf.exists()){
						is = JdbcUtil.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
						if(is == null){
							_p_[2] = conf.getAbsolutePath();
							StringBuffer sb = new StringBuffer();
							for(String p : _p_){
								sb.append(p).append(";");
							}
							throw new FileNotFoundException(sb.toString());
						}
					}
				}
			}
			// 加载数据库配置文件
			if(is != null){
				prop.load(is);
			}else{
				prop.load(new BufferedReader(new FileReader(conf)));
			}

			// 是否初始化数据源
			if (prop.getProperty(CONFIG_USEDATASOURCE_KEY) != null
					&& ("true".equals(prop
							.getProperty(CONFIG_USEDATASOURCE_KEY)
							.toLowerCase()) || "yes".equals(prop.getProperty(
							CONFIG_USEDATASOURCE_KEY).toLowerCase()))) {
				// 启动数据源
				setupDataSource();
			} else
				// 注册数据库驱动
				Class.forName(prop.getProperty(CONFIG_DRIVER_KEY));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到" + CONFIG_FILE_NAME + "数据库配置文件:" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到"
					+ prop.getProperty(CONFIG_USEDATASOURCE_KEY) + "驱动文件");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 启动连接池
	 * 
	 * @param prop
	 */
	private synchronized static void setupDataSource() {
		// 取得必须属性值
		String driver = prop.getProperty(CONFIG_DRIVER_KEY);
		String url = prop.getProperty(CONFIG_URL_KEY);
		String user = prop.getProperty(CONFIG_USER_KEY);
		String password = prop.getProperty(CONFIG_PASSWORD_KEY);
		// 取得可选属性值
		// 最大连接数
		String maxActive_str = prop.getProperty(CONFIG_MAXACTIVE_KEY);
		int maxActive = maxActive_str != null ? Integer.parseInt(maxActive_str)
				: 0;

		// 最大峰值
		String maxIdle_str = prop.getProperty(CONFIG_MAXIDLE_KEY);
		int maxIdle = maxIdle_str != null ? Integer.parseInt(maxIdle_str) : 0;

		// 最小峰值
		String minIdle_str = prop.getProperty(CONFIG_MINIDLE_KEY);
		int minIdle = minIdle_str != null ? Integer.parseInt(minIdle_str) : 0;

		// 初始化大小
		String initialSize_str = prop.getProperty(CONFIG_INITIALSIZE_KEY);
		int initialSize = initialSize_str != null ? Integer
				.parseInt(initialSize_str) : 0;

		// 最大等待时间
		String maxWait_str = prop.getProperty(CONFIG_MAXWAIT_KEY);
		long maxWait = maxWait_str != null ? Long.parseLong(maxWait_str) : 0L;

		// c3p0连接池
		if (prop.getProperty(CONFIG_DATASOURCE_KEY) != null
				&& "c3p0".equals(prop.getProperty(CONFIG_DATASOURCE_KEY)
						.toLowerCase())) {
			Object cpds = null;
			try {
				cpds = Class.forName(
						"com.mchange.v2.c3p0.ComboPooledDataSource")
						.newInstance();
				invoked(cpds, "setDriverClass", driver);
				invoked(cpds, "setJdbcUrl", url);
				invoked(cpds, "setUser", user);
				invoked(cpds, "setPassword", password);

				if (maxActive != 0)
					invoked(cpds, "setMaxStatements", maxActive);
				if (maxIdle != 0)
					invoked(cpds, "setMaxIdleTime", maxIdle);
				if (minIdle != 0)
					invoked(cpds, "setMinPoolSize", minIdle);
				if (initialSize != 0)
					invoked(cpds, "setInitialPoolSize", initialSize);
				if (maxWait != 0L)
					invoked(cpds, "setMaxConnectionAge", (int) maxWait);
				dataSource = (DataSource) cpds;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("找不到c3p0所需的jar包，请手动导入！");
			}
		}
		// dbcp连接池
		else {
			Object bds = null;
			try {
				bds = Class.forName("org.apache.commons.dbcp.BasicDataSource")
						.newInstance();
				invoked(bds, "setDriverClassName", driver);
				invoked(bds, "setUrl", url);
				invoked(bds, "setUsername", user);
				invoked(bds, "setPassword", password);

				if (maxActive != 0)
					invoked(bds, "setMaxActive", maxActive);
				if (maxIdle != 0)
					invoked(bds, "setMaxIdle", maxIdle);
				if (minIdle != 0)
					invoked(bds, "setMinIdle", minIdle);
				if (initialSize != 0)
					invoked(bds, "setInitialSize", initialSize);
				if (maxWait != 0L)
					invoked(bds, "setMaxWait", maxWait);
				dataSource = (DataSource) bds;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("找不到dbcp所需的jar包，请手动导入！");
			}
		}
	}

	/**
	 * 根据方法名调用给定对象的方法
	 * 
	 * @param obj
	 * @param methodName
	 * @param value
	 */
	private static void invoked(Object obj, String methodName, Object value) {
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		String _methodName = null;
		try {
			for (int i = 0; i < methods.length; i++) {
				_methodName = methods[i].getName();
				if (_methodName.equals(methodName)) {
					methods[i].invoke(obj, value);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Connection对象
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;
		try {
			/*
			 * if (dataSource != null) { connection =
			 * dataSource.getConnection(); } else { connection =
			 * DriverManager.getConnection( prop.getProperty("url"), prop); }
			 */
			connection = dataSource != null ? dataSource.getConnection()
					: DriverManager
							.getConnection(prop.getProperty("url"), prop);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 获取PreparedStatement对象
	 * 
	 * @param sql
	 * @return
	 */
	public static PreparedStatement getPstmt(Connection conn, String sql) {
		PreparedStatement pstmt = null;
		if (conn != null && sql != null) {
			try {
				pstmt = conn.prepareStatement(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("获取PreparedStatement异常");
			}
		}
		return pstmt;
	}

	/**
	 * 获取PreparedStatement对象
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            sql语句字符串
	 * @param params
	 *            sql语句中占位符所对应的参数数组
	 * @return
	 */
	public static PreparedStatement getPstmt(Connection conn, String sql,
			Object[] params) {
		PreparedStatement pstmt = null;
		if (conn != null && sql != null) {
			try {
				pstmt = conn.prepareStatement(sql);
				if (params != null) {
					Integer length = params.length;
					// 循环占位符数组对象
					for (int i = 0; i < length; i++) {
						pstmtSetValue(pstmt, params[i], i + 1);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("获取PreparedStatement异常");
			}
		}
		return pstmt;
	}

	/**
	 * 执行PreparedStatement的set方法为占位符设值
	 * 
	 * @param pstmt
	 * @param value
	 * @param index
	 */
	public static void pstmtSetValue(PreparedStatement pstmt, Object value,
			int index) {
		if (pstmt != null) {
			try {
				pstmt.setObject(index, value);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("PreparedStatement设置占位符异常");
			}
		}
	}

	/**
	 * 获取Statement对象
	 * 
	 * @param conn
	 * @return
	 */
	public static Statement getStmt(Connection conn) {
		Statement stmt = null;
		if (conn != null) {
			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("获取Statement异常");
			}
		}
		return stmt;

	}

	/**
	 * Connection关闭连接
	 */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection 关闭异常");
			}
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param pstmt
	 */
	public static void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
				pstmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("PreparedStatement 关闭异常");
			}
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param pstmt
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Statement 关闭异常");
			}
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("ResultSet 关闭异常");
			}
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param pstmt
	 * @param conn
	 */
	public static void close(PreparedStatement pstmt, Connection conn) {
		close(pstmt);
		close(conn);
	}

	/**
	 * 关闭连接
	 * 
	 * @param rs
	 * @param pstmt
	 * @param conn
	 */
	public static void close(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		close(rs);
		close(pstmt);
		close(conn);
	}

	/** ------------------------------事务相关---------------------------- **/

	/**
	 * 本地线程，用于存储当前事务公用的Connection对象
	 */
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

	/**
	 * 通过在本地线程中获取数据库连接对象
	 * 
	 * @return
	 */
	public static Connection getConnectionInThreadLocal() {
		Connection conn = tl.get();
		if (conn == null) {
			conn = getConnection();
			tl.set(conn);
		}
		return conn;
	}

	/**
	 * 开启事务
	 */
	public static void startTransaction() {
		Connection conn = getConnectionInThreadLocal();
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection 开启事务异常");
			}
		}
	}

	/**
	 * 开启事务
	 * 
	 * @param isolation
	 *            事务隔离级别<br >
	 *            取值为Connection中的4个常量<br >
	 *            取0时不设置，采用所使用的数据库系统的默认值
	 */
	public static void startTransaction(int isolation) {
		Connection conn = getConnectionInThreadLocal();
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
				if (isolation != 0)
					conn.setTransactionIsolation(isolation);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection 开启事务异常");
			}
		}
	}

	/**
	 * 事务回滚
	 */
	public static void rollback() {
		Connection conn = tl.get();
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection 事务回滚异常");
			}
		}
	}

	/**
	 * 事务提交
	 */
	public static void commit() {
		Connection conn = tl.get();
		if (conn != null) {
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Connection 事务提交异常");
			}
		}
	}

	/**
	 * 事务结束后关闭连接对象并移除本地线程中的连接对象
	 */
	public static void closeAndRemove() {
		Connection conn = tl.get();
		if (conn != null) {
			tl.remove();
			close(conn);
		}
	}

	/**
	 * 提交事务，移除连接，关闭连接
	 */
	public static void commit_Close_Remove() {
		Connection conn = tl.get();
		if (conn != null) {
			try {
				conn.commit();
				tl.remove();
				close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("事务提交、关闭、清空异常");
			}
		}
	}

	/**
	 * 获取本工具类的配置说明信息
	 */
	public static void getConfigIntru() {
		System.out.println("一、在工程的src目录下创建一个名为：" + CONFIG_FILE_NAME
				+ "的数据库配置文件。");
		System.out.println("");
		System.out.println("          这个文件有4个必需属性：");
		System.out.println("       1." + CONFIG_DRIVER_KEY + "：数据库驱动");
		System.out.println("       2." + CONFIG_URL_KEY + "：数据库url地址");
		System.out.println("       3." + CONFIG_USER_KEY + "：数据库用户名称");
		System.out.println("       4." + CONFIG_PASSWORD_KEY + "：数据库用户密码");
		System.out.println("");
		System.out.println("          这个文件有7个可选属性：");
		System.out.println("       5." + CONFIG_USEDATASOURCE_KEY + "：是否使用数据源");
		System.out.println("       6." + CONFIG_MAXACTIVE_KEY + "：数据源最大连接数");
		System.out.println("       7." + CONFIG_MAXIDLE_KEY + "：数据源最大峰值");
		System.out.println("       8." + CONFIG_MINIDLE_KEY + "：数据源最小峰值");
		System.out.println("       9." + CONFIG_INITIALSIZE_KEY + "：数据源初始值");
		System.out.println("       10." + CONFIG_MAXWAIT_KEY + "：最大等待时间");
		System.out.println("       11." + CONFIG_DATASOURCE_KEY + "：数据源的名称");
		System.out.println("");
		System.out.println("二、如果使用数据源，请指定" + CONFIG_USEDATASOURCE_KEY
				+ "=true或yes");
		System.out.println("");
		System.out.println("          当" + CONFIG_DATASOURCE_KEY
				+ "=c3p0,则使用c3p0连接池；");
		System.out.println("          当" + CONFIG_DATASOURCE_KEY
				+ "属性没有指定值，默认使用dbcp连接池。");
	}
}