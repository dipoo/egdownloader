package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;

/**
 * 单元格渲染器
 * @author 阿荣
 * @since 2014-05-23
 */
public class TaskTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3319745427208195393L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected){
			this.setForeground(new Color(230,230,230));
		}
		
		if(column == 0){//第一列：图标
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(30);
			tc.setMaxWidth(40);
			return new JLabel(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("folder"))), JLabel.CENTER);
		}else if(column == 1){//第二列：名称
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(200);
			tc.setMaxWidth(250);
			return new AJLabel(value.toString(), Color.BLUE, JLabel.LEFT);
		}else if(column == 2){//第三列：图片总数
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(100);
			tc.setMaxWidth(120);
			return new AJLabel(value.toString() + " P", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("picture"), Color.DARK_GRAY, JLabel.LEFT);
		}else if(column == 3){//第四列：进度
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(100);
			tc.setMaxWidth(150);
			return new AJLabel(value.toString() + "(" + getSchedule(value, table.getModel().getValueAt(row, 2)) + ")", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("download"), Color.DARK_GRAY, JLabel.LEFT);
		}else if(column == 4){//第五列：大小
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(100);
			tc.setMaxWidth(120);
			return new AJLabel(value.toString(), ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("size"), Color.DARK_GRAY, JLabel.LEFT);
		}else if(column == 5){//第六列：状态
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(table.getRowCount() > ComponentConst.MAX_TASK_PAGE ?  60 : 82);
			tc.setMaxWidth(80);
			return new AJLabel(value.toString(), Color.GRAY, JLabel.LEFT);
		}else{
			return null;
		}
	}
	
	private String getSchedule(Object current, Object total){
		Double d = Double.parseDouble(current.toString()) / Double.parseDouble(total.toString()) * 100;
		return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}
}
