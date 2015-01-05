package org.arong.egdownloader.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JDialog;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.spider.SpiderException;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.util.FileUtil;
import org.arong.util.JsonUtil;
import org.arong.util.Tracker;
/**
 * 脚本解析器
 * @author dipoo
 * @since 2014-01-04
 */
public class ScriptParser {
	
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private static File createScriptFile;
	private static File collectScriptFile;
	private static File downloadScriptFile;
	
	public static File getCreateScriptFile(String filePath) {
		return createScriptFile == null ? createScriptFile = new File(filePath) : createScriptFile;
	}
	
	public static File getCollectScriptFile(String filePath) {
		return collectScriptFile == null ? collectScriptFile = new File(filePath) : collectScriptFile;
	}
	
	public static File getDownloadScriptFile(String filePath) {
		return downloadScriptFile == null ? downloadScriptFile = new File(filePath) : downloadScriptFile;
	}
	
	/**
	 * 解析外部js文件并得到返回结果
	 * @return Object
	 */
	public static Object parseJsScript(Map<String, Object> params, File scriptFile) throws FileNotFoundException, ScriptException{
	    ScriptEngine engine = manager.getEngineByName("js");
	    if(params != null){
	    	for(String key : params.keySet()){
	    		engine.put(key, params.get(key));
	    	}
	    }
	    Object result = engine.eval(new FileReader(scriptFile));
	    return result;
	}
	
	/**
	 * 创建任务
	 */
	public static Task buildTaskByJavaScript(Task task, Setting setting, JDialog window) throws SpiderException, WebClientException, ConnectTimeoutException, SocketTimeoutException, FileNotFoundException, ScriptException{
		CreatingWindow creatingWindow = (CreatingWindow)window;
		if(task.getId() == null){
			task.setId(UUID.randomUUID().toString());
		}
		String source = WebClient.postRequestWithCookie(task.getUrl(), setting.getCookieInfo());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("htmlSource", source);
		Task t = JsonUtil.json2bean(Task.class, parseJsScript(param, getCreateScriptFile(setting.getCreateTaskScriptPath())).toString());
		//获取名称
		if(task.getName() == null){
			task.setName(t.getName());
		}
		//获取子名称
		if(task.getSubname() == null){
			task.setSubname(t.getSubname());
		}
		//获取漫画类别
		task.setType(t.getType());
        //获取封面路径
        task.setCoverUrl(t.getCoverUrl());
        //获取数目及大小
        task.setTotal(t.getTotal());
        task.setSize(t.getSize());
        //设置下载结束索引
        task.setEnd(task.getTotal());
        //获取漫画语言
        task.setLanguage(t.getLanguage());
        Tracker.println(ScriptParser.class, task.getName());
        Tracker.println(ScriptParser.class, task.getSubname());
        Tracker.println(ScriptParser.class, task.getType());
        Tracker.println(ScriptParser.class, task.getLanguage());
        Tracker.println(ScriptParser.class, task.getTotal() + "");
        Tracker.println(ScriptParser.class, task.getSize());
        Tracker.println(ScriptParser.class, task.getCoverUrl());
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
        List<Picture> pictures = null;
        int currCount = 0;
        for(int i = 0; i < page; i++){
        	if(i == page - 1){
				currCount = task.getTotal();// - (page - 1) * setting.getPageCount();
			}else{
				currCount = (i + 1) * setting.getPageCount();
			}
        	try{
	        	if(i == 0){
	        		pictures = collectpictrues(source, setting.getCollectPictureScriptPath(), creatingWindow);
	        	}else{
	        		source = WebClient.postRequestWithCookie(task.getUrl() + "?" + setting.getPageParam() + "=" + i, setting.getCookieInfo());
	        		pictures.addAll(collectpictrues(source, setting.getCollectPictureScriptPath(), creatingWindow));
	        	}
        	}catch(Exception e){
            	//未采集状态
            	task.setStatus(TaskStatus.UNCREATED);
            }
        	creatingWindow.bar.setValue(currCount);
        }
        if(pictures != null){
        	int i = 0;
        	for(Picture pic : pictures){
        		pic.setId(UUID.randomUUID().toString());
        		pic.setTid(task.getId());
        		pic.setNum(ParseEngine.genNum(task.getTotal(), i));
        		i ++;
        	}
        }
		task.setPictures(pictures);
		return task;
	}
	
	/**
	 * 收集图片
	 * @return List<Picture>
	 */
	private static List<Picture> collectpictrues(String source, String scriptPath, CreatingWindow creatingWindow) throws ConnectTimeoutException, SocketTimeoutException, SpiderException, FileNotFoundException, ScriptException{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("htmlSource", source);
		Object o = parseJsScript(param, getCollectScriptFile(scriptPath));
		return JsonUtil.jsonArray2beanList(Picture.class, o.toString());
	}
	
	/**
	 * 重建任务，主要重新采集语言、封面、小标题等信息
	 */
	public static void rebuildTask(Task task, Setting setting) throws ConnectTimeoutException, SocketTimeoutException, SpiderException, FileNotFoundException, ScriptException{
		if("".equals(task.getSubname()) || "".equals(task.getType()) || "".equals(task.getCoverUrl()) 
				||"".equals(task.getSize()) || "".equals(task.getLanguage())){
			String source = WebClient.postRequestWithCookie(task.getUrl(), setting.getCookieInfo());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("htmlSource", source);
			Task t = JsonUtil.json2bean(Task.class, parseJsScript(param, getCreateScriptFile(setting.getCreateTaskScriptPath())).toString());
			//获取子名称
	        task.setSubname(t.getSubname());
	        //获取类别
	        task.setType(t.getType());
	        //获取封面路径
	        task.setCoverUrl(t.getCoverUrl());
	        //获取大小
	        task.setSize(t.getSize());
	        //获取漫画语言
	        task.setLanguage(t.getLanguage());
		}
	}
	
	/**
	 * 获取图片真实下载地址
	 */
	public static String getdownloadUrl(String taskName, String sourceUrl, Setting setting) throws Exception{
		String url = null;
		try {
			String source = WebClient.postRequestWithCookie(sourceUrl, setting.getCookieInfo());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("htmlSource", source);
			url = parseJsScript(param, getDownloadScriptFile(setting.getDownloadScriptPath())).toString();
		} catch (Exception e) {
			Tracker.println(ScriptParser.class, taskName + ":getdownloadUrl异常");
			return null;
		}
		Tracker.println(url);
		return url;
	}
}
