package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;

public class TaskImagePanel extends JPanel {
	private EgDownloaderWindow mainWindow;
	public int selectIndex = 0;
	public static final int FISRTSIZE = 200;
	private FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
	public Map<String, JPanel> imagePanels = new HashMap<String, JPanel>(); 
	public TaskImagePanel(final EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.setLayout(layout);
		this.setBounds(10, 5, mainWindow.getWidth() - 20, 250 * 6);
		//初始化组件
		init(mainWindow.tasks, true, true);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//init(mainWindow.tasks);
			}
		});
	}
	
	public void changeViewSize(){
		if(this.getComponents() != null){
			this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 20, ((this.getComponents().length / (Toolkit.getDefaultToolkit().getScreenSize().width / mainWindow.setting.getCoverWidth())) + 2) * mainWindow.setting.getCoverHeight()));
			for(int i = 0; i < this.getComponents().length; i ++){
				JPanel p = (JPanel) this.getComponents()[i];
				for(int j = 0; j < p.getComponents().length; j ++){
					if(p.getComponents()[j].getName().startsWith("cover")){
						AJLabel l = (AJLabel) p.getComponents()[j];
						ImageIcon icon = (ImageIcon) l.getIcon();
						if(icon != null){
							double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
							int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
							l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
							l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
							/*try {
								BufferedImage image = Thumbnails.of((BufferedImage)icon.getImage()).size((int)w, height).asBufferedImage();
								icon.setImage(null);
								icon.setImage(image);
							} catch (IOException e) {
								e.printStackTrace();
							}*/
							icon.getImage().flush();//解决加载图片不完全问题
							l.setIcon(icon);
						}
						break;
					}
				}
			}
		}
	}
	public void init(List<Task> tasks){
		init(tasks, false, true);
	}
	public void init(List<Task> tasks, boolean first, boolean isshow){
		final TaskImagePanel this_ = this;
		if(tasks != null && tasks.size() > 0){
			int size = first && tasks.size() > FISRTSIZE ? FISRTSIZE : tasks.size();
			this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 20, ((size / (Toolkit.getDefaultToolkit().getScreenSize().width / mainWindow.setting.getCoverWidth())) + 2) * mainWindow.setting.getCoverHeight()));
			boolean hasnew = false;
			for(int i = 0; i < size; i ++){
				if(imagePanels.get(tasks.get(i).getId()) == null){
					hasnew = true;
					JPanel p = new JPanel();
					p.setName(i + "");
					p.setLayout(layout);
					p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					p.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							for(int i = 0; i < this_.getComponents().length; i ++){
								((JPanel)this_.getComponents()[i]).setBackground(Color.WHITE);
							}
							//((JPanel)this_.getComponents()[this_.getComponents().length - selectIndex - 1]).setBackground(Color.WHITE);
							JPanel p = (JPanel)e.getSource();
							p.setBackground(Color.MAGENTA);
							selectIndex = Integer.parseInt(p.getName());
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
							if(selectIndex != Integer.parseInt(p.getName())){
								p.setBackground(Color.WHITE);
							}
						}
					});
					AJLabel l = new AJLabel();
					l.setName("cover" + tasks.get(i).getId());
					l.setOpaque(true);
					l.setBackground(Color.BLACK);
					l.setForeground(Color.WHITE);
					l.setFont(FontConst.Microsoft_BOLD_12);
					l.setVerticalTextPosition(JLabel.TOP);
					l.setHorizontalTextPosition(JLabel.LEADING);
					l.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
					final String path = ComponentConst.getSavePathPreffix() + tasks.get(i).getSaveDir() + "/cover.jpg";
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
					imagePanels.put(tasks.get(i).getId(), p);
					if(!isshow){
						p.setVisible(false);
					}
					this.add(p, i);
				}else{
					imagePanels.get(tasks.get(i).getId()).setName(i + "");
					imagePanels.get(tasks.get(i).getId()).setVisible(true);
				}
			}
			if(hasnew){
				//回到顶部
				this.scrollRectToVisible(new Rectangle(0, 0));
			}
		}
	}
	public void removeIndexs(List<Integer> indexs){
		for(int i = 0; i < indexs.size(); i ++){
			this.remove(indexs.get(i) - i);
		}
	}
}
