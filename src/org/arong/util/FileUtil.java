package org.arong.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

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
			throw e;
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
}
