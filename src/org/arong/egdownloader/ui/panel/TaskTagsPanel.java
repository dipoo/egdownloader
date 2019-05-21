package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SimpleSearchWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.DateUtil;
import org.arong.util.FileUtil2;
import org.arong.util.HtmlUtils;

/**
 * 标签组面板
 *
 */
public class TaskTagsPanel extends JScrollPane {
	
	public final static String MISC = "misc";
	
	public EgDownloaderWindow mainWindow;
	private SearchTask searchTask;
	
	public AJTextPane textPane;
	public JPanel confirmPanel;
	public JPanel selectedPanel;
	/*public AJLabel selectTextLabel;*/
	AJButton favBtn;
	
	public static Map<String, String> tagscnMap = null;
	public static Map<String, String> rowsMap = null;
	public String selectTags = null;//已选择的全部标签
	public String selectTag = null;//当前选择的标签
	public boolean searchTags = false;//是否为搜索时使用
	public String currentTags = null;
	public boolean showMyFav = false;//显示我的收藏
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
				    			&& StringUtils.isNotBlank(arr[2].trim())){
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
						}
						System.out.println("在线下载中文标签库结束");
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
											System.out.println("开始同步任务[" + currentTask.getDisplayName() + "]的标签组信息");
											Task t = ScriptParser.getTaskByUrl(currentTask.getUrl(), mainWindow.setting);
											if(t != null && StringUtils.isNotBlank(t.getTags())){
												currentTask.setTags(t.getTags());
												currentTask.setSyncTime(DateUtil.YYYY_MM_DD_HH_MM_SS_FORMAT.format(new Date()));
												if(mainWindow.infoTabbedPane.getSelectedIndex() == 2 && index == (mainWindow.viewModel == 1 ? mainWindow.runningTable.selectRowIndex : mainWindow.taskImagePanel.selectIndex)){
													showTagGroup(currentTask);
												}
												mainWindow.taskDbTemplate.update(currentTask);
												System.out.println(HtmlUtils.greenColorHtml("成功同步任务[" + currentTask.getDisplayName() + "]的标签组信息"));
												JOptionPane.showMessageDialog(mainWindow, "同步任务[" + currentTask.getDisplayName() + "]标签组成功");
											}else{
												System.out.println(HtmlUtils.redColorHtml("同步任务[" + currentTask.getDisplayName() + "]的标签组信息失败"));
												JOptionPane.showMessageDialog(mainWindow, "同步任务[" + currentTask.getDisplayName() + "]标签组失败");
											}
											
										} catch (Exception e) {
											e.printStackTrace();
											JOptionPane.showMessageDialog(mainWindow, "同步任务[" + currentTask.getDisplayName() + "]标签组失败：" + e.getMessage());
										}
									}
								}).execute();
							}
						});
					}else if(e.getDescription().startsWith("clickTag|")){
						if(confirmPanel == null){
							initConfirmPanel(mainWindow);
						}
						//获取关键字
						String key = e.getDescription().replaceAll("clickTag\\|", "");
						
						renderSelectTags(key, true);
						
					}else if(e.getDescription().startsWith("trans_")){
						if(e.getDescription().contains("yes")){
							parseTaskAttribute(currentTags, true);
						}else{
							parseTaskAttribute(currentTags, false);
						}
					}else if("return".equals(e.getDescription())){
						showMyFav = false;
						parseTaskAttribute(currentTags, true);
					}else if("fav".equals(e.getDescription())){
						showMyFav = true;
						parseTaskAttribute(currentTags, true);
					}else if("uploadedby".equals(e.getDescription())){
						//搜索上传者
						if(searchTask != null && StringUtils.isNotBlank(searchTask.getUploader())){
							if(mainWindow.searchComicWindow == null){
								mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
							}
							try {
								mainWindow.searchComicWindow.doSearch("uploader:" + URLDecoder.decode(URLDecoder.decode(searchTask.getUploader(), "UTF-8"), "UTF-8"));
							} catch (UnsupportedEncodingException e1) {
								e1.printStackTrace();
							}
							mainWindow.searchComicWindow.setVisible(true);
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
		/* 分类条件 */
		selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(Integer.parseInt("bababa", 16)), 1), "已经选择的标签（点击可以移除；红色为最新选择，可以收藏或取消收藏）"));
		selectedPanel.setBounds(5, 5, mainWindow.getWidth() - 20, 100);
		/*selectTextLabel = new AJLabel("", Color.BLUE);
		selectTextLabel.setBounds(20, 10, 500, 30);*/
		AJButton localBtn = new AJButton("本地搜索");
		localBtn.setBounds(5, 110, 90, 30);
		localBtn.setUI(AJButton.blueBtnUi);
		AJButton onlineBtn = new AJButton("在线搜索");
		onlineBtn.setBounds(105, 110, 90, 30);
		onlineBtn.setUI(AJButton.blueBtnUi);
		favBtn = new AJButton("标签收藏");
		favBtn.setBounds(205, 110, 90, 30);
		AJButton clearBtn = new AJButton("清空所选");
		clearBtn.setBounds(305, 110, 90, 30);
		AJButton returnBtn = new AJButton("返回面板");
		returnBtn.setBounds(405, 110, 90, 30);
		//本地搜索
		localBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
				if(mainWindow.simpleSearchWindow == null){
					mainWindow.simpleSearchWindow = new SimpleSearchWindow(mainWindow);
				}
				SimpleSearchWindow ssw = (SimpleSearchWindow) mainWindow.simpleSearchWindow;
				ssw.keyTextField.setText("tags:" + selectTags.replaceAll("\\$\"", "").replaceAll("\"", ""));
				ssw.searchBtn.doClick();
			}
		});
		//在线搜索
		onlineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
				if(mainWindow.searchComicWindow == null){
					mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
				}
				mainWindow.searchComicWindow.doSearch(selectTags.replaceAll(";", " "));
				mainWindow.searchComicWindow.setVisible(true);
			}
		});
		//标签收藏
		favBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tag = selectTag.replaceAll("\\$\"", "").replaceAll("\"", "");
				if(StringUtils.isNotBlank(tag)){
					if(StringUtils.isNotBlank(mainWindow.setting.getFavTags())){
						if(mainWindow.setting.getFavTags().contains(tag + ";")){
							mainWindow.setting.setFavTags(mainWindow.setting.getFavTags().replaceAll(tag + ";", ""));
						}else{
							mainWindow.setting.setFavTags(mainWindow.setting.getFavTags() + tag + ";");
						}
					}else{
						mainWindow.setting.setFavTags(tag + ";");
					}
					mainWindow.settingDbTemplate.update(mainWindow.setting);
				}else{
					JOptionPane.showMessageDialog(mainWindow, "当前选择的标签为空");
				}
				setViewportView(textPane);
			}
		});
		returnBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
			}
		});
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectTags = "";
				selectTag = "";
				//清空
				renderSelectTags(null, false);
			}
		});
		ComponentUtil.addComponents(confirmPanel, selectedPanel, localBtn, onlineBtn, favBtn, clearBtn, returnBtn);
	}
	public void renderSelectTags(String tag, boolean add){
		
		if(StringUtils.isBlank(tag)){
			selectedPanel.removeAll();
		}else{
			
			Component[] comps = selectedPanel.getComponents();
			boolean contains = false;
			if(comps.length > 0){
				for(Component com : comps){
					JButton btn = (JButton)com;
					if(btn.getName().equals(tag)){
						selectedPanel.remove(btn);
						if(add){
							selectedPanel.add(btn);
							btn.setUI(AJButton.redBtnUi);
						}
						contains = true;
					}else{
						btn.setUI(AJButton.lightBlueUi);
						if(!add){
							if(btn.getName().equals(selectTag)){
								btn.setUI(AJButton.redBtnUi);
							}
						}
					}
				}
			}
			
			if(!contains && add){
				if(selectedPanel.getComponentCount() >= 20){
					JOptionPane.showMessageDialog(this, "你选择的标签太多了！");
					setViewportView(confirmPanel);
					return;
				}
				
				selectTag = tag;
				if(StringUtils.isBlank(selectTags)){
					selectTags = tag;
				}else{
					if(!selectTags.contains(tag)){
						selectTags += ";" + tag;
					}
				}
				
				String text = tag;
				if(mainWindow.setting.isTagsTranslate()){ //汉化
					String[] arr = tag.split(":");
					if(arr.length == 1){
						arr = (MISC + ":" + tag).split(":");
					}
					String stag = tagscnMap.get(arr[0] + ":" + arr[1].replaceAll("\\$\"", "").replaceAll("\"", ""));
					if(StringUtils.isBlank(stag)){
						stag = arr[1].replaceAll("\\$\"", "").replaceAll("\"", "");
					}
					String row = tagscnMap.get("rows:" + arr[0]);
					if(StringUtils.isBlank(row)){
						row = arr[0];
					}
					text = row + "：" + stag;
				}
				AJButton btn = new AJButton(text, null, new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						String tag_ = ((JButton)e.getSource()).getName();
						String tag = tag_.replaceAll("\\$\"", "=====\"");
						selectTags = selectTags.replaceAll("\\$\"", "=====\"").replaceAll(tag + ";", "").replaceAll("=====\"", "\\$\"");
						selectTag = selectTag.replaceAll("\\$\"", "=====\"").replaceAll(tag, "").replaceAll("=====\"", "\\$\"");
						renderSelectTags(tag_, false);
					}
				}, true);
				btn.setName(tag);
				btn.setToolTipText(tag);
				btn.setForeground(Color.WHITE);
				btn.setUI(AJButton.redBtnUi);
				selectedPanel.add(btn);
			}
			/**
			 * 是否已经收藏
			 */
			if(StringUtils.isNotBlank(mainWindow.setting.getFavTags()) && mainWindow.setting.getFavTags().contains(tag.replaceAll("\"", "").replaceAll("\\$", "") + ";")){
				favBtn.setText("取消收藏");
				favBtn.setUI(AJButton.redBtnUi);
			}else{
				favBtn.setText("标签收藏");
				favBtn.setUI(AJButton.blueBtnUi);
			}
			setViewportView(confirmPanel);
		}
		SwingUtilities.invokeLater(new Runnable(){
			public void run() { 
				selectedPanel.updateUI();
			}
		});
	}
	public void showTagGroup(Task t){
		showMyFav = false;
		searchTags = false;
		setViewportView(textPane);
		parseTaskAttribute(t);
	}
	public void showSearchTagGroup(SearchTask t){
		showMyFav = false;
		searchTags = true;
		setViewportView(textPane);
		parseTaskAttribute(t);
	}
	public void parseTaskAttribute(Task t){
		parseTaskAttribute(t.getTags(), mainWindow.setting.isTagsTranslate());
	}
	public void parseTaskAttribute(SearchTask t){
		setSearchTask(t);
		parseTaskAttribute(t.getTags(), mainWindow.setting.isTagsTranslate());
	}
	public void parseTaskAttribute(String tags, boolean trans){
		this.setVisible(false);
		trans = trans && tagscnMap != null;
		if(showMyFav){
			tags = mainWindow.setting.getFavTags();
		}else{
			currentTags = tags;
		}
		textPane.setText("");
		if(StringUtils.isNotBlank(tags)){
			StringBuffer sb = new StringBuffer("<div style='font-family:Consolas,微软雅黑;font-size:10px;margin-left:5px;'>");
			if(searchTask != null){
				sb.append(String.format("<b>名称：%s[uploaded by <a href='uploadedby' style='text-decoration:none;color:blue'>%s</a></b>]<br>", searchTask.getName(), searchTask.getUploader()));
			}
			if(!showMyFav && !searchTags){
				sb.append("<a href='refresh' style='font-size:10px;text-decoration:none;color:blue'><b>[同步]&nbsp;</b></a>");
			}
			if(showMyFav && currentTags != null){
				sb.append("<a href='return' style='text-decoration:none;color:blue'><b>[返回]</b>&nbsp;</a>");
			}
			if(!showMyFav){
				sb.append("<a href='fav' style='text-decoration:none;color:red'><b>[我的收藏]</b>&nbsp;</a>");
			}
			sb.append("<a href='trans_" + (trans ? "no" : "yes") + "' style='font-size:10px;text-decoration:none;color:blue'><b>[" + (trans ? "原文" : "翻译") + "]&nbsp;</b></a>" + (trans ? "--<font style='font-size:10px;color:green'>翻译词源来自<a href='https://github.com/Mapaler/EhTagTranslator/wiki'>https://github.com/Mapaler/EhTagTranslator/wiki</a></font>" : "") + "<br/>");
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
					sb.append("<a style='text-decoration:none' href='clickTag|");
					if(!group.equals(MISC)){
						sb.append(group).append(":");
					}
					sb.append("\"").append(attr.replaceAll("\\+", " ")).append("$\"'>[").append(trans ? (tagscnMap.containsKey(group + ":" + attr.replaceAll("\\+", " ")) ? tagscnMap.get(group + ":" + attr.replaceAll("\\+", " ")) : attr.replaceAll("\\+", " ")) : attr.replaceAll("\\+", " ")).append("]</a>&nbsp;");
				}
				if(groups.keySet().size() > 9){
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
			if(!showMyFav && !searchTags){
				textPane.setText("<div style='font-size:10px;margin-left:5px;'>该任务暂无标签组&nbsp;&nbsp;<a href='refresh' style='text-decoration:none;color:blue'><b>[同步]</b></a></div>");
			}else{
				if(!showMyFav){
					textPane.setText("<div style='font-size:10px;margin-left:5px;'>该任务暂无标签组</div>");
				}else{
					textPane.setText("<div style='font-size:10px;margin-left:5px;'>你还没有收藏任何标签！&nbsp;<a href='return' style='text-decoration:none;color:blue'><b>[返回]</b></a></div>");
				}
			}
		}
		this.setVisible(true);
	}

	public SearchTask getSearchTask() {
		return searchTask;
	}

	public void setSearchTask(SearchTask searchTask) {
		this.searchTask = searchTask;
	}
}
