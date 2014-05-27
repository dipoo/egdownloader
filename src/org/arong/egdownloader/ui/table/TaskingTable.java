package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.DownloadWorker;
/**
 * 正在下载任务表格
 * @author 阿荣
 * @since 2014-05-22
 */
public class TaskingTable extends JTable {

	private static final long serialVersionUID = 8917533573337061263L;
	private List<Task> tasks;
	private EgDownloaderWindow mainWindow;
	private TableModel tableModel;
	
	public TaskingTable(int x, int y, int width, int height, List<Task> tasks, EgDownloaderWindow mainWindow){
		this.setMainWindow(mainWindow);
		this.tasks = (tasks == null ? new ArrayList<Task>() : tasks);
		this.tableModel = new TaskTableModel(this.tasks);
		if(this.tasks.size() > ComponentConst.MAX_TASK_PAGE){
			height = ComponentConst.MAX_TASK_PAGE * 25;
		}
		this.setBounds(x, y, width, height);
//		this.setShowGrid(true);//显示单元格边框
//		this.setCellSelectionEnabled(true);//选择单元格
		this.setRowMargin(10);
		this.setCursor(CursorManager.getPointerCursor());//光标变手型
		this.getTableHeader().setReorderingAllowed(false);//不可移动列
		this.setBackground(Color.WHITE);
		
//		this.setOpaque(false);//设为透明
		
		this.setModel(this.tableModel);//设置数据模型
		TaskTableCellRenderer renderer = new TaskTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);   
		this.setDefaultRenderer(Object.class, renderer);//设置渲染器
		this.getTableHeader().setDefaultRenderer(new TaskTableHeaderRenderer());
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				TaskingTable table = (TaskingTable)e.getSource();
				//左键
				if(e.getButton() == MouseEvent.BUTTON1){
					//双击事件
					if(e.getClickCount() == 2){
						//获取点击的行数
						int rowIndex = table.rowAtPoint(e.getPoint());
						Task task = table.getTasks().get(rowIndex);
						//如果状态为未开始或者已暂停，则将状态改为下载中，随后开启下载线程
						if(task.getStatus() == TaskStatus.UNSTARTED || task.getStatus() == TaskStatus.STOPED){
							task.setStatus(TaskStatus.STARTED);
							if(task.downloadWorker == null || task.downloadWorker.getTask() == null){
								task.downloadWorker = new DownloadWorker(task, table.getMainWindow());
							}
							task.downloadWorker.execute();
						}
						//如果状态为下载中，则将状态改为已暂停，随后将下载线程取消掉
						else if(task.getStatus() == TaskStatus.STARTED){
							task.setStatus(TaskStatus.STOPED);
							if(task.downloadWorker != null){
								task.downloadWorker.cancel(true);
							}
						}
						table.updateUI();
					}
				}
				//右键
				else if(e.getButton() == MouseEvent.BUTTON3){
					table.getMainWindow().tablePopupMenu.show(table, e.getPoint().x, e.getPoint().y);
				}
			}
		});
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	public EgDownloaderWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	
}
