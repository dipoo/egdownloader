package org.arong.egdownloader.ui.work;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 * 用于处理主要采集业务的线程类，避免Swing事件派送线程造成的线程等待
 * 
 * @author 阿荣
 * @since 2013-8-21
 * 
 */
public class CollectWork extends SwingWorker<Void, Void> {
//	private ISiteEngine engine;

//	private SiteInfo siteInfo;

	private JFrame mainWindow;
	
	public CollectWork() {
	}

	public CollectWork(JFrame mainWindow) {
		this.mainWindow = mainWindow;
	}

	/*public CollectWork(ISiteEngine engine, JFrame mainWindow) {
		this.engine = engine;
		if (engine != null) {
			this.siteInfo = engine.getSiteInfo();
		}
		this.mainWindow = mainWindow;
	}

	public void setEngine(ISiteEngine engine) {
		this.engine = engine;
	}*/

	protected Void doInBackground() throws Exception {
		collecting();
		return null;
	}

	// 采集业务主流程
	private void collecting() {
		
	}

	protected void done() {
		//任务完成后可以执行的代码
		try {
			this.finalize();
			if(this.isCancelled()){
				System.out.println("【已取消任务】");
				return;
			}
			System.out.println("【任务已结束】");
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		} finally{
		}
	}
}
