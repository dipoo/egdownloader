package org.arong.egdownloader.model;
/**
 * 搜索绅士站漫画列表任务模型
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchTask extends Task {
	private String date;//发布时间

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
}
