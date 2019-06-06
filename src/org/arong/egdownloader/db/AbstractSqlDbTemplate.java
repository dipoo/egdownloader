package org.arong.egdownloader.db;

import java.sql.SQLException;

import org.arong.util.jdbc.JdbcSqlExecutor;
import org.arong.util.jdbc.JdbcUtil;

public abstract class AbstractSqlDbTemplate<T> implements DbTemplate<T>{
	public Object queryBySQL(String sql, JdbcSqlExecutor.CallBack<Object> callback){
		try {
			return JdbcSqlExecutor.getInstance().executeQuery(sql, JdbcUtil.getConnection(), callback);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	};
}
