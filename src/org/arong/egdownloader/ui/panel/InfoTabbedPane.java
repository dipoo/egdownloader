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
				int index = mainWindow.runningTable.getSelectedRow();
				if(mainWindow.tasks.size() > index){
					Task t = mainWindow.tasks.get(index);
					if(this_.getSelectedComponent() instanceof TaskInfoPanel){
						mainWindow.taskInfoPanel.parseTask(t, index);
					}else if(this_.getSelectedComponent() instanceof TaskTagsPanel){
						TaskTagsPanel p = (TaskTagsPanel) this_.getSelectedComponent();
						p.showTagGroup(t);
					}
					else if(this_.getSelectedComponent() instanceof PicturesInfoPanel){
						PicturesInfoPanel p = (PicturesInfoPanel) this_.getSelectedComponent();
						p.showPictures(t);
					}
				}
			}
		});
	}
}
