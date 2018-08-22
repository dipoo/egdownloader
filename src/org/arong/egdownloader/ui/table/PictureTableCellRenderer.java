package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.arong.egdownloader.ui.FontConst;
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
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected){
			fontColor = Color.BLUE;
		}else{
			fontColor = Color.DARK_GRAY;
		}
		String val = value == null ? "" : value.toString();
		if(column == 0){//第一列：序号
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(40);
			tc.setMaxWidth(40);
			return new JLabel(value.toString(), JLabel.LEFT);
		}else if(column == 1){//第二列：名称
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(260);
			tc.setMaxWidth(300);
		}else if(column == 2){//第三列：大小
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(50);
			tc.setMaxWidth(60);
			val = FileUtil.showSizeStr(Long.parseLong(val));
		}else if(column == 3){//第四列：状态
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(50);
			tc.setMaxWidth(80);
		}else if(column == 4){//第五列：地址
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(320);
			tc.setMaxWidth(320);
		}else if(column == 5){//第五列：时间
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(180);
			tc.setMaxWidth(180);
		}
		return new AJLabel(val, fontColor, FontConst.Songti_PLAIN_11, JLabel.LEFT);
	}
}
