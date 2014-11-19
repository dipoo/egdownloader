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
		
		JLabel keyLabel = new AJLabel("关键字：", Color.BLUE, 10, 40, 50, 40);
		keyTextField = new AJTextField("", "", 70, 40, 430, 40);
		JButton searchBtn = new AJButton("搜索", "", new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String key = keyTextField.getText();
				if(key == null || "".equals(key.trim())){
					JOptionPane.showMessageDialog(null, "请输入关键字！");
				}
				List<Task> allTasks = ((TaskingTable)mainWindow.runningTable).getTasks();
				int j = 0;
				for(int i = 0; i < allTasks.size(); i++){
					j ++;
					if(allTasks.get(i).getName().toLowerCase().contains(key.toLowerCase())){
						Tracker.println("简单搜索[" + key + "]:" + (i + 1) + "、" + allTasks.get(i).getName());
					}else if(allTasks.get(i).getSubname().contains(key.toLowerCase())){
						Tracker.println("简单搜索[" + key + "]:" + (i + 1) + "、" +  allTasks.get(i).getSubname());
					}else{
						j --;
					}
				}
				Tracker.println("[" + key + "]搜索完毕,结果【" + j + "】条。");
			}
		}, 510, 40, 60, 40);
		ComponentUtil.addComponents(this.getContentPane(), keyLabel, keyTextField, searchBtn);
		
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
