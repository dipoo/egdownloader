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
import org.arong.util.FileUtil;

public class TaskInfoPanel extends JScrollPane {
	
	private EgDownloaderWindow mainWindow;
	private AJTextPane textPane;
	private Task t;
	
	public TaskInfoPanel(final EgDownloaderWindow mainWindow) {
		this.mainWindow = mainWindow;
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
							Desktop.getDesktop().open(new File(ComponentConst.getSavePathPreffix() + t.getSaveDir()));
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "打开文件夹失败");
						}
					}
				}
			}
		});
	}
	
	public void parseTask(Task t, int index){
		textPane.setText("");
		try{
			if(t != null){
				this.t = t;
				long size = 0;
				for(Picture pic : t.getPictures()){
					if(pic.isCompleted()){
						size += pic.getSize();
					}
				}
				
				String text = ComponentConst.taskinfoHtml
						.replace("@t_name", t.getName())
						.replace("@t_subname", t.getSubname() == null ? "" : t.getSubname())
						.replace("@t_url", "<a href='" + t.getUrl() + "'>" + (t.getUrl() == null ? "" : t.getUrl()) + "</a>")
						.replace("@t_coverUrl", "<a href='" + t.getCoverUrl() + "'>" + (t.getCoverUrl() == null ? "" : t.getCoverUrl()) + "</a>")
						.replace("@t_size", t.getSize())
						.replace("@t_language", t.getLanguage() == null ? "" : t.getLanguage())
						.replace("@t_total", t.getTotal() + "")
						.replace("@t_type", t.getType() == null ? "" : t.getType())
						.replace("@t_uploader", t.getUploader() == null ? "" : URLDecoder.decode(URLDecoder.decode(t.getUploader(), "UTF-8"), "UTF-8"))
						.replace("@t_postedTime", t.getPostedTime() == null ? "" : t.getPostedTime())
						.replace("@t_status", renderStatus(t.getStatus()))
						.replace("@t_createTime", t.getCreateTime() == null ? "" : t.getCreateTime())
						.replace("@t_completedTime", t.getCompletedTime() == null ? "" : t.getCompletedTime())
						.replace("@t_current", t.getCurrent() + "")
						.replace("@t_ocurrentSize", FileUtil.showSizeStr(size))
						.replace("@t_onepsize", t.getCurrent() == 0 ? "0B" : FileUtil.showSizeStr(size / t.getCurrent()))
						.replace("@t_ocurrentrate", getSchedule(t.getCurrent(), t.getTotal()))
						.replace("@t_start", t.getStart() + "")
						.replace("@t_end", t.getEnd() + "")
						.replace("@t_readed", t.isReaded() ? "是" : "否")
						.replace("@t_original", t.isOriginal() ? "是" : "否")
						.replace("@t_saveDirAsSubname", t.isSaveDirAsSubname() ? "是" : "否")
						.replace("@t_tag", StringUtils.isBlank(t.getTag()) ? "一般" : t.getTag())
						.replace("@t_groupname", StringUtils.isBlank(t.getGroupname()) ? "默认空间" : t.getGroupname())
						.replace("@t_saveDir", t.getSaveDir());
				textPane.setText(text);
				/*JScrollBar jScrollBar = this.getVerticalScrollBar();
				jScrollBar.setValue(jScrollBar.getMinimum());//滚动到最前
*/			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
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
		return "<font color='" + color + "'><b>" + status.getStatus() + "</b></font>";
	}
	
	private String getSchedule(Object current, Object total){
		Double d = Double.parseDouble(current.toString()) / Double.parseDouble(total.toString()) * 100;
		return d > 100 ? "100.0%" : new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}
}
