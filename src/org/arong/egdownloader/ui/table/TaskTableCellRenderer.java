package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.Setting;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.FontConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJLabel;
import org.arong.util.HtmlUtils;

/**
 * 任务表格单元格渲染器
 * @author 阿荣
 * @since 2014-05-23
 */
public class TaskTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3319745427208195393L;
	
	private static Color fontColor;
	private static Color unstartedColor;
	private static Color startedColor;
	private static Color stopedColor;
	private static Color waitingColor;
	private static Color completedColor;
	private static Color uncreatedColor;
	private static Color progressBarBg = Color.WHITE;
	private static Color progressBarBorder = new Color(47,110,178);
	private Task task;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected){
			fontColor = Color.BLUE;
		}else{
			fontColor = Color.DARK_GRAY;
		}
		TableColumn tc = table.getColumnModel().getColumn(column);
		task = ((TaskingTable)table).getTasks().get(row); 
		Setting setting = ((TaskingTable)table).getMainWindow().setting;
		if(column == 0){//第一列：图标
			tc.setPreferredWidth(130);
			tc.setMaxWidth(160);
			JLabel l = new JLabel();
			
			if(task.getType() != null){
				ImageIcon icon = IconManager.getIcon(task.getType().toLowerCase());
				l.setIcon(icon != null ? icon : IconManager.getIcon("folder"));
			}
			
			l.setText("" + (row + 1));
			l.setForeground(Color.BLUE);
			//是否已读
			if(task.isReaded()){
				l.setText(l.getText() + "√");
				//l.setToolTipText("已经阅读过了");//设置鼠标移过提示
			}else{
				l.setText(l.getText() + "  ");
			}
			return l;
		}else if(column == 1){//第二列：名称
			tc.setPreferredWidth(480);
			tc.setMaxWidth(1200);
			String preffix = new StringBuffer("<html>").append(HtmlUtils.colorHtml("[" + task.getShortCreatetime() + "]", "#248FB7")).append((task.isSearched() ? "<b>" + HtmlUtils.redColorHtml("[搜]") + "</b>" : "")).append(("一般".equals(task.getTag().trim()) ? "" : HtmlUtils.colorHtml("[<i>" + task.getTag() + "</i>]", "#248FB7"))).toString();
			String suffix = (StringUtils.isNotBlank(task.getPostedTime()) ? HtmlUtils.colorHtml("[" + task.getPostedTime() + "]", "#248FB7") : "") + "</html>";
			if(setting.isShowAsSubname() && task.getSubname() !=null && !"".equals(task.getSubname().trim()) && !"null".equals(task.getSubname().trim())){
				String subname = task.getSubname().trim();
				JLabel nameLabel = null;
				if(subname.length() > 230){
					nameLabel = new AJLabel(preffix + subname.substring(0, 223) + " ......" + suffix, fontColor, isSelected ? FontConst.Microsoft_BOLD_11 : FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
				}else{
					nameLabel = new AJLabel(preffix + subname + suffix, fontColor, isSelected ? FontConst.Microsoft_BOLD_11 : FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
				}
				if(value != null){
					nameLabel.setToolTipText(task.getName());//设置鼠标移过提示
				}else{
					nameLabel.setToolTipText(subname);//设置鼠标移过提示
				}
				return nameLabel;
			}
			else if(value != null && value.toString().length() > 120){
				JLabel nameLabel = new AJLabel(preffix + value.toString().substring(0, 113) + suffix, fontColor, isSelected ? FontConst.Microsoft_BOLD_11 : FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
				if(StringUtils.isNotBlank(task.getSubname()) && !"null".equals(task.getSubname().trim())){
					nameLabel.setToolTipText(task.getSubname());//设置鼠标移过提示
				}
				return nameLabel;
			}else{
				JLabel nameLabel = new AJLabel(preffix + value.toString() + suffix, fontColor, isSelected ? FontConst.Microsoft_BOLD_11 : FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
				if(StringUtils.isNotBlank(task.getSubname()) && !"null".equals(task.getSubname().trim())){
					nameLabel.setToolTipText(task.getSubname());//设置鼠标移过提示
				}
				return nameLabel;
			}
		}else if(column == 2){//第三列：图片总数
			tc.setPreferredWidth(80);
			tc.setMaxWidth(120);
			JLabel l = new AJLabel(String.format("<html>%s</html>", value.toString()), fontColor, FontConst.Microsoft_BOLD_12, JLabel.LEFT);
			return l;
		}else if(column == 3){//第三列：语言
			tc.setPreferredWidth(50);
			tc.setMaxWidth(120);
			return new AJLabel(String.format("<html>%s</html>", value == null ? "" : value.toString()), fontColor, FontConst.Microsoft_BOLD_11, JLabel.LEFT);
		}else if(column == 4){//第四列：进度
			tc.setPreferredWidth(80);
			tc.setMaxWidth(150);
			if(value == null || Integer.parseInt(value.toString()) < 1){
				return new AJLabel("0(0.0%)", fontColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}
			JProgressBar bar = new JProgressBar(0, task.getTotal());
			bar.setBackground(progressBarBg);
			bar.setString(value.toString() + "(" + getSchedule(value, task.getTotal()) + ")");
			bar.setStringPainted(true);
			bar.setFont(FontConst.Microsoft_BOLD_11);bar.setPreferredSize(new Dimension(110, 13));
			bar.setForeground(progressBarBorder);
			bar.setBorder(BorderFactory.createLineBorder(progressBarBorder));
			bar.setValue(Integer.parseInt(value.toString()));
			return bar;
		}else if(column == 5){//第五列：状态
			tc.setPreferredWidth(table.getRowCount() > ComponentConst.MAX_TASK_PAGE ?  70 : 92);
			tc.setMaxWidth(200);
			if(value.toString().equals(TaskStatus.UNSTARTED.getStatus())){
				if(unstartedColor == null) unstartedColor = new Color(95,57,45);
				return new AJLabel(value.toString(), unstartedColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}else if(value.toString().equals(TaskStatus.STARTED.getStatus())){
				if(startedColor == null) startedColor = new Color(65,146,225);
				return new AJLabel(task.getDownSpeed().toLowerCase(), startedColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}else if(value.toString().equals(TaskStatus.STOPED.getStatus())){
				if(stopedColor == null) stopedColor = new Color(0,1,89);
				return new AJLabel(value.toString(), stopedColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}else if(value.toString().equals(TaskStatus.COMPLETED.getStatus())){
				if(completedColor == null) completedColor = new Color(65,145,65);
				return new AJLabel(value.toString(), completedColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}else if(value.toString().equals(TaskStatus.WAITING.getStatus())){
				if(waitingColor == null) waitingColor = new Color(210,105,30);
				return new AJLabel(value.toString(), startedColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}else if(value.toString().equals(TaskStatus.UNCREATED.getStatus())){
				if(uncreatedColor == null) uncreatedColor = new Color(218,165,32);
				return new AJLabel(value.toString(), uncreatedColor, FontConst.Microsoft_BOLD_11, JLabel.CENTER);
			}
		}
		return new AJLabel(value.toString(), fontColor, FontConst.Microsoft_PLAIN_11, JLabel.LEFT);
	}
	
	private String getSchedule(Object current, Object total){
		Double d = Double.parseDouble(current.toString()) / Double.parseDouble(total.toString()) * 100;
		return d > 100 ? "100.0%" : new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}
}
