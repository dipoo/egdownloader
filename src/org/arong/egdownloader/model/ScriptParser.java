package org.arong.egdownloader.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.arong.egdownloader.spider.SpiderException;
import org.arong.egdownloader.spider.WebClient;
import org.arong.egdownloader.spider.WebClientException;
import org.arong.egdownloader.ui.window.CreatingWindow;
import org.arong.util.FileUtil;
import org.arong.util.JsonUtil;
import org.arong.util.Tracker;

/*import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.Function;
import sun.org.mozilla.javascript.internal.Scriptable;*/
/**
 * 脚本解析器
 * @author dipoo
 * @since 2015-01-04
 */
public class ScriptParser {
	
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private static File createScriptFile;
	private static File collectScriptFile;
	private static File downloadScriptFile;
	private static File searchScriptFile;
	
	public static void clearFiles(){
		createScriptFile = null;
		collectScriptFile = null;
		downloadScriptFile = null;
		searchScriptFile = null;
	}
	
	public static File getCreateScriptFile(String filePath) {
		if(createScriptFile == null){
			createScriptFile = new File(filePath);
		}
		return createScriptFile;
	}
	
	public static File getCollectScriptFile(String filePath) {
		if(collectScriptFile == null){
			collectScriptFile = new File(filePath);
		}
		return collectScriptFile;
	}
	
	public static File getDownloadScriptFile(String filePath) {
		if(downloadScriptFile == null){
			downloadScriptFile = new File(filePath);
		}
		return downloadScriptFile;
	}
	
	public static File getSearchScriptFile(String filePath) {
		if(searchScriptFile == null){
			searchScriptFile = new File(filePath);
		}
		return searchScriptFile;
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
	 * 解析外部js文件并得到返回结果
	 * @return Object
	 */
	/*public static Object parseJsScriptUseRhino(File scriptFile, String function, Object[] functionArgs) throws FileNotFoundException, IOException{
        //开始调用javascript函数
        Context cx = Context.enter();
        try {
	        Scriptable scope = cx.initStandardObjects();
	        FileReader reader = new FileReader(scriptFile);
	        cx.evaluateReader(scope, reader, "<cmd>", 1, null);
	        Object fObj = scope.get(function, scope);
	        if (!(fObj instanceof Function)) {
	            System.out.println("找不到方法:" +function);
	        } else {
	            Function f = (Function)fObj;
	            Object result = f.call(cx, scope, scope, functionArgs);
	            return result;
	        }
        }finally {
            Context.exit();
        }
        return null;
	}*/
	
	/**
	 * 创建任务
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static Task buildTaskByJavaScript(Task task, Setting setting, JDialog window) throws SpiderException, WebClientException, ScriptException, KeyManagementException, NoSuchAlgorithmException, IOException{
		CreatingWindow creatingWindow = (CreatingWindow)window;
		if(task.getId() == null){
			task.setId(UUID.randomUUID().toString());
		}
		String source = WebClient.getRequestUseJavaWithCookie(task.getUrl(), "UTF-8", setting.getCookieInfo());//WebClient.postRequestWithCookie(task.getUrl(), setting.getCookieInfo());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("htmlSource", source);
		try{
			Task t = JsonUtil.json2bean(Task.class, parseJsScript(param, getCreateScriptFile(setting.getCreateTaskScriptPath())).toString());
			//获取名称
			task.setName(t.getName());
			//获取子名称
			task.setSubname(t.getSubname());
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
		}catch(Exception e){
			e.printStackTrace();
			Tracker.println(ScriptParser.class, setting.getCreateTaskScriptPath() + "脚本解析错误:" + e.getMessage());
		}
		//获取图片集合
		//计算页数(每40张一页)
        int page = task.getTotal() % setting.getPageCount() == 0 ? task.getTotal() / setting.getPageCount() : task.getTotal() / setting.getPageCount() + 1;
        List<Picture> pictures = new ArrayList<Picture>();
        int i = 0;
        while(pictures.size() < task.getTotal() && i < page){
        	try{
	        	if(i == 0){
	        		pictures.addAll(collectpictrues(source, setting.getCollectPictureScriptPath(), creatingWindow));
	        	}else{
	        		source = WebClient.getRequestUseJavaWithCookie(task.getUrl() + "?" + setting.getPageParam() + "=" + i, "UTF-8", setting.getCookieInfo());//WebClient.postRequestWithCookie(task.getUrl() + "?" + setting.getPageParam() + "=" + i, setting.getCookieInfo());
	        		pictures.addAll(collectpictrues(source, setting.getCollectPictureScriptPath(), creatingWindow));
	        	}
        	}catch(Exception e){
            	//未采集状态
            	task.setStatus(TaskStatus.UNCREATED);
            }
        	creatingWindow.bar.setValue(pictures.size());
        	i ++;
        }
        if(pictures != null){
        	i = 0;
        	for(Picture pic : pictures){
        		pic.setId(UUID.randomUUID().toString());
        		pic.setTid(task.getId());
        		pic.setNum(genNum(task.getTotal(), i));
        		pic.setSaveAsName(setting.isSaveAsName());
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
		if(o == null){
			Tracker.println(ScriptParser.class, scriptPath + "脚本解析错误");
		}
		return JsonUtil.jsonArray2beanList(Picture.class, o.toString());
	}
	
	/**
	 * 重建任务，主要重新采集语言、封面、小标题等信息
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static void rebuildTask(Task task, Setting setting) throws SpiderException, ScriptException, WebClientException, KeyManagementException, NoSuchAlgorithmException, IOException{
//		if("".equals(task.getSubname()) || "".equals(task.getType()) || "".equals(task.getCoverUrl()) 
//				||"".equals(task.getSize()) || "".equals(task.getLanguage())){
			String source = WebClient.getRequestUseJavaWithCookie(task.getUrl(), "UTF-8", setting.getCookieInfo());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("htmlSource", source);
			Task t = JsonUtil.json2bean(Task.class, parseJsScript(param, getCreateScriptFile(setting.getCreateTaskScriptPath())).toString());
			task.setName(t.getName());
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
//		}
	}
	
	/**
	 * 获取图片真实下载地址
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static String getdownloadUrl(String taskName, String sourceUrl, Setting setting) throws WebClientException, KeyManagementException, NoSuchAlgorithmException, IOException{
		String url = null;
		String source = WebClient.getRequestUseJavaWithCookie(sourceUrl, "UTF-8", setting.getCookieInfo());
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("htmlSource", source);
			param.put("down_original", setting.isDownloadOriginal());//是否下载原图
			url = parseJsScript(param, getDownloadScriptFile(setting.getDownloadScriptPath())).toString();
		} catch (Exception e) {
			e.printStackTrace();
			Tracker.println(ScriptParser.class, taskName + ":getdownloadUrl异常,请检查" + setting.getDownloadScriptPath() + "脚本是否出现问题！");
			return null;
		}
		Tracker.println(url);
		return url;
	}
	
	/**
	 * 搜索漫画列表,第一个元素为分页信息字符串，格式为 count,pageCount；
	 * 第二个元素为漫画列表JSON字符串
	 */
	public static String[] search(String source, Setting setting) throws ConnectTimeoutException, SocketTimeoutException, FileNotFoundException, ScriptException{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("htmlSource", source);
		Object result = parseJsScript(param, getSearchScriptFile(setting.getSearchScriptPath()));
		return result == null ? null : result.toString().split("\\###");
	}
	
	public static void testScript(String url, JTextArea resultArea, Setting setting, boolean create, boolean collect, boolean download){
		String source;
		try {
			source = WebClient.getRequestUseJavaWithCookie(url, "UTF-8", setting.getCookieInfo());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("htmlSource", source);
			Object result = parseJsScript(param, getCreateScriptFile(setting.getCreateTaskScriptPath()));
			if(result == null){
				resultArea.setText(setting.getCreateTaskScriptPath() + "脚本解析出错\r\n");
				return;
			}
			Task t = JsonUtil.json2bean(Task.class, result.toString());
			t.setUrl(url);
			if(create){
				//展示任务信息
				resultArea.setText("---任务---\r\n" + t.getScriptMember());
			}
			if(collect){
				result = parseJsScript(param, getCollectScriptFile(setting.getCollectPictureScriptPath()));
				if(result == null){
					resultArea.setText(setting.getCollectPictureScriptPath() + "脚本解析出错\r\n");
					return;
				}
				//展示图片信息
				resultArea.setText(resultArea.getText() + "\r\n---图片列表---\r\n" + result.toString());
				if(download){
					//展示第一张图片的真实下载地址
					List<Picture> pics = JsonUtil.jsonArray2beanList(Picture.class, result.toString());
					if(pics != null){
						source = WebClient.getRequestUseJavaWithCookie(pics.get(0).getUrl(), "UTF-8", setting.getCookieInfo());
						param.put("htmlSource", source);
						result = parseJsScript(param, getDownloadScriptFile(setting.getDownloadScriptPath()));
						if(result == null){
							resultArea.setText(setting.getDownloadScriptPath() + "脚本解析出错\r\n");
							return;
						}
						resultArea.setText(resultArea.getText() + "\r\n---第一张图片的真实下载地址---\r\n" + result.toString());
					}
				}
			}else if(download){
				Object o = parseJsScript(param, getCollectScriptFile(setting.getCollectPictureScriptPath()));
				//展示第一张图片的真实下载地址
				List<Picture> pics = JsonUtil.jsonArray2beanList(Picture.class, o.toString());
				if(pics != null){
					source = WebClient.getRequestUseJavaWithCookie(pics.get(0).getUrl(), "UTF-8", setting.getCookieInfo());
					param.put("htmlSource", source);
					result = parseJsScript(param, getDownloadScriptFile(setting.getDownloadScriptPath()));
					if(result == null){
						resultArea.setText(setting.getDownloadScriptPath() + "脚本解析出错\r\n");
						return;
					}
					resultArea.setText(resultArea.getText() + "\r\n---第一张图片的真实下载地址---\r\n" + result.toString());
				}
			}
		} catch (ConnectTimeoutException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + "网络连接超时");
		} catch (SocketTimeoutException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + "网络连接超时");
		} catch (FileNotFoundException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + e.getMessage());
		} catch (ScriptException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + e.getMessage());
		} catch (Exception e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + e.getMessage());
		}
	}
	
	/**public static void testScriptUseRhino(String url, JTextArea resultArea, Setting setting, boolean create, boolean collect, boolean download){
		String source;
		try {
			source = WebClient.postRequestWithCookie(url, setting.getCookieInfo());
			Object[] args = {source};
			//Task t = JsonUtil.json2bean(Task.class, parseJsScript(param, getCreateScriptFile(setting.getCreateTaskScriptPath())).toString());
			Object result = parseJsScriptUseRhino(getCreateScriptFile(setting.getCreateTaskScriptPath()),"parse", args);
			if(result == null){
				resultArea.setText(setting.getCreateTaskScriptPath() + "脚本解析出错\r\n");
				return;
			}
			Task t = JsonUtil.json2bean(Task.class, result.toString());
			t.setUrl(url);
			if(create){
				//展示任务信息
				resultArea.setText("---任务---\r\n" + t.getScriptMember() + "\r\n");
			}
			if(collect){
				result = parseJsScriptUseRhino(getCollectScriptFile(setting.getCollectPictureScriptPath()),"parse", args);
				if(result == null){
					resultArea.setText(resultArea.getText() + setting.getCollectPictureScriptPath() + "脚本解析出错\r\n");
					return;
				}
				//展示图片信息
				resultArea.setText(resultArea.getText() + "\r\n---图片列表---\r\n" + result.toString() + "\r\n");
				if(download){
					//展示第一张图片的真实下载地址
					List<Picture> pics = JsonUtil.jsonArray2beanList(Picture.class, result.toString());
					if(pics != null){
						source = WebClient.postRequestWithCookie(pics.get(0).getUrl(), setting.getCookieInfo());
						args[0] = source;
						result = parseJsScriptUseRhino(getDownloadScriptFile(setting.getDownloadScriptPath()),"parse", args);
						if(result == null){
							resultArea.setText(resultArea.getText() + setting.getDownloadScriptPath() + "脚本解析出错\r\n");
							return;
						}
						resultArea.setText(resultArea.getText() + "\r\n---第一张图片的真实下载地址---\r\n" + result.toString() + "\r\n");
					}
				}
			}else if(download){
				result = parseJsScriptUseRhino(getCollectScriptFile(setting.getCollectPictureScriptPath()),"parse", args);
				//展示第一张图片的真实下载地址
				List<Picture> pics = JsonUtil.jsonArray2beanList(Picture.class, result.toString());
				if(pics != null){
					source = WebClient.postRequestWithCookie(pics.get(0).getUrl(), setting.getCookieInfo());
					args[0] = source;
					result = parseJsScriptUseRhino(getDownloadScriptFile(setting.getDownloadScriptPath()),"parse", args);
					if(result == null){
						resultArea.setText(resultArea.getText() + setting.getDownloadScriptPath() + "脚本解析出错\r\n");
						return;
					}
					resultArea.setText(resultArea.getText() + "\r\n---第一张图片的真实下载地址---\r\n" + result.toString());
				}
			}
		} catch (ConnectTimeoutException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + "网络连接超时");
		} catch (SocketTimeoutException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + "网络连接超时");
		} catch (FileNotFoundException e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + e.getMessage());
		} catch (Exception e) {
			resultArea.setText(resultArea.getText() + "\r\n======异常======" + e.getMessage());
		}
	}*/
	/**
	 * 
	 * @param total
	 * @param index
	 * @return
	 */
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