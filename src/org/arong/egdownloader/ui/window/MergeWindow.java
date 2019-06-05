package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
/**
 * 任务合并面板
 * @author dipoo
 * @since 2019-06-05
 */
public class MergeWindow extends JDialog {

	public JTextPane htmlPanel;
	EgDownloaderWindow window;
	public CommonSwingWorker worker = null;
	public Object[] objs = new Object[2];
	public boolean merging = false;//是否合并中
	public MergeWindow(final EgDownloaderWindow window){
		this.window = window;
		// 设置主窗口
		this.setSize(700, 350);
		this.setIconImage(IconManager.getIcon("count").getImage());
		this.setTitle("任务合并");
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(window);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MergeWindow window = (MergeWindow) e.getSource();
				window.dispose();
			}
		});

		htmlPanel = new AJTextPane("", Color.BLUE);
		htmlPanel.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//e.getDescription():a标签href值
					if("merge".equals(e.getDescription())){
						new CommonSwingWorker(new Runnable() {
							public void run() {
								//开始合并
								if(merging){
									System.out.println("任务合并中...");
								}else{
									merging = true;
									showMergeInfo();
									//采集任务信息
									Task t = (Task)objs[0];
									SearchTask st = (SearchTask)objs[1];
									try {
										Task newtask = ScriptParser.getTaskByUrl(st.getUrl(), window.setting);
										//比较图片信息
										
										//复制已下载图片
										
										//删除从任务
										
									} catch (Exception e) {
										e.printStackTrace();
										JOptionPane.showConfirmDialog(MergeWindow.this, "任务合并失败");
									} finally{
										merging = false;
										showMergeInfo();
									}
								}
							}
						});
					}
				}
			}
		});
		this.getContentPane().add(htmlPanel);
		showMergeInfo();
	}
	
	public void setTask(Task task){
		objs[0] = task;
	}
	public void setSearchTask(SearchTask task){
		objs[1] = task;
	}
	
	public void showMergeInfo(){
		if(worker != null && !worker.isDone()){
			this.setVisible(true);
			return;
		}
		this.setVisible(true);
		htmlPanel.setText("<br><br><center>统计中...</center>");
		htmlPanel.setText(transferHtml());
		worker = null;
	}
	
	public String transferHtml(){
		Task t = (Task)objs[0];
		SearchTask st = (SearchTask)objs[1];
		if(t == null || st == null) return "";
		String s = String.format("<html>【主】(%s-%s)%s<br>【从】(%s-%s)%s<br><a style='text-decoration:underline' href='#merge'>%s</a></html>", 
				st.getDate(), st.getUploader(), st.getName(), 
				t.getPostedTime(), t.getUploader(), t.getName(),
				merging ? "任务合并中..." : "开始合并");
		return s;
	}
}
