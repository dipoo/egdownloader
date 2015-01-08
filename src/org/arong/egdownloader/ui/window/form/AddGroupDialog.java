package org.arong.egdownloader.ui.window.form;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import org.arong.egdownloader.ui.window.InitWindow;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.FileUtil;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
/**
 * 新建任务窗口
 * @author dipoo
 * @since 2015-01-08
 */
public class AddGroupDialog extends JDialog {

	private static final long serialVersionUID = -1381002320125000355L;
	
	public AddGroupDialog(final JFrame window){
		this.setTitle("新建任务组");
		this.setIconImage(new ImageIcon(getClass().getResource(ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("add"))).getImage());
		this.setSize(300, 100);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(window);
		
		JLabel groupNameLabel = new AJLabel("名称：", Color.BLUE, 15, 20, 40, 30);
		final JTextField groupNameField = new AJTextField("", "", 50, 20, 160, 30);
		JButton addBtn = new AJButton("新建", "", ComponentConst.SKIN_NUM + ComponentConst.SKIN_ICON.get("add"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window dialog, MouseEvent event) {
				AddGroupDialog addGroupDialog = (AddGroupDialog) dialog;
				String groupName = groupNameField.getText().trim();
				//验证名称
				if("".equals(groupName)){
					JOptionPane.showMessageDialog(null, "名称不能为空");
				}else if(! FileUtil.dirValidate(groupName)){
					JOptionPane.showMessageDialog(null, "名称不能包含? | * . < > : / \\等特殊字符");
				}else{
					//更新数据路径
					ComponentConst.groupName = groupName;
					ComponentConst.changeDataPath(groupName);
					ComponentConst.changeDataXmlPath();
					addGroupDialog.dispose();
					window.dispose();
					new InitWindow();
				}
			}
		}), 220, 20, 60, 30);
		addBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
		ComponentUtil.addComponents(getContentPane(), groupNameLabel, groupNameField, addBtn);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				window.setVisible(true);
				window.setEnabled(true);
				AddGroupDialog w = (AddGroupDialog)e.getSource();
				w.dispose();
			}
		});
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(true);
	}
}
