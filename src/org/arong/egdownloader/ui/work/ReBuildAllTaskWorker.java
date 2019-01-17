package org.arong.egdownloader.ui.work;

import java.util.List;

import javax.swing.SwingWorker;

import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.Tracker;
/**
 * 重建所有任务。（采集缺失的任务信息，用于低版本数据库文件升级）
 * @author dipoo
 * @since 2015-01-02
 */
public class ReBuildAllTaskWorker extends SwingWorker<Void, Void> {
	public EgDownloaderWindow window;
	public ReBuildAllTaskWorker(EgDownloaderWindow window) {
		this.window = window;
	}
	
	protected Void doInBackground() throws Exception {
		TaskingTable table = (TaskingTable) window.runningTable;
		List<Task> tasks = table.getTasks();
		Setting setting = window.setting;
		//设置重建状态
		table.setRebuild(true);
		int i = 0;
		long t = System.currentTimeMillis();
		for(Task task : tasks){
			i ++;
			ScriptParser.rebuildTask(task, setting);
			if(task.getPictures() != null){
				//保存数据
				window.taskDbTemplate.update(task);
				//删除原来的图片
				window.pictureDbTemplate.delete("tid", task.getId());
				//添加新采集的图片
				window.pictureDbTemplate.store(task.getPictures());
				Tracker.println(ReBuildAllTaskWorker.class, "重建任务-" + i + "/" + tasks.size() + ", 成功");
			}else{
				Tracker.println(ReBuildAllTaskWorker.class, "重建任务-" + i + "/" + tasks.size() + ", 失败");
			}
		}
		table.setRebuild(false);
		Tracker.println(ReBuildAllTaskWorker.class, "重建任务完成！耗时：" + (System.currentTimeMillis() - t) / 1000L + " 秒");
		return null;
	}

}
