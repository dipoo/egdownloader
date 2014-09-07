package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.window.CoverWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.DownloadWorker;
import org.arong.util.Tracker;
/**
 * 正在下载任务表格
 * @author 阿荣
 * @since 2014-05-22
 */
public class TaskingTable extends JTable {

	private static final long serialVersionUID = 8917533573337061263L;
	private List<Task> tasks;
	private EgDownloaderWindow mainWindow;
	private int runningNum = 0;
	
	public TaskingTable(int x, int y, int width, int height, List<Task> tasks, EgDownloaderWindow mainWindow){
		this.setMainWindow(mainWindow);
		this.tasks = (tasks == null ? new ArrayList<Task>() : tasks);
		
		if(this.tasks.size() > ComponentConst.MAX_TASK_PAGE){
			height = ComponentConst.MAX_TASK_PAGE * 25;
		}
		this.setBounds(x, y, width, height);
//		this.setShowGrid(true);//显示单元格边框
//		this.setCellSelectionEnabled(false);//选择单元格
		this.setCursor(CursorManager.getPointerCursor());//光标变手型
		this.getTableHeader().setReorderingAllowed(false);//不可移动列
		this.setBackground(Color.WHITE);
		
//		this.setOpaque(false);//设为透明
		TableModel tableModel = new TaskTableModel(this.tasks);
		this.setModel(tableModel);//设置数据模型
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
				//获取点击的行数
				int rowIndex = table.rowAtPoint(e.getPoint());
				//左键
				if(e.getButton() == MouseEvent.BUTTON1){
					//双击事件
					if(e.getClickCount() == 2){
						Task task = table.getTasks().get(rowIndex);
						//如果状态为未开始或者已暂停，则将状态改为下载中，随后开启下载线程
						if(task.getStatus() == TaskStatus.UNSTARTED || task.getStatus() == TaskStatus.STOPED){
							int maxThread = table.getMainWindow().setting.getMaxThread();
							if(table.getRunningNum() >= maxThread){
								JOptionPane.showMessageDialog(null, "已达下载任务开启上限：" + maxThread);
								return;
							}
							task.setStatus(TaskStatus.STARTED);
							task.setDownloadWorker(new DownloadWorker(task, table.getMainWindow()));
							task.getDownloadWorker().execute();
							table.setRunningNum(table.getRunningNum() + 1);
						}
						//如果状态为下载中，则将状态改为已暂停，随后将下载线程取消掉
						else if(task.getStatus() == TaskStatus.STARTED){
							task.setStatus(TaskStatus.STOPED);
							Tracker.println(getClass(), task.getName() + ":已暂停");
							if(task.getDownloadWorker() != null){
								task.getDownloadWorker().cancel(true);
								task.setDownloadWorker(null);//swingworker不能复用，需要重新建立
								//更新任务数据
								table.mainWindow.taskDbTemplate.update(task);
								table.setRunningNum(table.getRunningNum() - 1);
							}
						}
						table.updateUI();
					}else{//单击事件
						int column = table.columnAtPoint(e.getPoint());
						if(column == 0){
							EgDownloaderWindow window = table.getMainWindow();
							Task task = table.getTasks().get(rowIndex);
							CoverWindow cw = (CoverWindow) window.coverWindow;
							if(cw == null){
								window.coverWindow = new CoverWindow(task);
								cw = (CoverWindow) window.coverWindow;
							}else{
								cw.showCover(task);
							}
							cw.setVisible(true);
						}
					}
					
				}
				//右键
				else if(e.getButton() == MouseEvent.BUTTON3){
					//使之选中
					table.setRowSelectionInterval(rowIndex, rowIndex);
					table.updateUI();
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

	public EgDownloaderWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public int getRunningNum() {
		return runningNum;
	}

	public void setRunningNum(int runningNum) {
		this.runningNum = runningNum;
	}

	
}
