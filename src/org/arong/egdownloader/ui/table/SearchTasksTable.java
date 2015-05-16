package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.egdownloader.ui.window.SearchCoverWindow;
import org.arong.egdownloader.ui.window.form.AddFormDialog;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
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
//		TaskTableCellRenderer renderer = new TaskTableCellRenderer();
//		renderer.setHorizontalAlignment(JLabel.CENTER);   
		this.setDefaultRenderer(Object.class, new TableCellRenderer() {
			private Color c = new Color(47,110,178);
			private Color uploaderColor = Color.getHSBColor(122, 255, 122);
			private Font font = new Font("微软雅黑", Font.PLAIN, 11);
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				
				if(isSelected){
					c = Color.BLUE;
				}else{
					c = Color.DARK_GRAY;
				}
				SearchTasksTable tb = (SearchTasksTable) table;
				TableColumn tc = tb.getColumnModel().getColumn(column);
				if(column == 0){//类型
					tc.setPreferredWidth(105);
					tc.setMaxWidth(105);
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
					tc.setPreferredWidth(700);
					tc.setMaxWidth(1800);
					JLabel l = new AJLabel(value.toString(), c, font, JLabel.LEFT);
					SearchTask task = tasks.get(row);
					if(task.getBtUrl() != null){
						try{
							l.setIcon(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + "t.png")));
						}catch (Exception e) {
							
						}
					}
					l.setToolTipText(value.toString());
					return l;
				}else if(column == 2){//上传者
					tc.setPreferredWidth(100);
					tc.setMaxWidth(150);
					JLabel l = new AJLabel(value.toString(), c, font, JLabel.LEFT);
					l.setForeground(uploaderColor);
					l.setToolTipText("点击搜索该上传者的上传的漫画");
					return l;
				}else if(column == 3){//发布时间
					tc.setPreferredWidth(100);
					tc.setMaxWidth(150);
					return new AJLabel(value.toString(), c, font, JLabel.LEFT);
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
						currentRowIndex = -1;
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
				int columnIndex = table.columnAtPoint(e.getPoint());
				//左键
				if(e.getButton() == MouseEvent.BUTTON1){
					//点击上传者
					if(columnIndex == 2){
						comicWindow.keyField.setText("uploader:" + tasks.get(rowIndex).getUploader());
						comicWindow.searchBtn.doClick();
					}
				}
				//右键
				else if(e.getButton() == MouseEvent.BUTTON3){
					//使之选中
					table.setRowSelectionInterval(rowIndex, rowIndex);
					if(table.popupMenu == null){
						JMenuItem downItem = new AJMenuItem("创建任务", Color.BLACK,
								ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("add"),
								new MenuItemActonListener(comicWindow.mainWindow, new IMenuListenerTask() {
									public void doWork(Window window, ActionEvent e) {
										EgDownloaderWindow this_ = (EgDownloaderWindow) window;
										this_.setEnabled(false);
										final SearchTask task = table.getTasks().get(table.getSelectedRow());
										if(this_.creatingWindow != null && this_.creatingWindow.isVisible()){
											this_.creatingWindow.setVisible(true);
											this_.creatingWindow.toFront();
										}else{
											if (this_.addFormWindow == null) {
												this_.addFormWindow = new AddFormDialog(this_);
											}
											((AddFormDialog)this_.addFormWindow).emptyField();
											((AddFormDialog)this_.addFormWindow).setUrl(task.getUrl());
											this_.addFormWindow.setVisible(true);
											this_.addFormWindow.toFront();
										}
									}
								}));
						JMenuItem openPageItem = new AJMenuItem("打开网页", Color.BLACK,
								ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("browse"),
								new MenuItemActonListener(comicWindow.mainWindow, new IMenuListenerTask() {
									public void doWork(Window window, ActionEvent e) {
										final SearchTask task = table.getTasks().get(table.getSelectedRow());
										openPage(task.getUrl());
									}
								}));
						JMenuItem openBtPageItem = new AJMenuItem("下载BT", Color.BLACK,
								ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("download"),
								new MenuItemActonListener(comicWindow.mainWindow, new IMenuListenerTask() {
									public void doWork(Window window, ActionEvent e) {
										final SearchTask task = table.getTasks().get(table.getSelectedRow());
										if(task.getBtUrl() != null){
											openPage(task.getBtUrl());
										}else{
											JOptionPane.showMessageDialog(comicWindow, "该漫画没有可以下载的bt文件");
										}
									}
								}));
						table.popupMenu = new AJPopupMenu(downItem, openPageItem, openBtPageItem);
					}
					table.popupMenu.show(table, e.getPoint().x, e.getPoint().y);
				}
			}
		});
	}
	
	public void openPage(String url){
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e1) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start " + url);
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(comicWindow, "不支持此功能");
			}
		} catch (URISyntaxException e1) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start " + url);
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(comicWindow, "不支持此功能");
			}
		}finally{
			//隐藏tablePopupMenu
			popupMenu.setVisible(false);
		}
	}

	public List<SearchTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<SearchTask> tasks) {
		this.tasks = tasks;
	}

}