package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
/**
 * 搜索结果表格
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchTasksTable extends JTable {

	private static final long serialVersionUID = 8917533573337061263L;
	private List<SearchTask> tasks;
	private EgDownloaderWindow mainWindow;
	
	public void changeModel(List<SearchTask> tasks){
		this.setMainWindow(mainWindow);
		this.tasks = tasks;
		TableModel tableModel = new SearchTaskTableModel(tasks);
		this.setModel(tableModel);//设置数据模型
	}
	
	public SearchTasksTable(int x, int y, int width, int height, final List<SearchTask> tasks, EgDownloaderWindow mainWindow){
		this.setMainWindow(mainWindow);
		this.tasks = (tasks == null ? new ArrayList<SearchTask>() : tasks);
		
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
			private Font font = new Font("微软雅黑", Font.PLAIN, 11);
			private Font blodFont = new Font("微软雅黑", Font.BOLD, 11);
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
					tc.setPreferredWidth(120);
					tc.setMaxWidth(150);
					JLabel l = new AJLabel("", tb.getTasks().get(row).getType() == null ? "" : (value.toString() + ".png"), c, JLabel.LEFT);
					l.setToolTipText(value.toString());
					l.setFont(blodFont);
					return l;
				}else if(column == 1){//名称
					tc.setPreferredWidth(800);
					tc.setMaxWidth(800);
					return new AJLabel(value.toString(), c, font, JLabel.LEFT);
				}else if(column == 2){//发布时间
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
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				SearchTasksTable table = (SearchTasksTable)e.getSource();
				//获取点击的行数
				int rowIndex = table.rowAtPoint(e.getPoint());
				//左键
				if(e.getButton() == MouseEvent.BUTTON3){
					//使之选中
					table.setRowSelectionInterval(rowIndex, rowIndex);
					table.updateUI();
					table.getMainWindow().tablePopupMenu.show(table, e.getPoint().x, e.getPoint().y);
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

	public EgDownloaderWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

}
