package org.arong.egdownloader.ui.work;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.arong.egdownloader.ini.Configuration;
import org.arong.egdownloader.ini.IniException;
import org.arong.egdownloader.ini.SiteInfo;

/**
 * 用于处理主要采集业务的线程类，避免Swing事件派送线程造成的线程等待
 * 
 * @author 阿荣
 * @since 2013-8-21
 * 
 */
public class CollectWork extends SwingWorker<Void, Void> {
//	private ISiteEngine engine;

	private SiteInfo siteInfo;

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
		for (int i = siteInfo.getStartPage(); i <= siteInfo.getEndPage(); i++) {
			try {
//				engine.setHtmlSource(i);
//				engine.baseHandle(siteInfo.getIntoDao());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainWindow, e.getMessage(),
						"程序运行错误", JOptionPane.ERROR_MESSAGE);
				if(siteInfo.getIntoDao()){
					//保存页数
					try {
						//设置结束页码为当前采集的页码
						siteInfo.setEndPage(i);
						Configuration.store(siteInfo);
					} catch (IniException e1) {
						JOptionPane.showMessageDialog(mainWindow, e1.getMessage(),
								e1.getMessage(), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				return;
			}
			if (i < siteInfo.getEndPage()) {
				System.out.println("---------------------"
						+ siteInfo.getSiteName() + "["
						+ siteInfo.getTypeName()[siteInfo.getTypeIndex()]
						+ "]:第" + i + "页处理完毕---------------------");
			}
			if(i == siteInfo.getEndPage()){
				if(siteInfo.getIntoDao()){
					//保存页数
					try {
						Configuration.store(siteInfo);
					} catch (IniException e) {
						JOptionPane.showMessageDialog(mainWindow, e.getMessage(),
								e.getMessage(), JOptionPane.ERROR_MESSAGE);
						return;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainWindow, e.getMessage(),
								e.getMessage(), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		}
		System.out.println("---------------------" + siteInfo.getSiteName()
				+ "[" + siteInfo.getTypeName()[siteInfo.getTypeIndex()] + "]"
				+ siteInfo.getStartPage() + "到" + siteInfo.getEndPage()
				+ "页处理完毕---------------------");
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
