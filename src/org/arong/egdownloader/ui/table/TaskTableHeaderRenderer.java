package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.FontConst;
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
			TaskingTable t = (TaskingTable)table;
		switch (column){
			case 0://类别
				l = new AJLabel(t.getTasks().size() + "", "", color, JLabel.CENTER);
				l.setFont(FontConst.Georgia_BOLD_12);
				l.setToolTipText("漫画总数(按照阅读状态排序)");
				return l;
			case 1://名称
				l = new AJLabel(value.toString() + "", "", color, JLabel.LEFT);
				l.setToolTipText("切换排序（名称/创建时间）");
				return l;
			case 2://图片数
				l = new AJLabel(value.toString(), "", color, JLabel.LEFT);
				l.setToolTipText("按照漫画总数降序排序");
				return l;
			case 3://语言
				l =  new AJLabel(value.toString(), "", color, JLabel.LEFT);
				l.setToolTipText("按照漫画语言排序");
				return l;
			case 4://下载进度
				l =  new AJLabel(value.toString(), "", color, JLabel.CENTER);
				l.setToolTipText("按照漫画进度降序排序");
				return l;
			case 5://状态
				l = new AJLabel(value.toString(), "", color, JLabel.CENTER);
				l.setToolTipText("按照漫画下载状态排序");
				return l;
			default:
				return new AJLabel(value.toString(), color);
		}
	}

}
