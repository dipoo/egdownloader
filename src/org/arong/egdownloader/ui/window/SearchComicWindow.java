package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	public JTextField keyField;
	private JLabel loadingLabel;
	public JLabel totalLabel;
	public JButton searchBtn;
	private JButton clearCacheBtn;
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
		this.setIconImage(new ImageIcon(getClass().getResource(
				ComponentConst.ICON_PATH + "eh.png")).getImage());
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);//全屏
		//this.setResizable(false);
		this.setLocationRelativeTo(mainWindow);  
		JLabel keyLabel = new AJLabel("关键字", Color.BLUE, 10, 20, 50, 30);
		keyField = new AJTextField("", 60, 20, 440, 30);
		keyField.setText("language:chinese");
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
		optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		optionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(Integer.parseInt("bababa", 16)), 1), "条件过滤"));
		optionPanel.setBounds(6, 55, ComponentConst.CLIENT_WIDTH - 23, 65);
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
		final JComboBox language = new JComboBox(new String[]{"全部", "中文", "英文", "韩文", "法文", "西班牙"});
		language.setSelectedIndex(1);
		language.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String key = keyField.getText();
				String[] keys = key.split(" ");
				if(keys[0].indexOf("language:") != -1){
					key = "";
					for(int i = 1; i < keys.length; i ++){
						key += keys[i];
						if(i != keys.length - 1){
							key += "";
						}
					}
				}
				switch(language.getSelectedIndex()){
					case 0:
						keyField.setText(key);
						break;
					case 1:
						keyField.setText("language:chinese " + key);
						break;
					case 2:
						keyField.setText("language:english " + key);
						break;
					case 3:
						keyField.setText("language:korean " + key);
						break;
					case 4:
						keyField.setText("language:french " + key);
						break;
					case 5:
						keyField.setText("language:spanish " + key);
						break;	
				}
			}
		});
		final JCheckBox c11 = new AJCheckBox("ALL", Color.RED, font, false);
		c11.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Component[] cs = optionPanel.getComponents();
				for(int i = 0; i < cs.length; i ++){
					if(cs[i] instanceof JCheckBox){
						((JCheckBox)cs[i]).setSelected(c11.isSelected());
					}
				}
			}
		});
		ComponentUtil.addComponents(optionPanel, language, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11);
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
		final SearchComicWindow this_ = this;
		clearCacheBtn = new AJButton("清理缓存", "",  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				datas.clear();
				keyPage.clear();
				JOptionPane.showMessageDialog(this_, "清理成功");
			}
		}, this.getWidth() - 80, 20, 60, 30);
		clearCacheBtn.setUI(AJButton.blueBtnUi);
		ComponentUtil.addComponents(this.getContentPane(), keyLabel, keyField, searchBtn, loadingLabel, totalLabel, clearCacheBtn, optionPanel, pager);
		
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
		
		//窗口大小变化监听
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				SearchComicWindow window = (SearchComicWindow) e.getSource();
				//设置清理缓存按钮位置
				if(clearCacheBtn != null){
					clearCacheBtn.setLocation(window.getWidth() - 80, clearCacheBtn.getY());
				}
				//设置分类条件大小
				if(optionPanel != null){
					optionPanel.setSize(window.getWidth() - 23, optionPanel.getHeight());
				}
				//设置表格的大小
				if(searchTable != null){
					int height = window.getHeight() - 210;
					tablePane.setSize(window.getWidth() - 20, height);
					searchTable.setSize(window.getWidth() - 20, height);
				}
				//设置分页面板大小
				if(pager != null){
					pager.setBounds(pager.getX(), window.getHeight() - 80, window.getWidth() - 20, pager.getHeight());
				}
			}
		});
		
		//鼠标动作监听
		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				SearchComicWindow window = (SearchComicWindow) e.getSource();
				if(window.getWidth() < ComponentConst.CLIENT_WIDTH){
					window.setSize(ComponentConst.CLIENT_WIDTH, window.getHeight());
				}
				if(window.getHeight() < ComponentConst.CLIENT_HEIGHT){
					window.setSize(window.getWidth(), ComponentConst.CLIENT_HEIGHT);
				}
			}
		});
		
		//检测是否存在缓存目录,不存在则创建
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
			String exurl = "http://exhentai.org/?page=" + (Integer.parseInt(page) - 1) + parseOption();
			if(!keyText.equals("")){
				//过滤key
				try {
					keyText = URLEncoder.encode(keyText, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					
				}
				exurl = exurl + "&f_search=" + keyText;
			}
			new SearchComicWorker(mainWindow, exurl, Integer.parseInt(page)).execute();
		}
	}
	
	public String parseOption(){
		Component[] cs = optionPanel.getComponents();
		String option = "";
		JCheckBox jc = null;
		for(int i = 0; i < cs.length; i++){
			if(cs[i] instanceof JCheckBox){
				jc = (JCheckBox) cs[i];
				if(jc.isSelected()){
					option += "&f_" + jc.getText().toLowerCase() + "=on";
				}
			}
		}
		return option;
	}
	
	public void showResult(String totalPage, Integer currentPage){
		if(searchTable == null){
			searchTable = new SearchTasksTable(5, 130, this.getWidth() - 20,
					this.getHeight() - 210, searchTasks, this);
			tablePane = new JScrollPane(searchTable);
			tablePane.setBounds(5, 130, this.getWidth() - 20, this.getHeight() - 210);
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
