package org.arong.egdownloader.ui.work;

import java.awt.Window;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.InitWindow;
import org.arong.egdownloader.ui.window.SettingWindow;
import org.arong.egdownloader.version.Version;
import org.arong.util.FileUtil2;
/**
 * 更新脚本线程类
 * @author dipoo
 * @since 2015-03-24
 */
public class UpdateScriptWorker extends SwingWorker<Void, Void>{
	
	Window window;
	InitWindow initWindow;
	EgDownloaderWindow mainWindow;
	//window可能会是InitWindow或者EgDownloaderWindow
	public UpdateScriptWorker(Window window){
		this.window = window;
	}
	
	protected Void doInBackground() throws Exception {
		String dir = "script/";
		FileUtil2.ifNotExistsThenCreate(dir);
		if(window instanceof EgDownloaderWindow){
			mainWindow = (EgDownloaderWindow)window;
			SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
			settingWindow.updateBtn.setText("更新中...");
			settingWindow.loadingLabel.setVisible(true);
			settingWindow.updateBtn.setEnabled(false);
		}else{
			initWindow = (InitWindow) window;
			initWindow.textLabel.setText(Version.NAME + "-更新脚本");
		}
		try{
			//更新createTask.js
			if(initWindow != null){
				initWindow.textLabel.setText(Version.NAME + "-更新createTask.js");
			}
			FileUtil2.storeStream(dir, "createTask.js", WebClient.getStreamUseJava(ComponentConst.SCRIPT_CREATE_URL));
			//更新collectPicture.js
			if(initWindow != null){
				initWindow.textLabel.setText(Version.NAME + "-更新collectPicture.js");
			}
			FileUtil2.storeStream(dir, "collectPicture.js", WebClient.getStreamUseJava(ComponentConst.SCRIPT_COLLECT_URL));
			//更新download.js
			if(initWindow != null){
				initWindow.textLabel.setText(Version.NAME + "-更新download.js");
			}
			FileUtil2.storeStream(dir, "download.js", WebClient.getStreamUseJava(ComponentConst.SCRIPT_DOWNLOAD_URL));
			//更新search.js
			if(initWindow != null){
				initWindow.textLabel.setText(Version.NAME + "-更新search.js");
			}
			FileUtil2.storeStream(dir, "search.js", WebClient.getStreamUseJava(ComponentConst.SCRIPT_SEARCH_URL));
			//更新search2.js
			/*if(initWindow != null){
				initWindow.textLabel.setText(Version.NAME + "-更新search2.js");
			}
			FileUtil2.storeStream(dir, "search2.js", WebClient.getStreamUseJava(ComponentConst.SCRIPT_SEARCH2_URL));*/
			//更新脚本解析器 
			ScriptParser.clearFiles();
			//保存版本号
			FileUtil2.storeStr2file(ComponentConst.remoteScriptVersion, "script/", "version");
			ComponentConst.localScriptVersion = ComponentConst.remoteScriptVersion;
			ComponentConst.scriptChange = false;
			if(initWindow != null){
				//开启主界面
				initWindow.startMain();
			}else{
				JOptionPane.showMessageDialog(mainWindow.settingWindow, "同步完成");
			}
			
		}catch (ConnectTimeoutException e) {
			JOptionPane.showMessageDialog(null, "更新脚本出错，建议手动更新");
			if(initWindow != null){
				initWindow.startMain();
			}
		}catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "更新脚本出错，建议手动更新");
			if(initWindow != null){
				initWindow.startMain();
			}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "更新脚本出错，建议手动更新");
			if(initWindow != null){
				initWindow.startMain();
			}
		}finally{
			if(mainWindow != null){
				SettingWindow settingWindow = (SettingWindow) mainWindow.settingWindow;
				settingWindow.updateBtn.setEnabled(true);
				settingWindow.loadingLabel.setVisible(false);
				settingWindow.updateBtn.setText("同步脚本");
				mainWindow.setting.setUpdateScriptWorker(new UpdateScriptWorker(mainWindow));
			}
		}
		return null;
	}
}
