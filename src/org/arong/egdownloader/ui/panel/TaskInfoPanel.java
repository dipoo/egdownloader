package org.arong.egdownloader.ui.panel;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.swing.AJTextPane;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.util.FileUtil2;
import org.arong.util.HtmlUtils;

public class TaskInfoPanel extends JScrollPane {
	
	private AJTextPane textPane;
	private Task t;
	
	public TaskInfoPanel(final EgDownloaderWindow mainWindow) {
		textPane = new AJTextPane(null,
				Color.BLUE);
		textPane.setBorder(null);
		this.setViewportView(textPane);
		this.setBorder(null);
		//this.setBounds(5, 5, width, height);
		
		textPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if("openSaveDir".equals(e.getDescription())){
						//打开保存目录
						try {
							Desktop.getDesktop().open(new File(t.getSaveDir()));
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "打开文件夹失败");
						}
					}else if("searchUploader".equals(e.getDescription())){
						//搜索上传者
						if(StringUtils.isNotBlank(t.getUploader())){
							mainWindow.infoTabbedPane.setSelectedComponent(mainWindow.taskTagsPanel);
							mainWindow.taskTagsPanel.renderSelectTags(String.format("uploader:\"%s\"", t.getUploader()), true);
						}
					}else if("searchLanguage".equals(e.getDescription())){
						if(StringUtils.isNotBlank(t.getLanguage())){
							mainWindow.localSearchAndSortPanel.keyTextField.setText(String.format("language:%s", t.getLanguage()));
							mainWindow.localSearchAndSortPanel.searchBtn.doClick();
						}
					}else if("searchType".equals(e.getDescription())){
						if(StringUtils.isNotBlank(t.getType())){
							mainWindow.localSearchAndSortPanel.keyTextField.setText(String.format("type:%s", t.getType()));
							mainWindow.localSearchAndSortPanel.searchBtn.doClick();
						}
					}else if("searchLocalTag".equals(e.getDescription())){
						if(StringUtils.isNotBlank(t.getTag())){
							mainWindow.localSearchAndSortPanel.keyTextField.setText(String.format("localtag:%s", t.getTag()));
							mainWindow.localSearchAndSortPanel.searchBtn.doClick();
						}
					}
				}
			}
		});
	}
	
	public void parseTask(Task t){
		textPane.setText("<b>加载中...</b>");
		try{
			if(t != null){
				this.t = t;
				long size = 0;
				for(Picture pic : t.getPictures()){
					if(pic.isCompleted()){
						size += pic.getSize();
					}
				}
				String infoHtml = String.format(ComponentConst.taskinfoHtml, 
						t.getName(), 
						t.getSubname() == null ? "" : t.getSubname(),
						String.format("<a href='%s'>%s</a>", t.getUrl(), (t.getUrl() == null ? "" : t.getUrl())),
						String.format("<a href='%s'>%s</a>", t.getCoverUrl(), (t.getCoverUrl() == null ? "" : t.getCoverUrl())),
						t.getLanguage() == null ? "" : t.getLanguage(), 
						t.getTotal(), 
						t.getSize(), 
						t.getType() == null ? "" : t.getType(),
						t.getUploader() == null ? "" : URLDecoder.decode(URLDecoder.decode(t.getUploader(), "UTF-8"), "UTF-8"),
						t.getPostedTime() == null ? "" : t.getPostedTime(), 
						renderStatus(t.getStatus()),
						t.getCurrent() + "",
						FileUtil2.showSizeStr(size), 
						t.getCurrent() == 0 ? "0B" : FileUtil2.showSizeStr(size / t.getCurrent()), 
						getSchedule(t.getCurrent(), t.getTotal()), 
						t.getCreateTime() == null ? "" : t.getCreateTime(), 
						t.getShowSyncTime(), 
						t.getCompletedTime() == null ? "" : t.getCompletedTime(), 
						t.getStart(), 
						t.getEnd(), 
						t.isReaded() ? "是" : "否", 
						t.isOriginal() ? "是" : "否", 
						t.isSaveDirAsSubname() ? "是" : "否", 
						StringUtils.isBlank(t.getTag()) ? "一般" : t.getTag(), 
						StringUtils.isBlank(t.getGroupname()) ? "默认空间" : t.getGroupname(), 
						t.getSaveDir(), 
						StringUtils.isNotBlank(t.getOldurl()) ? String.format("旧版本<font color=black>：</font><a href='%s' style='font-weight:normal;text-decoration:underline;color:blue'>%s</a>", t.getOldurl(), t.getOldurl()) : "");
				textPane.setText(infoHtml);
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			textPane.setText(e1.getMessage());
		}
	}
	
	private String renderStatus(TaskStatus status){
		String color = "#5F392D";
		if(status == TaskStatus.COMPLETED){
			color = "#419141";
		}else if(status == TaskStatus.STARTED){
			color = "#4192FF";
		}else if(status == TaskStatus.STOPED){
			color = "#000159";
		}else if(status == TaskStatus.UNCREATED){
			color = "#DAA520";
		}else if(status == TaskStatus.WAITING){
			color = "#D2691E";
		}
		return "<b>" + HtmlUtils.colorHtml(status.getStatus(), color) + "</b>";
	}
	
	private String getSchedule(Object current, Object total){
		Double d = Double.parseDouble(current.toString()) / Double.parseDouble(total.toString()) * 100;
		return d > 100 ? "100.0%" : new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}
}
