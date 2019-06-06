package org.arong.egdownloader.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.db.AbstractSqlDbTemplate;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.ui.work.DownloadWorker;
import org.arong.util.Tracker;
import org.arong.util.jdbc.JdbcSqlExecutor;
import org.arong.util.jdbc.JdbcUtil;

public class PictureSqliteDbTemplate extends AbstractSqlDbTemplate<Picture> {
	
	static{
		StringBuffer sqlsb = new StringBuffer("create table picture (")
		.append("id VARCHAR(256) PRIMARY KEY NOT NULL,")
		.append("tid VARCHAR(256) NOT NULL,")
		.append("num VARCHAR(64) NOT NULL,")
		.append("name VARCHAR(1024),")
		.append("url VARCHAR(512),")
		.append("realUrl VARCHAR(512),")
		.append("size VARCHAR(64),")
		.append("time VARCHAR(64),")
		.append("saveAsName VARCHAR(64),")
		.append("isCompleted VARCHAR(64));");
		try {
			JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
		} catch (SQLException e1) {
		}
		try{
			JdbcSqlExecutor.getInstance().executeUpdate("alter table picture add column ppi varchar(64)", JdbcUtil.getConnection());
		} catch (SQLException e1) {
		}
		try{
			JdbcSqlExecutor.getInstance().executeUpdate("alter table picture add column oldurl varchar(512)", JdbcUtil.getConnection());
		} catch (SQLException e1) {
		}
	}
	
	public boolean store(Picture model) {
		StringBuffer sqlsb = new StringBuffer();
		storeSql(model, sqlsb);
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying store picture...");
				try{
					Thread.sleep(1000);
				}catch(Exception e2){}
				return store(model);
			}else{
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean store(List<Picture> list) {
		if(list != null && list.size() > 0){
			StringBuffer sqlsb = new StringBuffer();
			for (Picture model : list) {
				storeSql(model, sqlsb);
			}
			try {
				int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), true, JdbcUtil.getConnection());
				return c > 0;
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying store pictures...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return store(list);
				}else{
					e.printStackTrace();
				}
			}
		}
		/*for (Picture model : list) {
			store(model);
		}*/
		return false;
	}

	public boolean update(Picture t) {
		StringBuffer sqlsb = new StringBuffer();
		updateSql(t, sqlsb);
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(DownloadWorker.class , "database is locked, trying update picture...");
				try{
					Thread.sleep(1000);
				}catch(Exception e2){}
				return update(t);
			}else{
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean update(List<Picture> list) {
		if(list != null && list.size() > 0){
			StringBuffer sqlsb = new StringBuffer();
			for (Picture t : list) {
				updateSql(t, sqlsb);
			}
			try {
				int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), true, JdbcUtil.getConnection());
				return c > 0;
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying update pictures...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return update(list);
				}else{
					e.printStackTrace();
				}
			}
		}
		/*for (Picture model : list) {
			update(model);
		}*/
		return false;
	}

	public boolean delete(Picture t) {
		StringBuffer sqlsb = new StringBuffer();
		deleteSql(t, sqlsb);
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying delete picture...");
				try{
					Thread.sleep(1000);
				}catch(Exception e2){}
				return delete(t);
			}else{
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean delete(String name, String value) {
		String sql = "delete from picture where " + name + "='" + value + "'";
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sql, JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying delete pictures...");
				try{
					Thread.sleep(1000);
				}catch(Exception e2){}
				return delete(name, value);
			}else{
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delete(List<Picture> list) {
		StringBuffer sqlsb = new StringBuffer();
		for (Picture model : list) {
			deleteSql(model, sqlsb);
			try {
				int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), true, JdbcUtil.getConnection());
				return c > 0;
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying delete pictures...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return delete(list);
				}else{
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public List<Picture> query() {
		return query(null);
	}

	public List<Picture> query(Object o) {
		if(o == null){
			o = new Picture();
		}
		if(o instanceof Picture){
			Picture model = (Picture)o;
			StringBuffer sqlsb = new StringBuffer("select * from picture where 1=1");
			if(StringUtils.isNotBlank(model.getId())){
				sqlsb.append(" and id = '").append(model.getTid()).append("'");
			}
			if(StringUtils.isNotBlank(model.getTid())){
				sqlsb.append(" and tid = '").append(model.getTid()).append("'");
			}
			sqlsb.append(" order by num desc");
			try {
				return JdbcSqlExecutor.getInstance().executeQuery(sqlsb.toString(), JdbcUtil.getConnection(), new JdbcSqlExecutor.CallBack<List<Picture>>() {
					public List<Picture> action(ResultSet rs) throws SQLException {
						List<Picture> list = new ArrayList<Picture>();
						Picture model = null;
						while(rs.next()){
							model = new Picture();
							resultSet2Picture(rs, model);
							list.add(model);
						}
						return list;
					}
				});
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying query pictures...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return query(o);
				}else{
					e.printStackTrace();
				}
			}
			return null;
		}else{
			Picture model = new Picture();
			model.setId(o.toString());
			return query(model);
		}
	}

	public List<Picture> query(String name, String value) {
		String sql = "select * from picture where " + name + "='" + value + "'";
		try {
			return JdbcSqlExecutor.getInstance().executeQuery(sql, JdbcUtil.getConnection(), new JdbcSqlExecutor.CallBack<List<Picture>>() {
				public List<Picture> action(ResultSet rs) throws SQLException {
					List<Picture> list = new ArrayList<Picture>();
					Picture model = null;
					while(rs.next()){
						model = new Picture();
						resultSet2Picture(rs, model);
						list.add(model);
					}
					return list;
				}
			});
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(PictureSqliteDbTemplate.class , "database is locked, trying query pictures...");
				try{
					Thread.sleep(1000);
				}catch(Exception e2){}
				return query(name, value);
			}else{
				e.printStackTrace();
			}
		}
		return null;
	}

	public Picture get(Object id) {
		List<Picture> list = query(id);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public boolean exsits(String name, String value) {
		List<Picture> list = query(name, value);
		return list != null && list.size() > 0;
	}
	
	private void resultSet2Picture(ResultSet rs, Picture model) throws SQLException{
		model.setId(rs.getString("id"));
		model.setNum(rs.getString("num"));
		model.setTid(rs.getString("tid"));
		model.setUrl(rs.getString("url"));
		model.setOldurl(rs.getString("oldurl"));
		model.setName(rs.getString("name"));
		model.setRealUrl(rs.getString("realUrl"));
		model.setTime(rs.getString("time"));
		model.setSize(rs.getString("size") == null ? 1 : Integer.parseInt(rs.getString("size")));
		model.setPpi(rs.getString("ppi") == null ? "" : rs.getString("ppi"));
		model.setSaveAsName("true".equals(rs.getString("saveAsName")));
		model.setCompleted("true".equals(rs.getString("isCompleted")));
	}
	
	private void storeSql(Picture model, StringBuffer sqlsb){
		sqlsb.append("insert into picture(id,tid,num,name,url,oldurl,realUrl,size,ppi,time,saveAsName,isCompleted) values('")
		.append(model.getId()).append("','").append(model.getTid()).append("','")
		.append(model.getNum()).append("','").append(StringEscapeUtils.escapeSql(model.getName())).append("','")
		.append(StringEscapeUtils.escapeSql(model.getUrl())).append("','").append(StringEscapeUtils.escapeSql(model.getOldurl() == null ? "" : model.getOldurl())).append("','")
		.append(StringEscapeUtils.escapeSql(model.getRealUrl())).append("','")
		.append(model.getSize()).append("','").append(model.getPpi() == null ? "" : model.getPpi()).append("','").append(model.getTime() == null ? "" : model.getTime()).append("','")
		.append(model.isSaveAsName()).append("','").append(model.isCompleted())
		.append("')").append(JdbcSqlExecutor.BAT_SPLIT);
	}
	
	private void updateSql(Picture t, StringBuffer sqlsb){
		sqlsb.append("update picture set ")
		.append("tid='").append(t.getTid()).append("',")
		.append("num='").append(t.getNum()).append("',")
		.append("name='").append(StringEscapeUtils.escapeSql(t.getName())).append("',")
		.append("url='").append(StringEscapeUtils.escapeSql(t.getUrl())).append("',")
		.append("oldurl='").append(StringEscapeUtils.escapeSql(t.getOldurl() == null ? "" : t.getOldurl())).append("',")
		.append("realUrl='").append(StringEscapeUtils.escapeSql(t.getRealUrl())).append("',")
		.append("size='").append(t.getSize()).append("',")
		.append("ppi='").append(t.getPpi() == null ? "" : t.getPpi()).append("',")
		.append("time='").append(t.getTime()).append("',")
		.append("saveAsName='").append(t.isSaveAsName()).append("',")
		.append("isCompleted='").append(t.isCompleted())
		.append("' where id='").append(t.getId()).append("'").append(JdbcSqlExecutor.BAT_SPLIT);
	}
	
	private void deleteSql(Picture t, StringBuffer sqlsb){
		sqlsb.append("delete from picture where id='").append(t.getId()).append("'").append(JdbcSqlExecutor.BAT_SPLIT);
	}
}
