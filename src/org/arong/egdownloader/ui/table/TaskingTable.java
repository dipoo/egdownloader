package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.Task;
/**
 * 正在下载任务表格
 * @author 阿荣
 * @since 2014-05-22
 */
public class TaskingTable extends JTable {

	private static final long serialVersionUID = 8917533573337061263L;
	private List<Task> tasks;
	private TableModel tableModel;
	
	public TaskingTable(int x, int y, int width, int height, List<Task> tasks){
		this.tasks = tasks;
		this.setBounds(x, y, width, height);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		
		for(int i = 0; i < tasks.size(); i ++){
			
		}
		
		TableModel model = new TaskTableModel(tasks);
	}

	
}
