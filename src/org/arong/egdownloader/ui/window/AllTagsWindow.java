package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ButtonUI;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJPager;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.HtmlUtils;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * 显示所有标签窗口
 * @author dipoo
 * @date 2019-05-29
 */
public class AllTagsWindow extends JDialog {
	public EgDownloaderWindow mainWindow;
	public JPanel typeBtnPanel;
	public JPopupMenu popupMenu;//右键菜单
	public JSeparator separator;
	public JPanel tagPane;
	public JScrollPane scrollPane;
	//public AJTextPane tagPane;
	
	public String title = "选择标签";
	public CommonSwingWorker worker = null;
	public AJButton[] tagBtns;
	public String currentGroup;
	public int currentPage;
	public int pageSize = 300;
	public AJPager pager;
	public Map<String, Set<String>> allKeys;
	public void updateTagScrollPane(){
		updateTagScrollPane(1);
	}
	public void updateTagScrollPane(final int page){
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
						
						if(allKeys == null){
							allKeys = new HashMap<String, Set<String>>();
						}
						
						if(! allKeys.containsKey(currentGroup)){
							if(TaskTagsPanel.tagscnMap != null){
								String[] arr;
								for(String key : TaskTagsPanel.tagscnMap.keySet()){
									arr = key.split(":");
									if(arr.length == 1){
										arr = (TaskTagsPanel.MISC + ":" + key).split(":");
										arr = key.split(":");
									}
									if(!allKeys.containsKey(arr[0])){
										allKeys.put(arr[0], new HashSet<String>());
									}
									allKeys.get(arr[0]).add(arr[0] + ":" + arr[1]);
								}
							}
						}
						
						Set<String> keys = null;
						if(TaskTagsPanel.tagscnMap != null){
							keys = allKeys.get(currentGroup);
						}
						if(keys != null){
							currentPage = page;
							this_.setTitle(String.format("%s(%s条记录)", title, keys.size()));
							//分页处理
							if(keys.size() > pageSize){
								HashSet<String> pageKeys_ = new HashSet<String>();
								int i = (page - 1) * pageSize;
								for( String key : keys ){
									if(i < page * pageSize && i < keys.size()){
										pageKeys_.add(key);
										i ++;
									}else{
										break;
									}
								}
								pager.change(keys.size() % pageSize == 0 ? keys.size() / pageSize : keys.size() / pageSize + 1, currentPage);
								if(keys.size() > pageSize){
									pager.setVisible(true);
								}
								keys = pageKeys_;
							}
							int i = 0;int ebtnlength = tagBtns != null ? tagBtns.length : 0;
							//StringBuilder sb = new StringBuilder("<div style='font-family:微软雅黑;font-size:10px;padding:5px 10px;'>");
							for(final String key : keys){
								i ++;
								/*sb.append(String.format("<a style='text-decoration:none;margin-bottom:10px;' href='clickTag|%s:\"%s\"'>[%s]</a>&nbsp;&nbsp;", group, key, HtmlUtils.filterEmoji2SegoeUISymbolFont(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() ? 
										TaskTagsPanel.tagscnMap.get(key) : key.replaceAll(group + ":", ""))));*/
								if(i < ebtnlength){
									JButton b = tagBtns[i - 1];
									b.setText(mainWindow.setting.isDebug() ? "" + i : String.format("<html>%s</html>", HtmlUtils.filterEmoji2SegoeUISymbolFont(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() ? 
											TaskTagsPanel.tagscnMap.get(key) : key.replaceAll(currentGroup + ":", ""))));
									b.setName(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.setToolTipText(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.setVisible(true);
								}else{
									JButton b = new JButton(mainWindow.setting.isDebug() ? "" + i : String.format("<html>%s</html>", HtmlUtils.filterEmoji2SegoeUISymbolFont(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() ? 
											TaskTagsPanel.tagscnMap.get(key) : key.replaceAll(currentGroup + ":", ""))));
									b.setFont(FontConst.Microsoft_BOLD_12);
									b.setMargin(new Insets(1, 1, 1, 1));
									//随机颜色
									int r = (int) (Math.random() * 4);
									b.setUI(btnUis[r]);
									b.setForeground(Color.WHITE);
									b.setName(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.setToolTipText(String.format("%s$\"", key.replaceAll(":", ":\"")).replaceAll(TaskTagsPanel.MISC + ":", ""));
									b.addMouseListener(btnMouseListener);
									tagPane.add(b);
									if(i % 5 == 0) tagPane.updateUI();
								}
							}
							/*sb.append("</div>");
							tagPane.setText(sb.toString());*/
							tagPane.updateUI();
							if(tagBtns == null || tagBtns.length < i){
								tagBtns = (AJButton[]) tagPane.getComponents();
							}
						}
					}
				});
				worker.execute();
			}
		});
	}
	
	public AllTagsWindow(final EgDownloaderWindow egDownloaderWindow){
		
		this.mainWindow = egDownloaderWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setTitle(title);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		typeBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		typeBtnPanel.setBounds(5, 5, ComponentConst.CLIENT_WIDTH - 10, 40);
		ActionListener typeBtnActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AJButton btn = (AJButton) e.getSource();
				for(Component com : typeBtnPanel.getComponents()){
					if(com instanceof JButton){
						((JButton)com).setUI(AJButton.greenBtnUi);
					}
				}
				btn.setUI(AJButton.redBtnUi);
				String row = btn.getName();
				currentGroup = row;
				updateTagScrollPane();
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
		separator.setBounds(0, 45, this.getWidth(), 1);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 50, this.getWidth() - 10, this.getHeight() - 140);
		scrollPane.setBorder(null);
		
		tagPane = new JPanel();
		tagPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		tagPane.setBounds(0, 5, this.getWidth() - 30, this.getHeight() - 160);
		tagPane.setPreferredSize(new Dimension(this.getWidth() - 30,  this.getHeight() - 160));
		scrollPane.setViewportView(tagPane);
		
		pager = new AJPager(10, this.getHeight() - 80, this.getWidth() - 30, 40, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				updateTagScrollPane(Integer.parseInt(btn.getName()));
			}
		});
		pager.setVisible(false);
		
		ComponentUtil.addComponents(getContentPane(), typeBtnPanel, separator, scrollPane, pager);
		setVisible(true);
		
		((JButton)(typeBtnPanel.getComponent(6))).setUI(AJButton.redBtnUi);
		currentGroup = ComponentConst.TAGS_CN_FILENAMES[6].replaceAll(".md", "");
		updateTagScrollPane();
		
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
