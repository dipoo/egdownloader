package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
/**
 * 重启
 * @author dipoo
 * @since 2019-08-14
 */
public class RestartMenuItem extends JMenuItem {
	private static final long serialVersionUID = -3015271666943997829L;

	public RestartMenuItem(String text, final EgDownloaderWindow window){
		super(text);
		//this.setIcon(IconManager.getIcon("folder"));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(StringUtils.isNotBlank(ComponentConst.runExe4jLaunchName)){
					try {
						System.out.println("重启下载器：" + ComponentConst.runExe4jLaunchName);
						try {
							Thread.sleep(3000);
							Process proc = Runtime.getRuntime().exec("cmd.exe /c " + ComponentConst.runExe4jLaunchName);
							FileUtil2.storeStream("data", "11.log", proc.getInputStream());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//startProgram("E:\\zhangshaorong\\soft\\PanDownload\\PanDownload.exe"/*ComponentConst.runExe4jLaunchName*/);
						/*Desktop.getDesktop().open(new File(ComponentConst.runExe4jLaunchName));*/
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("重启失败：" + e.getMessage());
					}
					/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
						public void run() {
							try {
								System.out.println("重启下载器：" + ComponentConst.runExe4jLaunchName);
								Process proc = Runtime.getRuntime().exec("cmd.exe /c " + ComponentConst.runExe4jLaunchName);
								FileUtil2.storeStream("data", "11.log", proc.getInputStream());
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println("重启失败：" + e.getMessage());
							}
						}
					}));*/
					System.exit(0);
				}else{
					System.out.println("当前环境不支持重启。");
				}
			}
		});
	}
	
	public static void startProgram(String programPath) throws IOException {  
	    if (StringUtils.isNotBlank(programPath)) {  
	        try {  
	            String programName = programPath.substring(programPath.lastIndexOf("/") + 1, programPath.lastIndexOf("."));  
	            List<String> list = new ArrayList<String>();  
	            list.add("cmd.exe");  
	            list.add("/c");  
	            list.add("start");  
	            list.add("\"" + programName + "\"");  
	            list.add("\"" + programPath + "\"");  
	            ProcessBuilder pBuilder = new ProcessBuilder(list);  
	            pBuilder.start();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            System.out.println("应用程序：" + programPath + "不存在！");  
	        }  
	    }  
	}  
}
