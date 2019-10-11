package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.egdownloader.ui.work.JarUpdateWorker;
import org.arong.egdownloader.version.Version;
import org.arong.util.FileUtil2;
import org.arong.util.JsonUtil;

/**
 * 【关于】菜单的窗口类
 * 
 * @author 阿荣
 * @since 2014-05-21
 * 
 */
public class AboutMenuWindow extends JDialog {

	private static final long serialVersionUID = -6501253363937575294L;
	private EgDownloaderWindow mainWindow;
	public AJTextPane aboutTextPane;
	public JarUpdateWorker jarUpdateWorker;
	
	/**
	 * 加入参数mainWindow主要是使关于窗口始终在主窗口的中央弹出
	 * @param mainWindow
	 */
	public AboutMenuWindow(EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
		// 设置主窗口
		this.setSize(340, 250);
		this.setIconImage(IconManager.getIcon("user").getImage());
		this.setTitle("关于");
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(mainWindow);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.dispose();
			}
		});
		//添加鼠标活动监听器
		this.addMouseListener(new MouseAdapter() {
			// 当鼠标点击当前窗口时隐藏此窗口
			public void mouseClicked(MouseEvent e) {
				AboutMenuWindow window = (AboutMenuWindow) e.getSource();
				window.dispose();
			}
		});
		
		aboutTextPane = new AJTextPane(ComponentConst.ABOUT_TEXTPANE_TEXT,
				Color.BLUE);
		aboutTextPane.addHyperlinkListener(new HyperlinkListener() {
			
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//e.getDescription():a标签href值
					if("checkVersion".equals(e.getDescription())){
						checkVersion();
					}else if("printSystemProperties".equals(e.getDescription())){
						printSystemProperties();
					}
				}
			}
		});
		this.getContentPane().add(aboutTextPane);
	}
	
	public void checkVersion(){
		if(jarUpdateWorker != null){
			JOptionPane.showMessageDialog(mainWindow, "正在升级中...");
			return;
		}
		//检查版本号
		final String vtitle = this.getTitle();
		if(this.isVisible()){
			this.setTitle("正在检测版本...");
		}else{
			System.out.println("正在检测版本...");
		}
		final AboutMenuWindow this_ = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CommonSwingWorker(new Runnable() {
					public void run() {
						try {
							mainWindow.infoTabbedPane.setSelectedComponent(mainWindow.consolePanel);
							String egVersion = WebClient.getRequestUseJava(ComponentConst.EG_VERSION_URL, "UTF-8");
							System.out.println(egVersion);
							Map<String, String> version = JsonUtil.json2Map(egVersion);
							if(! Version.VERSION.equals(version.get("version"))){
								int r = JOptionPane.showConfirmDialog(null, "最新版本号为：" + version.get("version") + "，是否前往下载？");
								if(r == JOptionPane.OK_OPTION){
									try {
										Desktop.getDesktop().browse(new URI(version.get("url")));
									} catch (IOException e1) {
										try {
											Runtime.getRuntime().exec("cmd.exe /c start " + version.get("url"));
										} catch (IOException e2) {
										}
									} catch (URISyntaxException e1) {
										try {
											Runtime.getRuntime().exec("cmd.exe /c start " + version.get("url"));
										} catch (IOException e2) {
										}
									}
								}
							}else if(! Version.JARVERSION.equals(version.get("jarVersion")) && StringUtils.isNotBlank(version.get("jarUrl"))){
								
								String binPath = FileUtil2.getAppPath(getClass());
								if(binPath.endsWith("bin")){
									binPath = binPath.substring(0, binPath.length() - 3);
								}else{
									String defaultSavePath = mainWindow.setting.getDefaultSaveDir();
									FileUtil2.ifNotExistsThenCreate(defaultSavePath);
									File f = new File(defaultSavePath);
									binPath = f.getAbsolutePath().replaceAll(defaultSavePath, "");
								}
								File oldjar = new File(binPath + File.separator + "jre" + File.separator
										+ "lib" + File.separator + "ext" + File.separator + "egdownloader.jar");
								if(oldjar.exists()){
									int r = JOptionPane.showConfirmDialog(null, "最新程序jar文件版本号为：" + version.get("jarVersion") + "，是否更新？" + (StringUtils.isNotBlank(version.get("changelog")) ? "\n" + version.get("changelog") : ""), "新版本", JOptionPane.YES_NO_OPTION);
									if(r == JOptionPane.OK_OPTION){
										jarUpdateWorker = new JarUpdateWorker(mainWindow, version, binPath);
										jarUpdateWorker.execute();
										this_.dispose();
									}
								}else{
									if(this_.isVisible()){
										JOptionPane.showMessageDialog(this_, "当前已是最新版本");
									}else{
										System.out.println("当前已是最新版本");
									}
								}
							}else{
								if(this_.isVisible()){
									JOptionPane.showMessageDialog(this_, "当前已是最新版本");
								}else{
									System.out.println("当前已是最新版本");
								}
							}
						} catch (Exception e1) {
							if(this_.isVisible()){
								JOptionPane.showMessageDialog(this_, "检查版本失败," + e1.getMessage());
							}else{
								System.out.println("检查版本失败," + e1.getMessage());
							}
						}finally{
							this_.setTitle(vtitle);
						}
					}
				}).execute();
			}
		});
	}
	public void printSystemProperties(){
		System.out.println("java_version:	" + System.getProperty("java.version"));
	    System.out.println("java_vendor:	" + System.getProperty("java.vendor"));     
	    System.out.println("java_vendor_url:	" + System.getProperty("java.vendor.url"));     
	    System.out.println("java_home:	" + System.getProperty("java.home"));     
	    System.out.println("java_class_version:	" + System.getProperty("java.class.version"));     
	    //System.out.println("java_class_path:" + System.getProperty("java.class.path"));     
	    System.out.println("os_name:	" + System.getProperty("os.name"));     
	    System.out.println("os_arch:	" + System.getProperty("os.arch"));     
	    System.out.println("os_version:	" + System.getProperty("os.version"));     
	    System.out.println("user_name:	" + System.getProperty("user.name"));     
	    System.out.println("user_home:	" + System.getProperty("user.home"));     
	    System.out.println("user_dir:	" + System.getProperty("user.dir"));     
	    //System.out.println("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));     
	    System.out.println("java_vm_specification_vendor:	" + System.getProperty("java.vm.specification.vendor"));     
	    System.out.println("java_vm_specification_name:	" + System.getProperty("java.vm.specification.name"));     
	    System.out.println("java_vm_version:	" + System.getProperty("java.vm.version"));     
	    System.out.println("java_vm_vendor:	" + System.getProperty("java.vm.vendor"));     
	    System.out.println("java_vm_name:	" + System.getProperty("java.vm.name"));     
	    System.out.println("java_ext_dirs:	" + System.getProperty("java.ext.dirs"));     
	   /* System.out.println("file_separator:	"     
	            + System.getProperty("file.separator"));     
	    System.out.println("path_separator:	"     
	            + System.getProperty("path.separator"));     
	    System.out.println("line_separator:	"     
	            + System.getProperty("line.separator"));*/
	}
}
