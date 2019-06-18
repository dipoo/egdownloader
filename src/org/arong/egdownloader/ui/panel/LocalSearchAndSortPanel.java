package org.arong.egdownloader.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Task;
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
	public JComboBox sortCombobox;
	public JComboBox sortCombobox2;
	public JLabel searchResultLabel;
	
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
				String key_ = keyTextField.getText();
				if(key_ == null || "".equals(key_.trim())){
					JOptionPane.showMessageDialog(this_, "请输入关键字！");
					return;
				}
				TaskingTable table = (TaskingTable)mainWindow.runningTable;
				List<Task> allTasks = table.getTasks();
				int j = 0;
				List<Integer> indexs = new ArrayList<Integer>();
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
									allTasks.add(0, allTasks.remove(i));
									indexs.add(i);
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
								allTasks.add(0, allTasks.remove(i));
								indexs.add(i);
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
								allTasks.add(0, allTasks.remove(i));
								indexs.add(i);
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
								allTasks.add(0, allTasks.remove(i));
								indexs.add(i);
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
								allTasks.add(0, allTasks.remove(i));
								indexs.add(i);
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
								allTasks.add(0, allTasks.remove(i));
								indexs.add(i);
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
									allTasks.add(0, allTasks.remove(i));
									indexs.add(i);
									break;
								}else if(allTasks.get(i).getSubname() != null && allTasks.get(i).getSubname().contains(key.toLowerCase())){
									allTasks.get(i).setSearched(true);//标识为已被搜索
									allTasks.add(0, allTasks.remove(i));
									indexs.add(i);
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
				if(j > 0){
					table.setRowSelectionInterval(0, 0);
					table.scrollRectToVisible(table.getCellRect(0, 0, true));
					if(mainWindow.viewModel == 2){
						mainWindow.taskImagePanel.page = 1;
						mainWindow.taskImagePanel.init(table.getTasks());
						mainWindow.taskImagePanel.scrollRectToVisible(table.getCellRect(0, 0, true));
					}
				}
				searchResultLabel.setText("[" + key_ + "]搜索完毕,结果【" + j + "】条。");
				mainWindow.infoTabbedPane.setSelectedIndex(mainWindow.infoTabbedPane.indexOfComponent(this_));
			}
		}, 620, 20, 60, 30);
		
		sortCombobox = new JComboBox(new String[]{"无", "发布时间", "创建时间", "数目", "大小", "语言", "进度", "类型", "状态", "阅读状态"});
		sortCombobox.setSelectedIndex(1);
		sortCombobox.setBounds(700, 20, 100, 30);
		sortCombobox2 = new JComboBox(new String[]{"降序", "升序"});
		sortCombobox2.setBounds(810, 20, 100, 30);
		
		searchResultLabel = new AJLabel("", Color.BLUE, 10, 70, 900, 30);
		
		ComponentUtil.addComponents(textPanel, keyTextField, searchBtn, sortCombobox, sortCombobox2, searchResultLabel);
		this.add(textPanel, BorderLayout.CENTER);
	}
}
