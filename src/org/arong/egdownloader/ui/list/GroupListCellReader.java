package org.arong.egdownloader.ui.list;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJLabel;
/**
 * 任务组列表渲染器
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupListCellReader extends DefaultListCellRenderer {

	private static final long serialVersionUID = 4933882053356977723L;

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {  
            setBackground(Color.BLACK);  
            setForeground(Color.WHITE);  
        } else {  
            // 设置选取与取消选取的前景与背景颜色.  
            setBackground(Color.BLUE);  
            setForeground(Color.DARK_GRAY);  
        }
		return new AJLabel(value.toString(), IconManager.getIcon("folder"), value.toString().equals(ComponentConst.groupName) ? Color.RED : Color.BLUE, JLabel.LEFT);
	}
	
	
}
