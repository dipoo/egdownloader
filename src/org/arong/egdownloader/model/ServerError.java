package org.arong.egdownloader.model;
/**
 * 服务器错误
 * @author dipoo
 * @since 2019-08-12
 */
public enum ServerError {
	IP_BANED("Your IP address has been temporarily banned"),
	GALLERY_REMOVED("removed"),
	ONLY_VISIBLE_EXH("This gallery is pining for the fjords"),
	QUOTA_EXCEEDED("exceeded your image viewing limits");
	private String error;
	public String getEerror(){
		return error;
	}
	private ServerError(String error){
		this.error = error;
	}
	public static String parseServerError(ServerError error){
		if(IP_BANED == error){
			return "你的IP已被屏蔽";
		}else if(GALLERY_REMOVED == error){
			return "本子已删除";
		}else if(ONLY_VISIBLE_EXH == error){
			return "只能在里站访问";
		}else if(QUOTA_EXCEEDED == error){
			return "超出限额";
		}
		return null;
	}
}
