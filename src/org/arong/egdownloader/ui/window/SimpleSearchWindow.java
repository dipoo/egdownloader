package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.util.Tracker;
/**
 * 简单搜索输入框窗口
 * @author dipoo
 * @since 2014-11-19
 */
public class SimpleSearchWindow extends JDialog {

	private static final long serialVersionUID = -4022333509001414223L;
	
	public JTextField keyTextField;
	EgDownloaderWindow mainWindow;
	public JButton searchBtn;
	
	public SimpleSearchWindow(final EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.setTitle("简单任务搜索");
		this.setSize(600, 160);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				JDialog w = (JDialog)e.getSource();
				w.dispose();
			}
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
		});
		final SimpleSearchWindow this_ = this;
		JLabel descLabel = new AJLabel("Tips:搜索的结果会显示在控制台", Color.GRAY, 200, 10, 180, 30);
		JLabel keyLabel = new AJLabel("关键字：", Color.BLUE, 10, 50, 50, 30);
		keyTextField = new AJTextField("", "", 70, 50, 430, 30);
		keyTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					searchBtn.doClick();
				}
			}
		});
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
				Tracker.println("[" + key_ + "]搜索完毕,结果【" + j + "】条。");
				mainWindow.infoTabbedPane.setSelectedIndex(0);
				mainWindow.setVisible(true);
				mainWindow.toFront();
			}
		}, 510, 50, 60, 30);
		ComponentUtil.addComponents(this.getContentPane(), descLabel, keyLabel, keyTextField, searchBtn);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SimpleSearchWindow this_ = (SimpleSearchWindow) e.getSource();
				this_.dispose();
			}
		});
	}
	public void dispose() {
		mainWindow.setEnabled(true);
		mainWindow.setVisible(true);
		super.dispose();
	}
}
