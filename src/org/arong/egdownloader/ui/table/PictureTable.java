package org.arong.egdownloader.ui.table;

import java.awt.Color;

import javax.swing.JTable;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
/**
 * 任务详细面板图片表格类
 * @author 阿荣
 * @since 2014-06-05
 */
public class PictureTable extends JTable {

	private static final long serialVersionUID = 8864835740722285837L;
	
	private Task task;
	
	public EgDownloaderWindow mainWindow;
	
	public PictureTable(int x, int y, int width, int height, Task task, EgDownloaderWindow mainWindow){
		this.setTask(task);
		this.mainWindow = mainWindow;
		
		this.setBounds(x, y, width, height);
		this.setCursor(CursorManager.getPointerCursor());//光标变手型
		this.getTableHeader().setReorderingAllowed(false);//不可移动列
		this.setBackground(Color.WHITE);
		this.setCellSelectionEnabled(false);//选择单元格
		
		this.setModel(new PictureTableModel(task.getPictures()));
		this.setDefaultRenderer(Object.class, new PictureTableCellRenderer());
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
