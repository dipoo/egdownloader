package org.arong.egdownloader.ui.work.listenerWork;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.CheckingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
/**
 * 查缺补漏监听任务
 * @author 阿荣
 * @since 2014-06-21
 */
public class CheckResetWork implements IMenuListenerTask {

	public void doWork(Window window, ActionEvent e) {
		final EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		if(mainWindow.checkingWindow == null){
			mainWindow.checkingWindow = new CheckingWindow();
		}
		mainWindow.checkingWindow.setVisible(true);
		new CommonSwingWorker(new Runnable() {
			public void run() {
				TaskingTable table = (TaskingTable) mainWindow.runningTable;
				int index = table.getSelectedRow();
				Task task = table.getTasks().get(index);
				List<Picture> pics = task.getPictures();
				//任务为未开始状态或者图片列表为空都不用再检测了
				if(task.getStatus() != TaskStatus.UNSTARTED && pics != null && pics.size() > 0){
					String[] fileNames = getFiles(task.getSaveDir());
					if(fileNames != null){
						List<Integer> indexs = new ArrayList<Integer>();//需要重置状态的图片索引
						int completed = 0;
						Picture pic;
						String name;
						for(int i = 0; i < pics.size(); i ++){
							pic = pics.get(i);//图片
							if(pic.isCompleted()){//只有状态为已下载的才继续
								completed ++;
								name = pic.getName();//图片名称
								//真实保存名称
								if(!pic.isSaveAsName()){
									if(name.lastIndexOf(".") != -1){
										name = pic.getNum() + name.substring(name.lastIndexOf("."), name.length());
									}else{
										name = pic.getNum() + ".jpg";
									}
								}
								if(exists(fileNames, name)){
									continue;
								}
								indexs.add(i);//添加到索引集合
							}
						}
						if(indexs.size() > 0){
							mainWindow.checkingWindow.dispose();
							int result = JOptionPane.showConfirmDialog(mainWindow, "检测到" + indexs.size() + "张图片缺失，是否重置这些图片的下载状态？");
							if(result == JOptionPane.OK_OPTION){//确定
								mainWindow.checkingWindow.setVisible(true);
								for(int i = 0; i < indexs.size(); i ++){
									pic = task.getPictures().get(indexs.get(i));
									if(pic.isCompleted()){//如果当前图片的状态为已下载，则重置并记数
										pic.setCompleted(false);
										pic.setTime(null);
										if(task.getCurrent() >= 1){
											task.setCurrent(task.getCurrent() - 1);//任务进度减一
										}
										mainWindow.pictureDbTemplate.update(pic);//保存图片
									}
								}
								task.setCurrent(completed - indexs.size());
								task.setStatus(TaskStatus.STOPED);//重置任务为已暂停
								mainWindow.taskDbTemplate.update(task);//保存任务
								mainWindow.checkingWindow.dispose();
								JOptionPane.showMessageDialog(mainWindow, "重置任务完成！");
							}
							return;
						}else if(completed != task.getCurrent()){
							int result = JOptionPane.showConfirmDialog(mainWindow, "检测到图片下载数张与任务不一致，是否重置任务的当前下载？");
							if(result == JOptionPane.OK_OPTION){//确定
								task.setCurrent(completed);
								task.setStatus(TaskStatus.STOPED);//重置任务为已暂停
								mainWindow.taskDbTemplate.update(task);//保存任务
								mainWindow.checkingWindow.dispose();
								JOptionPane.showMessageDialog(mainWindow, "重置任务完成！");
							}
						}else{
							mainWindow.checkingWindow.dispose();
							JOptionPane.showMessageDialog(mainWindow, "检测完毕！无文件缺失");
						}
					}else{//文件夹不存在或者文件列表为空,重置全部图片状态
						mainWindow.checkingWindow.dispose();
						int result = JOptionPane.showConfirmDialog(mainWindow, "检测不到任何图片，是否重置任务的下载状态？");
						if(result == JOptionPane.OK_OPTION){//确定
							mainWindow.checkingWindow.setVisible(true);
							for(int i = 0; i < pics.size(); i ++){
								pics.get(i).setCompleted(false);
								pics.get(i).setTime(null);
							}
							task.setCurrent(0);//重置当前进度
							task.setStatus(TaskStatus.STOPED);//重置任务为已暂停
							mainWindow.pictureDbTemplate.update(pics);//保存图片
							mainWindow.taskDbTemplate.update(task);//保存任务
							mainWindow.checkingWindow.dispose();
							JOptionPane.showMessageDialog(mainWindow, "重置任务完成！");
						}
						return;
					}
				}
			}
		}).execute();
	}
	
	/**
	 * 获取下载目录下的所有文件名
	 * @param path
	 * @return
	 */
	private String[] getFiles(String path){
		File file = new File(path);
		if(file.exists()){
			return file.list();
		}
		return null;
	}
	
	/**
	 * 判断文件名是否存在文件名数组名
	 * @param names
	 * @param name
	 * @return
	 */
	private boolean exists(String[] names, String name){
		for (String str : names) {
			if(str.equals(name)){
				return true;
			}
		}
		return false;
	}

}
