package org.arong.egdownloader.ui.list;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.JList;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.CursorManager;
import org.arong.egdownloader.ui.window.GroupWindow;
import org.arong.egdownloader.ui.window.InitWindow;
/**
 * 任务组列表
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupList extends JList {

	private static final long serialVersionUID = -7702879865264332528L;
	public GroupList(List<File> groups, final GroupWindow window){
		this.setModel(new GroupListModel(groups));
		//this.setCellRenderer(new GroupListCellReader());
		this.setCursor(CursorManager.getPointerCursor());
		this.setForeground(Color.BLUE);
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent me) {
				int clickNum = me.getClickCount();
				//双击
				if(2 == clickNum){
					GroupList list = (GroupList)me.getSource();
					String groupName = "";
					if(list.getSelectedIndex() != 0){
						groupName = list.getSelectedValue().toString();
					}
					ComponentConst.changeDataPath(groupName);
					ComponentConst.changeDataXmlPath();
					window.dispose();
					new InitWindow();
				}
			}
		});
	}
}
