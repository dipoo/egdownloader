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
        String temp = Spider.getTextFromSource(source, setting.getTask_total_size()[0], setting.getTask_total_size()[1]);
        task.setTotal(Integer.parseInt(temp.split("@")[0].trim()));
        task.setSize(temp.split("@")[1].trim());
        //获取漫画语言
        task.setLanguage(Spider.getTextFromSource(source, setting.getTask_language()[0], setting.getTask_language()[1]));
        Tracker.println(ParseEngine.class, task.getName());
        Tracker.println(ParseEngine.class, task.getSubname());
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
	
	public static List<Picture> collectpictrues(Task task, Setting setting, int page, String picSource, CreatingWindow creatingWindow) throws ConnectTimeoutException, SocketTimeoutException, SpiderException{
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
	 * @since v0.40
	 */
	public static void rebuildTask(Task task, Setting setting) throws ConnectTimeoutException, SocketTimeoutException, SpiderException{
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
	//废弃
	public static Task buildTask_new_old(String url, String saveDir, Setting setting, JDialog window) throws SpiderException, WebClientException, ConnectTimeoutException, SocketTimeoutException{
		CreatingWindow creatingWindow = (CreatingWindow)window;
		Task task = new Task(url, saveDir);
		task.setId(UUID.randomUUID().toString());
		
		String host = url.substring(0, url.indexOf(setting.getGidPrefix()));
		Tracker.println("host:" + host);
		// 446779
		String gid = Spider.substring(url, setting.getGidPrefix()).substring(0,
				Spider.substring(url, setting.getGidPrefix()).indexOf("/"));
		// 553f5c4086
		String t = Spider.substring(url, gid + "/")
				.substring(0, Spider.substring(url, gid + "/").length())
				.replaceAll("/", "");
		//http://exhentai.org/hathdler.php?gid=446779&t=553f5c4086
		String hentaiHomeUrl = host + "/" + setting.getHentaiHome().getUri()
				+ "?" + setting.getHentaiHome().getFirstParameterName() + "="
				+ gid + "&" + setting.getHentaiHome().getSecondParameterName()
				+ "=" + t;
		Tracker.println("hentaiHomeUrl:" + hentaiHomeUrl);
		//EHG-446779.hathdl文件内容
		String hentaiHomeSource = WebClient.postRequestWithCookie(hentaiHomeUrl, setting.getCookieInfo());
//		Tracker.println(hentaiHomeSource);
		//数量
		String total_ = Spider.getTextFromSource(hentaiHomeSource, setting.getTotalPrefix(), "\n");
		Tracker.println("total:" + total_);
		String name = Spider.getTextFromSource(hentaiHomeSource, setting.getNamePrefix(), "\n");
		Tracker.println("name:" + name);
		creatingWindow.nameLabel.setText(creatingWindow.nameLabel.getText() + name);
		creatingWindow.totalLabel.setText(creatingWindow.totalLabel.getText() + total_);
		creatingWindow.nameLabel.setVisible(true);
		creatingWindow.totalLabel.setVisible(true);
		String fileList = Spider.getTextFromSource(hentaiHomeSource, setting.getFileListPrefix(), setting.getFileListSuffix());
		
		int total = Integer.parseInt(total_.trim());
		creatingWindow.bar.setMaximum(total);
		//获取图片集合
		/*List<Picture> pictures = getPictures(task, fileList, total);
		setPicturesUrl(url, pictures, setting, creatingWindow);*/
		task.setTotal(total);
		List<Picture> pictures = getPictures_new(host, setting, gid, fileList.trim(), task, creatingWindow);
		if(pictures.size() == 0){
			//采用下载页分页采集法
			pictures = getPictures(task, fileList, total);
			setPicturesUrl(url, pictures, setting, creatingWindow);
		}
		//setid
		task.setName(name);
		task.setSaveDir(saveDir + "/" + name);
		task.setPictures(pictures);
		return task;
	}
	//废弃
	private static List<Picture> getPictures_new(String host, Setting setting, String gid, String fileList,
			Task task, CreatingWindow creatingWindow) {
		List<Picture> pictures = new ArrayList<Picture>();
		String[] rows = fileList.split("\n");
		String[] rowInfos;
		for(int i = 0; i < rows.length; i ++){
			//1 39fd09ca334866a6e80248da147ed1a39666b956-267169-1200-813-jpg Img_000.jpg
			rowInfos = rows[i].split(" ");
			Picture picture = new Picture();
			picture.setTid(task.getId());
			picture.setId(UUID.randomUUID().toString());
			//设置图片浏览地址,组成规则：域名+(漫画浏览地址的前缀)showPicPrefix+gid+神马的前10个字符+短杠+序号
			picture.setUrl(host + setting.getShowPicPrefix() + rowInfos[1].substring(0, 10)+ "/" + gid  + "-" + rowInfos[0]);
			//检测地址是否正确（两次检测）
			if(i == 0){
				try {
					Spider.getTextFromSource(WebClient.postRequestWithCookie(picture.getUrl(), setting.getCookieInfo()),  setting.getRealUrlPrefix(), setting.getRealUrlSuffix());
				} catch (Exception e) {
					try {
						Spider.getTextFromSource(WebClient.postRequestWithCookie(picture.getUrl(), setting.getCookieInfo()),  setting.getRealUrlPrefix(), setting.getRealUrlSuffix());
					} catch (Exception e1) {
						Tracker.println(ParseEngine.class, task.getName() + ":采用下载页分页采集法");
						break;
					}
				}
			}
			//设置图片名称
			picture.setName(rowInfos[2]);
			//设置编号
			picture.setNum(genNum(task.getTotal(), i));
			pictures.add(picture);
			creatingWindow.bar.setValue(i + 1);
			
		}
		return pictures;
	}
	/**
	 * 步骤：
	 * 1、验证url的合法性。http://exhentai.org/g/446779/553f5c4086/
	 * 2、分析url构造hentai@home下载地址,通过hentai@home获取图片数量（size）、漫画名(name)、漫画查看地址集合
	 * 3、开启采集picture真实下载路径的任务。
	 * 4、根据pictures集合开始下载图片，存放到saveDir/name/目录下
	 * @throws SpiderException 
	 * @throws WebClientException 
	 * @throws ConnectTimeoutException 
	 * @throws SocketTimeoutException 
	 */
	
	public static Task buildTask(String url, String saveDir, Setting setting, JDialog window) throws SpiderException, WebClientException, ConnectTimeoutException, SocketTimeoutException{
		CreatingWindow creatingWindow = (CreatingWindow)window;
		Task task = new Task(url, saveDir);
		task.setId(UUID.randomUUID().toString());
		
		String host = url.substring(0, url.indexOf(setting.getGidPrefix()));
		Tracker.println("host:" + host);
		// 446779
		String gid = Spider.substring(url, setting.getGidPrefix()).substring(0,
				Spider.substring(url, setting.getGidPrefix()).indexOf("/"));
		// 553f5c4086
		String t = Spider.substring(url, gid + "/")
				.substring(0, Spider.substring(url, gid + "/").length())
				.replaceAll("/", "");
		//http://exhentai.org/hathdler.php?gid=446779&t=553f5c4086
		String hentaiHomeUrl = host + "/" + setting.getHentaiHome().getUri()
				+ "?" + setting.getHentaiHome().getFirstParameterName() + "="
				+ gid + "&" + setting.getHentaiHome().getSecondParameterName()
				+ "=" + t;
		Tracker.println("hentaiHomeUrl:" + hentaiHomeUrl);
		//EHG-446779.hathdl文件内容
		String hentaiHomeSource = WebClient.postRequestWithCookie(hentaiHomeUrl, setting.getCookieInfo());
//		Tracker.println(hentaiHomeSource);
		//数量
		String total_ = Spider.getTextFromSource(hentaiHomeSource, setting.getTotalPrefix(), "\n");
		Tracker.println("total:" + total_);
		String name = Spider.getTextFromSource(hentaiHomeSource, setting.getNamePrefix(), "\n");
		Tracker.println("name:" + name);
		if(total_ == null || name == null){
			return null;
		}
		creatingWindow.nameLabel.setText(creatingWindow.nameLabel.getText() + name);
		creatingWindow.totalLabel.setText(creatingWindow.totalLabel.getText() + total_);
		creatingWindow.nameLabel.setVisible(true);
		creatingWindow.totalLabel.setVisible(true);
		String fileList = Spider.getTextFromSource(hentaiHomeSource, setting.getFileListPrefix(), setting.getFileListSuffix());
		
		int total = Integer.parseInt(total_.trim());
		creatingWindow.bar.setMaximum(total);
		//获取图片集合
		List<Picture> pictures = getPictures(task, fileList, total);
		setPicturesUrl(url, pictures, setting, creatingWindow);
		//setid
		task.setTotal(total);
		task.setName(name);
		task.setSaveDir(saveDir + "/" + name);
		task.setPictures(pictures);
		return task;
	}
	
	public static String getdownloadUrl(String taskName, String sourceUrl, Setting setting) throws Exception{
		String url = null;
		try {
			String s = WebClient.postRequestWithCookie(sourceUrl, setting.getCookieInfo());
			url = Spider.getTextFromSource(s,  setting.getRealUrlPrefix(), setting.getRealUrlSuffix());
		} catch (Exception e) {
			Tracker.println(ParseEngine.class, taskName + ":getdownloadUrl异常");
			//return getdownloadUrl(taskName, sourceUrl, setting);
			return null;
		}
		Tracker.println(url);
		return url;
	}
	
	private static List<Picture> getPictures(Task task, String fileList, int total){
		if(fileList == null || "".equals(fileList.trim()) || total < 0){
			return null;
		}
		List<Picture> pictures = new ArrayList<Picture>();
		String[] rows = fileList.split("\n");
		String[] rowInfos;
		for(int i = 0; i < rows.length; i ++){
			//1 39fd09ca334866a6e80248da147ed1a39666b956-267169-1200-813-jpg Img_000.jpg
			rowInfos = rows[i].split(" ");
			Picture picture = new Picture();
			picture.setTid(task.getId());
			picture.setId(UUID.randomUUID().toString());
			picture.setName(rowInfos[2]);
			pictures.add(picture);
		}
		return pictures;
	}
	
	private static void setPicturesUrl(String url, List<Picture> pictures, Setting setting, CreatingWindow creatingWindow) throws SpiderException, WebClientException, ConnectTimeoutException, SocketTimeoutException{
		if(pictures != null){
			int total = pictures.size();
			//页数
			int page_num = total % setting.getPageCount() == 0 ? total / setting.getPageCount() : total / setting.getPageCount() + 1;
			String urlList;
			int count = setting.getPageCount();
			int picIndex;
			String showUrl;
			int currCount = 0;
			//http://exhentai.org/s/
			String showUrlPrefix = url.substring(0,url.indexOf(setting.getGidPrefix())) + setting.getShowPicPrefix();
			for(int i = 0; i < page_num; i ++){
				urlList = Spider.getTextFromSource(WebClient.postRequestWithCookie(url + "?" + setting.getPageParam() + "=" + i, setting.getCookieInfo()), showUrlPrefix, setting.getSourceSuffix());
				if(i == page_num - 1){
					count = total - (page_num - 1) * setting.getPageCount();
					currCount = count;
				}else{
					currCount = (i + 1) * setting.getPageCount();
				}
				for(int j = 0; j < count; j ++){
					picIndex = i * setting.getPageCount() + j;
					showUrl = showUrlPrefix + urlList.substring(0, urlList.indexOf(setting.getShowPicSuffix()));
					pictures.get(picIndex).setNum(genNum(total, picIndex));
					pictures.get(picIndex).setUrl(showUrl);
					Tracker.println(showUrl);
					if(urlList.indexOf(showUrlPrefix) != -1){
						urlList = Spider.substring(urlList, showUrlPrefix);
					}
				}
				creatingWindow.bar.setValue(currCount);
			}
		}
	}
	
	private static String genNum(int total, int index){
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
