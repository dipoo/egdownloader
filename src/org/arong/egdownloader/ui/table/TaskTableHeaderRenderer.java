package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.swing.AJLabel;

/**
 * 表头渲染器
 * @author 阿荣
 * @since 2014-05-24
 */
public class TaskTableHeaderRenderer implements TableCellRenderer {
	private Color color = (1 == ComponentConst.SKIN_NUM ? new Color(25, 12, 47) : new Color(0, 0, 0));
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel l = null;
		switch (column){
			case 0://数目
				l = new AJLabel(table.getRowCount() + "", null, color, JLabel.CENTER);
				l.setToolTipText("漫画总数");
				return l;
			case 1://名称
				l = new AJLabel(value.toString() + "", null, color, JLabel.LEFT);
				l.setCursor(CursorManager.getPointerCursor());
				l.setToolTipText("切换排序（名称/创建时间）");
				return l;
			case 2://图片数
				return new AJLabel(value.toString(), null, color, JLabel.LEFT);
			case 3://语言
				return new AJLabel(value.toString(), null, color, JLabel.LEFT);
			case 4://下载进度
				return new AJLabel(value.toString(), null, color, JLabel.CENTER);
			case 5://状态
				return new AJLabel(value.toString(), null, color, JLabel.CENTER);	
			default:
				return new AJLabel(value.toString(), color);
		}
	}

}
