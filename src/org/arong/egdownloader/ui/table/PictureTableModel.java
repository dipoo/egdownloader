package org.arong.egdownloader.ui.table;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.ui.ComponentConst;
/**
 * 任务详细面板图片表格数据模型
 * @author 阿荣
 * @since 2014-06-05
 */
public class PictureTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -4476733049330297521L;
	
	private List<Picture> pictures;
	
	public PictureTableModel(List<Picture> pictures){
		this.pictures = pictures;
	}

	public int getColumnCount() {
		return 6;//6列
	}

	public String getColumnName(int column) {
		return ComponentConst.PICTURE_TABLE_HEADER[column];
	}

	public int getRowCount() {
		return pictures == null ? 0 : pictures.size();
	}

	public Object getValueAt(int row, int column) {
		switch (column) {
		case 0:
			return pictures.get(row).getNum();
		case 1:
			return pictures.get(row).getName();
		case 2:
			return pictures.get(row).getSize();
		case 3:
			return pictures.get(row).isCompleted() ? "完成" : "未下载";
		case 4:
			return pictures.get(row).getUrl();
		case 5:
			return pictures.get(row).getTime();
		default:
			return "";
		}
	}

	public Class<?> getColumnClass(int columnIndex) {
		return java.lang.String.class;
	}
}
