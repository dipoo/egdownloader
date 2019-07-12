package org.arong.egdownloader.ui.panel;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;

public class InfoTabbedPane extends JTabbedPane {
	public EgDownloaderWindow mainWindow;
	public InfoTabbedPane(final EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = mainWindow.runningTable.getSelectedRow();
				if(index >= 0){
					Task t = mainWindow.tasks.get(index);
					flushTab(t);
				}
			}
		});
	}
	public void flushTab(Task t){
		if(this.getSelectedComponent() instanceof TaskInfoPanel){
			mainWindow.taskInfoPanel.parseTask(t);
		}else if(this.getSelectedComponent() instanceof TaskTagsPanel){
			TaskTagsPanel p = (TaskTagsPanel) this.getSelectedComponent();
			p.showTagGroup(t);
		}
		else if(this.getSelectedComponent() instanceof PicturesInfoPanel){
			PicturesInfoPanel p = (PicturesInfoPanel) this.getSelectedComponent();
			p.showPictures(t);
		}
		if(mainWindow.infoTabbedPane.indexOfComponent(mainWindow.taskTagsPanel) != -1){
			mainWindow.infoTabbedPane.setTitleAt(mainWindow.infoTabbedPane.indexOfComponent(mainWindow.taskTagsPanel), "标签组");
		}
	}
}
