package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.AllTagsWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SearchDetailInfoWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.DateUtil;
import org.arong.util.FileUtil2;
import org.arong.util.HtmlUtils;

/**
 * 标签组面板
 *
 */
public class TaskTagsPanel extends JScrollPane {
	
	public EgDownloaderWindow mainWindow;
	public Component parent;
	private SearchTask searchTask;
	
	public AJTextPane textPane;
	public JPanel confirmPanel;
	public JPanel selectedPanel;
	public JPanel excludePanel;
	/*public AJLabel selectTextLabel;*/
	AJButton favBtn;
	
	public static Map<String, String> tagscnMap = null;
	public String selectTags = "";//已选择的全部标签
	public String selectTag = "";//当前选择的标签
	public boolean searchTags = false;//是否为搜索时使用
	public String currentTags = null;//选择的标签
	public String excludeTags = "";//排除的标签
	public boolean showMyFav = false;//显示我的收藏
	
	static{
		tagscnMap = new HashMap<String, String>();String[] arr = null;String line = null;
		BufferedReader br = null;FileInputStream fs = null;InputStreamReader isr = null;
		try {
			for(String filename : ComponentConst.TAGS_CN_FILENAMES){
				fs = new FileInputStream(ComponentConst.TAGS_CN_FILE_PATH + filename);
				isr = new InputStreamReader(fs, "UTF-8");
				br = new BufferedReader(isr);
				while(true){
				    line = br.readLine();
				    if(line == null){ break; }
				    /*line = new String(line.getBytes(), "UTF-8");*/
				    arr = line.split("\\|");
				    if(arr.length > 3){
				    	if("".equals(arr[0].trim()) && StringUtils.isNotBlank(arr[1].trim())
				    			&& StringUtils.isNotBlank(arr[2].trim()) && !arr[1].trim().contains("--------") && !arr[1].trim().contains("英文原名")){
				    		tagscnMap.put(filename.replace(".md", "") + ":" + arr[1].trim(), ((arr[2].trim().indexOf(")") > -1 ? arr[2].trim().substring(arr[2].trim().indexOf(")") + 1) : arr[2].trim())).replace("\\", "").trim());
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
					FileUtil2.ifNotExistsThenCreate(ComponentConst.TAGS_CN_FILE_PATH);
					try {
						String[] arr = null;
						for(String filename : ComponentConst.TAGS_CN_FILENAMES){
							String text = WebClient.getRequestUseJava(ComponentConst.TAGS_CN_FILENAMES_DOWNLOAD_URL_PREFFIX + filename, "UTF-8");
							if(StringUtils.isNotBlank(text)){
								String[] lines = text.split("\n");
								if(lines.length > 1){
									for(String line : lines){
										arr = line.split("\\|");
									    if(arr.length > 3){
									    	if("".equals(arr[0].trim()) && StringUtils.isNotBlank(arr[1].trim())
									    			&& StringUtils.isNotBlank(arr[2].trim()) && !arr[0].trim().contains("--------") && !arr[0].trim().contains("英文原名")){
									    		tagscnMap.put(filename.replace(".md", "") + ":" + arr[1].trim(), ((arr[2].trim().indexOf(")") > -1 ? arr[2].trim().substring(arr[2].trim().indexOf(")") + 1) : arr[2].trim())).replace("\\", "").trim());
									    	}
									    }
									}
									FileUtil2.storeStr2file(text, ComponentConst.TAGS_CN_FILE_PATH, filename, "UTF-8");
								}
							}
						}
						System.out.println("在线下载中文标签库结束");
					}catch (Exception e1) {
						e1.printStackTrace();
						System.out.println(HtmlUtils.redColorHtml("在线下载中文标签库失败：" + e1.getMessage()));
					}
				}
			}).execute();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(HtmlUtils.redColorHtml("读取中文标签库失败：" + e.getMessage()));
		} finally{
			if(br != null){try {br.close();} catch (IOException e) {e.printStackTrace();}}
			if(isr != null){try {isr.close();} catch (IOException e) {e.printStackTrace();}}
			if(fs != null){try {fs.close();} catch (IOException e) {e.printStackTrace();}}
		}
	}
	public TaskTagsPanel(final EgDownloaderWindow mainWindow) {
		this(mainWindow, null);
	}
	public TaskTagsPanel(final EgDownloaderWindow mainWindow, Component parent) {
		this.mainWindow = mainWindow;
		if(parent != null){
			this.parent = parent;
		}else{
			this.parent = this.mainWindow;
		}
		textPane = new AJTextPane(null, Color.BLUE);
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
												if(StringUtils.isBlank(currentTask.getUploader())){
													currentTask.setUploader(t.getUploader());
												}
												if(StringUtils.isBlank(currentTask.getPostedTime())){
													currentTask.setPostedTime(t.getPostedTime());
												}
												if(mainWindow.infoTabbedPane.getSelectedComponent() == mainWindow.taskTagsPanel && index == (mainWindow.viewModel == 1 ? mainWindow.runningTable.selectRowIndex : mainWindow.taskImagePanel.selectIndex)){
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
						parseTaskAttribute(currentTags, mainWindow.setting.isTagsTranslate());
					}else if("fav".equals(e.getDescription())){
						showMyFav = true;
						parseTaskAttribute(currentTags, mainWindow.setting.isTagsTranslate());
					}else if("showAllTagsWindow".equals(e.getDescription())){
						if(mainWindow.allTagsWindow == null){
							mainWindow.allTagsWindow = new AllTagsWindow(mainWindow);
						}
						mainWindow.allTagsWindow.istask = false;
						mainWindow.allTagsWindow.searchBtn.doClick();
					}else if("showAllTaskTagsWindow".equals(e.getDescription())){
						if(mainWindow.allTagsWindow == null){
							mainWindow.allTagsWindow = new AllTagsWindow(mainWindow);
						}
						mainWindow.allTagsWindow.istask = true;
						mainWindow.allTagsWindow.searchBtn.doClick();
					}else if("uploadedby".equals(e.getDescription())){
						//搜索上传者
						if(searchTask != null && StringUtils.isNotBlank(searchTask.getUploader())){
							if(mainWindow.searchComicWindow == null){
								mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
							}
							try {
								mainWindow.searchComicWindow.doSearch(String.format("uploader:\"%s\"", URLDecoder.decode(URLDecoder.decode(searchTask.getUploader(), "UTF-8"), "UTF-8")));
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
		selectedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(Integer.parseInt("bababa", 16)), 1), "选择的标签（左键点击移除，右键点击添加至排除面板；红色为最新选择，可以收藏或取消收藏）"));
		selectedPanel.setBounds(5, 5, (parent.getWidth() - 30) / 2, 100);
		excludePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		excludePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(Integer.parseInt("bababa", 16)), 1), "排除的标签（左键点击可以移除）"));
		excludePanel.setBounds((parent.getWidth() - 30) / 2 + 10, 5, (parent.getWidth() - 30) / 2, 100);
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
		final TaskTagsPanel this_ = this;
		//本地搜索
		localBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewportView(textPane);
				mainWindow.localSearchAndSortPanel.keyTextField.setText("tags:" + selectTags.replaceAll("\\$\"", "").replaceAll("\"", "") + (StringUtils.isNotBlank(excludeTags) ? ";!" + excludeTags.replaceAll(";", ";!").replaceAll("\\$\"", "").replaceAll("\"", "") : ""));
				mainWindow.localSearchAndSortPanel.searchBtn.doClick();
				if(this_.getRootPane().getParent() instanceof SearchDetailInfoWindow){
					this_.getRootPane().getParent().setVisible(false);
				}
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
				if(this_.getRootPane().getParent() instanceof SearchDetailInfoWindow){
					this_.getRootPane().getParent().setVisible(false);
				}
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
				if(this_.getRootPane().getParent() instanceof SearchDetailInfoWindow){
					this_.getRootPane().getParent().setVisible(false);
				}else{
					setViewportView(textPane);
				}
			}
		});
		returnBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(this_.getRootPane().getParent() instanceof SearchDetailInfoWindow){
					this_.getRootPane().getParent().setVisible(false);
				}else{
					setViewportView(textPane);
				}
			}
		});
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectTags = "";
				selectTag = "";
				excludeTags = "";
				//清空
				renderSelectTags(null, false);
				renderExcludeTags(null, false);
				if(this_.getRootPane().getParent() instanceof SearchDetailInfoWindow){
					this_.getRootPane().getParent().setVisible(false);
				}else{
					setViewportView(textPane);
				}
			}
		});
		ComponentUtil.addComponents(confirmPanel, selectedPanel, excludePanel, localBtn, onlineBtn, favBtn, clearBtn, returnBtn);
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
						arr = (ComponentConst.MISC + ":" + tag).split(":");
					}
					String stag = tagscnMap.get(arr[0] + ":" + arr[1].replaceAll("\\$\"", "").replaceAll("\"", ""));
					if(StringUtils.isBlank(stag)){
						stag = arr[1].replaceAll("\\$\"", "").replaceAll("\"", "");
					}
					String row = tagscnMap.get("rows:" + arr[0]);
					if(StringUtils.isBlank(row)){
						row = arr[0];
					}
					text = row + ":" + stag;
				}
				AJButton btn = new AJButton("<html>" + HtmlUtils.filterEmoji2SegoeUISymbolFont(text) + "</html>", null, new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						String tag_ = ((JButton)e.getSource()).getName();
						String tag = tag_.replaceAll("\\$\"", "=====\"");
						//左键
						if(e.getButton() == MouseEvent.BUTTON1){
							selectTags = selectTags.replaceAll("\\$\"", "=====\"").replaceAll(tag, "").replaceAll("=====\"", "\\$\"").replaceAll(";;", ";");
							selectTag = selectTag.replaceAll("\\$\"", "=====\"").replaceAll(tag, "").replaceAll("=====\"", "\\$\"");
							renderSelectTags(tag_, false);
						}else if(e.getButton() == MouseEvent.BUTTON3){
							//右键
							selectTags = selectTags.replaceAll("\\$\"", "=====\"").replaceAll(tag, "").replaceAll("=====\"", "\\$\"").replaceAll(";;", ";");
							selectTag = selectTag.replaceAll("\\$\"", "=====\"").replaceAll(tag, "").replaceAll("=====\"", "\\$\"");
							System.out.println("selectTags:" + selectTags);
							System.out.println("selectTag:" + selectTag);
							renderSelectTags(tag_, false);
							renderExcludeTags(tag_, true);
						}
					}
				}, true);
				btn.setName(tag);
				btn.setFont(FontConst.Microsoft_BOLD_12);
				btn.setMargin(new Insets(1, 1, 1, 1));
				btn.setToolTipText(tag);
				btn.setForeground(Color.WHITE);
				btn.setUI(AJButton.redBtnUi);
				selectedPanel.add(btn);
			}
			/**
			 * 是否已经收藏
			 */
			if(StringUtils.isNotBlank(selectTag) && StringUtils.isNotBlank(mainWindow.setting.getFavTags()) && mainWindow.setting.getFavTags().contains(selectTag.replaceAll("\"", "").replaceAll("\\$", "") + ";")){
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
	
	public void renderExcludeTags(String tag, boolean add){
		
		if(StringUtils.isBlank(tag)){
			excludePanel.removeAll();
		}else{
			Component[] comps = excludePanel.getComponents();
			boolean contains = false;
			if(comps.length > 0){
				for(Component com : comps){
					JButton btn = (JButton)com;
					if(btn.getName().equals(tag)){
						excludePanel.remove(btn);
						if(add){
							excludePanel.add(btn);
						}
						contains = true;
					}else{
						btn.setUI(AJButton.lightBlueUi);
					}
				}
			}
			
			if(!contains && add){
				if(excludePanel.getComponentCount() >= 20){
					JOptionPane.showMessageDialog(this, "你选择的标签太多了！");
					setViewportView(confirmPanel);
					return;
				}
				
				if(StringUtils.isBlank(excludeTags)){
					excludeTags = tag;
				}else{
					if(!excludeTags.contains(tag)){
						excludeTags += ";" + tag;
					}
				}
				
				String text = tag;
				if(mainWindow.setting.isTagsTranslate()){ //汉化
					String[] arr = tag.split(":");
					if(arr.length == 1){
						arr = (ComponentConst.MISC + ":" + tag).split(":");
					}
					String stag = tagscnMap.get(arr[0] + ":" + arr[1].replaceAll("\\$\"", "").replaceAll("\"", ""));
					if(StringUtils.isBlank(stag)){
						stag = arr[1].replaceAll("\\$\"", "").replaceAll("\"", "");
					}
					String row = tagscnMap.get("rows:" + arr[0]);
					if(StringUtils.isBlank(row)){
						row = arr[0];
					}
					text = row + ":" + stag;
				}
				AJButton btn = new AJButton("<html>" + HtmlUtils.filterEmoji2SegoeUISymbolFont(text) + "</html>", null, new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						String tag_ = ((JButton)e.getSource()).getName();
						String tag = tag_.replaceAll("\\$\"", "=====\"");
						excludeTags = excludeTags.replaceAll("\\$\"", "=====\"").replaceAll(tag, "").replaceAll("=====\"", "\\$\"");
						renderExcludeTags(tag_, false);
					}
				}, true);
				btn.setName(tag);
				btn.setFont(FontConst.Microsoft_BOLD_12);
				btn.setMargin(new Insets(1, 1, 1, 1));
				btn.setToolTipText(tag);
				btn.setForeground(Color.WHITE);
				btn.setUI(AJButton.redBtnUi);
				excludePanel.add(btn);
			}
			setViewportView(confirmPanel);
		}
		SwingUtilities.invokeLater(new Runnable(){
			public void run() { 
				excludePanel.updateUI();
			}
		});
	}
	public void showTagGroup(Task t){
		try{
			showMyFav = false;
			searchTags = false;
			setViewportView(textPane);
			parseTaskAttribute(t);
		}catch(Exception e){}
	}
	public void showSearchTagGroup(SearchTask t){
		try{
			showMyFav = false;
			searchTags = true;
			setViewportView(textPane);
			parseTaskAttribute(t);
		}catch(Exception e){}
	}
	public void parseTaskAttribute(Task t){
		parseTaskAttribute(t.getTags(), mainWindow.setting.isTagsTranslate());
	}
	public void parseTaskAttribute(SearchTask t){
		setSearchTask(t);
		parseTaskAttribute(t.getTags(), mainWindow.setting.isTagsTranslate());
	}
	public void parseTaskAttribute(String tags, boolean trans){
		if(tags == null) tags = currentTags;
		trans = trans && tagscnMap != null;
		if(showMyFav){
			tags = mainWindow.setting.getFavTags();
		}else{
			currentTags = tags;
		}
		textPane.setText("...加载中...");
		if(StringUtils.isNotBlank(tags)){
			StringBuilder sb = new StringBuilder("<div style='font-family:微软雅黑;font-size:10px;padding:0px;margin:0px;'>");
			sb.append("<div style='padding:0 7px;margin:0px;text-align:left'>");
			if(searchTask != null){
				sb.append(String.format("<b style='font-size:11px;color:#666'>%s</b><br>", searchTask.getName(), searchTask.getUploader()));
				sb.append(String.format("<span>发布时间：<b style='color:orange'>%s</b>&nbsp;&nbsp;/&nbsp;&nbsp;图片数：<b style='color:orange'>%s</b>&nbsp;&nbsp;/&nbsp;&nbsp;评分：<b style='color:orange'>%s</b><br>上传者：<a href='uploadedby' style='text-decoration:none;'>%s</a>&nbsp;&nbsp;/&nbsp;&nbsp;BT：%s</span>", 
						searchTask.getDate(), searchTask.getFilenum(), searchTask.getRating(), searchTask.getUploader(), 
						StringUtils.isNotBlank(searchTask.getBtUrl()) ? String.format("<a href='%s' style='text-decoration:none;'>前往下载</a>", searchTask.getBtUrl()) : "无"));
				sb.append(String.format("<hr>"));
			}
			if(!showMyFav && !searchTags){
				sb.append("<a href='refresh' style='text-decoration:none;color:blue'><b>[&nbsp;同步&nbsp;]&nbsp;</b></a>");
			}
			if(showMyFav && currentTags != null){
				sb.append("<a href='return' style='text-decoration:none;color:blue'><b>[&nbsp;返回&nbsp;]</b>&nbsp;</a>");
			}
			if(!showMyFav){
				sb.append("<a href='fav' style='text-decoration:none;color:red'><b>[&nbsp;我的收藏&nbsp;]</b>&nbsp;</a>");
			}
			sb.append("<a href='showAllTagsWindow' style='text-decoration:none;color:blue;'><b>[&nbsp;所有标签&nbsp;]&nbsp;</b></a>");
			sb.append("<a href='showAllTaskTagsWindow' style='text-decoration:none;color:blue;'><b>[&nbsp;已建标签&nbsp;]&nbsp;</b></a>");
			sb.append("<a href='trans_" + (trans ? "no" : "yes") + "' style='text-decoration:none;color:blue'><b>[&nbsp;" + (trans ? "原文" : "翻译") + "&nbsp;]&nbsp;</b></a>" + (trans ? "--<font style='color:green'>翻译词源来自<a href='https://github.com/Mapaler/EhTagTranslator/wiki'>https://github.com/Mapaler/EhTagTranslator/wiki</a></font>" : "") + "<br/>");
			sb.append("</div>");
			sb.append("<table style='width:100%'>");
			//解析属性组
			// language:english;parody:zootopia;male:fox boy;male:furry;artist:yitexity;:xx;xx
			Map<String, List<String>> groups = parseTagGroup(tags);
			int i = 0;
			for(String group : groups.keySet()){
				i ++;
				sb.append("<tr style='padding:0px;margin:0px;'><td width='50px' style='text-align:right;padding:0px;margin:0px;'><span style='padding:0px;font-weight:bold;color:#D2691E'>").append(trans && tagscnMap.containsKey("rows:" + group) ? tagscnMap.get("rows:" + group) : group).append("</span>：</td><td style='text-align:left;padding:0px;margin:0px;'>");
				for(String attr : groups.get(group)){
					sb.append("<a style='text-decoration:none' href='clickTag|");
					if(!group.equals(ComponentConst.MISC)){
						sb.append(group).append(":");
					}
					attr = attr.replaceAll("\\+", " ");
					sb.append("\"").append(attr).append("$\"'>[&nbsp;").append(parseFav(group, attr, trans ? (tagscnMap.containsKey(group + ":" + attr) ? tagscnMap.get(group + ":" + attr) : attr) : attr)).append(String.format("<b>%s</b>&nbsp;]</a>&nbsp;", ComponentConst.allTaskCountMap.get((group + ":" + attr).replace(ComponentConst.MISC + ":", "")) == null ? "" : String.format("(%s)", ComponentConst.allTaskCountMap.get((group + ":" + attr).replace(ComponentConst.MISC + ":", "")))));
				}
				if(groups.keySet().size() > 8){
					if(i % 2 == 0){
						sb.append("<br/>");
					}
				}else{
					sb.append("<br/>");
				}
				sb.append("</td></tr>");
			}
			sb.append("</table></div>");
			textPane.setText(HtmlUtils.filterEmoji2SegoeUISymbolFont(sb.toString()));
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
		setViewportView(textPane);
	}
	
	private String parseFav(String group, String tag, String ftag){
		if(!showMyFav && StringUtils.isNotBlank(mainWindow.setting.getFavTags()) && (";" + mainWindow.setting.getFavTags()).contains(";" + (group.equals(ComponentConst.MISC) ? "" : (group + ":")) + tag + ";")){
			return "<font color='red'>" + ftag + "</font>";
		}
		return ftag;
	}

	public SearchTask getSearchTask() {
		return searchTask;
	}

	public void setSearchTask(SearchTask searchTask) {
		this.searchTask = searchTask;
	}
	
	public static Map<String, List<String>> parseTagGroup(String tags){
		//解析属性组
		// language:english;parody:zootopia;male:fox boy;male:furry;artist:yitexity;:xx;xx
		Map<String, List<String>> groups = new LinkedHashMap<String, List<String>>();
		if(tags == null) return groups;
		String[] attrs = tags.split(";");
		for(String attr : attrs){
			String[] arr = attr.split(":");
			if(arr.length == 1 || arr[0].equals("")){
				attr = ComponentConst.MISC + ":" + attr.replaceAll(":", "");
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
		return groups;
	}
	
	@Override
	public void setViewportView(Component view) {
		if(view == textPane){
			if(this.getParent() instanceof JTabbedPane){
				if(showMyFav){
					mainWindow.infoTabbedPane.setTitleAt(mainWindow.infoTabbedPane.indexOfComponent(this), "<html><font color='red'>我收藏的标签</a></html>");
				}else{
					mainWindow.infoTabbedPane.setTitleAt(mainWindow.infoTabbedPane.indexOfComponent(this), "标签组");
				}
			}
		}else if(view == confirmPanel){
			if(this.getParent() instanceof JTabbedPane){
				mainWindow.infoTabbedPane.setTitleAt(mainWindow.infoTabbedPane.indexOfComponent(this), "标签操作");
			}
		}
		super.setViewportView(view);
	}
}
