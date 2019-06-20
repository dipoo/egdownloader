package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskList;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.panel.PicturesInfoPanel;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.egdownloader.ui.window.CoverWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchCoverWindow;
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
	private TaskList<Task> tasks;
	private TaskList<Task> hiddentasks;
	public EgDownloaderWindow mainWindow;
	private int runningNum = 0;
	public boolean rebuild;
	private int sort = 1;//0为名称排序，1为时间排序
	public List<Task> waitingTasks;//排队等待的任务
	public static int wordNum = 230;//名称列最多显示字数，会随着窗口大小变化而改变
	public int currentRowIndex = -1;//用于封面显示
	public int selectRowIndex = 0;
	private boolean refresh;//是否应该刷新
	private Timer timer = new Timer(true); 
	
	public void changeModel(final EgDownloaderWindow mainWindow){
		this.setMainWindow(mainWindow);
		this.tasks = mainWindow.tasks;
		this.setHiddentasks(mainWindow.tasks);
		final TaskingTable table = this;
		this.tasks.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				table.setRefresh(true);
			}
		});
		for(final Task task : this.tasks){
			task.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					table.setRefresh(true);
					if(mainWindow.taskImagePanel != null){
						mainWindow.taskImagePanel.flush(task); 
					}
				}
			});
		}
		TableModel tableModel = new TaskTableModel(this.tasks);
		this.setModel(tableModel);//设置数据模型
		if(this.tasks != null && this.tasks.size() > 0){this.setRowSelectionInterval(0, 0);}
	}
	
	public TaskingTable(int x, int y, int width, int height, TaskList<Task> tasks, final EgDownloaderWindow mainWindow){
		this.setMainWindow(mainWindow);
		final TaskingTable table = this;
		this.tasks = (tasks == null ? new TaskList<Task>() : tasks);
		
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
		this.getTableHeader().setSize(this.getTableHeader().getWidth(), 120);
		//表头监听
		this.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == getTableHeader()) {
                    getTableHeader().removeMouseListener(this);  
                    int column = columnAtPoint(e.getPoint());  
                    //点击名称列，重新排序
                    table.setRefresh(true);
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
                		}
                    }
                    getTableHeader().addMouseListener(this);
                }  
			}
		});
		//单元格监听
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e){
				int rowIndex = table.rowAtPoint(e.getPoint());
				int columnIndex = table.columnAtPoint(e.getPoint());
				if(columnIndex == 0){
					Task task = table.getTasks().get(rowIndex);
					//切换行
					if(rowIndex != currentRowIndex){
						currentRowIndex = rowIndex;
						if(table.mainWindow.coverWindow == null){
							table.mainWindow.coverWindow = new SearchCoverWindow(table.mainWindow);
						}
						table.mainWindow.coverWindow.showCover(task, new Point(e.getXOnScreen() + 50, e.getYOnScreen()));
					}
				}else{
					if(table.mainWindow.coverWindow != null){
						table.mainWindow.coverWindow.setVisible(false);
						currentRowIndex = -1;
					}
				}
			}
		});
		//单元格监听
		this.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				if(table.mainWindow.coverWindow != null){
					table.mainWindow.coverWindow.setVisible(false);
					currentRowIndex = -1;
				}
			}
			public void mouseClicked(MouseEvent e) {
				EgDownloaderWindow window = table.getMainWindow();
				//获取点击的行数
				int rowIndex = table.rowAtPoint(e.getPoint());
				selectRowIndex = rowIndex;
				//切换信息面板tab
				window.infoTabbedPane.flushTab(window.tasks.get(rowIndex));
				
				//左键
				if(e.getButton() == MouseEvent.BUTTON1){
					//双击事件
					if(e.getClickCount() == 2){
						if(table.rebuild){
							Tracker.println(TaskingTable.class, "正在重建任务");
							return;
						}
						Task task = table.getTasks().get(rowIndex);
						//如果状态为未开始或者已暂停，则将状态改为下载中，随后开启下载线程
						if(task.getStatus() == TaskStatus.UNSTARTED || task.getStatus() == TaskStatus.STOPED){
							table.startTask(task);
						}
						//如果状态为下载中，则将状态改为已暂停，随后将下载线程取消掉
						else if(task.getStatus() == TaskStatus.STARTED || task.getStatus() == TaskStatus.WAITING){
							stopTask(task);
						}
						//如果状态为未创建，则开启创建线程
						else if(task.getStatus() == TaskStatus.UNCREATED){
							Tracker.println(getClass(), task.getName() + ":重新采集");
							task.setReCreateWorker(new ReCreateWorker(task, table.getMainWindow()));
							task.getReCreateWorker().execute();
						}
					}else{//单击事件
						int column = table.columnAtPoint(e.getPoint());
						//显示预览图
						if(column == 0){
							Task task = table.getTasks().get(rowIndex);
							String path = task.getSaveDir() + "/cover.jpg";
							File cover = new File(path);
							//不存在封面
							if(cover == null || !cover.exists()){
								CoverWindow cw = (CoverWindow) window.coverWindow2;
								if(cw == null){
									window.coverWindow2 = new CoverWindow(task, window);
									cw = (CoverWindow) window.coverWindow2;
								}else{
									cw.showCover(task);
								}
								cw.setVisible(true);
							}
						}
						
					}
					
				}
				//右键
				else if(e.getButton() == MouseEvent.BUTTON3){
					//使之选中
					table.setRowSelectionInterval(rowIndex, rowIndex);
					Task task = table.getTasks().get(rowIndex);
					table.getMainWindow().tablePopupMenu.show(task, table, e.getPoint().x, e.getPoint().y);
				}
			}
		});
		
		//注册属性变化监听器
		this.tasks.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				table.setRefresh(true);
			}
		});
		for(final Task task : tasks){
			task.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					table.setRefresh(true);
					if(mainWindow.taskImagePanel != null){
						mainWindow.taskImagePanel.flush(task); 
					}
				}
			});
		}
		//定时(每秒)刷新
		timer.schedule(new TimerTask() {
			public void run() {
				if(table.isRefresh()){
					//刷新表格
					try{
						SwingUtilities.updateComponentTreeUI(table);
					}catch(Exception e){}
					table.setRefresh(false);
				}
			}
		}, 1000, 1000);
		if(this.tasks != null && this.tasks.size() > 0){this.setRowSelectionInterval(0, 0);}
	}
	public void propertyChange(final Task task){
		final TaskingTable table = this;
		task.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				table.setRefresh(true);
				if(mainWindow.taskImagePanel != null){
					mainWindow.taskImagePanel.flush(task); 
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
	 * 开启任务下载,如果达到下载上限，则添加到排队列表中
	 * @param task 
	 * @return void
	 */
	public void startTask(Task task){
		if(this.getRunningNum() >= this.mainWindow.setting.getMaxThread()){
			this.addWaitingTask(task);
		}else{
			//如果是未采集，则先开启采集
			if(task.getStatus() == TaskStatus.UNCREATED || (task.getPictures() == null || task.getPictures().size() == 0)){
				task.setReCreateWorker(new ReCreateWorker(task, this.getMainWindow()));
				task.getReCreateWorker().execute();
			}else{
				task.setStatus(TaskStatus.STARTED);
				task.setDownloadWorker(new DownloadWorker(task, this.getMainWindow()));
				task.getDownloadWorker().execute();
				this.setRunningNum(this.getRunningNum() + 1);
			}
		}
	}
	/**
	 * 将排队等待中的第一个任务开启下载
	 * @param task 
	 * @return void
	 */
	public void startWaitingTask(){
		if(this.getWaitingTasks() != null && this.getWaitingTasks().size() > 0){
			Task task = this.getWaitingTasks().get(0);//第一个任务
			this.startTask(task);
			this.getWaitingTasks().remove(0);//将第一个任务移除排队列表
		}
	}
	
	/**
	 * 暂停下载任务
	 * @param task 
	 * @return void
	 */
	public void stopTask(Task task){
		//如果状态为下载中，则将状态改为已暂停，随后将下载线程取消掉
		if(task.getStatus() == TaskStatus.STARTED){
			task.setStatus(TaskStatus.STOPED);
			Tracker.println(getClass(), task.getName() + ":已暂停");
			if(task.getDownloadWorker() != null){
				task.getDownloadWorker().cancel(true);
				task.setDownloadWorker(null);//swingworker不能复用，需要重新建立
				//更新任务数据
				this.mainWindow.taskDbTemplate.update(task);
				this.setRunningNum(this.getRunningNum() - 1);
			}
			//开启排队等待的第一个任务
			this.startWaitingTask();
		}
		//如果状态为排队等待中，则将状态改为已暂停，并从排队等待列表中移除
		else if(task.getStatus() == TaskStatus.WAITING){
			task.setStatus(TaskStatus.STOPED);
			Tracker.println(getClass(), task.getName() + ":已暂停");
			this.waitingTasks.remove(task);
		}
	}
	
	/**
	 *暂定所有下载任务 
	 */
	public void stopAllTasks(){
		for(Task task: tasks){
			if(task.getStatus() == TaskStatus.STARTED || task.getStatus() == TaskStatus.WAITING){
				stopTask(task);
			}
		}
	}

	public TaskList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(TaskList<Task> tasks) {
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
	public boolean isRebuild() {
		return rebuild;
	}
	public void setRebuild(boolean rebuild) {
		this.rebuild = rebuild;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public TaskList<Task> getHiddentasks() {
		return hiddentasks;
	}

	public void setHiddentasks(TaskList<Task> hiddentasks) {
		this.hiddentasks = hiddentasks;
	}
}
