package org.arong.egdownloader.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.arong.egdownloader.model.Task;

public class TaskTableModel extends AbstractTableModel {
	private List<Task> tasks;
	private int column = 5;
	
	public TaskTableModel(List<Task> tasks){
		this.tasks = tasks;
	}

	public int getRowCount() {
		return tasks == null ? 0 : tasks.size();
	}

	public int getColumnCount() {
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		for(int i = 0; i < rowIndex; i ++){
			
		}
		return null;
	}

}
