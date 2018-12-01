package org.arong.egdownloader.ui.panel;

import java.awt.Color;

import javax.swing.JScrollPane;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.PictureTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;

public class PicturesInfoPanel extends JScrollPane {
	
	private EgDownloaderWindow mainWindow;
	public PictureTable pictureTable;
	
	public PicturesInfoPanel(final EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.setAutoscrolls(true);
		this.setBorder(null);
		this.getViewport().setBackground(new Color(254,254,254));
		this.setBounds(5, 5, mainWindow.getWidth() - 20, 200);
	}
	
	public void showPictures(Task t){
		if(pictureTable == null){
			pictureTable = new PictureTable(5, 5, mainWindow.getWidth() - 20, 180, t, mainWindow);
			this.setViewportView(pictureTable);
		}else{
			pictureTable.changeModel(t);
		}
	}
	
}
