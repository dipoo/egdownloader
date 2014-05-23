package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
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
		if(tasks.size() > ComponentConst.MAX_TASK_PAGE){
			height = ComponentConst.MAX_TASK_PAGE * 25;
		}
		this.setBounds(x, y, width, height);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		this.setShowGrid(true);
		this.tableModel = new TaskTableModel(this.tasks);
		this.setModel(this.tableModel);
		this.setCellSelectionEnabled(true);
		this.setRowMargin(10);
		this.setAutoscrolls(true);
		this.setCursor(CursorManager.getPointerCursor());
		
		TaskTableCellRenderer renderer = new TaskTableCellRenderer();   
		renderer.setHorizontalAlignment(JLabel.CENTER);   
		this.setDefaultRenderer(Object.class, renderer);
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				TaskingTable table = (TaskingTable)e.getSource();
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				table.setSelectionBackground(new Color(230, 230, 230));
				JOptionPane.showMessageDialog(table, table.getModel().getValueAt(row, column));
			}
		});
	}

	
}
