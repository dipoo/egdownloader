package org.arong.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 减少jdbc执行产生的代码量
 */
public class JdbcSqlExecutor {
	private static JdbcSqlExecutor executor = null;
	private JdbcSqlExecutor(){}
	public final static String BAT_SPLIT = "@<;>@";
	public static JdbcSqlExecutor getInstance(){
		if(executor == null){
			executor = new JdbcSqlExecutor();
		}
		return executor;
	}
	public <T> T executeQuery(String sql, Connection conn, CallBack<T> callback) throws SQLException{
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			return callback.action(rs);
			
		} catch (SQLException e) {
			throw e;
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst != null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int executeUpdate(String sql, Connection conn) throws SQLException{
		return executeUpdate(sql, false, conn);
	}
	
	public int executeUpdate(String sql, boolean batUpdate, Connection conn) throws SQLException{
		
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		int result = 0;
		try {
			if(batUpdate){
				st = conn.createStatement();
				String[] sqls = sql.split(BAT_SPLIT);
				for(int i = 0; i < sqls.length; i ++){
					st.addBatch(sqls[i]);
				}
				result =  st.executeBatch()[0];
			}else{
				pst = conn.prepareStatement(sql.replaceAll(BAT_SPLIT, ""));
				result = pst.executeUpdate();
			}
			return result;
		} catch (SQLException e) {
			throw e;
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst != null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(st != null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public interface CallBack<T>{
		T action(ResultSet rs) throws SQLException;
	}
}
