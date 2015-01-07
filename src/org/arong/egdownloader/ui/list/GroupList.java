package org.arong.egdownloader.ui.list;

import java.io.File;
import java.util.List;

import javax.swing.JList;
/**
 * 任务组列表
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupList extends JList {

	private static final long serialVersionUID = -7702879865264332528L;
	
	public GroupList(List<File> groups){
		this.setModel(new GroupListModel(groups));
		this.setCellRenderer(new GroupListCellReader());
	}
}
