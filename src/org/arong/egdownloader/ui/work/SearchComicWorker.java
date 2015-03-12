package org.arong.egdownloader.ui.work;

import java.awt.Color;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.spider.Spider;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.table.SearchTasksTable;
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
	public SearchComicWorker(EgDownloaderWindow mainWindow, String url){
		this.mainWindow = mainWindow;
		this.url = url;
	}
	
	protected Void doInBackground() throws Exception {
		SearchComicWindow searchComicWindow = (SearchComicWindow)this.mainWindow.searchComicWindow;
		try {
			String source = WebClient.postRequestWithCookie(this.url, mainWindow.setting.getCookieInfo());
			List<SearchTask> searchTasks = searchComicWindow.searchTasks;
			//总记录数
			String totalTasks = null;
			if(source.indexOf("Showing 1-25") == -1){
				if(source.indexOf("Showing 1") != -1){
					source = Spider.substring(source, "Showing 1");
					totalTasks = Spider.getTextFromSource(source, "-", "of ");
				}
			}else{
				totalTasks = Spider.getTextFromSource(source, "Showing 1-25 of ", "</p><table class=\"ptt");
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
				searchTasks.clear();
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
				
				if(mainWindow.searchTable == null){
					mainWindow.searchTable = new SearchTasksTable(5, 70, ComponentConst.CLIENT_WIDTH - 20,
							ComponentConst.CLIENT_HEIGHT - 150, searchTasks, mainWindow);
					JScrollPane tablePane = new JScrollPane(mainWindow.searchTable);
					tablePane.setBounds(5, 70, ComponentConst.CLIENT_WIDTH - 20, ComponentConst.CLIENT_HEIGHT - 150);
					tablePane.getViewport().setBackground(new Color(254,254,254));
					mainWindow.searchComicWindow.getContentPane().add(tablePane);
				}
				mainWindow.searchTable.setVisible(true);
				mainWindow.searchTable.updateUI();
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
