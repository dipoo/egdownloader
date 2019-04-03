package org.arong.egdownloader.ui.work;

import java.util.HashMap;
import java.util.List;

import javax.script.ScriptException;
import javax.swing.SwingWorker;

import org.apache.commons.lang.StringUtils;
import org.arong.egdownloader.model.ScriptParser;
import org.arong.egdownloader.model.SearchTask;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.ui.ComponentConst;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.SearchComicWindow;
import org.arong.util.FileUtil;
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
			//保存源文件
			FileUtil.storeStr2file(source, "source/", "search.html");
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
				searchComicWindow.searchTasks = searchTasks;
				
				//下载封面线程
				new DownloadCacheCoverWorker(searchTasks, mainWindow).execute();
				
				/*if(StringUtils.isNotBlank(mainWindow.setting.getCookieInfo2())){
					//二次搜索线程
					new CommonSwingWorker(new Runnable() {
						public void run() {
							try {
								String source = WebClient.getRequestUseJavaWithCookie(url, "UTF-8", mainWindow.setting.getCookieInfo2());
								if(source == null){
									return;
								}
								String[] result = ScriptParser.search(source, mainWindow.setting, false);
								if(result != null){
									String json = result[1];
									if(result.length > 2){
										for(int i = 2; i < result.length; i ++){
											json += "###" + result[i];
										}
									}
									List<SearchTask> searchTasks = JsonUtil.jsonArray2beanList(SearchTask.class, json);
									if(searchTasks != null){
										boolean p = false;int i = 0;
										for(SearchTask searchTask : searchTasks){
											for(SearchTask ost : mainWindow.searchComicWindow.searchTasks){
												p = true;i ++;
												if(ost.getUrl().equals(searchTask.getUrl())){
													if(StringUtils.isNotBlank(searchTask.getDate())){
														ost.setDate(searchTask.getDate());
													}
													if(StringUtils.isNotBlank(searchTask.getRating())){
														ost.setRating(searchTask.getRating());
													}
													if(StringUtils.isNotBlank(searchTask.getFilenum())){
														ost.setFilenum(searchTask.getFilenum());
													}
													if(StringUtils.isNotBlank(searchTask.getUploader())){
														ost.setUploader(searchTask.getUploader());
													}
													break;
												}
											}
										}
										if(p){
											mainWindow.searchComicWindow.updateTaskInfo();
										}
									}
									
								}
							 } catch (Exception e) {}
						}
					}).execute();
				}*/
				
				if(searchComicWindow.datas.get(searchComicWindow.key) == null){
					searchComicWindow.datas.put(searchComicWindow.key, new HashMap<String, List<SearchTask>>());
					searchComicWindow.keyPage.put(searchComicWindow.key, searchComicWindow.totalLabel.getText());
					searchComicWindow.pageInfo.put(searchComicWindow.key, totalPage);
				}
				searchComicWindow.datas.get(searchComicWindow.key).put((currentPage) + "", searchTasks);
				if(searchComicWindow.viewModel == 1){
					searchComicWindow.showResult(totalPage, currentPage);
				}else{
					searchComicWindow.showResult2(totalPage, currentPage);
				}
				
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
