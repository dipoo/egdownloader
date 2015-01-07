package org.arong.egdownloader.ui.list;

import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
/**
 * 任务组列表模型
 * @author dipoo
 * @since 2015-01-07
 */
public class GroupListModel extends DefaultListModel {

	private static final long serialVersionUID = 7983446371650239938L;
	
	public List<File> groups;
	
	public GroupListModel(List<File> groups){
		this.groups = groups;
	}

	public Object getElementAt(int index) {
		String text;
		if(index == 0){
			text = "默认空间";
		}else{
			text = groups.get(index - 1).getName();
		}
		return text;
	}

	public int getSize() {
		return groups.size() + 1;
	}
}
