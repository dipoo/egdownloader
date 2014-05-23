package org.arong.egdownloader.ui.table;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.CursorManager;
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
		this.setShowGrid(true);
		this.tableModel = new TaskTableModel(this.tasks);
		this.setModel(this.tableModel);
		this.setCellSelectionEnabled(false);
		this.setRowMargin(10);
		this.setAutoscrolls(true);
		this.setCursor(CursorManager.getPointerCursor());
		
		TaskTableCellRenderer renderer = new TaskTableCellRenderer();   
		renderer.setHorizontalAlignment(JLabel.CENTER);   
		this.setDefaultRenderer(Object.class, renderer);
	}

	
}
