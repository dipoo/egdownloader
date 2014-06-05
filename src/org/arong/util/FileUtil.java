package org.arong.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
	
	public static boolean storeStream(String path, String name, InputStream in) throws IOException{
    	File dir = new File(path);
    	FileUtil.ifNotExistsThenCreate(dir);
    	BufferedInputStream bis = null;
    	BufferedOutputStream bos = null;
    	try {
    		File fs = new File(path + "/" + name);
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(new FileOutputStream(fs));
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = bis.read(buff)) != -1) {
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
    	return false;
    }
}
