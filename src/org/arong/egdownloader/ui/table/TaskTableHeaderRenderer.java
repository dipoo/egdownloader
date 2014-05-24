package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;

/**
 * 表头渲染器
 * @author 阿荣
 * @since 2014-05-24
 */
public class TaskTableHeaderRenderer implements TableCellRenderer {
	private Color color = (1 == ComponentConst.SKIN_NUM ? new Color(156, 183, 124) : new Color(0, 0, 0));
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		switch (column){
			case 0: 
				return new JLabel(table.getRowCount() + "", JLabel.CENTER);
			case 2:	
				return new AJLabel(value.toString(), ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("picture"), color, JLabel.LEFT);
			case 3:
				return new AJLabel(value.toString(), ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("download"), color, JLabel.LEFT);
			case 4:
				return new AJLabel(value.toString(), ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("size"), color, JLabel.LEFT);
			default:
				return new AJLabel(value.toString(), color);
		}
	}

}
