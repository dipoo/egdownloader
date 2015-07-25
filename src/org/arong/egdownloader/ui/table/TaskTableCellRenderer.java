package org.arong.egdownloader.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.model.TaskStatus;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.IconManager;
import org.arong.egdownloader.ui.swing.AJLabel;

/**
 * 任务表格单元格渲染器
 * @author 阿荣
 * @since 2014-05-23
 */
public class TaskTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3319745427208195393L;
	
	private Color fontColor;
	private Color unstartedColor;
	private Color startedColor;
	private Color stopedColor;
	private Color completedColor;
	private Color progressBarBg = Color.WHITE;
	private Color progressBarBorder = new Color(47,110,178);
	private Font font = new Font("微软雅黑", Font.PLAIN, 11);
	private Font blodFont = new Font("微软雅黑", Font.BOLD, 11);
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
		if(column == 0){//第一列：图标
			tc.setPreferredWidth(130);
			tc.setMaxWidth(150);
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
			tc.setPreferredWidth(560);
			tc.setMaxWidth(1200);
			if(task.getSubname() !=null && !"".equals(task.getSubname().trim())){
				String subname = task.getSubname().trim();
				JLabel nameLabel = null;
				if(subname.length() > 230){
					nameLabel = new AJLabel("<html><font color=\"#248FB7\">[<i>" + task.getTag() + "</i> ]</font>" + subname.substring(0, 223) + " ......</html>", fontColor, font, JLabel.LEFT);
				}else{
					nameLabel = new AJLabel("<html><font color=\"#248FB7\">[<i>" + task.getTag() + "</i> ]</font>" + subname + "</html>", fontColor, font, JLabel.LEFT);
				}
				if(value != null){
					nameLabel.setToolTipText(task.getName());//设置鼠标移过提示
				}else{
					nameLabel.setToolTipText(subname);//设置鼠标移过提示
				}
				return nameLabel;
			}
			else if(value != null && value.toString().length() > 120){
				JLabel nameLabel = new AJLabel(value.toString().substring(0, 113) + " ......</html>", fontColor, font, JLabel.LEFT);
				nameLabel.setToolTipText(task.getName());//设置鼠标移过提示
				return nameLabel;
			}else{
				JLabel nameLabel = new AJLabel(value.toString(), fontColor, font, JLabel.LEFT);
				nameLabel.setToolTipText(task.getName());//设置鼠标移过提示
				return nameLabel;
			}
		}else if(column == 2){//第三列：图片总数
			tc.setPreferredWidth(50);
			tc.setMaxWidth(150);
			return new AJLabel(value.toString(), fontColor, font, JLabel.LEFT);
		}else if(column == 3){//第三列：语言
			tc.setPreferredWidth(70);
			tc.setMaxWidth(150);
			return new AJLabel(value == null ? "" : value.toString(), fontColor, font, JLabel.LEFT);
		}else if(column == 4){//第四列：进度
			tc.setPreferredWidth(80);
			tc.setMaxWidth(150);
			if(value == null || Integer.parseInt(value.toString()) < 1){
				return new AJLabel("0(0.0%)", fontColor, blodFont, JLabel.CENTER);
			}
			JProgressBar bar = new JProgressBar(0, task.getTotal());
			bar.setBackground(progressBarBg);
			bar.setString(value.toString() + "(" + getSchedule(value, task.getTotal()) + ")");
			bar.setStringPainted(true);
			bar.setFont(blodFont);bar.setPreferredSize(new Dimension(110, 13));
			bar.setForeground(progressBarBorder);
			bar.setBorder(BorderFactory.createLineBorder(progressBarBorder));
			bar.setValue(Integer.parseInt(value.toString()));
			return bar;
		}else if(column == 5){//第五列：状态
			tc.setPreferredWidth(table.getRowCount() > ComponentConst.MAX_TASK_PAGE ?  70 : 92);
			tc.setMaxWidth(200);
			if(value.toString().equals(TaskStatus.UNSTARTED.getStatus())){
				if(unstartedColor == null) unstartedColor = new Color(95,57,45);
				return new AJLabel(value.toString(), unstartedColor, blodFont, JLabel.CENTER);
			}
			if(value.toString().equals(TaskStatus.STARTED.getStatus())){
				if(startedColor == null) startedColor = new Color(65,146,225);
				return new AJLabel(task.getDownSpeed().toLowerCase(), startedColor, blodFont, JLabel.CENTER);
			}
			if(value.toString().equals(TaskStatus.STOPED.getStatus())){
				if(stopedColor == null) stopedColor = new Color(0,1,89);
				return new AJLabel(value.toString(), stopedColor, blodFont, JLabel.CENTER);
			}
			if(value.toString().equals(TaskStatus.COMPLETED.getStatus())){
				if(completedColor == null) completedColor = new Color(65,145,65);
				return new AJLabel(value.toString(), completedColor, blodFont, JLabel.CENTER);
			}
			if(value.toString().equals(TaskStatus.WAITING.getStatus())){
				if(startedColor == null) startedColor = new Color(65,145,65);
				return new AJLabel(value.toString(), startedColor, blodFont, JLabel.CENTER);
			}
		}
		return new AJLabel(value.toString(), fontColor, font, JLabel.LEFT);
	}
	
	private String getSchedule(Object current, Object total){
		Double d = Double.parseDouble(current.toString()) / Double.parseDouble(total.toString()) * 100;
		return d > 100 ? "100.0%" : new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}
}
