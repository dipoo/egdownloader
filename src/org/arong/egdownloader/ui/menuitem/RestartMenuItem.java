package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JMenuItem;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.RestartTipsWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
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
		this.setIcon(IconManager.getIcon("task"));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(StringUtils.isNotBlank(ComponentConst.runExe4jLaunchName)){
					try {
						window.setVisible(false);
						new RestartTipsWindow();
						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.MINUTE, 1); //十秒之后重启
						String task_create = String.format("cmd.exe /c schtasks /create /tn %s /tr \"%s\" /sc once /st \"%s\"", ComponentConst.RESTART_PLAN_TASK_NAME, 
								ComponentConst.runExe4jLaunchName, new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));
						Process proc = Runtime.getRuntime().exec(task_create);
						System.out.println("重启下载器：" + task_create);
						FileUtil2.storeStream("data", "restart.log", proc.getInputStream());
						final int se = 60 - calendar.get(Calendar.SECOND);
						if(se > 5){
							new CommonSwingWorker(new Runnable() {
								public void run() {
									try {
										Thread.sleep((se - 3) * 1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									} finally{
										System.exit(0);
									}
								}
							}).execute();
						}else{
							System.exit(0);
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("重启失败：" + e.getMessage());
						System.exit(0);
					}
				}else{
					System.out.println("当前环境不支持重启。");
				}
			}
		});
	}
}
