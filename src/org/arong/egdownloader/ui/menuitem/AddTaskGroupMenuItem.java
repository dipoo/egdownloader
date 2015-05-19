package org.arong.egdownloader.ui.menuitem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.GroupWindow;
import org.arong.egdownloader.ui.window.form.AddGroupDialog;
/**
 * 新建/切换任务组菜单项
 * @author dipoo
 * @since 2015-01-08
 */
public class AddTaskGroupMenuItem extends JMenuItem {

	private static final long serialVersionUID = 2616082975789594278L;
	
	public static final int ADDACTION = 1;
	public static final int CHANGEACTION = 2;

	public AddTaskGroupMenuItem(String text, final EgDownloaderWindow window, final int action){
		super(text);
		this.setIcon(IconManager.getIcon(action == ADDACTION ? "add" : "change"));
		this.setForeground(new Color(0,0,85));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				EgDownloaderWindow mainWindow = window;
				if(action == ADDACTION){//新增
					new AddGroupDialog(null, mainWindow);
				}else if(action == CHANGEACTION){//切换
					File dataFile = new File(ComponentConst.ROOT_DATA_PATH);
					if(!dataFile.exists()){
						dataFile.mkdirs();
						JOptionPane.showMessageDialog(mainWindow, "不存在其他任务组");
					}else{
						File[] files = dataFile.listFiles();
						List<File> groups = new ArrayList<File>();
						for(File file : files){
							if(file.isDirectory()){
								groups.add(file);
							}
						}
						if(groups.size() > 0){
							new GroupWindow(groups, mainWindow);
						}else{
							JOptionPane.showMessageDialog(mainWindow, "不存在其他任务组");
						}
					}
				}
				
			}
		});
	}
}
