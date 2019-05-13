package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
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
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SimpleSearchWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.FileUtil2;

/**
 * 标签组面板
 *
 */
public class TaskTagsPanel extends JScrollPane {
	
	public final static String MISC = "misc";
	
	public EgDownloaderWindow mainWindow;
	
	private AJTextPane textPane;
	public JPanel confirmPanel;
	public AJLabel selectTextLabel;
	
	public static Map<String, String> tagscnMap = null;
	public static Map<String, String> rowsMap = null;
	public boolean searchTags = false;//是否为搜索时使用
	public String currentTags = null;
	public static final String[] CNFILENAMES = new String[]{"artist.md", "character.md", "female.md", "group.md", "language.md", "male.md", "misc.md", "parody.md", "reclass.md", "rows.md"};
	
	static{
		tagscnMap = new HashMap<String, String>();String[] arr = null;
		try {
			for(String filename : CNFILENAMES){
				BufferedReader br = new BufferedReader(new FileReader("script/EhTagTranslator.wiki/database/" + filename));
				while(true){
				    String line = br.readLine();
				    if(line == null){ break; }
				    arr = line.split("\\|");
				    if(arr.length > 3){
				    	if("".equals(arr[0].trim()) && StringUtils.isNotBlank(arr[1].trim())
				    			&& StringUtils.isNotBlank(arr[2].trim()) && StringUtils.isNotBlank(arr[3].trim())){
				    		tagscnMap.put(filename.replace(".md", "") + ":" + arr[1].trim() , (arr[2].trim().indexOf(")") > -1 ? arr[2].trim().substring(arr[2].trim().indexOf(")") + 1) : arr[2].trim()).replaceAll("\\?", "").replaceAll("👙", "").replaceAll("✏", "").replaceAll("❄", "").replaceAll("👪", "").replaceAll("❤", "").replaceAll("🌠", "").replaceAll("⚾", "").replaceAll("📖", "").replaceAll("⚡️", "").replaceAll("🔪", "").replaceAll("Δ", ""));
				    	}
				    }
				}
				if(br != null){
					br.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			new CommonSwingWorker(new Runnable() {
				public void run() {
					//在线下载
					System.out.println("开始在线下载中文标签库...");
					String dir = "script/EhTagTranslator.wiki/database/";
					FileUtil2.ifNotExistsThenCreate(dir);
					try {
						String[] arr = null;
						for(String filename : CNFILENAMES){
							String text = WebClient.getRequestUseJava("https://raw.githubusercontent.com/wiki/Mapaler/EhTagTranslator/database/" + filename, "UTF-8");
							if(StringUtils.isNotBlank(text)){
								String[] lines = text.split("\n");
								if(lines.length > 1){
									for(String line : lines){
										arr = line.split("\\|");
									    if(arr.length > 3){
									    	if("".equals(arr[0].trim()) && StringUtils.isNotBlank(arr[1].trim())
									    			&& StringUtils.isNotBlank(arr[2].trim()) && StringUtils.isNotBlank(arr[3].trim())){
									    		tagscnMap.put(filename.replace(".md", "") + ":" + arr[1].trim() , (arr[2].trim().indexOf(")") > -1 ? arr[2].trim().substring(arr[2].trim().indexOf(")") + 1) : arr[2].trim()).replaceAll("\\?", "").replaceAll("👙", "").replaceAll("✏", "").replaceAll("❄", "").replaceAll("👪", "").replaceAll("❤", "").replaceAll("🌠", "").replaceAll("⚾", "").replaceAll("📖", "").replaceAll("⚡️", "").replaceAll("🔪", "").replaceAll("Δ", ""));
									    	}
									    }
									}
									FileUtil2.storeStr2file(text, dir, filename);
								}
							}
							System.out.println("在线下载中文标签库结束");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TaskTagsPanel(final EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
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
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
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
							}
						});
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
						if(e.getDescription().contains("yes")){
							parseTaskAttribute(currentTags, true);
						}else{
							parseTaskAttribute(currentTags, false);
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
	public void parseTaskAttribute(Task t){
		parseTaskAttribute(t.getTags(), mainWindow.setting.isTagsTranslate());
	}
	public void parseTaskAttribute(String tags, boolean trans){
		trans = trans && tagscnMap != null;
		textPane.setText("");
		currentTags = tags;
		if(StringUtils.isNotBlank(tags)){
			StringBuffer sb = new StringBuffer("<div style='font-family:Consolas,微软雅黑;font-size:10px;margin-left:5px;'>");
			if(!searchTags){
				sb.append("<a href='refresh' style='font-size:10px;text-decoration:none;color:blue'><b>[更新]</b></a>");
			}
			sb.append("<a href='trans_" + (trans ? "no" : "yes") + "' style='font-size:10px;text-decoration:none;color:blue'><b>[" + (trans ? "还原" : "翻译") + "]</b></a>" + (trans ? "--<font style='font-size:10px;color:green'>翻译词源来自<a href='https://github.com/Mapaler/EhTagTranslator/wiki'>https://github.com/Mapaler/EhTagTranslator/wiki</a></font>" : "") + "<br/>");
			//解析属性组
			// language:english;parody:zootopia;male:fox boy;male:furry;artist:yitexity;:xx;xx
			Map<String, List<String>> groups = new LinkedHashMap<String, List<String>>();
			String[] attrs = tags.split(";");
			for(String attr : attrs){
				String[] arr = attr.split(":");
				if(arr.length == 1 || arr[0].equals("")){
					attr = MISC + ":" + attr.replaceAll(":", "");
					arr = attr.split(":");
				}
				if(groups.containsKey(arr[0])){
					groups.get(arr[0]).add(arr[1]);
				}else{
					List<String> list = new ArrayList<String>();
					list.add(arr[1]);
					groups.put(arr[0], list);
				}
			}
			int i = 0;
			for(String group : groups.keySet()){
				i ++;
				sb.append("<span style='font-weight:bold;color:#D2691E'>").append(trans && tagscnMap.containsKey("rows:" + group) ? tagscnMap.get("rows:" + group) : group).append("</span>：");
				for(String attr : groups.get(group)){
					sb.append("<a style='text-decoration:none' href='search|");
					if(!group.equals(MISC)){
						sb.append(group).append(":");
					}
					sb.append("\"").append(attr.replaceAll("\\+", " ")).append("$\"'>[").append(trans ? (tagscnMap.containsKey(group + ":" + attr.replaceAll("\\+", " ")) ? tagscnMap.get(group + ":" + attr.replaceAll("\\+", " ")) : attr.replaceAll("\\+", " ")) : attr.replaceAll("\\+", " ")).append("]</a>&nbsp;");
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
			if(!searchTags){
				textPane.setText("<div style='font-size:10px;margin-left:20px;'>该任务暂无标签组&nbsp;&nbsp;<a href='refresh' style='text-decoration:none;color:blue'><b>[更新]</b></a></div>");
			}else{
				textPane.setText("<div style='font-size:10px;margin-left:20px;'>该任务暂无标签组</div>");
			}
		}
	}
}
