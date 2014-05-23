package org.arong.egdownloader.ui.window.form;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.window.AboutMenuWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
/**
 * 新建下载任务窗口
 * @author 阿荣
 * @since 2014-05-21
 *
 */
public class AddFormDialog extends JDialog {

	private static final long serialVersionUID = 6680144418171641216L;
	
	private JLabel urlLabel;
	private JTextField urlField;
	private JLabel saveDirLabel;
	private JTextField saveDirField;
	private JButton chooserBtn;
	private JButton addTaskBtn;
	private JLabel tipLabel;
	private JFileChooser saveDirChooser;
	
	public AddFormDialog(final JFrame mainWindow){
		this.setTitle("新建任务");
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + "add.gif")).getImage());
		this.setSize(480, 210);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(mainWindow);
		//添加窗口聚焦监听器
		this.addWindowFocusListener(new WindowFocusListener() {
			// 当失去活动状态的时候此窗口被隐藏
			public void windowLostFocus(WindowEvent e) {
				AddFormDialog window = (AddFormDialog) e.getSource();
				window.setVisible(false);
			}
			public void windowGainedFocus(WindowEvent e) {}
		});
		this.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				mainWindow.setVisible(true);
				mainWindow.enable();
			}
			public void windowClosing(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		tipLabel = new AJLabel("提示：保存目录不用填写具体文件夹名，下载器会自动生成", Color.LIGHT_GRAY, 80, 5, this.getWidth() - 80, 30);
		
		urlLabel = new AJLabel("下载地址", Color.BLUE, 5, 40, 60, 30);
		urlField = new AJTextField("urlField", 65, 40, 395, 30);
		saveDirLabel = new AJLabel("保存目录", Color.BLUE, 5, 80, 60, 30);
		saveDirField = new AJTextField("saveDirField", 65, 80, 320, 30);
		chooserBtn = new AJButton("浏览", "chooserBtn", "eye.png", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window addFormDialog) {
				AddFormDialog this_ = (AddFormDialog)addFormDialog;
				int result = this_.saveDirChooser.showOpenDialog(this_);
				File file = null;  
                if(result == JFileChooser.APPROVE_OPTION) {  
                    file = this_.saveDirChooser.getSelectedFile();  
                    if(!file.isDirectory()) {  
                        JOptionPane.showMessageDialog(this_, "你选择的目录不存在");
                        return ;
                    }  
                    String path = file.getAbsolutePath();
                    this_.saveDirField.setText(path);
                }
			}
		}) , 400, 80, 60, 30);
		
		addTaskBtn = new AJButton("新建", "", "add.gif", new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window addFormDialog) {
				AddFormDialog this_ = (AddFormDialog)addFormDialog;
				if("".equals(this_.urlField.getText().trim())){
					JOptionPane.showMessageDialog(this_, "请填写下载地址");
				}else if("".equals(this_.saveDirField.getText().trim())){
					JOptionPane.showMessageDialog(this_, "请选择保存路径");
				}else{
					//具体操作
					
				}
			}
		}), (this.getWidth() - 100) / 2, 130, 100, 30);
		saveDirChooser = new JFileChooser("/");
		saveDirChooser.setDialogTitle("选择保存目录");//选择框标题
		saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
		ComponentUtil.addComponents(this.getContentPane(), addTaskBtn, urlLabel, urlField, saveDirLabel, saveDirField, chooserBtn, tipLabel);
	}
	public void emptyField(){
		urlField.setText("");
		saveDirField.setText("");
	}
}
