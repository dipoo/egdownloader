package org.arong.egdownloader.model;
/**
 * 任务状态枚举类
 * @author 阿荣
 * @since 2014-05-23
 */
public enum TaskStatus {
	UNSTARTED("未开始"),
	STOPED("已暂停"),
	STARTED("下载中"),
	COMPLETED("已完成"),
	DELETED("已删除");
	private String status;
	public String getStatus(){
		return status;
	}
	private TaskStatus(String status){
		this.status = status;
	}
}
