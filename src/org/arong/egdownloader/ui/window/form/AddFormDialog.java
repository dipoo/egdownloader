package org.arong.egdownloader.ui.window.form;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentUtil;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.listener.MouseAction;
import org.arong.egdownloader.ui.listener.OperaBtnMouseListener;
import org.arong.egdownloader.ui.swing.AJButton;
import org.arong.egdownloader.ui.swing.AJComboBox;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.egdownloader.ui.swing.AJTextField;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.work.CreateWorker;
import org.arong.egdownloader.ui.work.interfaces.IListenerTask;
import org.arong.util.ArrayUtil;
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
	private JLabel tagLabel;
	private JTextField tagField;
	private JComboBox tagComboBox;
	
	public JFrame mainWindow;
	
	public void initOrUpdateTagComboBox(Setting setting){
		if(setting.getTags() != null){
			String[] tags = setting.getTags().split("\\$￥");
			if(tagComboBox == null){
				tagComboBox = new AJComboBox(true, new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						AJComboBox s = (AJComboBox) ae.getSource();
						tagField.setText(s.getSelectedItem().toString());
					}
				}, "", 370, 80, 90, 30, 5, tags);
				tagField.setSize(300, 30);
				ComponentUtil.addComponents(this.getContentPane(), tagComboBox);
			}else{
				this.getContentPane().remove(tagComboBox);
				tagComboBox = null;
				initOrUpdateTagComboBox(setting);
			}
		}
	}
	
	public AddFormDialog(final JFrame mainWindow){
		this.mainWindow = mainWindow;
		this.setTitle("新建任务");
		this.setIconImage(IconManager.getIcon("add").getImage());
		this.setSize(480, 250);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(mainWindow);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//关闭后显示主界面
				//mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				AddFormDialog w = (AddFormDialog)e.getSource();
				w.dispose();
			}
			//窗体由激活状态变成非激活状态
			/*public void windowDeactivated(WindowEvent e) {
				//关闭后显示主界面
				mainWindow.setVisible(true);
				mainWindow.setEnabled(true);
				AddFormDialog w = (AddFormDialog)e.getSource();
				w.dispose();
			}*/
			public void windowActivated(WindowEvent e) {
				mainWindow.setEnabled(false);
			}
		});
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		tipLabel = new AJLabel("提示：保存目录不用填写具体文件夹名，下载器会自动生成", Color.LIGHT_GRAY, 80, 5, this.getWidth() - 80, 30);
		
		urlLabel = new AJLabel("任务地址", Color.BLUE, 5, 40, 60, 30);
		urlField = new AJTextField("urlField", 65, 40, 395, 30);
		saveDirLabel = new AJLabel("保存目录", Color.BLUE, 5, 120, 60, 30);
		saveDirField = new AJTextField("saveDirField", 65, 120, 320, 30);
		tagLabel = new AJLabel("标签", Color.BLUE, 5, 80, 60, 30);
		final Setting setting = ((EgDownloaderWindow)mainWindow).setting;
		tagField = new AJTextField("tag", 65, 80, setting.getTags() != null ? 300 : 395, 30);//395
		initOrUpdateTagComboBox(setting);
		
		saveDirField.setText(setting.getDefaultSaveDir() + "\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		chooserBtn = new AJButton("浏览", IconManager.getIcon("select"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window addFormDialog, MouseEvent e) {
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
		}) , 400, 120, 60, 30);
		
		addTaskBtn = new AJButton("新建", IconManager.getIcon("add"), new OperaBtnMouseListener(this, MouseAction.CLICK, new IListenerTask() {
			public void doWork(Window addFormDialog, MouseEvent event) {
				AddFormDialog this_ = (AddFormDialog)addFormDialog;
				String url = this_.urlField.getText().trim();
				String saveDir = this_.saveDirField.getText().trim();
				String tag = this_.tagField.getText().trim();
				if("".equals(url)){
					JOptionPane.showMessageDialog(this_, "请填写下载地址");
				}else if("".equals(saveDir)){
					JOptionPane.showMessageDialog(this_, "请选择保存路径");
				}else{
					if("".equals(tag)){
						tag = "一般";
					}
					EgDownloaderWindow mainWindow = (EgDownloaderWindow)this_.mainWindow;
					if(isValidUrl(mainWindow.setting, url)){
						//存到数据库中的地址统一不以/结尾，方便验证重复
						if("/".equals(url.substring(url.length() - 1, url.length()))){
							url = url.substring(0, url.length() - 1);
						}
						if(!setting.isHttps() && url.startsWith("https:")){
							url = url.replace("https:", "http:");
						}
						//重复性验证
						if(! mainWindow.taskDbTemplate.exsits("url", url)){
							if(addTaskBtn.isEnabled()){
								addTaskBtn.setEnabled(false);
							}else{
								return;
							}
							if(((EgDownloaderWindow)this_.mainWindow).creatingWindow == null){
								((EgDownloaderWindow)this_.mainWindow).creatingWindow = new CreatingWindow(mainWindow);
							}
							
							//分析tag（如果不存在，则添加到标签记忆中,如果存在，则将此标签置于最前面）
							String tags = setting.getTags();
							if(! "一般".equals(tag)){
								if(tags == null){
									setting.setTags(tag);
									//保存配置文件
									mainWindow.settingDbTemplate.update(setting);
								}else{
									String[] tagsArr = tags.split("\\" + Setting.TAGSPLIT);
									if(!ArrayUtil.exists(tagsArr, tag)){
										setting.setTags(tag + Setting.TAGSPLIT + setting.getTags());
									}else{
										//置于最前面
										tags = change(tags.split("\\" + Setting.TAGSPLIT), tag, Setting.TAGSPLIT);
										setting.setTags(tags);
									}
									//保存配置文件
									mainWindow.settingDbTemplate.update(setting);
								}
								initOrUpdateTagComboBox(setting);
							}
							//开始采集
							Task task = new Task(url, saveDir);
							task.setTag(tag);
							task.setCreateWorker(new CreateWorker(task, mainWindow));
							task.getCreateWorker().execute();
							addTaskBtn.setEnabled(true);
						}else{
							JOptionPane.showMessageDialog(this_, "此下载地址已存在");
						}
					}else{
						JOptionPane.showMessageDialog(this_, "下载地址不合法");
					}
				}
			}
		}), (this.getWidth() - 100) / 2, 170, 100, 30);
		saveDirChooser = new JFileChooser("/");
		saveDirChooser.setDialogTitle("选择保存目录");//选择框标题
		saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
		ComponentUtil.addComponents(this.getContentPane(), addTaskBtn, urlLabel, urlField, tagLabel, tagField,
				 saveDirLabel, saveDirField, chooserBtn, tipLabel);
	}
	public void emptyField(){
		urlField.setText("");
	}
	public void setUrl(String url){
		urlField.setText(url);
	}
	private static boolean isValidUrl(Setting setting, String url){
		if(url != null){
			//假设url合法:http://exhentai.org/g/446779/553f5c4086/
			/*if(url.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
				//获取http://exhentai.org/部分
				String host;
				if(url.lastIndexOf("/") > 8){
					String protocal = "http://";
					if(url.indexOf("https") != -1){
						protocal = "https://";
					}
					String url_ = url.substring(url.indexOf(protocal) + 7, url.length());
					host = protocal + url_.substring(0, url_.indexOf("/"));
				}else{
					host = url;
				}
				return url.matches(host + setting.getGidPrefix() + "[a-zA-Z0-9]+/[a-zA-Z0-9]+/*");
			}*/
			return url.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
		}
		return false;
	}
	//将存在的标签放到最前前面
	private String change(String[] arr, String s, String split){
    	String str = "";
    	for(int i = 0; i < arr.length; i ++){
    		if(!s.equals(arr[i])){
    			str += split + arr[i];
    		}
    	}
    	return s + str;
    }
	public void dispose() {
		mainWindow.setEnabled(true);
		//mainWindow.setVisible(true);
		super.dispose();
	}
}
