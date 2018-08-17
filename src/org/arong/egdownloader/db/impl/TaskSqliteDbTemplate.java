package org.arong.egdownloader.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskList;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.jdbc.JdbcUtil;
import org.arong.util.JdbcSqlExecutor;
import org.arong.utils.StringUtil;

public class TaskSqliteDbTemplate implements DbTemplate<Task> {
	
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
	}

	public boolean store(List<Task> tasks) {
		for (Task model : tasks) {
			store(model);
		}
		return false;
	}
	
	public boolean store(Task model) {
		StringBuffer sqlsb = new StringBuffer("insert into task(id,url,name,subname,coverUrl,language,type,saveDir,tag,readed,createTime,completedTime,total,current,size,status,start,end) values('")
				.append(model.getId()).append("','").append(model.getUrl()).append("','")
				.append(model.getName()).append("','").append(model.getSubname()).append("','")
				.append(model.getCoverUrl()).append("','").append(model.getLanguage()).append("','")
				.append(model.getType()).append("','").append(model.getSaveDir()).append("','")
				.append(model.getTag()).append("','").append(model.isReaded()).append("','")
				.append(model.getCreateTime()).append("','").append(model.getCompletedTime() == null ? "" : model.getCompletedTime()).append("','")
				.append(model.getTotal()).append("','").append(model.getCurrent()).append("','")
				.append(model.getSize()).append("','").append(model.getStatus().getStatus()).append("','")
				.append(model.getStart()).append("','").append(model.getEnd()).append("'")
				.append(");");
		try {
			System.out.println(sqlsb.toString());
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(Task t) {
		StringBuffer sqlsb = new StringBuffer("update task set ")
		.append("url='").append(t.getUrl()).append("',")
		.append("name='").append(t.getName()).append("',")
		.append("subname='").append(t.getSubname()).append("',")
		.append("coverUrl='").append(t.getCoverUrl()).append("',")
		.append("language='").append(t.getLanguage()).append("',")
		.append("type='").append(t.getType()).append("',")
		.append("saveDir='").append(t.getSaveDir()).append("',")
		.append("tag='").append(t.getTag()).append("',")
		.append("readed='").append(t.isReaded()).append("',")
		.append("createTime='").append(t.getCreateTime()).append("',")
		.append("completedTime='").append(t.getCompletedTime()).append("',")
		.append("total='").append(t.getTotal()).append("',")
		.append("current='").append(t.getCurrent()).append("',")
		.append("size='").append(t.getSize()).append("',")
		.append("status='").append(TaskStatus.STARTED == t.getStatus() ? TaskStatus.STOPED.getStatus() : t.getStatus().getStatus()).append("',")
		.append("start='").append(t.getStart()).append("',")
		.append("end='").append(t.getEnd()).append("' where id='")
		.append(t.getId()).append("'");
		try {
			System.out.println(sqlsb.toString());
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(List<Task> tasks) {
		for (Task model : tasks) {
			update(model);
		}
		return false;
	}

	public boolean delete(Task t) {
		StringBuffer sqlsb = new StringBuffer("delete from task where id='").append(t.getId()).append("'");
		try {
			int c = JdbcSqlExecutor.getInstance().executeUpdate(sqlsb.toString(), JdbcUtil.getConnection());
			return c > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(List<Task> tasks) {
		for (Task model : tasks) {
			delete(model);
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
			if(StringUtil.notBlank(model.getName())){
				sqlsb.append(" and name like '%").append(model.getName()).append("%'");
			}
			if(StringUtil.notBlank(model.getSubname())){
				sqlsb.append(" and subname like '%").append(model.getSubname()).append("%'");
			}
			if(StringUtil.notBlank(model.getType())){
				sqlsb.append(" and type = '").append(model.getType()).append("'");
			}
			if(model.getStatus() != null){
				sqlsb.append(" and status = '").append(model.getStatus().getStatus()).append("'");
			}
			sqlsb.append(" order by createTime desc");
			try {
				System.out.println(sqlsb.toString());
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
				e.printStackTrace();
			}
			return null;
		}else{
			Task model = new Task();
			model.setId(o.toString());
			return query(model);
		}
	}

	public List<Task> query(String name, String value) {
		String sql = "select * from task where name='" + value + "'";
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
			e.printStackTrace();
		}
		return null;
	}

	public Task get(Object id) {
		List<Task> list = query(id);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public boolean exsits(String name, String value) {
		return query(name, value) != null && query(name, value).size() > 0;
	}

	private void resultSet2Task(ResultSet rs, Task model) throws SQLException{
		model.setId(rs.getString("id"));
		model.setUrl(rs.getString("url"));
		model.setName(rs.getString("name"));
		model.setSubname(rs.getString("subname") == null ? "" : rs.getString("subname"));
		model.setCoverUrl(rs.getString("coverUrl"));
		model.setLanguage(rs.getString("language") == null ? "" : rs.getString("language"));
		model.setType(rs.getString("type") == null ? "" : rs.getString("type"));
		model.setSaveDir(rs.getString("saveDir"));
		model.setTag(rs.getString("tag") == null ? "一般" : rs.getString("tag"));
		model.setReaded("true".equals(rs.getString("readed")));
		model.setCreateTime(rs.getString("createTime"));
		model.setCompletedTime(rs.getString("completedTime"));
		model.setTotal(rs.getString("total") == null ? 0 : Integer.parseInt(rs.getString("total")));
		model.setCurrent(rs.getString("current") == null ? 0 : Integer.parseInt(rs.getString("current")));
		model.setSize(rs.getString("size") == null ? "" : rs.getString("size"));
		model.setStatus(TaskStatus.parseTaskStatus(rs.getString("status")));
		model.setStart(rs.getString("start") == null ? 1 : Integer.parseInt(rs.getString("start")));
		model.setEnd(rs.getString("end") == null ? model.getTotal() : Integer.parseInt(rs.getString("end")));
	}
}
