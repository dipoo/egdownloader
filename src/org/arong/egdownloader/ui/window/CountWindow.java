package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTextPane;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJTextPane;
/**
 * 任务统计面板
 * @author dipoo
 * @since 2015-01-05
 */
public class CountWindow extends JDialog {

	private static final long serialVersionUID = 344119118958307328L;
	public JTextPane htmlPanel;
	EgDownloaderWindow window;
	public CountWindow(EgDownloaderWindow window){
		this.window = window;
		// 设置主窗口
		this.setSize(700, 250);
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("user"))).getImage());
		this.setTitle("统计");
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(window);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CountWindow window = (CountWindow) e.getSource();
				window.dispose();
			}
		});
		//添加鼠标活动监听器
		this.addMouseListener(new MouseAdapter() {
			// 当鼠标点击当前窗口时隐藏此窗口
			public void mouseClicked(MouseEvent e) {
				CountWindow window = (CountWindow) e.getSource();
				window.dispose();
			}
		});

		htmlPanel = new AJTextPane(transferHtml(), Color.BLUE);
		this.getContentPane().add(htmlPanel);
	}
	
	public void showCountPanel(){
		htmlPanel.setText(transferHtml());
		htmlPanel.setVisible(true);
	}
	
	public String transferHtml(){
		List<Task> tasks = window.tasks;
		Setting setting = window.setting;
		int t_count = tasks.size();
		int t_historyCount = setting.getTaskHistoryCount();
		int t_complete = 0;
		int t_uncomplete = 0;
		int p_count = 0;
		int p_historyCount = setting.getPictureHistoryCount();
		int p_complete = 0;
		int p_uncomplete = 0;
		double t_completionRate = 0.0;
		double p_completionRate = 0.0;
		String lastCreateTime = setting.getLastCreateTime();
		String lastDownloadTime = setting.getLastDownloadTime();
		for(Task task : tasks){
			if(task.getStatus() == TaskStatus.COMPLETED){
				t_complete ++;
			}
			p_count += task.getTotal();
			p_complete += task.getCurrent();
		}
		t_uncomplete = t_count - t_complete;
		p_uncomplete = p_count - p_complete;
		t_completionRate = t_complete / t_count;
		p_completionRate = p_complete / p_count;
		
		String s = ComponentConst.countHtml;
		s = s.replace("@t_count", t_count + "").
			replace("@t_historyCount", t_historyCount + "").
			replace("@t_complete", t_complete + "").
			replace("@t_uncomplete", t_uncomplete + "").
			replace("@p_count", p_count + "").
			replace("@p_historyCount", p_historyCount + "").
			replace("@p_complete", p_complete + "").
			replace("@p_uncomplete", p_uncomplete + "").
			replace("@t_completionRate", t_completionRate + "%").
			replace("@p_completionRate", p_completionRate + "%").
			replace("@lastCreateTime", lastCreateTime == null ? "" : lastCreateTime).
			replace("@lastDownloadTime", lastDownloadTime == null ? "" : lastDownloadTime);
		return s;
	}
}
