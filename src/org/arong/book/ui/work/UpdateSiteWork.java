package org.arong.book.ui.work;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.arong.book.caiji.WebClient;
import org.arong.book.caiji.WebClientException;

/**
 * 用于处理更新[书脊电子书]首页分类的线程类，避免Swing事件派送线程造成的线程等待
 * 
 * @author 阿荣
 * @since 2013-8-31
 * 
 */
public class UpdateSiteWork extends SwingWorker<Void, Void> {

	private JFrame mainWindow;
	
	public UpdateSiteWork() {
	}

	public UpdateSiteWork(JFrame mainWindow) {
		this.mainWindow = mainWindow;
	}

	protected Void doInBackground() throws Exception {
		updateSite();
		return null;
	}

	// 更新网站主流程
	private void updateSite() {
		try {
			WebClient.postRequest("http://www.shujixiazai.com/update", "utf-8", null);
			return;
		} catch (WebClientException e) {
			JOptionPane.showMessageDialog(mainWindow, e.getMessage());
			return;
		}
	}

	protected void done() {
		//任务完成后可以执行的代码
		try {
			this.finalize();
			if(this.isCancelled()){
				System.out.println("【已取消更新】");
				return;
			}
			JOptionPane.showMessageDialog(mainWindow, "更新完成");
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		} finally{
		}
	}
}
