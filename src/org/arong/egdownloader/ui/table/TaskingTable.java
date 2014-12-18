package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.arong.egdownloader.ui.work.ReCreateWorker;
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
	private int sort = 0;//0为名称排序，1为时间排序
	private List<Task> waitingTasks;//排队等待的任务
	
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
		//表头监听
		this.getTableHeader().addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == getTableHeader()) {
					TaskingTable table = (TaskingTable)getTableHeader().getTable();
                    getTableHeader().removeMouseListener(this);  
                    int column = columnAtPoint(e.getPoint());  
                    //点击名称列，重新排序
                    if(column == 1){
                    	if(sort == 0){
                    		sort = 1;
                    		//时间排序
                    		if(table.getMainWindow().tasks != null){
                    			//按照名称排序
                    			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                    				@Override
                    				public int compare(Task o1, Task o2) {
                    					return o2.getCreateTime().toLowerCase().compareTo(o1.getCreateTime().toLowerCase());
                    				}
                    			});
                    			table.setTasks(table.getMainWindow().tasks);
                    			table.updateUI();
                    		}
                    	}else{
                    		sort = 0;
                    		//名称排序
                    		if(table.getMainWindow().tasks != null){
                    			//按照名称排序
                    			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                    				@Override
                    				public int compare(Task o1, Task o2) {
                    					return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    				}
                    			});
                    			table.setTasks(table.getMainWindow().tasks);
                    			table.updateUI();
                    		}
                    	}
                    }
                    //漫画总数排序
                    else if(column == 2){
                    	if(table.getMainWindow().tasks != null){
                			//按照名称排序
                			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                				@Override
                				public int compare(Task o1, Task o2) {
                					if(o1.getTotal() > o2.getTotal())
                						return -1;
                					if(o1.getTotal() < o2.getTotal())
                						return 1;
                					return 0;
                				}
                			});
                			table.setTasks(table.getMainWindow().tasks);
                			table.updateUI();
                		}
                    }
                    //漫画语言排序
                    else if(column == 3){
                    	if(table.getMainWindow().tasks != null){
                			//按照名称排序
                			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                				@Override
                				public int compare(Task o1, Task o2) {
                					return o1.getLanguage().toLowerCase().compareTo(o2.getLanguage().toLowerCase());
                				}
                			});
                			table.setTasks(table.getMainWindow().tasks);
                			table.updateUI();
                		}
                    }
                    //漫画下载进度排序
                    else if(column == 4){
                    	if(table.getMainWindow().tasks != null){
                			//按照名称排序
                			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                				@Override
                				public int compare(Task o1, Task o2) {
                					double j1 = Double.parseDouble(o1.getCurrent() + "") / Double.parseDouble(o1.getTotal() + "") * 100;
                					double j2 = Double.parseDouble(o2.getCurrent() + "") / Double.parseDouble(o2.getTotal() + "") * 100;
                					if(j1 > j2)
                						return -1;
                					if(j1 < j2)
                						return 1;
                					return 0;
                				}
                			});
                			table.setTasks(table.getMainWindow().tasks);
                			table.updateUI();
                		}
                    }
                    //漫画下载状态排序
                    else if(column == 5){
                    	if(table.getMainWindow().tasks != null){
                			//按照名称排序
                			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                				@Override
                				public int compare(Task o1, Task o2) {
                					return o1.getStatus().toString().compareTo(o2.getStatus().toString());
                				}
                			});
                			table.setTasks(table.getMainWindow().tasks);
                			table.updateUI();
                		}
                    }
                    //漫画阅读状态排序
                    else if(column == 0){
                    	if(table.getMainWindow().tasks != null){
                			//按照名称排序
                			Collections.sort(table.getMainWindow().tasks, new Comparator<Task>() {
                				@Override
                				public int compare(Task o1, Task o2) {
                					return (o2.isReaded() + "").toLowerCase().compareTo((o1.isReaded() + "").toLowerCase());
                				}
                			});
                			table.setTasks(table.getMainWindow().tasks);
                			table.updateUI();
                		}
                    }
                    getTableHeader().addMouseListener(this);
                }  
			}
		});
		//单元格监听
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
								table.addWaitingTask(task);
								return;
							}
							table.startTask(task);
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
							//开启排队等待的第一个任务
							table.startWaitingTask(task);
						}
						//如果状态为排队等待中，则将状态改为已暂停，并从排队等待列表中移除
						else if(task.getStatus() == TaskStatus.WAITING){
							task.setStatus(TaskStatus.STOPED);
							Tracker.println(getClass(), task.getName() + ":已暂停");
							table.waitingTasks.remove(task);
						}
						//如果状态为未创建，则开启创建线程
						else if(task.getStatus() == TaskStatus.UNCREATED){
							Tracker.println(getClass(), task.getName() + ":重新采集");
							task.setReCreateWorker(new ReCreateWorker(task, table.getMainWindow()));
							task.getReCreateWorker().execute();
						}
						table.updateUI();
					}else{//单击事件
						int column = table.columnAtPoint(e.getPoint());
						//显示预览图
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
	/**
	 * 添加排队等待的任务
	 * @param task
	 */
	public void addWaitingTask(Task task){
		if(waitingTasks == null){
			waitingTasks = new ArrayList<Task>();
		}
		if(!waitingTasks.contains(task)){
			task.setStatus(TaskStatus.WAITING);
			waitingTasks.add(task);
		}
	}
	
	/**
	 * 开启任务下载
	 * @param task 
	 * @return void
	 */
	public void startTask(Task task){
		task.setStatus(TaskStatus.STARTED);
		task.setDownloadWorker(new DownloadWorker(task, this.getMainWindow()));
		task.getDownloadWorker().execute();
		this.setRunningNum(this.getRunningNum() + 1);
	}
	/**
	 * 将排队等待中的第一个任务开启下载
	 * @param task 
	 * @return void
	 */
	public void startWaitingTask(Task task){
		if(this.getWaitingTasks() != null && this.getWaitingTasks().size() > 0){
			task = this.getWaitingTasks().get(0);//第一个任务
			this.startTask(task);
			this.getWaitingTasks().remove(0);//将第一个任务移除排队列表
		}
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

	public List<Task> getWaitingTasks() {
		return waitingTasks;
	}
	public void setWaitingTasks(List<Task> waitingTasks) {
		this.waitingTasks = waitingTasks;
	}
}
