package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJPager;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
/**
 * 任务的图片模式展示面板
 * @author Administrator
 *
 */
public class TaskImagePanel extends JPanel {
	private EgDownloaderWindow mainWindow;
	public int selectIndex = 0;
	public static final int PAGESIZE = 200;
	public int page = 1;
	private FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
	public AJPager imageTaskPager;
	public TaskImagePanel(final EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.setLayout(layout);
		this.setBounds(10, 5, mainWindow.getWidth() - 20, 250 * 6);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//init(mainWindow.tasks);
			}
		});
		final TaskImagePanel this_ = this;
		imageTaskPager = new AJPager(0, 0, this.getWidth() - 20, 200, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				this_.page = Integer.parseInt(btn.getName());
				this_.init();
			}
		});
		imageTaskPager.setName("");
		//初始化组件
		init(mainWindow.tasks);
	}
	
	public void changeViewSize(){
		if(this.getComponents() != null){
			this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 20, ((this.getComponents().length / (Toolkit.getDefaultToolkit().getScreenSize().width / mainWindow.setting.getCoverWidth())) + 8) * mainWindow.setting.getCoverHeight()));
			this.scrollRectToVisible(new Rectangle(0, 0));
			for(int i = 0; i < this.getComponents().length; i ++){
				JPanel p = (JPanel) this.getComponents()[i];
				for(int j = 0; j < p.getComponents().length; j ++){
					if(p.getComponents()[j].getName() != null && p.getComponents()[j].getName().startsWith("cover")){
						
						AJLabel l = (AJLabel) p.getComponents()[j];
						ImageIcon icon = (ImageIcon) l.getIcon();
						if(icon != null){
							double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
							int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
							l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
							l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
							icon.getImage().flush();//解决加载图片不完全问题
							l.setIcon(icon);
						}
						break;
					}
				}
			}
		}
	}
	public void init(){
		init(mainWindow.tasks);
	}
	public void init(final List<Task> tasks){
		final TaskImagePanel this_ = this;
		new CommonSwingWorker(new Runnable() {
			public void run() {
				Component[] oldcomps = this_.getComponents();
				this_.removeAll();
				if(tasks != null && tasks.size() > 0){
					this_.scrollRectToVisible(new Rectangle(0, 0));
					List<Task> ptasks = new ArrayList<Task>();
					for(int i = (page - 1) * PAGESIZE; i < page * PAGESIZE && i < tasks.size(); i ++){
						ptasks.add(tasks.get(i));
					}
					this_.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 20, ((ptasks.size() / (Toolkit.getDefaultToolkit().getScreenSize().width / mainWindow.setting.getCoverWidth())) + 8) * mainWindow.setting.getCoverHeight()));
					for(int i = 0; i < ptasks.size(); i ++){
						//判断JPanel是否存在
						JPanel p = null;
						for(Component comp : oldcomps){
							if(comp.getName() != null && comp.getName().startsWith(ptasks.get(i).getId())){
								p = (JPanel)comp;
								break;
							}
						}
						if(p != null){
							//name规则：taskID|list索引
							p.setName(ptasks.get(i).getId() + "|" + ((page - 1) * PAGESIZE) + i + "");
						}else{
							p = new JPanel();
							//name规则：taskID|list索引
							p.setName(ptasks.get(i).getId() + "|" + ((page - 1) * PAGESIZE) + i + "");
							p.setLayout(layout);
							p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							p.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent e) {
									for(int i = 0; i < this_.getComponents().length; i ++){
										((JPanel)this_.getComponents()[i]).setBackground(Color.WHITE);
									}
									JPanel p = (JPanel)e.getSource();
									p.setBackground(Color.MAGENTA);
									selectIndex = Integer.parseInt(p.getName().split("\\|")[1]);
									//同步任务表格的选中状态
									mainWindow.runningTable.setRowSelectionInterval(selectIndex, selectIndex);
									if(e.getButton() == MouseEvent.BUTTON3){
										mainWindow.tablePopupMenu.show(p, e.getPoint().x, e.getPoint().y);
									}
									//双击切换
									if(e.getClickCount() == 2){
										mainWindow.infoTabbedPane.setSelectedIndex(1);
									}else{
										//切换信息面板tab
										if(mainWindow.infoTabbedPane.getSelectedIndex() == 1){
											mainWindow.taskInfoPanel.parseTask(mainWindow.tasks.get(selectIndex), selectIndex);
										}else if(mainWindow.infoTabbedPane.getSelectedIndex() == 2){
											PicturesInfoPanel infoPanel = (PicturesInfoPanel) mainWindow.infoTabbedPane.getComponent(2);
											infoPanel.showPictures(mainWindow.tasks.get(selectIndex));
										}
									}
								}
								public void mouseEntered(MouseEvent e) {
									JPanel p = (JPanel)e.getSource();
									p.setBackground(Color.ORANGE);
								}

								public void mouseExited(MouseEvent e) {
									JPanel p = (JPanel)e.getSource();
									if(selectIndex != Integer.parseInt(p.getName().split("\\|")[1])){
										p.setBackground(Color.WHITE);
									}
								}
							});
							AJLabel l = new AJLabel();
							l.setName("cover" + ptasks.get(i).getId());
							l.setOpaque(true);
							l.setBackground(Color.BLACK);
							l.setForeground(Color.WHITE);
							l.setFont(FontConst.Microsoft_BOLD_12);
							l.setVerticalTextPosition(JLabel.TOP);
							l.setHorizontalTextPosition(JLabel.LEADING);
							l.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
							final String path = ComponentConst.getSavePathPreffix() + ptasks.get(i).getSaveDir() + "/cover.jpg";
							File cover = new File(path);
							if(cover != null && cover.exists()){
								try{
									ImageIcon icon = new ImageIcon(ImageIO.read(cover));
									double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
									int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
									l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
									l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
									icon.getImage().flush();//解决加载图片不完全问题
									l.setImage(icon);
									l.setIcon(icon);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							p.add(l);
						}
						this_.add(p, i);
						this_.updateUI();
					}
					int totalPage = tasks.size() / PAGESIZE + 1;
					if(totalPage > 1){
						imageTaskPager.setTotal(tasks.size());
						imageTaskPager.change(totalPage, page);
						this_.add(imageTaskPager);
						this_.updateUI();
					}
				}
				System.gc();
			}
		}).execute();
	}
}
