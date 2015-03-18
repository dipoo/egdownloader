package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.util.Tracker;
/**
 * 简单搜索输入框窗口
 * @author dipoo
 * @since 2014-11-19
 */
public class SimpleSearchWindow extends JDialog {

	private static final long serialVersionUID = -4022333509001414223L;
	
	public JTextField keyTextField;
	EgDownloaderWindow mainWindow;
	
	public SimpleSearchWindow(final EgDownloaderWindow mainWindow){
		this.mainWindow = mainWindow;
		this.setTitle("简单任务搜索");
		this.setSize(600, 160);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				JDialog w = (JDialog)e.getSource();
				w.dispose();
			}
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
		});
		final SimpleSearchWindow this_ = this;
		JLabel descLabel = new AJLabel("Tips:搜索的结果会显示在控制台", Color.GRAY, 200, 10, 180, 30);
		JLabel keyLabel = new AJLabel("关键字：", Color.BLUE, 10, 50, 50, 30);
		keyTextField = new AJTextField("", "", 70, 50, 430, 30);
		JButton searchBtn = new AJButton("搜索", "", new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String key = keyTextField.getText();
				if(key == null || "".equals(key.trim())){
					JOptionPane.showMessageDialog(this_, "请输入关键字！");
					return;
				}
				TaskingTable table = (TaskingTable)mainWindow.runningTable;
				List<Task> allTasks = table.getTasks();
				int j = 0;
				for(int i = 0; i < allTasks.size(); i++){
					j ++;
					if(allTasks.get(i).getName().toLowerCase().contains(key.toLowerCase())){
						Tracker.println("简单搜索[" + key + "]:" + (i + 1) + "、" + allTasks.get(i).getName());
					}else if(allTasks.get(i).getSubname().contains(key.toLowerCase())){
						Tracker.println("简单搜索[" + key + "]:" + (i + 1) + "、" +  allTasks.get(i).getSubname());
					}else{
						j --;
						continue;
					}
					//定位到第一条任务处
					if(j == 1){
						//使之选中
						table.setRowSelectionInterval(i, i);
						//定位
						table.scrollRectToVisible(table.getCellRect(i, 0, true));
					}
				}
				Tracker.println("[" + key + "]搜索完毕,结果【" + j + "】条。");
			}
		}, 510, 50, 60, 30);
		ComponentUtil.addComponents(this.getContentPane(), descLabel, keyLabel, keyTextField, searchBtn);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SimpleSearchWindow this_ = (SimpleSearchWindow) e.getSource();
				this_.dispose();
			}
		});
	}
	public void dispose() {
		mainWindow.setEnabled(true);
		mainWindow.setVisible(true);
		super.dispose();
	}
}
