package org.arong.egdownloader.ui.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingWorker;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.spider.Spider;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
/**
 * 搜索漫画线程类
 * @author dipoo
 * @since 2015-03-11
 */
public class SearchComicWorker extends SwingWorker<Void, Void>{
	private EgDownloaderWindow mainWindow;
	private String url;
	private Integer currentPage;
	public SearchComicWorker(EgDownloaderWindow mainWindow, String url, Integer currentPage){
		this.mainWindow = mainWindow;
		this.url = url;
		this.currentPage = currentPage;
	}
	
	protected Void doInBackground() throws Exception {
		SearchComicWindow searchComicWindow = (SearchComicWindow)this.mainWindow.searchComicWindow;
		try {
			String source = WebClient.postRequestWithCookie(this.url, mainWindow.setting.getCookieInfo());
//			System.err.println(source);
			List<SearchTask> searchTasks = new ArrayList<SearchTask>();
			//总记录数
			String totalTasks = null;
			if(source.indexOf("Showing ") != -1){
				source = Spider.substring(source, "Showing ");
				totalTasks = Spider.getTextFromSource(source, "of ", "</p><table class=\"ptt");
			}
			if(totalTasks == null){
				searchComicWindow.totalLabel.setText("搜索不到相关内容");
				searchComicWindow.hideLoading();
				return null;
			}
			int total = Integer.parseInt(totalTasks.replaceAll(",", ""));
			//总页数
			String totalPage = (total % 25 == 0 ? total / 25 : total / 25 + 1) + "";//Spider.getTextFromSource(source, "+Math.min(", ", Math.max(");
			
			searchComicWindow.setTotalInfo(totalPage, totalTasks);
			
			if(source.indexOf("preload_pane_image_cancel()") != -1){
				source = Spider.getTextFromSource(source, "preload_pane_image_cancel()", "<table class=\"ptb\"");
				int i = 0;
				while(source.indexOf(")\">") != -1){
					String name = Spider.getTextFromSource(source, ")\">", "</a></div><div class=");
					String url = Spider.getTextFromSource(source, "class=\"it5\"><a href=\"", "\" onmouseover");
					String coverUrl;
					String date = "";
					String type = "";
					if(i == 0){
						coverUrl = Spider.getTextFromSource(source, "<img src=\"", "\" alt=\"");
					}else{
						coverUrl = "http://exhentai.org/" + Spider.getTextFromSource(source, "init~exhentai.org~", "~" + name);
					}
					if(source.indexOf("white-space:nowrap\">") != -1){
						date = Spider.getTextFromSource(source, "white-space:nowrap\">", "</td><td class=\"itd\" onmouseover");
					}
					if(source.indexOf(".png\" alt=\"") != -1){
						type = Spider.getTextFromSource(source, ".png\" alt=\"", "\" class=\"ic\" />");
					}
					SearchTask searchTask = new SearchTask();
					searchTask.setName(name); 
					searchTask.setUrl(url);
					searchTask.setCoverUrl(coverUrl);
					searchTask.setDate(date);
					searchTask.setType(type.toUpperCase());
					searchTasks.add(searchTask);
					if(source.indexOf("preload_pane_image_cancel()") == -1){
						break;
					}
					
					source = Spider.substring(source, "preload_pane_image_cancel()");
					i++;
				}
				searchComicWindow.searchTasks = searchTasks;
				if(searchComicWindow.datas.get(searchComicWindow.key) == null){
					searchComicWindow.datas.put(searchComicWindow.key, new HashMap<String, List<SearchTask>>());
				}
				searchComicWindow.datas.get(searchComicWindow.key).put(currentPage + "", searchTasks);
				searchComicWindow.showResult(totalPage, currentPage);
			}
		} catch (Exception e) {
			searchComicWindow.key = " ";
			searchComicWindow.totalLabel.setText(e.getMessage());
		} finally{
			searchComicWindow.hideLoading();
		}
		return null;
	}

}
