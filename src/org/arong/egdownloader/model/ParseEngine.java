package org.arong.egdownloader.model;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JDialog;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.spider.Spider;
import org.arong.egdownloader.spider.SpiderException;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.util.FileUtil;
import org.arong.util.Tracker;

/**
 * Task分析引擎
 * @author 阿荣
 * @since 2014-05-25
 */
public final class ParseEngine {
	/**
	 * 步骤：
	 * 1、验证url的合法性。http://exhentai.org/g/446779/553f5c4086/
	 * 2、分析url获取图片数量（size）、漫画名(name)、漫画查看地址集合
	 * 3、开启采集picture真实下载路径的任务。
	 * 4、根据pictures集合开始下载图片
	 * @throws SpiderException 
	 * @throws WebClientException 
	 * @throws ConnectTimeoutException 
	 * @throws SocketTimeoutException 
	 */
	public static Task buildTask_new(Task task, Setting setting, JDialog window) throws SpiderException, WebClientException, ConnectTimeoutException, SocketTimeoutException{
		CreatingWindow creatingWindow = (CreatingWindow)window;
		if(task.getId() == null){
			task.setId(UUID.randomUUID().toString());
		}
		String source = WebClient.postRequestWithCookie(task.getUrl(), setting.getCookieInfo());
		//获取名称
		if(task.getName() == null){
			task.setName(Spider.getTextFromSource(source, setting.getTask_name()[0], setting.getTask_name()[1]));
		}
		//获取子名称
		if(task.getSubname() == null){
			task.setSubname(Spider.getTextFromSource(source, setting.getTask_subname()[0], setting.getTask_subname()[1]));
		}
		//获取漫画类别
		task.setType(Spider.getTextFromSource(source, setting.getTask_type()[0], setting.getTask_type()[1]));
        //获取封面路径
        task.setCoverUrl(Spider.getTextFromSource(source, setting.getTask_coverUrl()[0], setting.getTask_coverUrl()[1]));
        //获取数目及大小
        /*String temp = Spider.getTextFromSource(source, setting.getTask_total_size()[0], setting.getTask_total_size()[1]);
        task.setTotal(Integer.parseInt(temp.split("@")[0].trim()));
        task.setSize(temp.split("@")[1].trim());*/
        
        task.setSize(Spider.getTextFromSource(source, "File Size:</td><td class=\"gdt2\">", "B &nbsp;<span class=\"halp\""));
        task.setTotal(Integer.parseInt(Spider.getTextFromSource(source, "Length:</td><td class=\"gdt2\">", " pages</td></tr><tr><td class=\"gdt1")));
        //设置下载结束索引
        task.setEnd(task.getTotal());
        //获取漫画语言
        task.setLanguage(Spider.getTextFromSource(source, setting.getTask_language()[0], setting.getTask_language()[1]));
        Tracker.println(ParseEngine.class, task.getName());
        Tracker.println(ParseEngine.class, task.getSubname());
        Tracker.println(ParseEngine.class, task.getType());
        Tracker.println(ParseEngine.class, task.getLanguage());
        Tracker.println(ParseEngine.class, task.getTotal() + "");
        Tracker.println(ParseEngine.class, task.getSize());
        Tracker.println(ParseEngine.class, task.getCoverUrl());
		creatingWindow.nameLabel.setText(creatingWindow.nameLabel.getText() + task.getName());
		creatingWindow.subnameLabel.setText(creatingWindow.subnameLabel.getText() + task.getSubname());
		creatingWindow.totalLabel.setText(creatingWindow.totalLabel.getText() + task.getTotal());
		creatingWindow.sizeLabel.setText(creatingWindow.sizeLabel.getText() + task.getSize());
		creatingWindow.languageLabel.setText(creatingWindow.languageLabel.getText() + task.getLanguage());
		creatingWindow.nameLabel.setVisible(true);
		creatingWindow.subnameLabel.setVisible(true);
		creatingWindow.totalLabel.setVisible(true);
		creatingWindow.sizeLabel.setVisible(true);
		creatingWindow.languageLabel.setVisible(true);
		creatingWindow.bar.setMaximum(task.getTotal());
		task.setSaveDir(task.getSaveDir() + "/" + FileUtil.filterDir(task.getName()));
		
		//获取图片集合
		//计算页数(每40张一页)
        int page = task.getTotal() % setting.getPageCount() == 0 ? task.getTotal() / setting.getPageCount() : task.getTotal() / setting.getPageCount() + 1;
        //获取图片列表源码
        String picSource = Spider.getTextFromSource(source, setting.getPicture_listSource()[0], setting.getPicture_listSource()[1]);
        List<Picture> pictures = null;
        try{
        	pictures = collectpictrues(task, setting, page, picSource, creatingWindow);
        }catch(Exception e){
        	//e.printStackTrace();
        	//未采集状态
        	task.setStatus(TaskStatus.UNCREATED);
        }
		task.setPictures(pictures);
		return task;
	}
	
	public static List<Picture> collectpictrues(Task task, Setting setting, int page, String picSource, CreatingWindow creatingWindow) throws ConnectTimeoutException, SocketTimeoutException, SpiderException, WebClientException{
		List<Picture> pics = new ArrayList<Picture>();
		if(page != 0){
			int currCount = 0;
			for(int i = 0; i < page; i ++){
				if(picSource == null){
					picSource = Spider.getTextFromSource(WebClient.postRequestWithCookie(task.getUrl() + "?" + setting.getPageParam() + "=" + i, setting.getCookieInfo()), setting.getPicture_listSource()[0], setting.getPicture_listSource()[1]);
				}
                String prefix = setting.getPicture_intercept()[1];//截取的标志
                picSource = Spider.substring(picSource, prefix);
                if(i == page - 1){
					currCount = task.getTotal();// - (page - 1) * setting.getPageCount();
				}else{
					currCount = (i + 1) * setting.getPageCount();
				}
                int j = 0;
                while(picSource.indexOf(setting.getPicture_intercept()[0]) != -1){
                    Picture picture = new Picture();
                    picture.setTid(task.getId());
                    picture.setId(UUID.randomUUID().toString());
                    picture.setNum(genNum(task.getTotal(), i * setting.getPageCount() + j++));
                    //获取图片浏览地址
                    picture.setUrl(Spider.getTextFromSource(picSource, setting.getPicture_showUrl()[0], setting.getPicture_showUrl()[1]));
                    //获取图片名称
                    picture.setName(Spider.getTextFromSource(picSource, setting.getPicture_name()[0], setting.getPicture_name()[1]));
                    pics.add(picture);
                    if(picSource.indexOf(prefix) != -1){
                    	picSource = Spider.substring(picSource, prefix);
                    }else{
                    	picSource = null;
                    	break;
                    }
                    creatingWindow.bar.setValue(currCount);
                }
			}
			
		}
		return pics;
	}
	/**
	 * 重建任务，主要重新采集语言、封面、小标题等信息
	 * @param task
	 * @throws WebClientException 
	 * @since v0.40
	 */
	public static void rebuildTask(Task task, Setting setting) throws ConnectTimeoutException, SocketTimeoutException, SpiderException, WebClientException{
		if("".equals(task.getSubname()) || "".equals(task.getType()) || "".equals(task.getCoverUrl()) 
				||"".equals(task.getSize()) || "".equals(task.getLanguage())){
			String source = WebClient.postRequestWithCookie(task.getUrl(), setting.getCookieInfo());
			//获取子名称
	        task.setSubname(Spider.getTextFromSource(source, setting.getTask_subname()[0], setting.getTask_subname()[1]));
	        //获取类别
	        task.setType(Spider.getTextFromSource(source, setting.getTask_type()[0], setting.getTask_type()[1]));
	        //获取封面路径
	        task.setCoverUrl(Spider.getTextFromSource(source, setting.getTask_coverUrl()[0], setting.getTask_coverUrl()[1]));
	        //获取大小
	        String temp = Spider.getTextFromSource(source, setting.getTask_total_size()[0], setting.getTask_total_size()[1]);
	        task.setSize(temp.split("@")[1].trim());
	        //获取漫画语言
	        task.setLanguage(Spider.getTextFromSource(source, setting.getTask_language()[0], setting.getTask_language()[1]));
		}
		
	}
	
	public static String getdownloadUrl(String taskName, String sourceUrl, Setting setting) throws Exception{
		String url = null;
		try {
			String s = WebClient.postRequestWithCookie(sourceUrl, setting.getCookieInfo());
			url = Spider.getTextFromSource(s,  setting.getPicture_realUrl()[0], setting.getPicture_realUrl()[1]);
		} catch (Exception e) {
			Tracker.println(ParseEngine.class, taskName + ":getdownloadUrl异常,请检查引擎配置:图片真实地址是否出现问题！");
			//return getdownloadUrl(taskName, sourceUrl, setting);
			return null;
		}
		Tracker.println(url);
		return url;
	}
	
	public static String genNum(int total, int index){
		int bit = 2;
		if(total <= 10){
			bit = 2;
		}else if(total <= 100){
			bit = 3;
		}else if(total <= 1000){
			bit = 4;
		}else if(total <= 10000){
			bit = 5;
		}else{
			bit = 6;
		}
		String num = "";
		int index_ = index + 1;
		for(int i = 1; i < bit - (index_ + "").length(); i++){
			num += "0";
		}
		return num + index_;
	}
}
