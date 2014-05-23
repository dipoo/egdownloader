package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;

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
		if (row % 2 == 0) {
            setBackground(Color.YELLOW); //设置奇数行底色
        } else if (row % 2 == 1) {
            setBackground(new Color(206, 231, 255)); //设置偶数行底色
        }
		if(column == 0){
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(30);
			tc.setMaxWidth(40);
			return new JLabel(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + "folder.gif")), JLabel.CENTER);
		}else if(column == 1){
			TableColumn tc = table.getColumnModel().getColumn(column);
			tc.setPreferredWidth(250);
			tc.setMaxWidth(300);
			return new AJLabel(value.toString(),Color.BLUE, JLabel.LEFT);
		}else{
			return new AJLabel(value.toString(),Color.GRAY, JLabel.CENTER);
		}
	}

}
