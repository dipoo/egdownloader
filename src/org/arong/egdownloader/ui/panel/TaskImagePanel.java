package org.arong.egdownloader.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJPager;
import org.arong.egdownloader.ui.swing.AJPanel;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.egdownloader.ui.work.ReCreateWorker;
import org.arong.util.Tracker;
/**
 * 任务的图片模式展示面板
 * @author Administrator
 *
 */
public class TaskImagePanel extends AJPanel {
	public TaskImagePanel this_ = this;
	private EgDownloaderWindow mainWindow;
	public int selectIndex = 0;
	public static final int PAGESIZE = 200;
	public int page = 1;
	private FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
	public AJPager imageTaskPager;
	private static Color progressBarColor = new Color(47, 110, 178);
	private static Color progressBarCompletedColor = new Color(65, 145, 65);//已完成颜色
	private static Color progressBarSearchedColor = new Color(139, 26, 26);//搜索颜色
	private static Color progressBarSelectedColor = Color.RED;//选中颜色
	public int scrollValue = -1; 
	public TimerTask timerTask;
	public Timer timer;
	public List<AJPanel> allPanels = new ArrayList<AJPanel>();
	
	public TaskImagePanel(final EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.setLayout(layout);
		this.setLocation(10,  5);
		this.setPreferredSize(new Dimension(mainWindow.getWidth() - 20, 10000));
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//init(mainWindow.tasks);
			}
		});
		
		//初始化组件
		init(mainWindow.tasks);
		runTimer();
	}
	public void changeViewSize(){
		final TaskImagePanel this_ = this;
		if(this.getComponents() != null){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new CommonSwingWorker(new Runnable() {
						public void run() {
							this_.scrollRectToVisible(new Rectangle(0, 0));
							for(int i = 0; i < this_.getComponents().length; i ++){
								if(this_.getComponents()[i] instanceof AJPanel){
									AJPanel p = (AJPanel) this_.getComponents()[i];
									for(int j = 0; j < p.getComponents().length; j ++){
										if(p.getComponents()[j].getName() != null && p.getComponents()[j].getName().startsWith("cover")){
											AJLabel l = (AJLabel) p.getComponents()[j];
											ImageIcon icon = (ImageIcon) l.getIcon();
											if(icon != null){
												double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
												int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
												l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
												l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
												//icon.getImage().flush();//解决加载图片不完全问题
												l.setImage(icon);
												l.setIcon(icon);
											}
											//break;
										}else{
											JProgressBar b = (JProgressBar) p.getComponents()[j];
											b.setString(getTaskInfo(mainWindow.runningTable.getTasks().get(Integer.parseInt(p.getName().split("\\|")[1]))));
										}
									}
									//p.updateUI();
								}
							}
							this_.updateUI();
							resetPanelHeight();
							mainWindow.tablePane.getVerticalScrollBar().setValue(mainWindow.tablePane.getVerticalScrollBar().getValue() + 1);
							try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
							mainWindow.tablePane.getVerticalScrollBar().setValue(mainWindow.tablePane.getVerticalScrollBar().getValue() - 1);
						}}).execute();
				}
			});
		}
	}
	public void resetPanelHeight(){
		resetPanelHeight(true);
	}
	public void resetPanelHeight(boolean wait){
		try {
			if(wait){
				Thread.sleep(100);
			}
			int maxheight = 0;
			for(int i = 0; i < this.getComponents().length; i ++){
				if(maxheight < ((int)this.getComponents()[i].getLocation().getY() + this.getComponents()[i].getHeight())){
					maxheight = ((int)this.getComponents()[i].getLocation().getY() + this.getComponents()[i].getHeight());
				}
			}
			if(maxheight == 0){
				resetPanelHeight(wait);
			}else{
				this.setPreferredSize(new Dimension(this.getWidth(), maxheight + 10));	
				this.updateUI();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void init(){
		init(mainWindow.tasks);
	}
	public void flush(final Task task){
		flush(task, false);
	}
	public void flush(final Task task, final boolean iconload){
		final TaskImagePanel this_= this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CommonSwingWorker(new Runnable() {
					public void run() {
						Component[] oldcomps = this_.getComponents();
						AJPanel p = null;
						for(Component comp : oldcomps){
							if(comp.getName() != null && comp.getName().startsWith(task.getId() + "|")){
								p = (AJPanel)comp;
								break;
							}
						}
						if(p != null){
							AJLabel l = (AJLabel)p.getComponent(0);
							if(iconload || l.getIcon() == null){
								String path = task.getSaveDir() + "/cover.jpg";
								File cover = new File(path);
								if(cover != null && cover.exists()){
									try{
										ImageIcon icon = new ImageIcon(ImageIO.read(cover));
										double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
										int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
										l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
										l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
										l.setImage(icon);
										l.setIcon(icon);
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}
							JProgressBar bar = (JProgressBar)p.getComponent(1);
							bar.setValue(task.getCurrent());
							bar.setString(getTaskInfo(task));
							renderSelectBarColor(p, bar, true, task);
						}
					}
				}).execute();
			}
		});
	}
	
	public void init(final List<Task> tasks){
		final TaskImagePanel this_ = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CommonSwingWorker(new Runnable() {
					public void run() {
						this_.removeAll();
						if(tasks != null && tasks.size() > 0){
							this_.scrollRectToVisible(new Rectangle(0, 0));
							List<Task> ptasks = new ArrayList<Task>();
							for(int i = (page - 1) * PAGESIZE; i < page * PAGESIZE && i < tasks.size(); i ++){
								ptasks.add(tasks.get(i));
							}
							//this_.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 20, ((ptasks.size() / (Toolkit.getDefaultToolkit().getScreenSize().width / mainWindow.setting.getCoverWidth())) + 8) * (mainWindow.setting.getCoverHeight() + 20)));
							if(imageTaskPager != null){
								imageTaskPager.setVisible(false);
							}
							selectIndex = mainWindow.runningTable.getSelectedRow();
							for(int i = 0; i < ptasks.size(); i ++){
								//判断AJPanel是否存在
								AJPanel p = null;JProgressBar bar = null;
								for(AJPanel comp : allPanels){
									if(comp.getName() != null && comp.getName().startsWith(ptasks.get(i).getId() + "|")){
										p = comp;
										allPanels.remove(comp);
										break;
									}
								}
								//获取选中行
								boolean selected = ((page - 1) * PAGESIZE + i) == selectIndex;
								if(p != null){
									//name规则：taskID|list索引
									p.setName(ptasks.get(i).getId() + "|" + ((page - 1) * PAGESIZE + i));
									bar = (JProgressBar)p.getComponent(1);
									bar.setString(getTaskInfo(ptasks.get(i)));
								}else{
									AJLabel l = null;
									if(allPanels.size() >= ptasks.size()){
										//组件重用
										p = allPanels.remove(allPanels.size() - 1);
										l = (AJLabel) p.getComponent(0);
										bar = (JProgressBar) p.getComponent(1);
									}else{
										p = new AJPanel();
										p.setLayout(new BorderLayout());
										p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
										p.setBackground(Color.WHITE);
										p.setForeground(Color.WHITE);
										p.addMouseListener(panelMouselistener);
										l = new AJLabel();
										l.setOpaque(true);
										l.setBackground(Color.BLACK);
										l.setForeground(Color.WHITE);
										l.setFont(FontConst.Microsoft_BOLD_12);
										l.setVerticalTextPosition(JLabel.TOP);
										bar = new JProgressBar(0, 1);
										bar.setBackground(Color.WHITE);
										bar.setBorder(null);
										bar.setStringPainted(true);
										bar.setFont(FontConst.Microsoft_BOLD_11);
										bar.setPreferredSize(new Dimension(110, 13));
									}
									//name规则：taskID|list索引
									p.setName(ptasks.get(i).getId() + "|" + ((page - 1) * PAGESIZE + i));
									p.setToolTipText(ptasks.get(i).getDisplayName(mainWindow.setting));
									
									l.setName("cover" + ptasks.get(i).getId());
									if(l.getIcon() != IconManager.getIcon("init")){
										ImageIcon icon = IconManager.getIcon("init");
										double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
										int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
										l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
										l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
										l.setImage(icon);
										l.setIcon(icon);
									}
									
									bar.setMaximum(ptasks.get(i).getTotal());
									bar.setString(getTaskInfo(ptasks.get(i)));
									bar.setValue(ptasks.get(i).getCurrent());
									p.add(l, BorderLayout.SOUTH);
									p.add(bar, BorderLayout.NORTH);
								}
								renderSelectBarColor(p, bar, selected, ptasks.get(i));
								if(allPanels.size() < PAGESIZE){
									allPanels.add(0, p);
								}
								this_.add(p, i);
								this_.updateUI();
							}
							resetPanelHeight();
							
							int totalPage = tasks.size() % PAGESIZE == 0 ? tasks.size() / PAGESIZE : tasks.size() / PAGESIZE + 1;
							if(totalPage > 1){
								if(imageTaskPager == null){
									imageTaskPager = new AJPager(40 , mainWindow.infoTabbedPane.getY() + 25, (int)(mainWindow.taskInfoPanel.getWidth() - 80), 40, new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											JButton btn = (JButton) e.getSource();
											this_.page = Integer.parseInt(btn.getName());
											mainWindow.runningTable.setRowSelectionInterval((this_.page - 1) * PAGESIZE, (this_.page - 1) * PAGESIZE);
											mainWindow.infoTabbedPane.flushTab(mainWindow.tasks.get((this_.page - 1) * PAGESIZE));
											this_.init();
										}
									});
									((FlowLayout)imageTaskPager.getLayout()).setAlignment(FlowLayout.RIGHT);
									JButton btn = new AJButton("顶部⇧");
									btn.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											mainWindow.tablePane.getVerticalScrollBar().setValue(0);
										}
									});
									imageTaskPager.setMaxPage(6);
									imageTaskPager.setExt(new JComponent[]{btn});
									imageTaskPager.setOpaque(false);
									mainWindow.getLayeredPane().add(imageTaskPager, 10, 0);
								}
								imageTaskPager.setTotal(tasks.size());
								imageTaskPager.change(totalPage, page);
								imageTaskPager.updateUI();
								imageTaskPager.setVisible(true);
							}
							mainWindow.tablePane.getVerticalScrollBar().setValue(1);
							try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
							mainWindow.tablePane.getVerticalScrollBar().setValue(0);
						}
					}
				}).execute();
			}
		});
	}
	
	private String getTaskInfo(Task task){
		StringBuffer txtsb = new StringBuffer("");
		if(task.isSearched()){
			txtsb.append("[搜] ");
		}
		if(task.getStatus() != TaskStatus.COMPLETED && task.getCurrent() > 0){
			txtsb.append(task.getCurrent()).append("/");
		} 
		txtsb.append(task.getTotal()).append(" ");
		if(task.getStatus() == TaskStatus.WAITING){
			txtsb.append(TaskStatus.WAITING.getStatus()).append(" ");
		}
		/*String statusColor = "";
		if(task.getStatus() == TaskStatus.UNSTARTED){
			statusColor = "#5f392d";
		}else if(task.getStatus() == TaskStatus.STARTED){
			statusColor = "#4192ff";
		}else if(task.getStatus() == TaskStatus.STOPED){
			statusColor = "#000159";
		}else if(task.getStatus() == TaskStatus.COMPLETED){
			statusColor = "#419141";
		}else if(task.getStatus() == TaskStatus.WAITING){
			statusColor = "#d2691e";
		}*/
		if(task.getStatus() == TaskStatus.STARTED){
			txtsb.append(task.getDownSpeed().toLowerCase());
		}else/* if(mainWindow.setting.getCoverWidth() > 100)*/{
			txtsb.append(task.getType())/*.append(" ").append(task.getLanguage())*/;
		}
		if(mainWindow.setting.getCoverWidth() == 350){
			if(StringUtils.isNotBlank(task.getRealLanguage())){
				txtsb.append(" ").append(task.getRealLanguage());
			}
		}
		if(task.isReaded()){
			txtsb.append(" √");
		}
		//txtsb.append("</div>");
		return txtsb.toString();
	}
	
	public void runTimer(){
		timerTask = new TimerTask() {
			public void run() {
				if(scrollValue == -1 || scrollValue != mainWindow.tablePane.getVerticalScrollBar().getValue()){
					scrollValue = mainWindow.tablePane.getVerticalScrollBar().getValue();
					Component[] comps = mainWindow.taskImagePanel.getComponents();
					AJLabel l;
					for(int i = 0; i < comps.length; i ++){
						l = (AJLabel) ((JPanel)comps[i]).getComponent(0);
						if((comps[i].getHeight() + comps[i].getY())  >= mainWindow.tablePane.getVerticalScrollBar().getValue()
								&& comps[i].getY() - mainWindow.tablePane.getVerticalScrollBar().getValue() <= mainWindow.tablePane.getHeight()){
							//加载真实封面
							if(l.getImage() == IconManager.getIcon("init") || l.getImage() == null){
								final String path = mainWindow.runningTable.getTasks().get(Integer.parseInt(comps[i].getName().split("\\|")[1])).getSaveDir() + "/cover.jpg";
								File cover = new File(path);
								if(cover != null && cover.exists()){
									try{
										ImageIcon icon = new ImageIcon(ImageIO.read(cover));
										double w = icon.getIconWidth() > mainWindow.setting.getCoverWidth() ? mainWindow.setting.getCoverWidth() : icon.getIconWidth();
										int height = w > icon.getIconWidth() ? icon.getIconHeight() : (int)(icon.getIconHeight() * (w / icon.getIconWidth()));
										l.setSize((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height);
										l.setPreferredSize(new Dimension((int)w, height > mainWindow.setting.getCoverHeight() ? mainWindow.setting.getCoverHeight() : height));
										l.setImage(icon);
										l.setIcon(icon);
									}catch(Exception e){
										e.printStackTrace();
									}
								}
								resetPanelHeight(false);
							}
						}else{
							//更换为预载封面
							if(l.getImage() != null){
								try{
									l.setImage(null);
									l.setIcon(null);
								}catch(Exception e){
									e.printStackTrace();
								}
								resetPanelHeight(false);
							}
						}
					}
					System.gc();
				}
			}
		};
		timer = new Timer(true);
		//1秒执行一次
		timer.scheduleAtFixedRate(timerTask, 1000, 1000);
	}
	
	public void stopTimer(){
		timer.cancel();
		timerTask.cancel();
		timer = null;
		timerTask = null;
	}
	
	public MouseListener panelMouselistener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if(selectIndex - (page - 1) * PAGESIZE > -1){
				JProgressBar bar = (JProgressBar) ((AJPanel)this_.getComponents()[selectIndex - (page - 1) * PAGESIZE]).getComponent(1);
				renderSelectBarColor((AJPanel)this_.getComponents()[selectIndex - (page - 1) * PAGESIZE], bar, false, mainWindow.runningTable.getTasks().get(selectIndex));
			}
			AJPanel p = (AJPanel)e.getSource();
			selectIndex = Integer.parseInt(p.getName().split("\\|")[1]);
			renderSelectBarColor(p, (JProgressBar)p.getComponent(1), true, mainWindow.runningTable.getTasks().get(selectIndex));
			//同步任务表格的选中状态
			mainWindow.runningTable.setRowSelectionInterval(selectIndex, selectIndex);
			//切换信息面板tab
			mainWindow.infoTabbedPane.flushTab(mainWindow.tasks.get(selectIndex));
			if(e.getButton() == MouseEvent.BUTTON3){
				Task task = mainWindow.runningTable.getTasks().get(selectIndex);
				mainWindow.tablePopupMenu.show(task, p, e.getPoint().x - 1, e.getPoint().y - 1);
			}else{
				//双击切换
				if(e.getClickCount() == 2){
					//任务开始或暂停
					if(mainWindow.runningTable.rebuild){
						Tracker.println(TaskingTable.class, "正在重建任务");
						return;
					}
					Task task = mainWindow.runningTable.getTasks().get(selectIndex);
					//如果状态为未开始或者已暂停，则将状态改为下载中，随后开启下载线程
					if(task.getStatus() == TaskStatus.UNSTARTED || task.getStatus() == TaskStatus.STOPED){
						mainWindow.runningTable.startTask(task);
					}
					//如果状态为下载中，则将状态改为已暂停，随后将下载线程取消掉
					else if(task.getStatus() == TaskStatus.STARTED || task.getStatus() == TaskStatus.WAITING){
						mainWindow.runningTable.stopTask(task);
					}
					//如果状态为未创建，则开启创建线程
					else if(task.getStatus() == TaskStatus.UNCREATED){
						Tracker.println(getClass(), task.getName() + ":重新采集");
						task.setReCreateWorker(new ReCreateWorker(task, mainWindow));
						task.getReCreateWorker().execute();
					}
				}
			}
		}
		public void mouseEntered(MouseEvent e) {
			AJPanel p = (AJPanel)e.getSource();
			renderSelectBarColor(p, (JProgressBar)p.getComponent(1), true,  mainWindow.runningTable.getTasks().get(Integer.parseInt(p.getName().split("\\|")[1])));
		}

		public void mouseExited(MouseEvent e) {
			AJPanel p = (AJPanel)e.getSource();
			if(selectIndex != Integer.parseInt(p.getName().split("\\|")[1])){
				renderSelectBarColor(p, (JProgressBar)p.getComponent(1), false,  mainWindow.runningTable.getTasks().get(Integer.parseInt(p.getName().split("\\|")[1])));
			}else{
				renderSelectBarColor(p, (JProgressBar)p.getComponent(1), true,  mainWindow.runningTable.getTasks().get(Integer.parseInt(p.getName().split("\\|")[1])));
			}
		}
	};
	
	public void renderSelectBarColor(JPanel p, JProgressBar bar, boolean selected, Task task){
		if(selected){
			bar.setForeground(progressBarSelectedColor);
			p.setBorder(BorderFactory.createLineBorder(progressBarSelectedColor, 2));
		}else{
			if(task.isSearched()){
				bar.setForeground(progressBarSearchedColor);
				p.setBorder(BorderFactory.createLineBorder(progressBarSearchedColor, 2));
			}else if(task.getStatus() == TaskStatus.COMPLETED){
				bar.setForeground(progressBarCompletedColor);
				p.setBorder(BorderFactory.createLineBorder(progressBarCompletedColor, 2));
			}else{
				bar.setForeground(progressBarColor);
				p.setBorder(BorderFactory.createLineBorder(progressBarColor, 2));
			}
		}
	}
}
