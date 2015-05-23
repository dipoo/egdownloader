package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.version.Version;
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

	private AJTextPane aboutTextPane;
	
	/**
	 * 加入参数mainWindow主要是使关于窗口始终在主窗口的中央弹出
	 * @param mainWindow
	 */
	public AboutMenuWindow(final JFrame mainWindow) {
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
		final JDialog this_ = this;
		aboutTextPane.addHyperlinkListener(new HyperlinkListener() {
			
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//e.getDescription():a标签href值
					if("checkVersion".equals(e.getDescription())){
						//检查版本号
						try {
							String egVersion = WebClient.getRequestUseJava(ComponentConst.EG_VERSION_URL, null);
							Map<String, String> version = JsonUtil.json2Map(egVersion);
							if(! Version.VERSION.equals(version.get("version"))){
								int r = JOptionPane.showConfirmDialog(this_, "最新版本号为：" + version.get("version") + "，是否前往下载？");
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
							}else{
								JOptionPane.showMessageDialog(this_, "当前已是最新版本");
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(this_, "检查版本失败，请确定网络是否可用");
						}
					}
				}
			}
		});
		this.getContentPane().add(aboutTextPane);
	}
}
