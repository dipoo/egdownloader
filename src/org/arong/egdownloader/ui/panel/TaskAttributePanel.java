package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;

public class TaskAttributePanel extends JScrollPane {
	
	public final static String MISC = "misc";
	
	private AJTextPane textPane;
	
	public TaskAttributePanel(final EgDownloaderWindow mainWindow) {
		textPane = new AJTextPane(null,
				Color.BLUE);
		textPane.setBorder(null);
		this.setViewportView(textPane);
		this.setBorder(null);
		
		textPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if("refresh".equals(e.getDescription())){
						new CommonSwingWorker(new Runnable() {
							public void run() {
								int index = mainWindow.viewModel == 1 ? mainWindow.runningTable.selectRowIndex : mainWindow.taskImagePanel.selectIndex;
								Task currentTask = mainWindow.tasks.get(index);
								try {
									System.out.println("开始更新任务[" + currentTask.getDisplayName() + "]的标签组信息");
									Task t = ScriptParser.getTaskByUrl(currentTask.getUrl(), mainWindow.setting);
									if(t != null && StringUtils.isNotBlank(t.getTags())){
										currentTask.setTags(t.getTags());
										if(mainWindow.infoTabbedPane.getSelectedIndex() == 2 && index == (mainWindow.viewModel == 1 ? mainWindow.runningTable.selectRowIndex : mainWindow.taskImagePanel.selectIndex)){
											parseTaskAttribute(currentTask);
										}
										mainWindow.taskDbTemplate.update(currentTask);
										System.out.println("<font style='color:green'>成功更新任务[" + currentTask.getDisplayName() + "]的标签组信息</font>");
										JOptionPane.showMessageDialog(mainWindow, "更新任务[" + currentTask.getDisplayName() + "]标签组成功");
									}else{
										System.out.println("<font style='color:color'>更新任务[" + currentTask.getDisplayName() + "]的标签组信息失败</font>");
										JOptionPane.showMessageDialog(mainWindow, "更新任务[" + currentTask.getDisplayName() + "]标签组失败");
									}
									
								} catch (Exception e) {
									e.printStackTrace();
									JOptionPane.showMessageDialog(mainWindow, "更新任务[" + currentTask.getDisplayName() + "]标签组失败：" + e.getMessage());
								}
							}
						}).execute();
					}else if(e.getDescription().startsWith("search|")){
						if(mainWindow.searchComicWindow == null){
							mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
						}
						mainWindow.searchComicWindow.doSearch(e.getDescription().replaceAll("search\\|", ""));
						mainWindow.searchComicWindow.setVisible(true);
					}
				}
			}
		});
	}
	
	public void parseTaskAttribute(Task t){
		textPane.setText("");
		if(t != null && StringUtils.isNotBlank(t.getTags())){
			StringBuffer sb = new StringBuffer("<div style='font-family:Consolas,微软雅黑;font-size:12px;margin-left:20px;'><a href='refresh' style='font-size:10px;'>更新</a><br/>");
			//解析属性组
			Map<String, List<String>> groups = new LinkedHashMap<String, List<String>>();
			String[] attrs = t.getTags().split(";");
			for(String attr : attrs){
				String[] arr = attr.split(":");
				if(arr.length == 1){
					if(groups.containsKey(MISC)){
						groups.get(MISC).add(arr[1]);
					}else{
						List<String> list = new ArrayList<String>();
						list.add(arr[0]);
						groups.put(MISC, list);
					}
				}else{
					if(groups.containsKey(arr[0])){
						groups.get(arr[0]).add(arr[1]);
					}else{
						List<String> list = new ArrayList<String>();
						list.add(arr[1]);
						groups.put(arr[0], list);
					}
				}
			}
			for(String group : groups.keySet()){
				sb.append("<span style='font-weight:bold;color:#D2691E'>").append(group).append("</span>：");
				for(String attr : groups.get(group)){
					sb.append("<a style='text-decoration:underline' href='search|");
					if(!group.equals(MISC)){
						sb.append(group).append(":");
					}
					sb.append("\"").append(attr.replaceAll("\\+", " ")).append("$\"'>").append(attr.replaceAll("\\+", " ")).append("</a>&nbsp;&nbsp;");
				}
				sb.append("<br/>");
			}
			sb.append("</div>");
			textPane.setText(sb.toString());
		}else{
			textPane.setText("<h3>该任务暂无标签组&nbsp;&nbsp;<a href='refresh' style='font-size:10px;'>更新</a></h3>");
		}
	}
}
