package org.arong.egdownloader.ui.list;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public GroupListModel(List<File> groups){
		this.groups = groups;
	}

	public Object getElementAt(int index) {
		String text;
		if(index == 0){
			text = sdf.format(new Date(groups.get(0).getParentFile().lastModified())) + "     |     " + "默认空间" ;
		}else{
			text = sdf.format(new Date(groups.get(index - 1).lastModified())) + "     |     " + groups.get(index - 1).getName();
		}
		return text;
	}

	public int getSize() {
		return groups.size() + 1;
	}
}
