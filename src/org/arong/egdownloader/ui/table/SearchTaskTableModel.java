package org.arong.egdownloader.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.arong.egdownloader.model.SearchTask;
/**
 * 搜索任务表格数据模型
 * @author 阿荣
 * @since 2015-03-11
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
		return 6;
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
				return tasks.get(rowIndex).getFilenum() == null ? "" : tasks.get(rowIndex).getFilenum();
			case 3 :
				return tasks.get(rowIndex).getRating() == null ? "" : tasks.get(rowIndex).getRating();
			case 4 :
				return tasks.get(rowIndex).getUploader() == null ? "" : tasks.get(rowIndex).getUploader();
			case 5 :
				return tasks.get(rowIndex).getDate() == null ? "" : tasks.get(rowIndex).getDate();	
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
			return "图片个数";
		}else if(column == 3){
			return "评分";
		}else if(column == 4){
			return "上传者";
		}else if(column == 5){
			return "发布时间";
		}else{
			return "";
		}
	}
}
