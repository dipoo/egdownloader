package org.arong.egdownloader.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
/**
 * 任务表格数据模型
 * @author 阿荣
 * @since 2014-05-23
 */
public class TaskTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -7062795869810088466L;
	private List<Task> tasks;
	
	public TaskTableModel(List<Task> tasks){
		this.tasks = tasks;
	}

	public int getRowCount() {
		return tasks == null ? 0 : tasks.size();
	}

	public int getColumnCount() {
		return ComponentConst.TASK_TABLE_HEADER.length;
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		 return java.lang.String.class; 
	}

	//数据显示
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
			case 0 :
				return "";
			case 1 :
				return (tasks.get(rowIndex).getTag() == null ? "" : "[" + tasks.get(rowIndex).getTag() + "]") + tasks.get(rowIndex).getName();
			case 2 :
				return tasks.get(rowIndex).getTotal();
			case 3 :
				return tasks.get(rowIndex).getLanguage();
			case 4 :
				return tasks.get(rowIndex).getCurrent();
			case 5 :	
				return tasks.get(rowIndex).getStatus().getStatus();
			default :
				return "";
		}
	}

	//表头显示
	public String getColumnName(int column) {
		if(column == 0){
			return tasks.size() + "";
		}
		return ComponentConst.TASK_TABLE_HEADER[column];
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Task> getTasks() {
		return tasks;
	}

}
