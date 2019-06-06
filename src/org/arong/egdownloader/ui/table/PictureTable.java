package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.popmenu.PicturesPopMenu;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchCoverWindow;
/**
 * 任务详细面板图片表格类
 * @author 阿荣
 * @since 2014-06-05
 */
public class PictureTable extends JTable {

	private static final long serialVersionUID = 8864835740722285837L;
	
	private Task task;
	public EgDownloaderWindow mainWindow;
	public PicturesPopMenu popupMenu;
	public int showPictureTimes = 0;
	
	public void changeModel(Task task){
		this.task = task;
		this.scrollRectToVisible(new Rectangle(0, 0));
		this.setModel(new PictureTableModel(task.getPictures()));
	}
	public PictureTable(int x, int y, int width, int height, Task task, EgDownloaderWindow mainWindow){
		this.setTask(task);
		this.mainWindow = mainWindow;
		popupMenu = new PicturesPopMenu(mainWindow, this);
		
		this.setBounds(x, y, width, height);
		this.setCursor(CursorManager.getPointerCursor());//光标变手型
		this.getTableHeader().setReorderingAllowed(false);//不可移动列
		this.setBackground(Color.WHITE);
		//this.setCellSelectionEnabled(false);//选择单元格
		this.getTableHeader().setDefaultRenderer(new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				return new AJLabel(value.toString(), null);
			}
		});
		this.setBorder(null);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//this.getTableHeader().setSize(this.getTableHeader().getWidth(), 60);
		
		this.setModel(new PictureTableModel(task.getPictures()));
		this.setDefaultRenderer(Object.class, new PictureTableCellRenderer());
		this.addMouseMotionListener(new MouseAdapter() {
			int moveCurrentRow = -1;
			public void mouseMoved(MouseEvent e) {
				PictureTable table = (PictureTable)e.getSource();
				int rowIndex = table.rowAtPoint(e.getPoint());
				int columnIndex = table.columnAtPoint(e.getPoint());
				//最后一列
				if(columnIndex == ComponentConst.PICTURE_TABLE_HEADER.length - 1){
					if(moveCurrentRow != rowIndex){
						moveCurrentRow = rowIndex;
						Task task = table.getTask();
						//切换行
						if(table.mainWindow.coverWindow == null){
							table.mainWindow.coverWindow = new SearchCoverWindow(table.mainWindow);
						}
						showPictureTimes ++;
						table.mainWindow.coverWindow.showCover(task, task.getPictures().get(rowIndex), new Point(e.getXOnScreen() + 50, e.getYOnScreen()));
					}
				}else{
					moveCurrentRow = -1;
					if(table.mainWindow.coverWindow != null){
						table.mainWindow.coverWindow.setVisible(false);
						table.mainWindow.coverWindow.releaseCover();
						if(showPictureTimes % 3 == 0){
							System.gc();
						}
					}
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				PictureTable table = (PictureTable)e.getSource();
				int rowIndex = table.rowAtPoint(e.getPoint());
				/*StringSelection ss = new StringSelection(pic.getUrl());
				table.getToolkit().getSystemClipboard().setContents(ss, ss);*/
				if(e.getButton() == MouseEvent.BUTTON3){
					//使之选中
					table.setRowSelectionInterval(rowIndex, rowIndex);
					Task task = table.getTask();
					if(StringUtils.isNotBlank(task.getPictures().get(rowIndex).getOldurl())){
						popupMenu.oldUrlItem.setVisible(true);
					}else{
						popupMenu.oldUrlItem.setVisible(false);
					}
					popupMenu.show(table, e.getPoint().x, e.getPoint().y);
				}
			}
		});
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
