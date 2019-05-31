package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.util.FileUtil2;
/**
 * 图片表格单元格渲染器
 * @author 阿荣
 * @since 2014-06-06
 */
public class PictureTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 5170655726223645364L;
	
	private Color fontColor;
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, final int row, int column) {
//		final PictureTable pt = (PictureTable) table;
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
		}else if(column == 1){//名称
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(180);
			tc.setMaxWidth(250);
		}else if(column == 2){//存储名称
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(180);
			tc.setMaxWidth(250);
		}else if(column == 3){//大小
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
			val = FileUtil2.showSizeStr(Long.parseLong(val));
		}else if(column == 4){//分别率
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(60);
			tc.setMaxWidth(80);
		}else if(column == 5){//状态
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(50);
			tc.setMaxWidth(120);
		}else if(column == 6){//地址
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(320);
			tc.setMaxWidth(320);
		}else if(column == 7){//时间
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(120);
			tc.setMaxWidth(140);
		}else if(column == 8){//查看
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(40);
			tc.setMaxWidth(60);
			AJLabel aj = new AJLabel("", IconManager.getIcon("openpic"), fontColor, JLabel.LEFT);
			return aj;
		}
		if(isSelected){
			return new AJLabel(val, fontColor, FontConst.Songti_BOLD_12, JLabel.LEFT);
		}
		return new AJLabel(val, fontColor, FontConst.Songti_PLAIN_11, JLabel.LEFT);
	}
}
