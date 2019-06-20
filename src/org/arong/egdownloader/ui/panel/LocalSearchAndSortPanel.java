package org.arong.egdownloader.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskList;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;

public class LocalSearchAndSortPanel extends JPanel {
	public LocalSearchAndSortPanel this_ = this;
	public EgDownloaderWindow mainWindow;
	public JTextField keyTextField;
	public JButton searchBtn;
	public JComboBox sortCombobox;//排序字段
	public JComboBox sortCombobox2;//升序、降序
	public JLabel searchResultLabel;//搜索结果
	
	public final static String[] sortColumnTexts = {"无", "发布时间", "创建时间", "数目", "大小", "进度", "语言", "类型",
		"状态", "阅读状态", "名称", "子标题", "已下载数", "上传者", "是否原图", "作者"};
	public final static String[] sortTypeTexts = {"降序", "升序"};
	
	public void doSearch(String key){
		keyTextField.setText(key);
		searchBtn.doClick();
	}
	
	public LocalSearchAndSortPanel(EgDownloaderWindow window){
		mainWindow = window;
		this.setLayout(new BorderLayout());
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(null);
		textPanel.setBorder(null);
		textPanel.setBounds(5, 5, -1, 120);
		keyTextField = new AJTextField("", "", 10, 20, 600, 30);
		keyTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					searchBtn.doClick();
				}
			}
		});
		textPanel.add(keyTextField);
		
		searchBtn = new AJButton("搜索", "", new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				long t = System.currentTimeMillis();
				String key_ = keyTextField.getText();
				/*if(key_ == null || "".equals(key_.trim())){
					JOptionPane.showMessageDialog(this_, "请输入关键字！");
					return;
				}*/
				TaskingTable table = (TaskingTable)mainWindow.runningTable;
				TaskList<Task> allTasks = table.getTasks();
				int j = 0;
				TaskList<Task> results = new TaskList<Task>();
				boolean all = false;
				if(StringUtils.isNotBlank(key_) && !"".equals(key_.trim())){
					try {
						if(key_.startsWith("tags:") && !key_.startsWith("tags:uploader:")){
							String keys = key_.replaceAll("tags:", "").replaceAll(" ", "+");
							String[] keysArr = keys.split(";");
							boolean hits;
							for(int i = 0; i < allTasks.size(); i++){
								j ++;
								if(allTasks.get(i).getTags() != null){
									hits = true;
									//需要全部命中
									for(String key : keysArr){
										if(StringUtils.isNotBlank(key)){
											if(key.startsWith("!")){
												key = key.replace("!", "");
												if(allTasks.get(i).getTags().toLowerCase().startsWith(key.toLowerCase() + ";") || allTasks.get(i).getTags().toLowerCase().contains(";" + key.toLowerCase() + ";")){
													hits = false;
												}
											}else{
												if(!allTasks.get(i).getTags().toLowerCase().startsWith(key.toLowerCase() + ";") && !allTasks.get(i).getTags().toLowerCase().contains(";" + key.toLowerCase() + ";")){
													hits = false;
												}
											}
										}
									}
									if(hits){
										allTasks.get(i).setSearched(true);//标识为已被搜索
										results.add(allTasks.get(i));
									}else{
										allTasks.get(i).setSearched(false);
										j --;
									}
								}else{
									allTasks.get(i).setSearched(false);
									j --;
								}
							}
						}else if(key_.startsWith("cover:")){
							String key = key_.replaceAll("cover:", "");
							for(int i = 0; i < allTasks.size(); i++){
								j ++;
								if(allTasks.get(i).getCoverUrl().toLowerCase().contains(key.toLowerCase())){
									allTasks.get(i).setSearched(true);//标识为已被搜索
									results.add(allTasks.get(i));
								}else{
									allTasks.get(i).setSearched(false);
									j --;
								}
							}
						}else if(key_.startsWith("type:")){
							String key = key_.replaceAll("type:", "");
							for(int i = 0; i < allTasks.size(); i++){
								j ++;
								if(allTasks.get(i).getType().toLowerCase().contains(key.toLowerCase())){
									allTasks.get(i).setSearched(true);//标识为已被搜索
									results.add(allTasks.get(i));
								}else{
									allTasks.get(i).setSearched(false);
									j --;
								}
							}
						}else if(key_.startsWith("uploader:") || key_.startsWith("tags:uploader:")){
							String key = key_.replaceAll("tags:uploader:", "").replaceAll("uploader:", "");
							for(int i = 0; i < allTasks.size(); i++){
								j ++;
								if(allTasks.get(i).getUploader().toLowerCase().contains(key.toLowerCase())){
									allTasks.get(i).setSearched(true);//标识为已被搜索
									results.add(allTasks.get(i));
								}else{
									allTasks.get(i).setSearched(false);
									j --;
								}
							}
						}else if(key_.startsWith("language:")){
							String key = key_.replaceAll("language:", "");
							for(int i = 0; i < allTasks.size(); i++){
								j ++;
								if(allTasks.get(i).getLanguage().toLowerCase().contains(key.toLowerCase())){
									allTasks.get(i).setSearched(true);//标识为已被搜索
									results.add(allTasks.get(i));
								}else{
									allTasks.get(i).setSearched(false);
									j --;
								}
							}
						}else if(key_.startsWith("localtag:")){
							String key = key_.replaceAll("localtag:", "");
							for(int i = 0; i < allTasks.size(); i++){
								j ++;
								if((key_.equals("一般") && StringUtils.isBlank(allTasks.get(i).getTag())) || allTasks.get(i).getTag().toLowerCase().contains(key.toLowerCase())){
									allTasks.get(i).setSearched(true);//标识为已被搜索
									results.add(allTasks.get(i));
								}else{
									allTasks.get(i).setSearched(false);
									j --;
								}
							}
						}else{
							String[] keys = key_.trim().split("\\|\\|");
							for(int i = 0; i < allTasks.size(); i++){
								for(String key : keys){
									j ++;
									if(allTasks.get(i).getName().toLowerCase().contains(key.toLowerCase())){
										allTasks.get(i).setSearched(true);//标识为已被搜索
										results.add(allTasks.get(i));
										break;
									}else if(allTasks.get(i).getSubname() != null && allTasks.get(i).getSubname().contains(key.toLowerCase())){
										allTasks.get(i).setSearched(true);//标识为已被搜索
										results.add(allTasks.get(i));
										break;
									}else{
										allTasks.get(i).setSearched(false);
										j --;
									}
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					all = true;
				}
					
				if(j > 0 || all){
					//合并
					if(all || table.getTasks().removeAll(results)){
						//排序
						final String sortColumn = (String) sortCombobox.getSelectedItem();
						final int sortTypeIndex = sortCombobox2.getSelectedIndex();
						final boolean all_ = all;
						Collections.sort(all ? table.getTasks() : results, new Comparator<Task>() {
							public int compare(Task t1, Task t2) {
								if(all_){
									t1.setSearched(false);t2.setSearched(false);
								}
								if(sortColumn.equals(sortColumnTexts[1])){
									return sortTypeIndex == 0 ? t2.getPostedTime().compareTo(t1.getPostedTime()) : 
										t1.getPostedTime().compareTo(t2.getPostedTime());
								}else if(sortColumn.equals(sortColumnTexts[2])){
									return sortTypeIndex == 0 ? t2.getCreateTime().compareTo(t1.getCreateTime()) : 
										t1.getCreateTime().compareTo(t2.getCreateTime());
								}else if(sortColumn.equals(sortColumnTexts[3])){
									return sortTypeIndex == 0 ? (t2.getTotal() >= t1.getTotal() ? 1 : -1)
											: (t2.getTotal() >= t1.getTotal() ? -1 : 1);
								}else if(sortColumn.equals(sortColumnTexts[4])){
									try{
										return sortTypeIndex == 0 ? (parseLongSize(t2.getSize()) >= parseLongSize(t1.getSize()) ? 1 : -1)
												: (parseLongSize(t2.getSize()) >= parseLongSize(t1.getSize()) ? -1 : 1);
									}catch(Exception e){
										return 0;
									}
								}else if(sortColumn.equals(sortColumnTexts[5])){
									return sortTypeIndex == 0 ? (Double.parseDouble(t2.getCurrent() + "") / Double.parseDouble(t2.getTotal() + "")) >= (Double.parseDouble(t1.getCurrent() + "") / Double.parseDouble(t1.getTotal() + "")) ? 1 : -1
											: (Double.parseDouble(t2.getCurrent() + "") / Double.parseDouble(t2.getTotal() + "")) >= (Double.parseDouble(t1.getCurrent() + "") / Double.parseDouble(t1.getTotal() + "")) ? -1 : 1;
								}else if(sortColumn.equals(sortColumnTexts[6])){
									return sortTypeIndex == 0 ? t2.getLanguage().compareTo(t1.getLanguage()) : 
										t1.getLanguage().compareTo(t2.getLanguage());
								}else if(sortColumn.equals(sortColumnTexts[7])){
									return sortTypeIndex == 0 ? t2.getType().compareTo(t1.getType()) : 
										t1.getType().compareTo(t2.getType());
								}else if(sortColumn.equals(sortColumnTexts[8])){
									return sortTypeIndex == 0 ? t2.getStatus().compareTo(t1.getStatus()) : 
										t1.getStatus().compareTo(t2.getStatus());
								}else if(sortColumn.equals(sortColumnTexts[9])){
									return sortTypeIndex == 0 ? t2.isReaded() ? 1 : -1 : t2.isReaded() ? -1 : 1;
								}else if(sortColumn.equals(sortColumnTexts[10])){
									return sortTypeIndex == 0 ? t2.getName().compareTo(t1.getName()) : 
										t1.getName().compareTo(t2.getName());
								}else if(sortColumn.equals(sortColumnTexts[11])){
									return sortTypeIndex == 0 ? t2.getSubname().compareTo(t1.getSubname()) : 
										t1.getSubname().compareTo(t2.getSubname());
								}else if(sortColumn.equals(sortColumnTexts[12])){
									return sortTypeIndex == 0 ? (t2.getCurrent() >= t1.getCurrent() ? 1 : -1)
											: (t2.getCurrent() >= t1.getCurrent() ? -1 : 1);
								}else if(sortColumn.equals(sortColumnTexts[13])){
									return sortTypeIndex == 0 ? t2.getUploader().compareTo(t1.getUploader()) : 
										t1.getUploader().compareTo(t2.getUploader());
								}else if(sortColumn.equals(sortColumnTexts[14])){
									return sortTypeIndex == 0 ? t2.isOriginal() ? 1 : -1 : t2.isOriginal() ? -1 : 1;
								}else if(sortColumn.equals(sortColumnTexts[15])){
									return sortTypeIndex == 0 ? (t2.getAuthor() == null ? "" : t2.getAuthor()).compareTo((t1.getAuthor() == null ? "" : t1.getAuthor())) : 
										(t2.getAuthor() == null ? "" : t2.getAuthor()).compareTo((t1.getAuthor() == null ? "" : t1.getAuthor()));
								}
								return 0;
							}
						});
						if(!all)
							table.getTasks().addAll(0, results);
					}
					table.setRowSelectionInterval(0, 0);
					table.scrollRectToVisible(table.getCellRect(0, 0, true));
					table.updateUI();
					if(mainWindow.viewModel == 2){
						mainWindow.taskImagePanel.page = 1;
						mainWindow.taskImagePanel.init(table.getTasks());
						mainWindow.taskImagePanel.scrollRectToVisible(table.getCellRect(0, 0, true));
					}
				}
				searchResultLabel.setText(String.format("<html>%s%s%s完毕%s，耗时：%s。</html>",
						all ?  "" : String.format("搜索<font color='red'>[%s]</font>", key_),
						all ? "" : "，以",
						sortCombobox.getSelectedIndex() == 0 ? "" : String.format("<font color='orange'>[%s->%s]</font>排序", sortCombobox.getSelectedItem(), sortCombobox2.getSelectedItem()),
						all ? "" : String.format("，结果<font color='green'>[%s]</font>条", j),
						formatSecend(System.currentTimeMillis() - t)));
				mainWindow.setVisible(true);
				mainWindow.toFront();
				mainWindow.infoTabbedPane.setSelectedIndex(mainWindow.infoTabbedPane.indexOfComponent(this_));
			}
		}, 620, 20, 60, 30);
		
		sortCombobox = new JComboBox(sortColumnTexts);
		sortCombobox.setSelectedIndex(1);
		sortCombobox.setBounds(700, 20, 100, 30);
		sortCombobox2 = new JComboBox(sortTypeTexts);
		sortCombobox2.setBounds(810, 20, 100, 30);
		
		searchResultLabel = new AJLabel("", Color.BLUE, 10, 70, 1000, 30);
		
		ComponentUtil.addComponents(textPanel, keyTextField, searchBtn, sortCombobox, sortCombobox2, searchResultLabel);
		this.add(textPanel, BorderLayout.CENTER);
	}
	
	public long parseLongSize(String size){
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
	private String formatSecend(long t){
		return String.format("%s秒", String.format("%.2f", (t / 1000f)));
	}
}
