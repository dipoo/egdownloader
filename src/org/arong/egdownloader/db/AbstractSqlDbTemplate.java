package org.arong.egdownloader.db;

import java.sql.SQLException;

import org.arong.jdbc.JdbcUtil;
import org.arong.util.JdbcSqlExecutor;

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
