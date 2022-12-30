package org.arong.egdownloader.ui.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
/**
 * 分页组件（针对里站非常规分页算法设计）
 * @author dipoo
 * @since 2022-12-21
 */
public class AJPager2 extends JPanel {

	private Integer total;//数据总数
	
	private Integer next;//下一页
	
	private Integer prev;//上一页
	
	
	private Object data;
	
	private JComponent[] ext;
	
	private ActionListener pageListener;
	
	public AJPager2(int x, int y, int width, int height, ActionListener pageListener){
		this.setBounds(x, y, width, height);
		this.pageListener = pageListener;
		this.setLayout(new FlowLayout());
	}
	
	public void change( Integer next, Integer prev){
		this.next = next;
		this.prev = prev;
		
		this.removeAll();
		JButton fbtn = new AJButton("首页");
		fbtn.setName("");
		fbtn.setToolTipText("1");
		if(pageListener != null){
			fbtn.addActionListener(pageListener);
		}
		this.add(fbtn);
		
		if(prev != null){
			JButton btn = new AJButton(String.format("上一页(%s)", prev));
			btn.setName("prev=" + prev);
			btn.setToolTipText(prev + "");
			if(pageListener != null){
				btn.addActionListener(pageListener);
			}
			this.add(btn);
		}
		
		if(next != null){
			JButton btn = new AJButton(String.format("下一页(%s)", next));
			btn.setToolTipText(next + "");
			btn.setName("next=" + next);
			if(pageListener != null){
				btn.addActionListener(pageListener);
			}
			this.add(btn);
		}
		
		JButton lbtn = new AJButton("尾页");
		lbtn.setName("prev=1");
		lbtn.setToolTipText("prev=1");
		if(pageListener != null){
			lbtn.addActionListener(pageListener);
		}
		this.add(lbtn);
		
		//添加扩展组件
		if(ext != null){
			for(JComponent e : ext){
				this.add(e);
			}
		}
	}
	
	

	public void setPageListener(ActionListener pageListener) {
		this.pageListener = pageListener;
	}


	public ActionListener getPageListener() {
		return pageListener;
	}


	public Integer getNext() {
		return next;
	}

	public Integer getPrev() {
		return prev;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public JComponent[] getExt() {
		return ext;
	}

	public void setExt(JComponent[] ext) {
		this.ext = ext;
	}

}
