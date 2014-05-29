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
	public static TaskStatus parseTaskStatus(String status){
		if(TaskStatus.UNSTARTED.getStatus().equals(status)){
			return TaskStatus.UNSTARTED;
		}else if(TaskStatus.STOPED.getStatus().equals(status)){
			return TaskStatus.STOPED;
		}else if(TaskStatus.STARTED.getStatus().equals(status)){
			return TaskStatus.STARTED;
		}else if(TaskStatus.COMPLETED.getStatus().equals(status)){
			return TaskStatus.COMPLETED;
		}else if(TaskStatus.DELETED.getStatus().equals(status)){
			return TaskStatus.DELETED;
		}
		return null;
	}
}
