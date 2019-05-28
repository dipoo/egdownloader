package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJCheckBox;
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
	private JLabel startLabel;
	private JTextField startField;
	private JLabel endLabel;
	private JTextField endField;
	private JCheckBox originalCheckBox;
	private Task task;
	EgDownloaderWindow mainWindow;
	
	public EditWindow(EgDownloaderWindow _mainWindow, Task _task){
		this.mainWindow = _mainWindow;
		this.setTask(_task);
		this.setTitle("编辑任务信息");
		this.setSize(600, 250);
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		nameLabel = new AJLabel("名称：", Color.BLUE, 5, 10, 40, 30);
		nameField = new AJTextField("", 65, 10, 500, 30);
		subnameLabel = new AJLabel("子标题", Color.BLUE, 5, 55, 40, 30);
		subnameField = new AJTextField("", 65, 55, 500, 30);
		tagLabel = new AJLabel("标签：", Color.BLUE, 5, 100, 40, 30);
		tagField = new AJTextField("", 65, 100, 160, 30);
		startLabel = new AJLabel("开始：", Color.BLUE, 250, 100, 40, 30);
		startField = new AJTextField("", 290, 100, 50, 30);
		endLabel = new AJLabel("结束：", Color.BLUE, 360, 100, 40, 30);
		endField = new AJTextField("", 400, 100, 50, 30);
		originalCheckBox = new AJCheckBox("是否下载原图", Color.BLUE, task.isOriginal());
		originalCheckBox.setBounds(460, 100, 150, 30);
		editTaskBtn = new AJButton("保存", IconManager.getIcon("save"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window dialog, MouseEvent event) {
				String name = nameField.getText().trim();
				String subname = subnameField.getText().trim();
				String tag = tagField.getText().trim();
				String start = startField.getText().trim();
				String end = endField.getText().trim();
				boolean original = originalCheckBox.isSelected();
				if("".equals(name)){
					JOptionPane.showMessageDialog(dialog, "请填写任务名称");
				}else if(!start.matches("^[0-9]*[1-9][0-9]*$")){
					JOptionPane.showMessageDialog(dialog, "[开始]请填写正整数");
				}else if(!end.matches("^[0-9]*[1-9][0-9]*$")){
					JOptionPane.showMessageDialog(dialog, "[结束]请填写正整数");
				}else if(Integer.parseInt(start) > Integer.parseInt(end)){
					JOptionPane.showMessageDialog(dialog, "[开始]不能大于[结束]");
				}else if(Integer.parseInt(end) > task.getTotal()){
					JOptionPane.showMessageDialog(dialog, "[结束]不能大于图片总数：" + task.getTotal());
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
					task.setStart(Integer.parseInt(start));
					task.setEnd(Integer.parseInt(end));
					task.setOriginal(original);
					//保存
					mainWindow.taskDbTemplate.update(task);
					dialog.dispose();
				}
			}
		}), 250, 145, 100, 30);
		ComponentUtil.addComponents(getContentPane(), nameLabel, nameField,
				subnameLabel, subnameField, tagLabel, tagField, startLabel,
				startField, endLabel, endField, originalCheckBox, editTaskBtn);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				EditWindow this_ = (EditWindow) e.getSource();
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				this_.dispose();
			}
			//窗体由激活状态变成非激活状态
			/*public void windowDeactivated(WindowEvent e) {
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
			}*/
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
			
		});
	}
	public void initInfo(){
		nameField.setText(task.getName());
		subnameField.setText(task.getSubname());
		tagField.setText(task.getTag());
		startField.setText(task.getStart() + "");
		endField.setText(task.getEnd() + "");
		originalCheckBox.setSelected(task.isOriginal());
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public void dispose() {
		mainWindow.setEnabled(true);
		mainWindow.setVisible(true);
		super.dispose();
	}
}
