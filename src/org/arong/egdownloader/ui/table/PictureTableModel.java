package org.arong.egdownloader.ui.table;

import java.util.ArrayList;
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
	
	private static List<Picture> emptyPictures = new ArrayList<Picture>();
	
	private List<Picture> pictures;
	
	public PictureTableModel(List<Picture> pictures){
		if(pictures == null){
			this.pictures = emptyPictures;
		}else{
			this.pictures = pictures;
		}
	}

	public int getColumnCount() {
		return ComponentConst.PICTURE_TABLE_HEADER.length;//6列
	}

	public String getColumnName(int column) {
		return ComponentConst.PICTURE_TABLE_HEADER[column];
	}

	public int getRowCount() {
		return pictures == null ? 0 : pictures.size();
	}
	private static final String COMPLETED_VALUE = "<html><font color='green'><b>已完成</b></font></html>";
	private static final String UNCOMPLETED_VALUE = "未下载";
	public Object getValueAt(int row, int column) {
		Picture pic = pictures.get(row);
		switch (column) {
		case 0:
			return pic.getNum();
		case 1:
			return pic.getName();
		case 2:
			if(!pic.isSaveAsName()){
				if(pic.getName().indexOf(".") != -1){
					return pic.getNum() + pic.getName().substring(pic.getName().lastIndexOf("."), pic.getName().length());
				}else{
					return pic.getNum() + ".jpg";
				}
			}else{
				return pic.getName();
			}
		case 3:
			return pic.getSize();
		case 4:
			return pic.getPpi();	
		case 5:
			return pic.isCompleted() ? COMPLETED_VALUE : UNCOMPLETED_VALUE;
		case 6:
			return pic.getUrl();
		case 7:
			return pic.getTime();
		case 8:
			return "";	
		default:
			return "";
		}
	}

	public Class<?> getColumnClass(int columnIndex) {
		return java.lang.String.class;
	}
	
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
}
