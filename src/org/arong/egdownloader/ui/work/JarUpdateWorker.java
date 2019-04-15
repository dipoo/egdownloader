package org.arong.egdownloader.ui.work;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.version.Version;
import org.arong.util.FileUtil2;
/**
 * Jar包更新任务线程类
 * @author dipoo
 * @since 2019-03-22
 */
public class JarUpdateWorker extends SwingWorker<Void, Void>{
	
	private EgDownloaderWindow mainWindow;
	private Map<String, String> version;
	private String binPath;
	public JarUpdateWorker(EgDownloaderWindow mainWindow, Map<String, String> version, String binPath){
		this.mainWindow = mainWindow;
		this.version = version;
		this.binPath = binPath;
	}
	
	protected Void doInBackground() throws Exception {
		String jarName = "egdownloader.jar";
		InputStream is;
		System.out.println("开始下载jar文件...");
		try {
			//检测是否为支持jar更新的类型
			
			File oldjar = new File(binPath + File.separator + "jre" + File.separator
					+ "lib" + File.separator + "ext" + File.separator + jarName);
			if(!oldjar.exists()){
				JOptionPane.showMessageDialog(null, "当前模式不支持jar文件更新");
				return null;
			}
			
			String url = version.get("jarUrl");
			Object[] o = WebClient.getStreamAndLengthUseJavaWithCookie(url, null);
			is = (InputStream) o[0];
			int totalLength = (Integer) o[1];
			System.out.println("jar文件大小：" + FileUtil2.showSizeStr((long)totalLength));
			if(is == null){
				JOptionPane.showMessageDialog(null, "jar文件更新失败");
			}else{
				//备份
				String bakPath = binPath + File.separator + "bak" + File.separator + Version.JARVERSION + File.separator;
				FileUtil2.ifNotExistsThenCreate(bakPath);
				org.arong.utils.FileUtil.copyFile(oldjar.getPath(), bakPath + jarName);
				//保存
				int fsize = FileUtil2.storeStream(oldjar.getParent(), jarName, is);
				if(fsize != totalLength){
					JOptionPane.showMessageDialog(null, "更新失败，jar文件下载不完整(" + FileUtil2.showSizeStr((long)fsize) + ")，请重试");
				}else{
					JOptionPane.showMessageDialog(null, "jar文件更新成功，重启后生效。");
				}
			}
		}catch(Exception e1) {
			JOptionPane.showMessageDialog(null, "jar文件更新失败，" + e1.getMessage());
			e1.printStackTrace();
		}
		
		return null;
	}
	 
}
