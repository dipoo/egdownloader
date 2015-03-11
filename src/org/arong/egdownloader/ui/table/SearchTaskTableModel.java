package org.arong.egdownloader.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.arong.egdownloader.model.SearchTask;
/**
 * 任务表格数据模型
 * @author 阿荣
 * @since 2014-05-23
 */
public class SearchTaskTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -7062795869810088466L;
	private List<SearchTask> tasks;
	
	public SearchTaskTableModel(List<SearchTask> tasks){
		this.tasks = tasks;
	}

	public int getRowCount() {
		return tasks == null ? 0 : tasks.size();
	}

	public int getColumnCount() {
		return 3;
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		 return java.lang.String.class; 
	}

	//数据显示
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
			case 0 :
				return tasks.get(rowIndex).getType();
			case 1 :
				return tasks.get(rowIndex).getName();
			case 2 :
				return tasks.get(rowIndex).getDate();
			default :
				return "";
		}
	}

	//表头显示
	public String getColumnName(int column) {
		if(column == 0){
			return "类型";
		}else if(column == 1){
			return "名称";
		}else if(column == 2){
			return "发布时间";
		}else{
			return "";
		}
	}
}
