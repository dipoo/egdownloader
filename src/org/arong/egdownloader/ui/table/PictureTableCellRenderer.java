package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.util.FileUtil;
/**
 * 图片表格单元格渲染器
 * @author 阿荣
 * @since 2014-06-06
 */
public class PictureTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 5170655726223645364L;
	
	private Color fontColor;
	private Font font = new Font("微软雅黑", Font.PLAIN, 11);
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected){
			fontColor = Color.BLUE;
		}else{
			fontColor = Color.DARK_GRAY;
		}
		String val = value.toString();
		if(column == 0){//第一列：图标
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
			return new JLabel(value.toString(), JLabel.LEFT);
		}else if(column == 1){//第二列：名称
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(200);
			tc.setMaxWidth(250);
		}else if(column == 2){//第三列：大小
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
			val = FileUtil.showSizeStr(Integer.parseInt(val));
		}else if(column == 3){//第四列：状态
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
		}else if(column == 4){//第五列：地址
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(350);
			tc.setMaxWidth(400);
		}else if(column == 5){//第五列：时间
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(120);
			tc.setMaxWidth(150);
		}
		return new AJLabel(val, fontColor, font, JLabel.LEFT);
	}
	

}
