package org.arong.egdownloader.ui.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.work.CommonSwingWorker;
import org.arong.util.DateUtil;
import org.arong.util.FileUtil2;
/**
 * 任务合并面板
 * @author dipoo
 * @since 2019-06-05
 */
public class MergeWindow extends JDialog {

	public JTextPane htmlPanel;
	EgDownloaderWindow window;
	public CommonSwingWorker worker = null;
	public Object[] objs = new Object[2];
	public boolean merging = false;//是否合并中
	public MergeWindow(final EgDownloaderWindow window){
		this.window = window;
		// 设置主窗口
		this.setSize(ComponentConst.CLIENT_WIDTH, 320);
		this.setIconImage(IconManager.getIcon("count").getImage());
		this.setTitle("版本合并");
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(window.searchComicWindow);
		//关闭监听，释放窗口资源，否则消耗大量CPU
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MergeWindow this_ = (MergeWindow) e.getSource();
				this_.dispose();
				if(window.searchComicWindow != null){
					window.searchComicWindow.setVisible(true);
					window.searchComicWindow.toFront();
				}
			}
		});

		htmlPanel = new AJTextPane("", Color.BLUE);
		htmlPanel.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//e.getDescription():a标签href值
					if("merge".equals(e.getDescription())){
						new CommonSwingWorker(new Runnable() {
							public void run() {
								//开始合并
								if(merging){
									System.out.println("任务合并中...");
								}else{
									window.infoTabbedPane.setSelectedComponent(window.consolePanel);
									merging = true;showMergeInfo();
									//采集新版本任务信息
									Task oldtask = (Task)objs[0];
									SearchTask st = (SearchTask)objs[1];
									System.out.println(String.format("开始合并任务【%s】", oldtask.getDisplayName()));
									long t = System.currentTimeMillis();
									try {
										String tid = UUID.randomUUID().toString();
										Task newtask = ScriptParser.getTaskAndPicByUrl(st.getUrl(), tid, window.setting);
										if(newtask == null){
											JOptionPane.showConfirmDialog(MergeWindow.this, "任务合并失败：无法获取新版本任务信息");
											merging = false;showMergeInfo();
											return;
										}
										if(StringUtils.isBlank(newtask.getUploader())){
											JOptionPane.showConfirmDialog(MergeWindow.this, "任务合并失败：新版本任务上传者为空！");
											merging = false;showMergeInfo();
											return;
										}
										if(newtask.getPictures() == null || newtask.getPictures().size() == 0){
											JOptionPane.showConfirmDialog(MergeWindow.this, "任务合并失败：新版本任务图片列表为空！");
											merging = false;showMergeInfo();
											return;
										}
										newtask.setId(tid);
										newtask.setSaveDir(oldtask.getSaveDir() + "_new");
										newtask.setTag(oldtask.getTag());
										newtask.setOriginal(oldtask.isOriginal());
										newtask.setSaveDirAsSubname(oldtask.isSaveDirAsSubname());
										//创建保存目录
										FileUtil2.ifNotExistsThenCreate(newtask.getSaveDir());
										System.out.println("复制封面");
										File cover = new File(oldtask.getSaveDir() + File.separator + "cover.jpg");
										if(cover.exists()){
											//FileUtil.copyFile(cover.getAbsolutePath(), deskPath);
											FileUtil2.storeStream(newtask.getSaveDir(), "cover.jpg", new FileInputStream(cover));//保存到目录
										}
										System.out.println("比较图片信息");
										if(oldtask.getCurrent() > 0 || oldtask.getPictures() != null){
											for(Picture oldpic : oldtask.getPictures()){
												if(oldpic.isCompleted()){
													//查询相同名称的新版本图片
													for(Picture newpic : newtask.getPictures()){
														if(newpic.getName().equals(oldpic.getName())){
															//复制已下载图片
															String name = oldpic.getName();
															String name2 = null;
															if(name.indexOf(".") != -1){
																name = (oldpic.isSaveAsName() ? name.substring(0, name.lastIndexOf(".") - 1) : oldpic.getNum()) + name.substring(name.lastIndexOf("."), name.length());
																name2 = (newpic.isSaveAsName() ? name.substring(0, name.lastIndexOf(".") - 1) : newpic.getNum()) + name.substring(name.lastIndexOf("."), name.length());
															}else{
																name = (oldpic.isSaveAsName() ? name.substring(0, name.lastIndexOf(".") - 1) : oldpic.getNum()) + ".jpg";
																name = (newpic.isSaveAsName() ? name.substring(0, name.lastIndexOf(".") - 1) : newpic.getNum()) + ".jpg";
															}
															try{
																FileUtil2.copyFile(oldtask.getSaveDir() + File.separator + name, newtask.getSaveDir() + File.separator + name2);
																newpic.setCompleted(true);
																newpic.setPpi(oldpic.getPpi());
																newpic.setSize(oldpic.getSize());
																newpic.setTime(oldpic.getTime());
																newpic.setOldurl(oldpic.getUrl());
																newpic.setRealUrl(oldpic.getRealUrl());
																newpic.setTid(newtask.getId());
																newtask.setCurrent(newtask.getCurrent() + 1);
																newtask.setOldurl(oldtask.getUrl());
															}catch(Exception e){
																e.printStackTrace();
															}
															break;
														}
													}
												}
											}
										}
										System.out.println("保存新版本");
										//保存新版本任务
										window.taskDbTemplate.store(newtask);
										//保存新版本图片
										window.pictureDbTemplate.store(newtask.getPictures());
										System.out.println(String.format("删除旧版本：%s", oldtask.getUrl()));
										//删除旧版本任务
										window.taskDbTemplate.delete(oldtask);
										//删除旧版本图片
										window.pictureDbTemplate.delete("tid", oldtask.getId());
										System.out.println(String.format("删除旧版本文件：%s", oldtask.getSaveDir()));
										//删除旧版本文件
										FileUtil2.deleteFile(new File(oldtask.getSaveDir()));
										//文件夹重命名
										try{
											boolean success = new File(newtask.getSaveDir()).renameTo(new File(oldtask.getSaveDir()));
											if(success){
												newtask.setSaveDir(oldtask.getSaveDir());
												//重新更新新版本任务
												window.taskDbTemplate.update(newtask);
											}
										}catch(Exception e){}
										
										//保存到内存
										final TaskingTable taskTable = (TaskingTable)window.runningTable;
										taskTable.getTasks().add(0, newtask);//将任务添加到列表最前面
										taskTable.propertyChange(newtask);//开始观察者模式，显示下载速度
										taskTable.getTasks().remove(oldtask);
										if(window.taskImagePanel != null){
											window.taskImagePanel.init();
										}
										System.out.println(String.format("结束合并任务【%s】，耗时%s秒", oldtask.getDisplayName(), String.format("%.2f", ((System.currentTimeMillis() - t) / 1000f))));
										JOptionPane.showMessageDialog(MergeWindow.this, "任务合并成功");
									} catch (Exception e) {
										e.printStackTrace();
										JOptionPane.showMessageDialog(MergeWindow.this, "任务合并失败：" + e.getMessage());
									} finally{
										merging = false;
										showMergeInfo();
										window.searchComicWindow.mergeWindow.dispose();
									}
								}
							}
						}).execute();
					}
				}
			}
		});
		this.getContentPane().add(htmlPanel);
		showMergeInfo();
	}
	
	public void setTask(Task task){
		objs[0] = task;
	}
	public void setSearchTask(SearchTask task){
		objs[1] = task;
	}
	
	public void showMergeInfo(){
		if(worker != null && !worker.isDone()){
			this.setVisible(true);
			return;
		}
		this.setVisible(true);
		htmlPanel.setText("<br><br><center>操作中...</center>");
		htmlPanel.setText(transferHtml());
		worker = null;
	}
	
	public String transferHtml(){
		Task t = (Task)objs[0];
		SearchTask st = (SearchTask)objs[1];
		if(t == null || st == null) return "";
		String cantMsg = null;
		if(st.getUrl().replaceAll("https://", "http://").equals(t.getUrl().replaceAll("https://", "http://"))){
			cantMsg =  "同一任务不能合并";
		}/*else if(StringUtils.isBlank(st.getUploader())){
			cantMsg =  "新版本上传者为空，无法合并";
		}else if(StringUtils.isBlank(t.getUploader())){
			cantMsg =  "旧版本上传者为空，无法合并";
		}else if(!st.getUploader().equals(t.getUploader())){
			cantMsg =  "上传者不一致，无法合并，请确认所选中的任务是否为同一个本子";
		}else if(StringUtil.getSimilarityRatio(st.getName(), t.getName()) < 0.95f){
			cantMsg =  String.format("标题相似度%s％低于95％，无法合并，请确认所选中的任务是否为同一个本子", new BigDecimal(StringUtil.getSimilarityRatio(st.getName(), t.getName()) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}*/
		//<br>可以合并的前提条件为：1、上传者一致。2、标题相似度95％以上。
		String tipsMsg = null;
		//比较两个任务发布时间
		if(StringUtils.isNotBlank(t.getPostedTime()) && StringUtils.isNotBlank(st.getDate())){
			try{
				if(DateUtil.String2Date(t.getPostedTime()).after(DateUtil.String2Date(st.getDate()))){
					tipsMsg = "请注意：新版本发布时间晚于当前版本";
				}
			}catch(Exception e){}
		}else if(StringUtils.isNotBlank(st.getUploader()) && StringUtils.isNotBlank(t.getUploader()) && !st.getUploader().equals(t.getUploader())){
			tipsMsg = "请注意：新旧版本上传者不一致";
		}
		String s = String.format("<html><div style='font-family:微软雅黑;font-size:10px;'>【新版本】<font color='red'>%s</font>[%s-<font color='blue'>%s</font>-%sP]<br>【旧版本】<font color='red'>%s</font>[%s-<font color='blue'>%s</font>-%sP]<br><br><h1><font color=red>%s</font></h1><br><b style='color:green'>说明：本功能主要用来对某些持续更新而生成新版本的本子与旧版本进行合并，过程比较耗时，请耐心等候。<br>合并的操作为：创建新版本任务，与旧版本比较：已完成的图片，名称相同的部分直接复制到新版本，复制完成后删除旧版本任务，新增的图片继续从服务器下载。</b></div></html>", 
				st.getName(), st.getDate(), st.getUploader(), st.getFilenum(),
				t.getName(), t.getPostedTime(), t.getUploader(), t.getTotal(),
				cantMsg == null ? String.format("<a style='text-decoration:underline' href='merge'>%s</a>%s", merging ? "任务合并中..." : "开始合并", tipsMsg == null ? "" : String.format("&nbsp;(%s)", tipsMsg)) :
					String.format("%s", cantMsg));
		return s;
	}
}
