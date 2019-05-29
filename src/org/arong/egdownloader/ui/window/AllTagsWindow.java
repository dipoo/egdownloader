package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.plaf.ButtonUI;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MenuItemActonListener;
import org.arong.egdownloader.ui.panel.TaskTagsPanel;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJMenuItem;
import org.arong.egdownloader.ui.swing.AJPopupMenu;
import org.arong.egdownloader.ui.work.interfaces.IMenuListenerTask;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * 显示所有标签窗口
 * @author dipoo
 * @date 2019-05-29
 */
public class AllTagsWindow extends JDialog {
	public EgDownloaderWindow mainWindow;
	public JPopupMenu popupMenu;//右键菜单
	public JSeparator separator;
	public JPanel tagPane;
	
	public AllTagsWindow(final EgDownloaderWindow egDownloaderWindow){
		
		this.mainWindow = egDownloaderWindow;
		this.setSize(ComponentConst.CLIENT_WIDTH - 200, 300);
		this.setTitle("选择标签");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		JPanel typeBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		typeBtnPanel.setBounds(5, 5, ComponentConst.CLIENT_WIDTH - 200, 60);
		ActionListener typeBtnActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				String row = btn.getName();
				
			}
		};
		for(String row : ComponentConst.TAGS_CN_FILENAMES){
			AJButton btn = new AJButton(TaskTagsPanel.tagscnMap != null && mainWindow.setting.isTagsTranslate() ? 
					TaskTagsPanel.tagscnMap.get("rows:" + row.replaceAll(".md", "")) : row.replaceAll(".md", ""), null);
			btn.setName(row.replaceAll(".md", ""));
			btn.addActionListener(typeBtnActionListener);
		}
		
		separator = new JSeparator(); 
		separator.setBounds(0, 45, this.getWidth(), 1);
		
		tagPane = new JPanel();
		tagPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		tagPane.setBounds(10, 50, this.getWidth() - 30, this.getHeight() - 80);
		updateTagScrollPane();
		
		ComponentUtil.addComponents(getContentPane(), typeBtnPanel, separator, tagPane);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				Window this_ = (Window) e.getSource();
				this_.dispose();
			}
		});
	}
	public ButtonUI[] btnUis = {
			AJButton.blueBtnUi,
			AJButton.greenBtnUi,
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue),
			new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red)
	};
	public void updateTagScrollPane(){
		tagPane.removeAll();
		Set<String> keys = null;
		if(TaskTagsPanel.tagscnMap != null){
			keys = TaskTagsPanel.tagscnMap.keySet();
		}
		if(keys != null){
			final AllTagsWindow this_ = this;
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
							//searchComicWindow.doSearch("female:"+ tags.getProperty(key) + "$");
						}
						//右键
						else if(e.getButton() == MouseEvent.BUTTON3){
							if(popupMenu == null){
								JMenuItem deleteItem = new AJMenuItem("收藏", Color.BLACK, "",
									new MenuItemActonListener(null, new IMenuListenerTask() {
										public void doWork(Window window, ActionEvent e) {
											
										}
									}));
								popupMenu = new AJPopupMenu(deleteItem);
							}
							popupMenu.show(btn, e.getPoint().x, e.getPoint().y);
						}
					}
				});
				tagPane.add(b);
			}
		}
	}
}
