package org.arong.egdownloader.ui.window.form;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.arong.egdownloader.db.impl.PictureDom4jDbTemplate;
import org.arong.egdownloader.db.impl.SettingDom4jDbTemplate;
import org.arong.egdownloader.db.impl.TaskDom4jDbTemplate;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.GroupWindow;
import org.arong.egdownloader.ui.window.InitWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.FileUtil2;
/**
 * 新建任务窗口
 * @author dipoo
 * @since 2015-01-08
 */
public class AddGroupDialog extends JDialog {

	private static final long serialVersionUID = -1381002320125000355L;
	
	public AddGroupDialog(final GroupWindow window, final EgDownloaderWindow mainWindow){
		this.setTitle("新建任务组");
		this.setIconImage(IconManager.getIcon("add").getImage());
		this.setSize(300, 120);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(window);
		
		JLabel groupNameLabel = new AJLabel("名称：", Color.BLUE, 15, 20, 40, 30);
		final JTextField groupNameField = new AJTextField("", "", 50, 20, 160, 30);
		JButton addBtn = new AJButton("新建", IconManager.getIcon("add"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window dialog, MouseEvent event) {
				AddGroupDialog addGroupDialog = (AddGroupDialog) dialog;
				String groupName = groupNameField.getText().trim();
				//验证名称
				if("".equals(groupName)){
					JOptionPane.showMessageDialog(null, "名称不能为空");
				}else if(! FileUtil2.dirValidate(groupName)){
					JOptionPane.showMessageDialog(null, "名称不能包含? | * . < > : / \\等特殊字符");
				}else{
					FileUtil2.ifNotExistsThenCreate(ComponentConst.ROOT_DATA_PATH);
					File dataFile = new File(ComponentConst.ROOT_DATA_PATH);
					for(File file : dataFile.listFiles()){
						if(file.getName().equals(groupName)){
							JOptionPane.showMessageDialog(null, "该任务组已存在");
							return;
						}
					}					
					if (mainWindow != null) {
						//保存前一个任务组的数据
						mainWindow.saveTaskGroupData();
					}
					//if(window)
					//更新数据路径
					ComponentConst.groupName = groupName;
					ComponentConst.changeDataPath(groupName);
					ComponentConst.changeDataXmlPath();
					if(mainWindow != null){
						FileUtil2.ifNotExistsThenCreate(ComponentConst.getXmlDirPath());
						/**
						 * 更新dom
						 */
						SettingDom4jDbTemplate.updateDom();
						TaskDom4jDbTemplate.updateDom();
						PictureDom4jDbTemplate.updateDom();
						mainWindow.dispose();
					}
					if(window != null){
						window.dispose();
					}
					addGroupDialog.dispose();
					new InitWindow();
				}
			}
		}), 220, 20, 60, 30);
		addBtn.setUI(AJButton.blueBtnUi);
		ComponentUtil.addComponents(getContentPane(), groupNameLabel, groupNameField, addBtn);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				AddGroupDialog w = (AddGroupDialog)e.getSource();
				w.dispose();
			}
			//窗体由激活状态变成非激活状态
			public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				AddGroupDialog w = (AddGroupDialog)e.getSource();
				w.dispose();
			}
		});
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
