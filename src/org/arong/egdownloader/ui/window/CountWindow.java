package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JTextPane;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.FileUtil2;
import org.arong.util.jdbc.JdbcSqlExecutor;
import org.arong.util.jdbc.JdbcUtil;
/**
 * 任务统计面板
 * @author dipoo
 * @since 2015-01-05
 */
public class CountWindow extends JDialog {

	private static final long serialVersionUID = 344119118958307328L;
	public JTextPane htmlPanel;
	EgDownloaderWindow window;
	public CommonSwingWorker worker = null;
	public CountWindow(EgDownloaderWindow window){
		this.window = window;
		// 设置主窗口
		this.setSize(700, 350);
		this.setIconImage(IconManager.getIcon("count").getImage());
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

		htmlPanel = new AJTextPane("", Color.BLUE);
		this.getContentPane().add(htmlPanel);
		showCountPanel();
	}
	
	public void showCountPanel(){
		if(worker != null && !worker.isDone()){
			this.setVisible(true);
			//System.out.println("请等待...");
			return;
		}
		final CountWindow this_ = this;
		worker = new CommonSwingWorker(new Runnable() {
			public void run() {
				this_.setVisible(true);
				htmlPanel.setText("<br><br><center>统计中...</center>");
				htmlPanel.setText(transferHtml());
				worker = null;
			}
		});
		worker.execute();
		
	}
	
	public String transferHtml(){
		List<Task> tasks = window.tasks;
		if(tasks.size() == 0){
			return "<center><h2>无数据</h2></center>";
		}
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
		long totalSize = 0;
		long downSize = 0;
		Map<String, Integer> tagMap = new HashMap<String, Integer>();
		for(Task task : tasks){
			if(task.getStatus() == TaskStatus.COMPLETED){
				t_complete ++;
			}
			p_count += task.getTotal();
			p_complete += task.getCurrent();
			totalSize += parseLongSize(task.getSize());
			if(StringUtils.isNotBlank(task.getTags())){
				String[] arr = task.getTags().split(";");
				for(String tag : arr){
					if(StringUtils.isNotBlank(tag.trim())){
						tagMap.put(tag, 0);
					}
				}
			}
		}
		String sql = "select sum(size) as totalsize from picture where size is not null and size <> ''";
		try {
			downSize = JdbcSqlExecutor.getInstance().executeQuery(sql, JdbcUtil.getConnection(), new JdbcSqlExecutor.CallBack<Long>(){
				public Long action(ResultSet rs) throws SQLException {
					if(rs.next()){
						return rs.getLong("totalsize");
					}
					return 0L;
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		t_uncomplete = t_count - t_complete;
		p_uncomplete = p_count - p_complete;
		t_completionRate = new BigDecimal(Double.parseDouble(t_complete + "") * 100 / Double.parseDouble(t_count + "")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		p_completionRate = new BigDecimal(Double.parseDouble(p_complete + "") * 100 / Double.parseDouble(p_count + "")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		String s = String.format(ComponentConst.countHtml, t_count, t_historyCount, t_complete, t_uncomplete, p_count, 
				p_historyCount, p_complete, p_uncomplete,t_completionRate, p_completionRate, FileUtil2.showSizeStr(totalSize), FileUtil2.showSizeStr(downSize),
				TaskTagsPanel.tagscnMap == null ? 0 : TaskTagsPanel.tagscnMap.size(), tagMap.size(),
				lastCreateTime == null ? "" : lastCreateTime, lastDownloadTime == null ? "" : lastDownloadTime);
		tagMap = null;
		
		/*Component[] comps = window.taskImagePanel.getComponents();
		AJLabel l;int count = 0;
		for(int i = 0; i < comps.length; i ++){
			l = (AJLabel) ((JPanel)comps[i]).getComponent(0);
			if(l.getImage() != null && l.getImage() != IconManager.getIcon("init")){
				count ++;
			}
		}
		System.out.println(count);*/
		
		return s;
	}
	
	private long parseLongSize(String size){
		long s = 0;
		if(StringUtils.isNotBlank(size)){
			try{
				if(size.contains("G")){
					s += ((Double)Double.parseDouble(size.substring(0, size.indexOf("G")))).longValue() * 1024 * 1024 * 1024;
				}else if(size.contains("M")){
					s += ((Double)Double.parseDouble(size.substring(0, size.indexOf("M")))).longValue() * 1024 * 1024;
				}else if(size.contains("K")){
					s += ((Double)Double.parseDouble(size.substring(0, size.indexOf("K")))).longValue() * 1024;
				}else if(size.contains("B")){
					s += ((Double)Double.parseDouble(size.substring(0, size.indexOf("B")))).longValue();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return s;
	}
}
