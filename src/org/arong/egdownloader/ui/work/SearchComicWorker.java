package org.arong.egdownloader.ui.work;

import java.util.HashMap;
import java.util.List;

import javax.script.ScriptException;
import javax.swing.SwingWorker;

import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.util.JsonUtil;
import org.arong.util.Tracker;
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
			String source = WebClient.getRequestUseJavaWithCookie(this.url, "UTF-8", mainWindow.setting.getCookieInfo());
			if(source == null){
				Tracker.println(this.getClass(), this.url + ":搜索出错");
				return null;
			}
			String[] result = ScriptParser.search(source, mainWindow.setting);
			if(result != null){
				String json = result[1];
				if(result.length > 2){
					for(int i = 2; i < result.length; i ++){
						json += "###" + result[i];
					}
				}
				List<SearchTask> searchTasks = JsonUtil.jsonArray2beanList(SearchTask.class, json);
				String totalTasks = result[0].split(",")[0];
				//总页数
				String totalPage = result[0].split(",")[1];//Spider.getTextFromSource(source, "+Math.min(", ", Math.max(");
				
				searchComicWindow.setTotalInfo(totalPage, totalTasks);
				
				//下载封面线程
				new DownloadCacheCoverWorker(searchTasks, mainWindow).execute();
				searchComicWindow.searchTasks = searchTasks;
				if(searchComicWindow.datas.get(searchComicWindow.key) == null){
					searchComicWindow.datas.put(searchComicWindow.key, new HashMap<String, List<SearchTask>>());
					searchComicWindow.keyPage.put(searchComicWindow.key, searchComicWindow.totalLabel.getText());
					searchComicWindow.pageInfo.put(searchComicWindow.key, totalPage);
				}
				searchComicWindow.datas.get(searchComicWindow.key).put((currentPage) + "", searchTasks);
				searchComicWindow.showResult(totalPage, currentPage);
			}else{
				searchComicWindow.totalLabel.setText("搜索不到相关内容");
				searchComicWindow.hideLoading();
				return null;
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (Exception e) {
			searchComicWindow.key = " ";
			searchComicWindow.totalLabel.setText(e.getMessage());
		} finally{
			searchComicWindow.hideLoading();
			searchComicWindow.leftBtn.setEnabled(true);
			searchComicWindow.rightBtn.setEnabled(true);
			if(searchComicWindow.isVisible()){
				searchComicWindow.toFront();
			}
		}
		return null;
	}
}
