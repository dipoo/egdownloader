package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 编辑任务信息窗口
 * @author 阿荣
 * @since 2014-09-18
 */
public class EditWindow extends JDialog {

	private static final long serialVersionUID = -4627145534664363270L;
	
	private JButton editTaskBtn;
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel subnameLabel;
	private JTextField subnameField;
	private JLabel tagLabel;
	private JTextField tagField;
	private Task task;
	EgDownloaderWindow mainWindow;
	
	public EditWindow(EgDownloaderWindow _mainWindow, Task _task){
		this.mainWindow = _mainWindow;
		this.setTask(_task);
		this.setTitle("编辑任务信息");
		this.setSize(480, 250);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		nameLabel = new AJLabel("名称：", Color.BLUE, 5, 10, 40, 30);
		nameField = new AJTextField("", 65, 10, 395, 30);
		subnameLabel = new AJLabel("子标题", Color.BLUE, 5, 55, 40, 30);
		subnameField = new AJTextField("", 65, 55, 395, 30);
		tagLabel = new AJLabel("标签：", Color.BLUE, 5, 100, 40, 30);
		tagField = new AJTextField("", 65, 100, 395, 30);
		editTaskBtn = new AJButton("保存", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("save"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window dialog, MouseEvent event) {
				String name = nameField.getText().trim();
				String subname = subnameField.getText().trim();
				String tag = tagField.getText().trim();
				if("".equals(name)){
					JOptionPane.showMessageDialog(null, "请填写任务名称");
				}else{
					if("".equals(subname)){
						subname = null;
					}
					if("".equals(tag)){
						tag = "一般";
					}
					task.setName(name);
					task.setSubname(subname);
					task.setTag(tag);
					//保存
					mainWindow.taskDbTemplate.update(task);
					//更新table
					mainWindow.runningTable.updateUI();
					dialog.dispose();
				}
			}
		}), 190, 145, 100, 30);
		ComponentUtil.addComponents(getContentPane(), nameLabel, nameField,
				subnameLabel, subnameField, tagLabel, tagField, editTaskBtn);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				EditWindow this_ = (EditWindow) e.getSource();
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				this_.dispose();
			}
			public void windowDeactivated(WindowEvent e) {
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
			}
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
			
		});
	}
	public void initInfo(){
		nameField.setText(task.getName());
		subnameField.setText(task.getSubname());
		tagField.setText(task.getTag());
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
}
