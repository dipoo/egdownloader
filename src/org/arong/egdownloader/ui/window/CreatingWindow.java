package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJProgressBar;
/**
 * 新建任务细节显示窗口
 * @author 阿荣
 * @since 2014-06-01
 */
public class CreatingWindow extends JDialog {

	private static final long serialVersionUID = -2544191890083257820L;
	
	public JFrame mainWindow;
	public JProgressBar bar;
	public JLabel totalLabel;
	public JLabel nameLabel;
	public JLabel subnameLabel;
	public JLabel sizeLabel;
	public JLabel languageLabel;
	public JLabel typeLabel;
	
	public CreatingWindow(JFrame window){
		this.mainWindow = window;
		this.setTitle("任务创建中");
		this.setSize(540, 150);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(this.mainWindow);
		this.setBackground(Color.WHITE);
		
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CreatingWindow window = (CreatingWindow) e.getSource();
				window.mainWindow.setEnabled(true);
				window.mainWindow.setVisible(true);
				window.dispose();
			}
			//窗体由激活状态变成非激活状态
			/*public void windowDeactivated(WindowEvent e) {
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				CreatingWindow window = (CreatingWindow) e.getSource();
				window.dispose();
			}*/
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
		});
		
		nameLabel = new AJLabel("名称：", Color.BLACK, 10, 5, 520, 20);
		nameLabel.setVisible(false);
		subnameLabel = new AJLabel("子标题：", Color.BLACK, 10, 25, 520, 20);
		subnameLabel.setVisible(false);
		totalLabel = new AJLabel("数目：", Color.BLACK, 10, 45, 100, 20);
		totalLabel.setVisible(false);
		sizeLabel = new AJLabel("大小：", Color.BLACK, 120, 45, 120, 20);
		sizeLabel.setVisible(false);
		languageLabel = new AJLabel("语言：", Color.BLACK, 250, 45, 120, 20);
		languageLabel.setVisible(false);
		typeLabel = new AJLabel("类别：", Color.BLACK, 380, 45, 120, 20);
		typeLabel.setVisible(false);
		bar = new AJProgressBar(40, 75, 450, 20, 0, 100);
		bar.setStringPainted(true);
		ComponentUtil.addComponents(getContentPane(), nameLabel, subnameLabel,
				totalLabel, sizeLabel, languageLabel, typeLabel, bar);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		//关闭事件
		if(e.getID() == WindowEvent.WINDOW_CLOSING){
			//do nothing
		}else{
			super.processWindowEvent(e);
		}
	}

	public void reset(){
		nameLabel.setText("名称：");
		subnameLabel.setText("子标题：");
		totalLabel.setText("数目：");
		sizeLabel.setText("大小：");
		languageLabel.setText("语言：");
		typeLabel.setText("类别：");
		nameLabel.setVisible(false);
		subnameLabel.setVisible(false);
		totalLabel.setVisible(false);
		sizeLabel.setVisible(false);
		languageLabel.setVisible(false);
		typeLabel.setVisible(false);
		bar.setValue(0);
	}
	
	public void showInfo(Task task){
		nameLabel.setText(nameLabel.getText() + task.getName());
		subnameLabel.setText(subnameLabel.getText() + task.getSubname());
		totalLabel.setText(totalLabel.getText() + task.getTotal());
		sizeLabel.setText(sizeLabel.getText() + task.getSize());
		languageLabel.setText(languageLabel.getText() + task.getLanguage());
		typeLabel.setText(typeLabel.getText() + task.getType());
		nameLabel.setVisible(true);
		subnameLabel.setVisible(true);
		totalLabel.setVisible(true);
		sizeLabel.setVisible(true);
		languageLabel.setVisible(true);
		typeLabel.setVisible(true);
		bar.setMaximum(task.getTotal());
	}

	public void dispose() {
		mainWindow.setEnabled(true);
		mainWindow.setVisible(true);
		super.dispose();
	}

}
