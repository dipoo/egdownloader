package org.arong.egdownloader.ui.work;

import javax.swing.SwingWorker;
/**
 * 公共耗时任务类
 * @author Administrator
 *
 */
public class CommonSwingWorker extends SwingWorker<Void, Void> {
	Runnable action;
	public CommonSwingWorker(Runnable action) {
		this.action = action;
	}
	protected Void doInBackground() throws Exception {
		action.run();
		return null;
	}

}
