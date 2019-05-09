package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SimpleSearchWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.FileUtil2;
import org.arong.util.JsonUtil;

public class TaskTagsPanel extends JScrollPane {
	
	public final static String MISC = "misc";
	
	private AJTextPane textPane;
	public JPanel confirmPanel;
	public AJLabel selectTextLabel;
	
	public static Map<String, String> tagscnMap = null;
	
	static{
		try {
			String text = FileUtil2.getTextFromReader(new FileReader("script/ehtags-cn.json"));
			if(StringUtils.isNotBlank(text)){
				List<Map<String, String>> list = JsonUtil.jsonArray2MapList(text.trim());
				if(list != null && list.size() > 0){
					tagscnMap = new HashMap<String, String>();
					for(Map<String, String> m : list){
						tagscnMap.put(m.get("k"), m.get("v"));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TaskTagsPanel(final EgDownloaderWindow mainWindow) {
		textPane = new AJTextPane(null,
				Color.BLUE);
		textPane.setBorder(null);
		this.setViewportView(textPane);
		this.setBorder(null);
		initConfirmPanel(mainWindow);
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
											parseTaskAttribute(currentTask, false);
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
						//获取关键字
						String key = e.getDescription().replaceAll("search\\|", "");
						if(confirmPanel == null){
							initConfirmPanel(mainWindow);
						}
						selectTextLabel.setText("请选择[" + key + "]标签的操作");
						confirmPanel.setName(key);
						setViewportView(confirmPanel);
					}else if(e.getDescription().startsWith("trans_")){
						int index = mainWindow.viewModel == 1 ? mainWindow.runningTable.selectRowIndex : mainWindow.taskImagePanel.selectIndex;
						Task currentTask = mainWindow.tasks.get(index);
						if(e.getDescription().contains("yes")){
							parseTaskAttribute(currentTask, true);
						}else{
							parseTaskAttribute(currentTask, false);
						}
					}
				}
			}
		});
	}
	
	public void initConfirmPanel(final EgDownloaderWindow mainWindow){
		confirmPanel = new JPanel();
		confirmPanel.setBounds(100, 20, 120, 40);
		confirmPanel.setLayout(null);
		selectTextLabel = new AJLabel("", Color.BLUE);
		selectTextLabel.setBounds(20, 10, 500, 30);
		AJButton b1 = new AJButton("本地搜索");
		b1.setBounds(20, 50, 90, 30);
		b1.setUI(AJButton.blueBtnUi);
		AJButton b2 = new AJButton("在线搜索");
		b2.setBounds(120, 50, 90, 30);
		b2.setUI(AJButton.blueBtnUi);
		AJButton b3 = new AJButton("返回");
		b3.setBounds(220, 50, 60, 30);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
				if(mainWindow.simpleSearchWindow == null){
					mainWindow.simpleSearchWindow = new SimpleSearchWindow(mainWindow);
				}
				SimpleSearchWindow ssw = (SimpleSearchWindow) mainWindow.simpleSearchWindow;
				ssw.keyTextField.setText("tags:" + confirmPanel.getName().replaceAll("\"", "").replaceAll("\\$", ""));
				ssw.searchBtn.doClick();
			}
		});
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
				if(mainWindow.searchComicWindow == null){
					mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
				}
				mainWindow.searchComicWindow.doSearch(confirmPanel.getName());
				mainWindow.searchComicWindow.setVisible(true);
			}
		});
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
			}
		});
		ComponentUtil.addComponents(confirmPanel, selectTextLabel, b1, b2, b3);
	}
	
	public void parseTaskAttribute(Task t, boolean trans){
		trans = trans && tagscnMap != null;
		textPane.setText("");
		if(t != null && StringUtils.isNotBlank(t.getTags())){
			StringBuffer sb = new StringBuffer("<div style='font-family:Consolas,微软雅黑;font-size:11px;margin-left:20px;'><a href='refresh' style='font-size:10px;text-decoration:none;color:blue'><b>[更新]</b></a><a href='trans_" + (trans ? "no" : "yes") + "' style='font-size:10px;text-decoration:none;color:blue'><b>[" + (trans ? "还原" : "翻译") + "]</b></a>" + (trans ? "--<font style='font-size:10px;color:green'>翻译词源来自<a href='https://github.com/scooderic/exhentai-tags-chinese-translation/'>https://github.com/scooderic/exhentai-tags-chinese-translation/</a></font>" : "") + "<br/>");
			//解析属性组
			Map<String, List<String>> groups = new LinkedHashMap<String, List<String>>();
			String[] attrs = t.getTags().split(";");
			for(String attr : attrs){
				String[] arr = attr.split(":");
				if(arr.length == 1){
					if(groups.containsKey(MISC)){
						groups.get(MISC).add(arr[0]);
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
			int i = 0;
			for(String group : groups.keySet()){
				i ++;
				sb.append("<span style='font-weight:bold;color:#D2691E'>").append(group).append("</span>：");
				for(String attr : groups.get(group)){
					sb.append("<a style='text-decoration:none' href='search|");
					if(!group.equals(MISC)){
						sb.append(group).append(":");
					}
					sb.append("\"").append(attr.replaceAll("\\+", " ")).append("$\"'>[").append(trans ? (tagscnMap.containsKey(attr.replaceAll("\\+", " ")) ? tagscnMap.get(attr.replaceAll("\\+", " ")) : attr.replaceAll("\\+", " ")) : attr.replaceAll("\\+", " ")).append("]</a>&nbsp;");
				}
				if(groups.keySet().size() > 8){
					if(i % 2 == 0){
						sb.append("<br/>");
					}
				}else{
					sb.append("<br/>");
				}
			}
			sb.append("</div>");
			textPane.setText(sb.toString());
		}else{
			textPane.setText("<div style='font-size:10px;margin-left:20px;'>该任务暂无标签组&nbsp;&nbsp;<a href='refresh' style='text-decoration:none;color:blue'>[更新]</a></div>");
		}
	}
}
