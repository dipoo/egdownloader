package org.arong.egdownloader.ui.work;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.DeletingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
import org.arong.util.Tracker;
/**
 * 删除任务线程类
 * @author 阿荣
 * @since 2014-09-19
 */
public class DeleteWorker extends SwingWorker<Void, Void>{
	
	private EgDownloaderWindow mainWindow;
	private TaskingTable table;
	private DeletingWindow w;
	private int[] rows;
	private boolean deleteFile;
	public DeleteWorker(EgDownloaderWindow mainWindow, TaskingTable table, DeletingWindow w, int[] rows, boolean deleteFile){
		this.mainWindow = mainWindow;
		this.table = table;
		this.rows = rows;
		this.w = w;
		this.deleteFile = deleteFile;
	}
	
	protected Void doInBackground() throws Exception {
		try{
			Task task;
			List<Task> tasks = new ArrayList<Task>();
			for(int i = 0; i < rows.length; i ++){
				if(table.getTasks().size() >= (rows[i])){
					task = table.getTasks().get(rows[i]);
					tasks.add(task);
					w.setData((i + 1) + "/" + rows.length);
					w.setInfo("收集：" + task.getName());
					Tracker.println("待删除：" + task.getName());
				}
			}
			//操作数据库
			if(tasks.size() > 0){
				for(Task t : tasks){
					if(t.getPictures() != null && t.getPictures().size() > 0){
						w.setInfo("正在删除任务图片");
						mainWindow.pictureDbTemplate.delete("tid", t.getId());
						if(deleteFile){
							//删除文件
							FileUtil2.deleteFile(new File(t.getSaveDir()));
						}
					}
					w.setInfo(String.format("正在删除任务【%s】-%s", t.getDisplayName(), t.getUrl()));
					mainWindow.taskDbTemplate.delete(t);//删除任务
					//更新内存
					table.getTasks().remove(t);
					if(table.getTasks().size() > 0){
						table.setRowSelectionInterval(0, 0);
						table.scrollRectToVisible(table.getCellRect(0, 0, true));
					}
					t.flushTagsCount(false);
					if(mainWindow.taskImagePanel != null){
						mainWindow.taskImagePanel.init();
					}
				}
				Tracker.println("删除" + tasks.size() + "个任务完成");
			}
			table.clearSelection();//使之不选中任何行
			if(table.getTasks().size() == 0){
				mainWindow.tablePane.setVisible(false);//将任务panel隐藏
				mainWindow.emptyPanel.setVisible(true);//将空任务label显示
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mainWindow.setEnabled(true);
			mainWindow.toFront();
			w.dispose();
		}	
		return null;
	}
	 
}
