package org.arong.egdownloader.ui.panel;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;

public class InfoTabbedPane extends JTabbedPane {
	public InfoTabbedPane(final EgDownloaderWindow mainWindow) {
		this.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				InfoTabbedPane this_ = (InfoTabbedPane) e.getSource();
				int index = mainWindow.viewModel == 1 ? mainWindow.runningTable.selectRowIndex : mainWindow.taskImagePanel.selectIndex;
				if(mainWindow.tasks.size() > index){
					Task t = mainWindow.tasks.get(index);
					if(this_.getSelectedIndex() == 1){
						mainWindow.taskInfoPanel.parseTask(t, index);
					}else if(this_.getSelectedIndex() == 2){
						TaskTagsPanel p = (TaskTagsPanel) this_.getComponent(2);
						p.showTagGroup(t);
					}
					else if(this_.getSelectedIndex() == 3){
						PicturesInfoPanel p = (PicturesInfoPanel) this_.getComponent(3);
						p.showPictures(t);
					}
				}
			}
		});
	}
}
