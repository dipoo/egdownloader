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
		String tmpjarName = "egdownloader.jar_tmp";
		InputStream is = null;
		System.out.println("开始下载jar文件...");
		String bakPath = binPath + File.separator + "bak" + File.separator + Version.JARVERSION + File.separator;
		File oldjar = null;
		try {
			//检测是否为支持jar更新的类型
			oldjar = new File(binPath + File.separator + "jre" + File.separator
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
				FileUtil2.ifNotExistsThenCreate(bakPath);
				System.out.println(String.format("备份文件%s至%s", oldjar.getPath(), bakPath + jarName));
				FileUtil2.copyFile(oldjar.getPath(), bakPath + jarName);
				System.out.println(String.format("下载文件至%s", oldjar.getParent() + File.separator + tmpjarName));
				System.out.println("请耐心等候...");
				//保存
				int fsize = FileUtil2.storeStream(oldjar.getParent(), tmpjarName, is);
				if(fsize != totalLength){
					JOptionPane.showMessageDialog(null, "更新失败，jar文件下载不完整(" + FileUtil2.showSizeStr((long)fsize) + ")，请重试");
				}else{
					File tmpfile = new File(oldjar.getParent() + File.separator + tmpjarName);
					System.out.println(String.format("重命名文件%s为%s", tmpjarName, jarName));
					FileUtil2.copyByte(tmpfile, oldjar);
					System.out.println(String.format("删除临时文件%s", tmpjarName));
					FileUtil2.deleteFile(tmpfile);
					int r = JOptionPane.showConfirmDialog(null, "jar文件更新成功，重启后下载器后生效，是否立即重启？", "更新完成", JOptionPane.YES_NO_OPTION);
					if(r == JOptionPane.OK_OPTION){
						mainWindow.restartMenuItem.doClick();
					}
				}
			}
		}catch(Exception e1) {
			JOptionPane.showMessageDialog(null, "jar文件更新失败，" + e1.getMessage());
			//还原
			System.out.println(String.format("还原文件%s至%s", bakPath + jarName, oldjar.getPath()));
			FileUtil2.copyFile(bakPath + jarName, oldjar.getPath());
			e1.printStackTrace();
		}finally{
			if(is != null){
				try{is.close();}catch(Exception e){}
			}
			mainWindow.aboutWindow.jarUpdateWorker = null;
		}
		
		return null;
	}
	 
}
