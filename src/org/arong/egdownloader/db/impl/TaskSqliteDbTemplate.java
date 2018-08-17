package org.arong.egdownloader.db.impl;

import java.sql.SQLException;
import java.util.List;

import org.arong.egdownloader.db.DbTemplate;
import org.arong.egdownloader.model.Task;
import org.arong.jdbc.JdbcUtil;
import org.arong.util.JdbcSqlExecutor;

public class TaskSqliteDbTemplate implements DbTemplate<Task> {

	public boolean store(List<Task> tasks) {
		for (Task model : tasks) {
			store(model);
		}
		return false;
	}
	
	public boolean store(Task model) {
		StringBuffer sqlsb = new StringBuffer("insert into task(id,url,name,subname,coverUrl,language,type,saveDir,tag,readed,createTime,completedTime,total,current,size,status,start,end) values('").
				append("'").append(model.getId()).append("','").append(model.getUrl()).append("','")
				.append(model.getName()).append("','").append(model.getSubname()).append("','")
				.append(model.getCoverUrl()).append("','").append(model.getLanguage()).append("','")
				.append(model.getType()).append("','").append(model.getSaveDir()).append("','")
				.append(model.getTag()).append("','").append(model.isReaded()).append("','")
				.append(model.getCreateTime()).append("','").append(model.getCompletedTime()).append("','")
				.append(model.getTotal()).append("','").append(model.getCurrent()).append("','")
				.append(model.getSize()).append("','").append(model.getStatus()).append("','")
				.append(model.getStart()).append("','").append(model.getEnd()).append("'")
				.append(");");
		try {
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
		.append("completedTime='").append(t.getUrl()).append("',")
		.append("total='").append(t.getTotal()).append("',")
		.append("current='").append(t.getCurrent()).append("',")
		.append("size='").append(t.getSize()).append("',")
		.append("status='").append(t.getStatus()).append("',")
		.append("start='").append(t.getStart()).append("',")
		.append("end='").append(t.getEnd()).append("' where id='")
		.append(t.getId()).append("'");
		try {
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
		StringBuffer sqlsb = new StringBuffer("select * from task");
		return null;
	}

	public List<Task> query(Object id) {
		return null;
	}

	public List<Task> query(String name, String value) {
		return null;
	}

	public Task get(Object id) {
		return null;
	}

	public boolean exsits(String name, String value) {
		return false;
	}

	

}
