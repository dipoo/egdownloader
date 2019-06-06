package org.arong.egdownloader.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.db.AbstractSqlDbTemplate;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskList;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.util.Tracker;
import org.arong.util.jdbc.JdbcSqlExecutor;
import org.arong.util.jdbc.JdbcUtil;

public class TaskSqliteDbTemplate extends AbstractSqlDbTemplate<Task> {
	
	public static void main(String[] args) {
		
	}
	public int executeSql(String sql){
		try {
			return JdbcSqlExecutor.getInstance().executeUpdate(sql, JdbcUtil.getConnection());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	
	static{
			StringBuffer sqlsb = new StringBuffer("create table task (")
			.append("id VARCHAR(256) PRIMARY KEY NOT NULL,")
			.append("url VARCHAR(512) NOT NULL,")
			.append("name VARCHAR(1024) NOT NULL,")
			.append("subname VARCHAR(1024),")
			.append("coverUrl VARCHAR(1024),")
			.append("language VARCHAR(64),")
			.append("type VARCHAR(64),")
			.append("saveDir VARCHAR(1024),")
			.append("tag VARCHAR(512),")
			.append("readed VARCHAR(64),")
			.append("createTime VARCHAR(64),")
			.append("completedTime VARCHAR(64),")
			.append("total VARCHAR(64),")
			.append("current VARCHAR(64),")
			.append("size VARCHAR(64),")
			.append("status VARCHAR(64),")
			.append("start VARCHAR(64),")
			.append("end VARCHAR(64));");
			try {
				JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column groupname varchar(512)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column postedTime varchar(64)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column uploader varchar(64)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column original varchar(64)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column tags varchar(2048)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column syncTime varchar(64)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
			try{
				JdbcSqlExecutor.getInstance().executeUpdate("alter table task add column oldurl varchar(512)", JdbcUtil.getConnection());
			} catch (SQLException e1) {
			}
	}

	public boolean store(List<Task> tasks) {
		if(tasks != null && tasks.size() > 0){
			StringBuffer sqlsb = new StringBuffer();
			for (Task t : tasks) {
				storeSql(t, sqlsb);
			}
			try {
				int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), true, JdbcUtil.getConnection());
				return c > 0;
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying store tasks...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return store(tasks);
				}else{
					e.printStackTrace();
				}
			}
		}
		/*for (Task model : tasks) {
			store(model);
		}*/
		return true;
	}
	
	public boolean store(Task model) {
		StringBuffer sqlsb = new StringBuffer();
		storeSql(model, sqlsb);	
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying store task...");
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
	
	public boolean update(Task t) {
		StringBuffer sqlsb = new StringBuffer();
		updateSql(t, sqlsb);
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying update task...");
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

	public boolean update(List<Task> tasks) {
		if(tasks != null && tasks.size() > 0){
			StringBuffer sqlsb = new StringBuffer();
			for (Task t : tasks) {
				updateSql(t, sqlsb);
			}
			try {
				int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), true, JdbcUtil.getConnection());
				return c > 0;
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying update tasks...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return update(tasks);
				}else{
					e.printStackTrace();
				}
			}
		}
		/*for (Task model : tasks) {
			update(model);
		}*/
		return true;
	}

	public boolean delete(Task t) {
		StringBuffer sqlsb = new StringBuffer("delete from task where id='").append(t.getId()).append("'");
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying delete task...");
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
		String sql = "delete from task where " + name + "='" + value + "'";
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sql, JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying delete tasks...");
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

	public boolean delete(List<Task> tasks) {
		StringBuffer sqlsb = new StringBuffer();
		for (Task model : tasks) {
			deleteSql(model, sqlsb);
			try {
				int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), true, JdbcUtil.getConnection());
				return c > 0;
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying delete tasks...");
					try{
						Thread.sleep(1000);
					}catch(Exception e2){}
					return delete(tasks);
				}else{
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public List<Task> query() {
		return query(null);
	}

	public List<Task> query(Object o) {
		if(o == null){
			o = new Task();
			((Task)o).setStatus(null);
		}
		if(o instanceof Task){
			Task model = (Task)o;
			StringBuffer sqlsb = new StringBuffer("select * from task where 1=1");
			if(StringUtils.isNotBlank(model.getName())){
				sqlsb.append(" and name like '%").append(model.getName()).append("%'");
			}
			if(StringUtils.isNotBlank(model.getSubname())){
				sqlsb.append(" and subname like '%").append(model.getSubname()).append("%'");
			}
			if(StringUtils.isNotBlank(model.getType())){
				sqlsb.append(" and type = '").append(model.getType()).append("'");
			}
			if(model.getStatus() != null){
				sqlsb.append(" and status = '").append(model.getStatus().getStatus()).append("'");
			}
			if(StringUtils.isNotBlank(model.getGroupname())){
				sqlsb.append(" and groupname = '").append(model.getGroupname()).append("'");
			}else{
				sqlsb.append(" and (groupname is null or groupname = '')");
			}
			sqlsb.append(" order by createTime desc");
			try {
				return JdbcSqlExecutor.getInstance().executeQuery(sqlsb.toString(), JdbcUtil.getConnection(), new JdbcSqlExecutor.CallBack<List<Task>>() {
					public List<Task> action(ResultSet rs) throws SQLException {
						TaskList<Task> list = new TaskList<Task>();
						Task model = null;
						while(rs.next()){
							model = new Task();
							resultSet2Task(rs, model);
							list.add(model);
						}
						return list;
					}
				});
			} catch (SQLException e) {
				if(e.getMessage() != null && e.getMessage().contains("database is locked")){
					Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying query tasks...");
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
			Task model = new Task();
			model.setId(o.toString());
			return query(model);
		}
	}

	public List<Task> query(String name, String value) {
		String sql = "select * from task where " + name + "='" + value + "'";
		try {
			return JdbcSqlExecutor.getInstance().executeQuery(sql, JdbcUtil.getConnection(), new JdbcSqlExecutor.CallBack<List<Task>>() {
				public List<Task> action(ResultSet rs) throws SQLException {
					TaskList<Task> list = new TaskList<Task>();
					Task model = null;
					while(rs.next()){
						model = new Task();
						resultSet2Task(rs, model);
						list.add(model);
					}
					return list;
				}
			});
		} catch (SQLException e) {
			if(e.getMessage() != null && e.getMessage().contains("database is locked")){
				Tracker.println(TaskSqliteDbTemplate.class , "database is locked, trying query tasks...");
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

	public Task get(Object id) {
		List<Task> list = query(id);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public boolean exsits(String name, String value) {
		List<Task> list = query(name, value);
		return list != null && list.size() > 0;
	}

	private void resultSet2Task(ResultSet rs, Task model) throws SQLException{
		model.setId(rs.getString("id"));
		model.setGroupname(rs.getString("groupname"));
		model.setUrl(rs.getString("url"));
		model.setOldurl(rs.getString("oldurl"));
		model.setName(rs.getString("name"));
		model.setSubname(rs.getString("subname") == null ? "" : rs.getString("subname"));
		model.setCoverUrl(rs.getString("coverUrl"));
		model.setLanguage(rs.getString("language") == null ? "" : rs.getString("language"));
		model.setType(rs.getString("type") == null ? "" : rs.getString("type"));
		model.setSaveDir(rs.getString("saveDir"));
		model.setTag(rs.getString("tag") == null ? "一般" : rs.getString("tag"));
		model.setTags(rs.getString("tags") == null ? "" : rs.getString("tags"));
		model.setReaded("true".equals(rs.getString("readed")));
		model.setCreateTime(rs.getString("createTime"));
		model.setSyncTime(rs.getString("syncTime"));
		model.setCompletedTime(rs.getString("completedTime"));
		model.setPostedTime(rs.getString("postedTime"));
		model.setUploader(rs.getString("uploader") == null ? "" : rs.getString("uploader"));
		model.setOriginal("true".equals(rs.getString("original")));
		model.setTotal(rs.getString("total") == null ? 0 : Integer.parseInt(rs.getString("total")));
		model.setCurrent(rs.getString("current") == null ? 0 : Integer.parseInt(rs.getString("current")));
		model.setSize(rs.getString("size") == null ? "" : rs.getString("size"));
		TaskStatus status = TaskStatus.parseTaskStatus(rs.getString("status"));
		model.setStatus(status == null ? TaskStatus.UNSTARTED : (status == TaskStatus.WAITING ? TaskStatus.STOPED : status));
		model.setStart(rs.getString("start") == null ? 1 : Integer.parseInt(rs.getString("start")));
		model.setEnd(rs.getString("end") == null ? model.getTotal() : Integer.parseInt(rs.getString("end")));
	}
	
	private void storeSql(Task model, StringBuffer sqlsb){
		sqlsb.append("insert into task(id,groupname,url,oldurl,name,subname,coverUrl,language,type,saveDir,tag,tags,readed,createTime,syncTime,completedTime,postedTime,uploader,original,total,current,size,status,start,end) values('")
		.append(model.getId()).append("','").append(StringEscapeUtils.escapeSql(ComponentConst.groupName)).append("','")
		.append(StringEscapeUtils.escapeSql(model.getUrl())).append("','").append(StringEscapeUtils.escapeSql(model.getOldurl() == null ? "" : model.getOldurl())).append("','")
		.append(StringEscapeUtils.escapeSql(model.getName())).append("','").append(StringEscapeUtils.escapeSql(model.getSubname())).append("','")
		.append(StringEscapeUtils.escapeSql(model.getCoverUrl())).append("','").append(StringEscapeUtils.escapeSql(model.getLanguage())).append("','")
		.append(StringEscapeUtils.escapeSql(model.getType())).append("','").append(StringEscapeUtils.escapeSql(model.getSaveDir())).append("','")
		.append(StringEscapeUtils.escapeSql(model.getTag())).append("','").append(StringEscapeUtils.escapeSql(model.getTags())).append("','").append(model.isReaded()).append("','")
		.append(model.getCreateTime()).append("','").append(model.getStoreSyncTime()).append("','")
		.append(model.getCompletedTime() == null ? "" : model.getCompletedTime()).append("','")
		.append(model.getPostedTime() == null ? "" : model.getPostedTime()).append("','")
		.append(model.getUploader() == null ? "" : model.getUploader()).append("','").append(model.isOriginal()).append("','")
		.append(model.getTotal()).append("','").append(model.getCurrent()).append("','")
		.append(model.getSize()).append("','").append(model.getStatus().getStatus()).append("','")
		.append(model.getStart()).append("','").append(model.getEnd()).append("'")
		.append(")").append(JdbcSqlExecutor.BAT_SPLIT);
	}
	
	private void updateSql(Task t, StringBuffer sqlsb){
		sqlsb.append("update task set ")
		.append("groupname='").append(StringEscapeUtils.escapeSql(ComponentConst.groupName)).append("',")
		.append("url='").append(StringEscapeUtils.escapeSql(t.getUrl())).append("',")
		.append("oldurl='").append(StringEscapeUtils.escapeSql(t.getOldurl() == null ? "" : t.getOldurl())).append("',")
		.append("name='").append(StringEscapeUtils.escapeSql(t.getName())).append("',")
		.append("subname='").append(StringEscapeUtils.escapeSql(t.getSubname() == null ? "" : t.getSubname())).append("',")
		.append("coverUrl='").append(StringEscapeUtils.escapeSql(t.getCoverUrl())).append("',")
		.append("language='").append(StringEscapeUtils.escapeSql(t.getLanguage())).append("',")
		.append("type='").append(StringEscapeUtils.escapeSql(t.getType())).append("',")
		.append("saveDir='").append(StringEscapeUtils.escapeSql(t.getSaveDir())).append("',")
		.append("tag='").append(StringEscapeUtils.escapeSql(t.getTag())).append("',")
		.append("tags='").append(StringEscapeUtils.escapeSql(t.getTags())).append("',")
		.append("readed='").append(t.isReaded()).append("',")
		.append("createTime='").append(t.getCreateTime()).append("',")
		.append("syncTime='").append(t.getStoreSyncTime()).append("',")
		.append("completedTime='").append(t.getCompletedTime() == null ? "" : t.getCompletedTime()).append("',")
		.append("postedTime='").append(t.getPostedTime() == null ? "" : t.getPostedTime()).append("',")
		.append("uploader='").append(t.getUploader()).append("',")
		.append("original='").append(t.isOriginal()).append("',")
		.append("total='").append(t.getTotal()).append("',")
		.append("current='").append(t.getCurrent()).append("',")
		.append("size='").append(t.getSize()).append("',")
		.append("status='").append(TaskStatus.STARTED == t.getStatus() ? TaskStatus.STOPED.getStatus() : t.getStatus().getStatus()).append("',")
		.append("start='").append(t.getStart()).append("',")
		.append("end='").append(t.getEnd()).append("' where id='")
		.append(t.getId()).append("'").append(JdbcSqlExecutor.BAT_SPLIT);
	}
	
	private void deleteSql(Task t, StringBuffer sqlsb){
		sqlsb.append("delete from task where id='").append(t.getId()).append("'").append(JdbcSqlExecutor.BAT_SPLIT);
	}
}
