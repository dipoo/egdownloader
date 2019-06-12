package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.util.FileUtil2;

/**
 * EX搜索历史窗口
 * 
 * @author dipoo
 * @since 2019-06-10
 * 
 */
public class SearchHistoryWindow extends JDialog {

	private static final long serialVersionUID = 5258712248585280958L;
	private SearchComicWindow comicWindow;
	public AJTextPane textPane;
	public static Map<String, String> historyMap; //key为搜索关键字，value为时间
	
	/**
	 * 加入参数mainWindow主要是使关于窗口始终在主窗口的中央弹出
	 * @param mainWindow
	 */
	public SearchHistoryWindow(SearchComicWindow comicWindow_) {
		this.comicWindow = comicWindow_;
		// 设置主窗口
		this.setSize(800, 480);
		this.setTitle("Exhentai搜索历史");
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(comicWindow);
		this.setLayout(null);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SearchHistoryWindow window = (SearchHistoryWindow) e.getSource();
				window.dispose();
			}
		});
		//添加鼠标活动监听器
		this.addMouseListener(new MouseAdapter() {
			// 当鼠标点击当前窗口时隐藏此窗口
			public void mouseClicked(MouseEvent e) {
				SearchHistoryWindow window = (SearchHistoryWindow) e.getSource();
				window.dispose();
			}
		});
		final SearchHistoryWindow this_ = this;
		textPane = new AJTextPane("", Color.BLUE);
		textPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//e.getDescription():a标签href值
					if(e.getDescription().startsWith("search:")){
						String key = e.getDescription().replace("search:", "");
						comicWindow.doSearch(key, true);
						comicWindow.toFront();
						this_.dispose();
					}
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setSize(790, 480);
		this.getContentPane().add(scrollPane);
		this.setVisible(false);
	}
	
	public void render(){
		if(historyMap == null){
			textPane.setText("<html><br/><br/><center><h3>正在加载历史记录...</h3></center></html>");
			loadConsoleLog();
		}
		if(! historyMap.isEmpty()){
			StringBuilder sb = new StringBuilder("<table style='width:600px;font-family:微软雅黑;font-size:10px;color:#666;border-collapse:collapse;'>");
			List<String> list = new ArrayList<String>(historyMap.keySet());
			Collections.sort(list, new Comparator<String>() {
				public int compare(String key1, String key2) {
					return historyMap.get(key2).compareTo(historyMap.get(key1));
				}
			});
			for(String key : list){
				sb.append("<tr style='border-bottom:1px solid #BFBFBF;'>");
				sb.append(String.format("<td style='text-align:left;width:120px'><b>%s</b></td>", historyMap.get(key)));
				try{key = URLDecoder.decode(key, "UTF-8");}catch(Exception e){}
				sb.append(String.format("<td style='text-align:left;font-size:11px;'><a style='text-decoration:none' href='search:%s'>%s</a></td>", key, key));
			}
			sb.append("</table>");
			textPane.setText(sb.toString());
			textPane.setCaretPosition(0);
		}else{
			textPane.setText("<html><br/><br/><center><h2>无历史搜索记录</h2></center></html>");
		}
	}
	
	public static void loadConsoleLog(){
		historyMap = new LinkedHashMap<String, String>();
		String binPath = FileUtil2.getProjectPath();
		if(binPath.endsWith("bin")){
			binPath = binPath.substring(0, binPath.length() - 3);
		}else{
			FileUtil2.ifNotExistsThenCreate("");
			File f = new File("");
			binPath = f.getAbsolutePath();
		}
		//读取日志文件
		File logfile = new File(binPath + File.separator + "console.log");
		if(logfile.exists()){
			BufferedReader br =	null;
			try {
				br = new BufferedReader(new FileReader(logfile));
				String line = null;String[] linearr;String[] linearr2;int num = 0;int linenum = 0;
				while((line = br.readLine()) != null){
					linenum ++;
					if(linenum > 50000) break;
					//2019-06-10 12:31:26 https://exhentai.org/?advsearch=1&f_sname=on&f_stags=on&f_sh=on&f_spf=&f_spt=&page=0&f_cats=0&f_sto=&f_search=language%3A%22chinese%24%22,proxy:null
					if(line.contains("&f_search=")){
						try{
							linearr = line.split(" https");
							linearr2 = line.split("&f_search=");
							if(linearr.length > 1 && linearr2.length > 1 && !historyMap.containsKey(linearr2[1].replace(",proxy:null", ""))){
								num ++;
								historyMap.put(linearr2[1].replace(",proxy:null", ""), linearr[0]);
								if(num > 50){ break; }
							}
						}catch (Exception e) {}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(br != null){try {br.close();} catch (IOException e) {}}
			}
		}
	}
}
