package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJCheckBox;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJPager;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.table.SearchTasksTable;
import org.arong.egdownloader.ui.work.SearchComicWorker;
import org.arong.util.FileUtil;

/**
 * 显示绅士站漫画列表窗口
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchComicWindow extends JFrame {

	private static final long serialVersionUID = -3912589805632312855L;
	public EgDownloaderWindow mainWindow;
	public SearchCoverWindow coverWindow;
	private JTextField keyField;
	private JLabel loadingLabel;
	public JLabel totalLabel;
	private JButton searchBtn;
	public SearchTasksTable searchTable;
	public JScrollPane tablePane;
	public JPanel optionPanel;
	public AJPager pager;
	public String key = " ";//搜索条件的字符串
	public List<SearchTask> searchTasks = new ArrayList<SearchTask>();
	public Map<String, Map<String, List<SearchTask>>> datas = new HashMap<String, Map<String, List<SearchTask>>>();
	public Map<String, String> keyPage = new HashMap<String, String>();
	private Font font = new Font("宋体", 0, 12); 
	public SearchComicWindow(final EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT);
		this.setTitle("搜索里站漫画");
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);  
		JLabel keyLabel = new AJLabel("关键字", Color.BLUE, 20, 20, 50, 30);
		keyField = new AJTextField("", 60, 20, 440, 30);
		keyField.setText("chinese");
		keyField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					searchBtn.doClick();
				}
			}
			
		});
		
		loadingLabel = new AJLabel("正在加载数据", "loading.gif", Color.BLACK, JLabel.LEFT);
		loadingLabel.setBounds(600, 20, 120, 30);
		loadingLabel.setVisible(false);
		
		totalLabel = new AJLabel("", null, Color.BLACK, JLabel.LEFT);
		totalLabel.setBounds(600, 20, 300, 30);
		totalLabel.setVisible(false);
		
		/* 分类条件 */
		optionPanel = new JPanel(new GridLayout());
		optionPanel.setBounds(0, 55, ComponentConst.CLIENT_WIDTH, 40);
		optionPanel.setAlignmentX(FlowLayout.LEFT);
		JCheckBox c1 = new AJCheckBox("DOUJINSHI", Color.BLUE, font, true);
		JCheckBox c2 = new AJCheckBox("MANGA", Color.BLUE, font, true);
		JCheckBox c3 = new AJCheckBox("ARTISTCG", Color.BLUE, font, true);
		JCheckBox c4 = new AJCheckBox("GAMECG", Color.BLUE, font, true);
		JCheckBox c5 = new AJCheckBox("WESTERN", Color.BLUE, font, true);
		JCheckBox c6 = new AJCheckBox("NON-H", Color.BLUE, font, true);
		JCheckBox c7 = new AJCheckBox("IMAGESET", Color.BLUE, font, true);
		JCheckBox c8 = new AJCheckBox("COSPLAY", Color.BLUE, font, true);
		JCheckBox c9 = new AJCheckBox("ASIANPORN", Color.BLUE, font, true);
		JCheckBox c10 = new AJCheckBox("MISC", Color.BLUE, font, true);
		ComponentUtil.addComponents(optionPanel, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
		/* 分类条件 end*/
		
		pager = new AJPager(20, ComponentConst.CLIENT_HEIGHT - 80, ComponentConst.CLIENT_WIDTH, ComponentConst.CLIENT_HEIGHT, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				search(Integer.parseInt(btn.getName()) + "");
			}
		});
		pager.setVisible(false);
		
		searchBtn = new AJButton("搜索", "", new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				search("1");
			}
			
		}, 510, 20, 60, 30);
		
		ComponentUtil.addComponents(this.getContentPane(), keyLabel, keyField, searchBtn, loadingLabel, totalLabel, optionPanel, pager);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				JFrame w = (JFrame)e.getSource();
				w.dispose();
			}
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SearchComicWindow this_ = (SearchComicWindow) e.getSource();
				this_.dispose();
			}
		});
		
		//检测是否存在缓存目录
		FileUtil.ifNotExistsThenCreate(ComponentConst.CACHE_PATH);
	}
	
	public void search(String page){
		showLoading();
		String keyText = keyField.getText().trim();
		String k = parseOption() + keyText;
		if(datas.containsKey(k) && datas.get(k).containsKey(page)){
			searchTasks = datas.get(k).get(page);
			showResult(pager.getPageCount()+"", Integer.parseInt(page));
			totalLabel.setText(keyPage.get(k));
			hideLoading();
		}else{
			key = k;
			//过滤key
			keyText = filterUrl(keyText);
			String exurl = "http://exhentai.org/?page=" + (Integer.parseInt(page) - 1) + parseOption() + "&f_search=" + keyText;
			new SearchComicWorker(mainWindow, exurl, Integer.parseInt(page)).execute();
		}
	}
	
	public String filterUrl(String url){
		if(url != null){
			return url.replaceAll("\\%", "%25")
			.replaceAll("\\+", "%2B")
			.replaceAll(" ", "+")
			.replaceAll("\\/", "%2F")
			.replaceAll("\\?", "%3F")
			.replaceAll("\\#", "%23")
			.replaceAll("\\&", "%26")
			.replaceAll("\\=", "%3D");
		}
		return null;
	}
	
	public String parseOption(){
		Component[] cs = optionPanel.getComponents();
		String option = "";
		JCheckBox jc = null;
		for(int i = 0; i < cs.length; i++){
			jc = (JCheckBox) cs[i];
			option += "&f_" + jc.getText().toLowerCase() + "=" + (jc.isSelected() ? "1" : "0");
		}
		return option;
	}
	
	public void showResult(String totalPage, Integer currentPage){
		if(searchTable == null){
			searchTable = new SearchTasksTable(5, 100, ComponentConst.CLIENT_WIDTH - 20,
					ComponentConst.CLIENT_HEIGHT - 180, searchTasks, this);
			tablePane = new JScrollPane(searchTable);
			tablePane.setBounds(5, 100, ComponentConst.CLIENT_WIDTH - 20, ComponentConst.CLIENT_HEIGHT - 180);
			tablePane.getViewport().setBackground(new Color(254,254,254));
			mainWindow.searchComicWindow.getContentPane().add(tablePane);
		}
		searchTable.setVisible(true);
		searchTable.changeModel(searchTasks);
		searchTable.updateUI();
		if(totalPage != null && currentPage != null){
			mainWindow.searchComicWindow.pager.change(Integer.parseInt(totalPage), currentPage);
			mainWindow.searchComicWindow.pager.setVisible(true);
		}
	}
	
	public void showLoading(){
		totalLabel.setVisible(false);
		loadingLabel.setVisible(true);
		searchBtn.setEnabled(false);
		if(tablePane != null){
			tablePane.setVisible(false);
		}
		pager.setVisible(false);
	}
	
	public void hideLoading(){
		loadingLabel.setVisible(false);
		searchBtn.setEnabled(true);
		totalLabel.setVisible(true);
		if(tablePane != null){
			tablePane.setVisible(true);
		}
		pager.setVisible(true);
	}
	
	public void setTotalInfo(String totalPage, String totalTasks){
		totalLabel.setText("共搜索到 " + totalPage + " 页,总计 " + totalTasks + " 本漫画");
	}
	
	public void dispose() {
		mainWindow.setEnabled(true);
		mainWindow.setVisible(true);
		super.dispose();
	}
}
