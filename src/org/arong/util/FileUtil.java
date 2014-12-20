package org.arong.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * 目录文件工具类
 * @author 阿荣
 * @since 2014-05-26
 */
public final class FileUtil {
	/**
	 * 如果目录不存在，则创建
	 * @param path
	 */
	public static void ifNotExistsThenCreate(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	/**
	 * 如果目录不存在，则创建
	 * @param path
	 */
	public static void ifNotExistsThenCreate(File file){
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	/**
	 * 存储文件流，返回文件的大小
	 * @param path
	 * @param name
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static int storeStream(String path, String name, InputStream in) throws IOException{
    	File dir = new File(path);
    	FileUtil.ifNotExistsThenCreate(dir);
    	BufferedInputStream bis = null;
    	BufferedOutputStream bos = null;
    	int size = 0;
    	try {
    		File fs = new File(path + "/" + name);
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(new FileOutputStream(fs));
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = bis.read(buff)) != -1) {
				size += len;
				bos.write(buff, 0, len);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return size;
    }
	/**
	 * 将字节数转化为合适的单位字符串
	 * @param size
	 * @return
	 */
	public static String showSizeStr(int size){
		String s = "0B";
		double d = (double)size;
		DecimalFormat df = new DecimalFormat("#####0.00");
		if(size < 1024){
			s = d + "B";
		}else if(d < 1024 * 1024 ){
			s = df.format(d / 1024.0) + "K";
		}else if(d < 1024 * 1024 * 1024){
			s = df.format(d / (1024.0 * 1024.0)) + "M";
		}else if(d < 1024 * 1024 * 1024 * 1024){
			s = df.format(d / (1024.0 * 1024.0 * 1024.0)) + "G";
		}
		return s;
	}
	
	public static String filterDir(String dir){
		try{
			dir = dir.trim();
			if("".equals(dir) || dir == null){
				return "新建文件夹";
			}
			//过滤windows下的文件夹保留单词
			if("auk".equalsIgnoreCase(dir) || "com1".equalsIgnoreCase(dir)
					|| "com2".equalsIgnoreCase(dir) || "com3".equalsIgnoreCase(dir)
					|| "com4".equalsIgnoreCase(dir) || "com5".equalsIgnoreCase(dir)
					|| "com6".equalsIgnoreCase(dir) || "com7".equalsIgnoreCase(dir)
					|| "com8".equalsIgnoreCase(dir) || "com9".equalsIgnoreCase(dir)
					|| "con".equalsIgnoreCase(dir) || "nul".equalsIgnoreCase(dir)
					|| "lpt1".equalsIgnoreCase(dir)|| "lpt2".equalsIgnoreCase(dir)
					|| "lpt3".equalsIgnoreCase(dir)|| "lpt4".equalsIgnoreCase(dir)
					|| "lpt5".equalsIgnoreCase(dir)|| "lpt6".equalsIgnoreCase(dir)
					|| "lpt7".equalsIgnoreCase(dir)|| "lpt8".equalsIgnoreCase(dir)
					|| "lpt9".equalsIgnoreCase(dir)){
				dir += "-" + UUID.randomUUID().toString();
				return dir;
			}
			//过滤windows下的文件夹无效字符
			else{
				return dir.replaceAll("\\|", "-")
						.replaceAll("\\*", "-")
						.replaceAll("\\.", "-")
						.replaceAll("\\<", "-")
						.replaceAll("\\>", "-")
						.replaceAll("\\?", "-")
						.replaceAll("\\:", "-")
						.replaceAll("\\/", "-")
						.replaceAll("\\\\", "-");
			}
			
		}catch(Exception e){
			Tracker.println(FileUtil.class, e.getMessage());
		}
		return null;
	}
	
	public static String getAppPath(Class cls) {
        //检查用户传入的参数是否为空
        if (cls == null)
            throw new java.lang.IllegalArgumentException("参数不能为空！");
        ClassLoader loader = cls.getClassLoader();
        //获得类的全名，包括包名
        String clsName = cls.getName() + ".class";
        //获得传入参数所在的包
        Package pack = cls.getPackage();
        String path = "";
        //如果不是匿名包，将包名转化为路径
        if (pack != null) {
            String packName = pack.getName();
            //此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
            if (packName.startsWith("java.") || packName.startsWith("javax."))
                throw new java.lang.IllegalArgumentException("不要传送系统类！");
            //在类的名称中，去掉包名的部分，获得类的文件名
            clsName = clsName.substring(packName.length() + 1);
            //判定包名是否是简单包名，如果是，则直接将包名转换为路径，
            if (packName.indexOf(".") < 0)
                path = packName + "/";
            else {//否则按照包名的组成部分，将包名转换为路径
                int start = 0, end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }
       
        //调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        java.net.URL url = loader.getResource(path + clsName);
        //从URL对象中获取路径信息
       
        String realPath = url.getPath();
        //去掉路径信息中的协议名"file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1)
            realPath = realPath.substring(pos + 5);
       
       
        //去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos = realPath.indexOf(path + clsName);
        realPath = realPath.substring(0, pos - 1);
       
        //如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        if (realPath.endsWith("!"))
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
       
       
        //结果字符串可能因平台默认编码不同而不同。因此，改用 decode(String,String) 方法指定编码。
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(realPath);
        return realPath;
    }//getAppPath定义结束
	
}
