package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.egdownloader.ui.popmenu.SearchWindowPopMenu;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SearchCoverWindow;
import org.arong.egdownloader.ui.window.SearchDetailInfoWindow;
import org.arong.util.HtmlUtils;
/**
 * 搜索结果表格
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchTasksTable extends JTable {

	private static final long serialVersionUID = 8917533573337061263L;
	private List<SearchTask> tasks;
	public SearchComicWindow comicWindow;
	public JPopupMenu popupMenu;//右键菜单
	public int currentRowIndex = -1;
	
	public void changeModel(List<SearchTask> tasks){
		this.tasks = tasks;
		TableModel tableModel = new SearchTaskTableModel(this.tasks);
		this.setModel(tableModel);//设置数据模型
	}
	
	public SearchTasksTable(int x, int y, int width, int height, final List<SearchTask> tasks_, SearchComicWindow comicWindow_){
		this.comicWindow = comicWindow_;
		this.tasks = (tasks_ == null ? new ArrayList<SearchTask>() : tasks_);
		
		if(this.tasks.size() > ComponentConst.MAX_TASK_PAGE){
			height = ComponentConst.MAX_TASK_PAGE * 25;
		}
		this.setBounds(x, y, width, height);
//		this.setShowGrid(true);//显示单元格边框
//		this.setCellSelectionEnabled(false);//选择单元格
		this.setCursor(CursorManager.getPointerCursor());//光标变手型
		this.getTableHeader().setReorderingAllowed(false);//不可移动列
		this.setBackground(Color.WHITE);
//		this.setOpaque(false);//设为透明
		TableModel tableModel = new SearchTaskTableModel(this.tasks);
		this.setModel(tableModel);//设置数据模型
		this.setRowHeight(21);
//		TaskTableCellRenderer renderer = new TaskTableCellRenderer();
//		renderer.setHorizontalAlignment(JLabel.CENTER);   
		this.setDefaultRenderer(Object.class, new TableCellRenderer() {
			private Color c = new Color(47,110,178);
			private Color uploaderColor = Color.getHSBColor(122, 255, 122);
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				
				if(isSelected){
					c = Color.BLUE;
				}else{
					c = Color.DARK_GRAY;
				}
				
				if(value == null) return null;
				
				SearchTasksTable tb = (SearchTasksTable) table;
				TableColumn tc = tb.getColumnModel().getColumn(column);
				if(column == 0){//类型
					tc.setPreferredWidth(105);
					tc.setMaxWidth(150);
					
					JLabel l = new AJLabel("", c, JLabel.LEFT);
					if(tasks.get(row).getType() != null){
						ImageIcon icon = IconManager.getIcon(tasks.get(row).getType().toLowerCase());
						if(icon != null){
							l.setIcon(icon);
						}else{
							l.setText(tasks.get(row).getType());
						}
					}
					return l;
				}else if(column == 1){//名称
					SearchTask task = tasks.get(row);
					//是否已创建该任务
					boolean contains = comicWindow.mainWindow.tasks.getTaskUrlMap().containsKey(task.getUrl().replaceAll("https://", "http://")) || comicWindow.mainWindow.tasks.getTaskUrlMap().containsKey(task.getUrl().substring(0, task.getUrl().length() - 1).replaceAll("https://", "http://"));
					
					//当选择语言选择全部时，标题上显示语言
					StringBuilder lang = null;
					if(comicWindow.language.getSelectedIndex() == 0){
						if(StringUtils.isNotBlank(task.getTags()) && task.getTags().contains("language:")){
							for(String tag : task.getTags().split(";")){
								if(tag.contains("language:") && ! tag.contains("translated")){
									if(lang == null) lang = new StringBuilder();
									if(TaskTagsPanel.tagscnMap != null && comicWindow.mainWindow.setting.isTagsTranslate()){
										lang.append(lang.length() > 0 ? "," : "").append(TaskTagsPanel.tagscnMap.get(tag));
									}else{
										lang.append(lang.length() > 0 ? "," : "").append(tag.replace("language:", ""));
									}
								}
							}
						}
					}
					
					tc.setPreferredWidth(700);
					tc.setMaxWidth(1800);
					JLabel l = new AJLabel(String.format("<html>%s%s%s%s%s</html>", 
							lang != null ? HtmlUtils.colorHtml(String.format("[%s]", lang.toString()), "#0719f1") : "", 
									(comicWindow.checkNewVersion(task) ? HtmlUtils.redColorHtml("[新版本]") : ""), 
									(contains ? HtmlUtils.redColorHtml("[已存在]") : ""), 
									(task.isFavAuthorOrGroup(comicWindow.mainWindow.setting.getFavTags()) ? "[<font color=red>★</font>]" : ""), 
									value.toString()), c, isSelected ? FontConst.Microsoft_BOLD_11 : FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
					if(task.getBtUrl() != null){
						try{
							l.setIcon(IconManager.getIcon("t"));
						}catch (Exception e) {
							
						}
					}
					l.setToolTipText(value.toString());
					return l;
				}else if(column == 2){//图片个数
					tc.setPreferredWidth(60);
					tc.setMaxWidth(80);
					return new AJLabel("   " + value.toString(), c, FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
				}else if(column == 3){//评分
					tc.setPreferredWidth(80);
					tc.setMaxWidth(120);
					return new AJLabel(String.format("<html>&nbsp;&nbsp;&nbsp;&nbsp;%s★</html>", value.toString()), c, FontConst.Microsoft_PLAIN_11, JLabel.CENTER);
				}else if(column == 4){//上传者
					tc.setPreferredWidth(100);
					tc.setMaxWidth(150);
					JLabel l = new AJLabel(value.toString(), c, FontConst.Microsoft_PLAIN_11, JLabel.CENTER);
					l.setForeground(uploaderColor);
					l.setToolTipText("点击搜索该上传者的上传的漫画");
					return l;
				}else if(column == 5){//发布时间
					tc.setPreferredWidth(100);
					tc.setMaxWidth(150);
					return new AJLabel(value.toString(), c, FontConst.Microsoft_PLAIN_11, JLabel.CENTER);
				}else{
					return null;
				}
			}
		});//设置渲染器
//		this.getTableHeader().setDefaultRenderer(new TaskTableHeaderRenderer());
		//单元格监听
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e){
				final SearchTasksTable table = comicWindow.searchTable;
				int rowIndex = table.rowAtPoint(e.getPoint());
				int columnIndex = table.columnAtPoint(e.getPoint());
				if(columnIndex == 0){
					SearchTask task = table.getTasks().get(rowIndex);
					//切换行
					if(rowIndex != currentRowIndex){
						currentRowIndex = rowIndex;
						if(comicWindow.coverWindow == null){
							comicWindow.coverWindow = new SearchCoverWindow(comicWindow);
						}
						comicWindow.coverWindow.showCover(task, new Point(e.getXOnScreen() + 50, e.getYOnScreen()));
					}
				}else{
					if(comicWindow.coverWindow != null){
						comicWindow.coverWindow.setVisible(false);
						if(columnIndex != 1){
							currentRowIndex = -1;
						}
					}
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseExited(MouseEvent e) {
				if(comicWindow.coverWindow != null){
					comicWindow.coverWindow.setVisible(false);
					currentRowIndex = -1;
				}
			}
			public void mouseClicked(MouseEvent e) {
				final SearchTasksTable table = (SearchTasksTable)e.getSource();
				//获取点击的行数
				int rowIndex = table.rowAtPoint(e.getPoint());
				comicWindow.selectTaskIndex = rowIndex;
				int columnIndex = table.columnAtPoint(e.getPoint());
				//左键
				if(e.getButton() == MouseEvent.BUTTON1){
					//点击上传者
					if(columnIndex == 4){
						comicWindow.doSearch(String.format("uploader:\"%s\"", tasks.get(rowIndex).getUploader()));
					}
					if(columnIndex == 1){
						SearchTask task = table.getTasks().get(rowIndex);
						//切换行
						if(rowIndex != currentRowIndex){
							currentRowIndex = rowIndex;
							if(comicWindow.searchDetailInfoWindow == null){
								comicWindow.searchDetailInfoWindow = new SearchDetailInfoWindow(comicWindow);
							}
							int x = 0, y = 0;
							if(Toolkit.getDefaultToolkit().getScreenSize().width - e.getXOnScreen() < (comicWindow.searchDetailInfoWindow.getWidth())){
								x = e.getXOnScreen() - comicWindow.searchDetailInfoWindow.getWidth();
							}else{
								x = e.getXOnScreen();
							}
							if(Toolkit.getDefaultToolkit().getScreenSize().height - e.getYOnScreen() < (comicWindow.searchDetailInfoWindow.getHeight())){
								y = e.getYOnScreen() - comicWindow.searchDetailInfoWindow.getHeight();
							}else{
								y = e.getYOnScreen();
							}
							comicWindow.searchDetailInfoWindow.showDetail(task, new Point(x, y));
						}
					}
				}
				//右键
				else if(e.getButton() == MouseEvent.BUTTON3){
					//使之选中
					table.setRowSelectionInterval(rowIndex, rowIndex);
					if(comicWindow.popMenu == null){
						comicWindow.popMenu = new SearchWindowPopMenu(comicWindow.mainWindow);
					}
					SearchTask task = comicWindow.searchTasks.get(comicWindow.selectTaskIndex);
					boolean contains = comicWindow.mainWindow.tasks.getTaskUrlMap().containsKey(task.getUrl().replaceAll("https://", "http://")) || comicWindow.mainWindow.tasks.getTaskUrlMap().containsKey(task.getUrl().substring(0, task.getUrl().length() - 1).replaceAll("https://", "http://"));
					if(contains){
						comicWindow.popMenu.openPictureItem.setVisible(true);
						comicWindow.popMenu.downItem.setVisible(false);
						comicWindow.popMenu.showMergeItem.setVisible(false);
					}else{
						comicWindow.popMenu.openPictureItem.setVisible(false);
						comicWindow.popMenu.downItem.setVisible(true);
						comicWindow.popMenu.showMergeItem.setVisible(comicWindow.checkNewVersion(task) ? true : false);
					}
					if(StringUtils.isNotBlank(task.getBtUrl())){
						comicWindow.popMenu.openBtPageItem.setVisible(true);
					}else{
						comicWindow.popMenu.openBtPageItem.setVisible(false);
					}
					if(comicWindow.searchDetailInfoWindow != null){
						comicWindow.searchDetailInfoWindow.setVisible(false);
					}
					comicWindow.popMenu.show(table, e.getPoint().x, e.getPoint().y);
				}
			}
		});
	}
	
	public List<SearchTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<SearchTask> tasks) {
		this.tasks = tasks;
	}

}