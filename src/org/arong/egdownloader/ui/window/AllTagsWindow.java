package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ButtonUI;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJPager;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.HtmlUtils;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * 显示所有标签窗口
 * @author dipoo
 * @date 2019-05-29
 */
public class AllTagsWindow extends JDialog {
	public boolean istask;//是否展示的是已建任务中所包含的标签
	public EgDownloaderWindow mainWindow;
	public JPanel typeBtnPanel;
	public AJTextField searchField;
	public AJButton searchBtn;
	public JSeparator separator;
	public JPanel tagPane;
	public JScrollPane scrollPane;
	public AJLabel emptyLabel;
	//public AJTextPane tagPane;
	
	public CommonSwingWorker worker = null;
	public Component[] tagBtns;
	public String currentGroup;
	public int currentPage;
	public int pageSize = 250;
	public AJPager pager;
	public Map<String, Set<String>> allKeys;//所有任务标签	
	public Map<String, Set<String>> allTaskKeys;//已建任务标签
	public Map<String, Integer> allTaskCountMap;
	public String emptyText = "在分类[%s]下搜索不到匹配的标签";
	
	public String getTitle(){
		return istask ? "已建任务标签" : "全部任务标签";
	}
	
	public Map<String, Set<String>> getKeys(){
		return istask ? allTaskKeys : allKeys;
	}
	public void setkeys(Map<String, Set<String>> keys){
		if(istask){
			allTaskKeys = keys;
		}else{
			allKeys = keys;
		}
	}
	public AllTagsWindow(final EgDownloaderWindow egDownloaderWindow){
		this(egDownloaderWindow, false);
	}
	
	public AllTagsWindow(final EgDownloaderWindow egDownloaderWindow, boolean istask_){
		this.istask = istask_;
		this.mainWindow = egDownloaderWindow;
		this.setSize((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50) > ComponentConst.CLIENT_WIDTH ? ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50) : ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setTitle(getTitle());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		searchField = new AJTextField(null, 5, 10, 200, 25);
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					searchBtn.doClick();
				}
			}
		});
		searchBtn = new AJButton("搜索", "", new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				searchTags(1, searchField.getText().trim());
			}
		}, 210, 10, 60, 25);
		searchBtn.setForeground(Color.WHITE);
		searchBtn.setUI(AJButton.blueBtnUi);
		
		JButton toggleBtn = new AJButton("切换", "", new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				istask = !istask;
				searchBtn.doClick();
			}
		}, 950, 10, 60, 25);
		toggleBtn.setForeground(Color.WHITE);
		toggleBtn.setUI(AJButton.blueBtnUi);
		
		typeBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		typeBtnPanel.setBounds(280, 5, 620, 40);
		ActionListener typeBtnActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AJButton btn = (AJButton) e.getSource();
				for(Component com : typeBtnPanel.getComponents()){
					if(com instanceof JButton){
						((JButton)com).setUI(AJButton.greenBtnUi);
					}
				}
				btn.setUI(AJButton.redBtnUi);
				btn.setSize(60, 25);
				String row = btn.getName();
				currentGroup = row;
				searchTags(1, searchField.getText().trim());
			}
		};
		for(String row : ComponentConst.TAGS_CN_FILENAMES){
			if("rows.md".equals(row)) continue;
			AJButton btn = new AJButton(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() && TaskTagsPanel.tagscnMap.containsKey("rows:" + row.replaceAll(".md", ""))? 
					TaskTagsPanel.tagscnMap.get("rows:" + row.replaceAll(".md", "")) : row.replaceAll(".md", ""), null);
			btn.setName(row.replaceAll(".md", ""));
			btn.setToolTipText(row.replaceAll(".md", ""));
			btn.addActionListener(typeBtnActionListener);
			typeBtnPanel.add(btn);
		}
		separator = new JSeparator(); 
		separator.setBounds(0, 48, this.getWidth(), 1);
		
		tagPane = new JPanel();
		tagPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		tagPane.setBounds(5, 5, this.getWidth() - 40, this.getHeight() - 160);
		tagPane.setPreferredSize(new Dimension(this.getWidth() - 40,  this.getHeight() - 60));
		tagPane.setBorder(null);
		
		scrollPane = new JScrollPane(tagPane);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setBounds(0, 50, this.getWidth() - 20, this.getHeight() - 140);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		
		emptyLabel = new AJLabel("", Color.RED);
		
		pager = new AJPager(10, this.getHeight() - 80, this.getWidth() - 30, 40, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				searchTags(Integer.parseInt(btn.getName()), searchField.getText().trim());
			}
		});
		pager.setVisible(false);
		
		ComponentUtil.addComponents(getContentPane(), typeBtnPanel, searchField, searchBtn, toggleBtn, separator, scrollPane, pager);
		setVisible(true);
		
		((JButton)(typeBtnPanel.getComponent(6))).setUI(AJButton.redBtnUi);
		currentGroup = ComponentConst.TAGS_CN_FILENAMES[6].replaceAll(".md", "");
		this.setVisible(false);
		searchTags(istask);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
	}
	public ButtonUI[] btnUis = {
			AJButton.blueBtnUi,
			AJButton.greenBtnUi,
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue),
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red)
	};
	
	public void searchTags(){
		searchTags(1, null);
	}
	public void searchTags(boolean istask){
		this.istask = istask;
		searchTags(1, null);
	}
	public void addTaskTags(String tags){
		if(allTaskKeys != null){
			if(StringUtils.isNotBlank(tags)){
				String[] arr = tags.split(";"); String[] arr_= null;String mapkey;
				for(String tag : arr){
					arr_ = tag.split(":");
					if(arr_.length == 1){
						arr_ = (TaskTagsPanel.MISC + ":" + tag).split(":");
					}
					if(!getKeys().containsKey(arr_[0])){
						getKeys().put(arr_[0], new HashSet<String>());
					}
					mapkey = arr_[0] + ":" + arr_[1].replaceAll("\\+", " ");
					getKeys().get(arr_[0]).add(mapkey);
					if(allTaskCountMap.containsKey(mapkey)){
						allTaskCountMap.put(mapkey, allTaskCountMap.get(mapkey) + 1);
					}else{
						allTaskCountMap.put(mapkey, 1);
					}
				}
			}
		}
	}
	public void searchTags(final int page, final String searchkey){
		if(worker != null && !worker.isDone()){
			System.out.println("请等待...");
			return;
		}
		final AllTagsWindow this_ = this;
		SwingUtilities.invokeLater(new Runnable(){
			public void run() { 
				worker = new CommonSwingWorker(new Runnable() {
					public void run() {
						tagPane.removeAll();
						pager.setVisible(false);
						this_.setVisible(true);
						
						if(getKeys() == null){
							setkeys(new HashMap<String, Set<String>>());
						}
						
						if(getKeys().isEmpty()){
							String[] arr;
							if(!istask && TaskTagsPanel.tagscnMap != null){
								for(String key : TaskTagsPanel.tagscnMap.keySet()){
									arr = key.split(":");
									if(arr.length == 1){
										arr = (TaskTagsPanel.MISC + ":" + key).split(":");
										arr = key.split(":");
									}
									if(!getKeys().containsKey(arr[0])){
										getKeys().put(arr[0], new HashSet<String>());
									}
									getKeys().get(arr[0]).add(arr[0] + ":" + arr[1]);
								}
							}else if(mainWindow.tasks != null){
								if(allTaskCountMap == null){
									allTaskCountMap = new HashMap<String, Integer>();
								}
								String[] arr_;String mapkey;
								for(Task task : mainWindow.tasks){
									if(StringUtils.isNotBlank(task.getTags())){
										arr = task.getTags().split(";");
										for(String tag : arr){
											arr_ = tag.split(":");
											if(arr_.length == 1){
												arr_ = (TaskTagsPanel.MISC + ":" + tag).split(":");
											}
											if(!getKeys().containsKey(arr_[0])){
												getKeys().put(arr_[0], new HashSet<String>());
											}
											mapkey = arr_[0] + ":" + arr_[1].replaceAll("\\+", " ");
											getKeys().get(arr_[0]).add(mapkey);
											if(allTaskCountMap.containsKey(mapkey)){
												allTaskCountMap.put(mapkey, allTaskCountMap.get(mapkey) + 1);
											}else{
												allTaskCountMap.put(mapkey, 1);
											}
										}
									}
								}
							}
						}
						
						Set<String> keys = getKeys().get(currentGroup);
						if(keys != null){
							//关键字过滤
							if(StringUtils.isNotBlank(searchkey)){
								HashSet<String> filterKeys = new HashSet<String>();
								for( String key : keys){
									if(key.toLowerCase().replace(currentGroup + ":", "").contains(searchkey.toLowerCase())){
										filterKeys.add(key);
									}else if(mainWindow.setting.isTagsTranslate() && TaskTagsPanel.tagscnMap.get(key).contains(searchkey.toLowerCase())){
										filterKeys.add(key);
									}
								}
								keys = filterKeys;
							}
							if(istask){
								//按照任务个数排序
								Set<String> sortSet = new TreeSet<String>(new Comparator<String>() {
									public int compare(String key1, String key2) {
										return allTaskCountMap.get(key1) >= allTaskCountMap.get(key2) ? -1 : 1;
									}
								});
						        sortSet.addAll(keys);
						        keys = sortSet;
							}
							this_.setTitle(String.format("%s(%s条记录)", getTitle(), keys.size()));
							if(keys.size() == 0){
								emptyLabel.setText(String.format(emptyText, mainWindow.setting.isTagsTranslate() ? TaskTagsPanel.tagscnMap.get("rows:" + currentGroup) : currentGroup));
								tagPane.add(emptyLabel);
								tagPane.updateUI();
								return;
							}
							currentPage = page;
							//分页处理
							if(keys.size() > pageSize){
								Set<String> pageKeys = new HashSet<String>();
								int index = (page - 1) * pageSize, i = 0;
								for( String key : keys ){
									i ++;
									if(i > index && i <= page * pageSize){
										if(i > keys.size()) break;
										pageKeys.add(key);
									}else{
										continue;
									}
								}
								pager.change(keys.size() % pageSize == 0 ? keys.size() / pageSize : keys.size() / pageSize + 1, currentPage);
								if(keys.size() > pageSize){
									pager.setVisible(true);
								}
								keys = pageKeys;
							}
							int i = 0;int ebtnlength = tagBtns != null ? tagBtns.length : 0;
							JButton b = null;
							for(String key : keys){
								i ++;
								if(i < ebtnlength){
									b = (JButton) tagBtns[i - 1];
									b.setText(!mainWindow.setting.isDebug() ? "" + i : String.format(istask ? "<html>%s(%s)</html>" : "<html>%s</html>", HtmlUtils.filterEmoji2SegoeUISymbolFont(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() && TaskTagsPanel.tagscnMap.containsKey(key) ? 
											TaskTagsPanel.tagscnMap.get(key) : key.replaceAll(currentGroup + ":", "")), allTaskCountMap.get(key)));
									b.setName(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.setToolTipText(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.setVisible(true);
								}else{
									b = new JButton(!mainWindow.setting.isDebug() ? "" + i : String.format(istask ? "<html>%s(%s)</html>" : "<html>%s</html>", HtmlUtils.filterEmoji2SegoeUISymbolFont(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() && TaskTagsPanel.tagscnMap.containsKey(key) ? 
											TaskTagsPanel.tagscnMap.get(key) : key.replaceAll(currentGroup + ":", "")), allTaskCountMap.get(key)));
									b.setFont(FontConst.Microsoft_BOLD_12);
									b.setMargin(new Insets(1, 1, 1, 1));
									//随机颜色
									int r = (int) (Math.random() * 4);
									b.setUI(btnUis[r]);
									b.setForeground(Color.WHITE);
									b.setName(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.setToolTipText(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.addMouseListener(btnMouseListener);
								}
								tagPane.add(b, i - 1);
								tagPane.updateUI();
							}
							if(tagBtns == null || tagBtns.length < i){
								tagBtns = tagPane.getComponents();
							}
						}else{
							this_.setTitle(String.format("%s(%s条记录)", getTitle(), 0));
							emptyLabel.setText(String.format(emptyText, mainWindow.setting.isTagsTranslate() ? TaskTagsPanel.tagscnMap.get("rows:" + currentGroup) : currentGroup));
							tagPane.add(emptyLabel);
							tagPane.updateUI();
							return;
						}
					}
				});
				worker.execute();
			}
		});
	}
	
	public MouseListener btnMouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			//左键
			if(e.getButton() == MouseEvent.BUTTON1){
				//this_.dispose();
				//searchComicWindow.doSearch("female:"+ tags.getProperty(key) + "$");
				if(mainWindow.searchComicWindow == null){
					mainWindow.searchComicWindow = new SearchComicWindow(mainWindow);
					if(mainWindow.searchComicWindow.searchDetailInfoWindow == null){
						mainWindow.searchComicWindow.searchDetailInfoWindow = new SearchDetailInfoWindow(mainWindow.searchComicWindow);
					}
				}
				mainWindow.searchComicWindow.searchDetailInfoWindow.taskTagsPanel.renderSelectTags(btn.getName(), true);
				mainWindow.searchComicWindow.searchDetailInfoWindow.taskTagsPanel.textPane.setCom(mainWindow.searchComicWindow.searchDetailInfoWindow);
				mainWindow.searchComicWindow.searchDetailInfoWindow.setVisible(true);
			}
			//右键
			else if(e.getButton() == MouseEvent.BUTTON3){
				/*if(popupMenu == null){
					JMenuItem deleteItem = new AJMenuItem("收藏", Color.BLACK, "",
						new MenuItemActonListener(null, new IMenuListenerTask() {
							public void doWork(Window window, ActionEvent e) {
								
							}
						}));
					popupMenu = new AJPopupMenu(deleteItem);
				}
				popupMenu.show(btn, e.getPoint().x, e.getPoint().y);*/
			}
		}
	};
	
}
