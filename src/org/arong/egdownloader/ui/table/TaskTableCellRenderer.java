package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJLabel;

/**
 * 单元格渲染器
 * @author 阿荣
 * @since 2014-05-23
 */
public class TaskTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3319745427208195393L;
	
	private Color fontColor;
	private Color unstartedColor;
	private Color startedColor;
	private Color stopedColor;
	private Color completedColor;
	private Font font = new Font("微软雅黑", Font.PLAIN, 11);

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected){
			fontColor = Color.BLUE;
		}else{
			fontColor = Color.DARK_GRAY;
		}
		
		if(column == 0){//第一列：图标
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(30);
			tc.setMaxWidth(40);
			return new JLabel(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("folder"))), JLabel.CENTER);
		}else if(column == 1){//第二列：名称
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(400);
			tc.setMaxWidth(450);
			if(value != null && value.toString().length() > 55){
				return new AJLabel(value.toString().substring(0, 55) + " ......", fontColor, font, JLabel.LEFT);
			}
		}else if(column == 2){//第三列：图片总数
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
			//return new AJLabel(value.toString(), ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("picture"), fontColor, JLabel.LEFT);
		}else if(column == 3){//第四列：进度
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(70);
			tc.setMaxWidth(80);
			return new AJLabel(value.toString() + "(" + getSchedule(value, table.getModel().getValueAt(row, 2)) + ")", fontColor, font, JLabel.LEFT);
		}/*else if(column == 4){//第五列：大小
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
			//return new AJLabel(value.toString(), ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("size"), fontColor, JLabel.LEFT);
		}*/else if(column == 4){//第六列：状态
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(table.getRowCount() > ComponentConst.MAX_TASK_PAGE ?  40 : 62);
			tc.setMaxWidth(80);
			if(value.toString().equals(TaskStatus.UNSTARTED.getStatus())){
				if(unstartedColor == null) unstartedColor = new Color(95,57,45);
				return new AJLabel(value.toString(), unstartedColor, font, JLabel.LEFT);
			}
			if(value.toString().equals(TaskStatus.STARTED.getStatus())){
				if(startedColor == null) startedColor = new Color(65,146,225);
				return new AJLabel(value.toString(), startedColor, font, JLabel.LEFT);
			}
			if(value.toString().equals(TaskStatus.STOPED.getStatus())){
				if(stopedColor == null) stopedColor = new Color(0,1,89);
				return new AJLabel(value.toString(), stopedColor, font, JLabel.LEFT);
			}
			if(value.toString().equals(TaskStatus.COMPLETED.getStatus())){
				if(completedColor == null) completedColor = new Color(65,145,65);
				return new AJLabel(value.toString(), completedColor, font, JLabel.LEFT);
			}
		}
		return new AJLabel(value.toString(), fontColor, font, JLabel.LEFT);
	}
	
	private String getSchedule(Object current, Object total){
		Double d = Double.parseDouble(current.toString()) / Double.parseDouble(total.toString()) * 100;
		return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}
}
