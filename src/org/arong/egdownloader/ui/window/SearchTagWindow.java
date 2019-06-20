package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.ButtonUI;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.arong.util.FileUtil2;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
/**
 * 废除
 * @author Administrator
 *
 */
@Deprecated
public class SearchTagWindow extends JDialog {

	private static final long serialVersionUID = 682782292634346851L;
	
	public SearchComicWindow searchComicWindow;
	public Properties tags;
	public Properties importTags;
	public JButton addBtn;
	public JButton importBtn;
	public JSeparator separator;
	public JPanel addPanel;
	public JPanel importPanel;
	public JTextArea tagArea;
	public JPanel tagPane;
	public JPopupMenu popupMenu;//右键菜单
	public JFileChooser importFileChooser;
	
	public SearchTagWindow(SearchComicWindow searchComicWindow){
		this.searchComicWindow = searchComicWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH - 200, ComponentConst.CLIENT_HEIGHT - 100);
		this.setTitle("选择标签");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(searchComicWindow);
		
		//加载标签定义文件
		tags = new Properties();
		try {
			tags.load(new FileReader(ComponentConst.TAG_FILE_PATH));
		} catch (FileNotFoundException e1) {
			try {
				FileUtil2.storeStr2file("", ComponentConst.ROOT_DATA_PATH, ComponentConst.TAG_FILE_NAME);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		final SearchTagWindow this_ = this;
		
		addBtn = new AJButton("添加标签", "",  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(addPanel == null){
					/**
					 * 表单
					 */
					addPanel = new JPanel();
					addPanel.setBounds((this_.getWidth() - 300) / 2, (this_.getHeight() - 130) / 2, 300, 130);
					addPanel.setVisible(false);
					addPanel.setLayout(null);
					addPanel.setBackground(Color.WHITE);
					addPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
					JLabel nameLabel = new AJLabel("名(如:全彩)", Color.blue, 10, 10, 100, 30);
					final JTextField nameField = new AJTextField("", 120, 10, 150, 30);
					JLabel valueLabel = new AJLabel("值(如:fullcolor)", Color.blue, 10, 50, 100, 30);
					final JTextField valueField = new AJTextField("", 120, 50, 150, 30);
					JButton saveBtn = new AJButton("保存", "",  new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							String name = nameField.getText();
							String value = valueField.getText();
							if("".equals(name.trim()) || "".equals(value.trim())){
								JOptionPane.showMessageDialog(this_, "名或值不能为空");
							}else{
								tags.put(name, value);
								//保存
								storeTags();
								updateTagScrollPane();
								addPanel.setVisible(false);
								tagPane.setVisible(true);
								addBtn.setEnabled(true);
								importBtn.setEnabled(true);
							}
						}
					}, 120, 90, 60, 30);
					JButton cancelBtn = new AJButton("取消", "",  new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							addPanel.setVisible(false);
							tagPane.setVisible(true);
							addBtn.setEnabled(true);
							importBtn.setEnabled(true);
						}
					}, 210, 90, 60, 30);
					ComponentUtil.addComponents(addPanel, nameLabel, nameField, valueLabel, valueField, saveBtn, cancelBtn);
					this_.getContentPane().add(addPanel);
				}
				addPanel.setVisible(true);
				tagPane.setVisible(false);
				addBtn.setEnabled(false);
				importBtn.setEnabled(false);
			}
		}, (this.getWidth() - 130) / 2, 10, 60, 30);
		
		importBtn = new AJButton("导入标签", "",  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(importFileChooser == null){
					importFileChooser = new JFileChooser("/");
					importFileChooser.setDialogTitle("选择标签文件");//选择框标题
					importFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//只能选择目录
					importFileChooser.setFileFilter(new FileFilter() {
						public String getDescription() {
							return ".properties";
						}
						public boolean accept(File f) {
							if (f.isDirectory()) return true;
						    return f.getName().endsWith(".properties");
						}
					});
				}
				int result = importFileChooser.showOpenDialog(this_);
				if(result == JFileChooser.APPROVE_OPTION) {  
                    File file = importFileChooser.getSelectedFile();  
                    if(!file.isFile()) {  
                        JOptionPane.showMessageDialog(this_, "你选择的文件不存在");
                        return ;
                    }  
                    if(importTags == null){
                    	importTags = new Properties();
                    }
                    try {
                    	importTags.clear();
						importTags.load(new FileReader(file));
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						if(importTags.isEmpty()){
							JOptionPane.showMessageDialog(this_, "导入的文件中不存在标签");
						}else{
							if(importPanel == null){
								importPanel = new JPanel();
								importPanel.setBounds((this_.getWidth() - 500) / 2, (this_.getHeight() - 400) / 2, 500, 400);
								importPanel.setVisible(false);
								importPanel.setLayout(null);
								importPanel.setBackground(Color.WHITE);
								importPanel.setBorder(null);
								JScrollPane scrollPane = new JScrollPane();
								scrollPane.setAutoscrolls(true);
								scrollPane.setBounds(0, 0, importPanel.getWidth(), 300);
								tagArea = new JTextArea();
								tagArea.setEditable(false);
								tagArea.setAutoscrolls(true);
								tagArea.setLineWrap(true);
								tagArea.setBorder(null);
								scrollPane.getViewport().add(tagArea);
								JButton importBtn_ = new AJButton("导入", "",  new ActionListener() {
									public void actionPerformed(ActionEvent ae) {
										tags.putAll(importTags);
										JOptionPane.showMessageDialog(this_, "导入成功，共导入了" + importTags.size() + "个标签");
										storeTags();
										updateTagScrollPane();
										tagPane.updateUI();
										importPanel.setVisible(false);
										tagPane.setVisible(true);
										addBtn.setEnabled(true);
										importBtn.setEnabled(true);
									}
								}, 185, 320, 60, 30);
								JButton cancelBtn = new AJButton("取消", "",  new ActionListener() {
									public void actionPerformed(ActionEvent ae) {
										importPanel.setVisible(false);
										tagPane.setVisible(true);
										addBtn.setEnabled(true);
										importBtn.setEnabled(true);
									}
								}, 255, 320, 60, 30);
								JLabel tipLabel = new AJLabel("相同标签会被覆盖；如果内容为乱码，请将导入的文件编码设置为UTF-8。", Color.BLUE);
								tipLabel.setBounds(60, 360, 450, 30);
								ComponentUtil.addComponents(importPanel, scrollPane, importBtn_, cancelBtn, tipLabel);
								this_.getContentPane().add(importPanel);
							}
							String text = importTags.toString();
							tagArea.setText(text);
							importPanel.setVisible(true);
							tagPane.setVisible(false);
							addBtn.setEnabled(false);
							importBtn.setEnabled(false);
						}
					}
                }
				
			}
		}, (this.getWidth() - 130) / 2 + 70, 10, 60, 30);
		separator = new JSeparator(); 
		separator.setBounds(0, 45, this.getWidth(), 1);
		
		tagPane = new JPanel();
		tagPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		tagPane.setBounds(10, 50, this.getWidth() - 30, this.getHeight() - 80);
		updateTagScrollPane();
		
		ComponentUtil.addComponents(getContentPane(), addBtn, importBtn, separator, tagPane);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
		//窗口大小变化监听
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Window window = (Window) e.getSource();
				if(addBtn != null){
					addBtn.setLocation((window.getWidth() - 130) / 2, addBtn.getY());
				}
				if(importBtn != null){
					importBtn.setLocation((window.getWidth() - 130) / 2 + 70, addBtn.getY());
				}
				separator.setSize(window.getWidth(), 1);
				if(addPanel != null){
					addPanel.setLocation((window.getWidth() - addPanel.getWidth()) / 2, (window.getHeight() - addPanel.getHeight()) / 2);
				}
				tagPane.setSize(window.getWidth() - 30, window.getHeight() - 80);
			}
		});
		
	}
	
	public ButtonUI[] btnUis = {
			AJButton.blueBtnUi,
			AJButton.greenBtnUi,
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue),
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red)
	};
	
	public String btnText;
	
	public void updateTagScrollPane(){
		tagPane.removeAll();
		Set<String> keys = tags.stringPropertyNames();
		final SearchTagWindow this_ = this;
		for(final String key : keys){
			JButton b = new JButton(key);
			//随机颜色
			int r = (int) (Math.random() * 4);
			b.setUI(btnUis[r]);
			b.setForeground(Color.WHITE);
			b.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					JButton btn = (JButton) e.getSource();
					//左键
					if(e.getButton() == MouseEvent.BUTTON1){
						this_.dispose();
						searchComicWindow.doSearch("female:"+ tags.getProperty(key) + "$");
					}
					//右键
					else if(e.getButton() == MouseEvent.BUTTON3){
						if(popupMenu == null){
							JMenuItem deleteItem = new AJMenuItem("删除", Color.BLACK, IconManager.getIcon("delete"),
								new MenuItemActonListener(null, new IMenuListenerTask() {
									public void doWork(Window window, ActionEvent e) {
										int r = JOptionPane.showConfirmDialog(this_, "确定要删除[" + btnText + "]这个标签吗");
										if(r == JOptionPane.OK_OPTION){
											tags.remove(btnText);
											popupMenu.setVisible(false);
											storeTags();
											updateTagScrollPane();
											tagPane.updateUI();
											tagPane.setVisible(true);
										}
									}
								}));
							popupMenu = new AJPopupMenu(deleteItem);
						}
						btnText = btn.getText();
						popupMenu.show(btn, e.getPoint().x, e.getPoint().y);
					}
				}
			});
			tagPane.add(b);
		}
	}
	
	public void storeTags(){
		//保存
		try {
			tags.store(new FileWriter(ComponentConst.TAG_FILE_PATH), "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
