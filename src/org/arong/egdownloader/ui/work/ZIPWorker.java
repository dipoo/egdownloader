package org.arong.egdownloader.ui.work;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.table.TaskingTable;
import org.arong.egdownloader.ui.window.EgDownloaderWindow;
import org.arong.egdownloader.ui.window.ZiptingWindow;
import org.arong.util.Tracker;
/**
 * 打包任务线程类
 * @author dipoo
 * @since 2016-03-30
 */
public class ZIPWorker extends SwingWorker<Void, Void>{
	
	private JFrame window;
	private TaskingTable table;
	private ZiptingWindow w;
	public ZIPWorker(JFrame mainWindow, TaskingTable table, ZiptingWindow w){
		this.window = mainWindow;
		this.table = table;
		this.w = w;
	}
	
	protected Void doInBackground() throws Exception {
		EgDownloaderWindow mainWindow = (EgDownloaderWindow)window;
		int index = table.getSelectedRow();
		Task task = table.getTasks().get(index);
		List<Picture> pics = task.getPictures();
		ZipOutputStream out = null;
		BufferedOutputStream bo = null;
		FileOutputStream fos = null;
		int success = 0;
		String zipfilepath = null;
		try{
			w.nameLabel.setText(task.getDisplayName());
			int fileLength = task.getTotal();
			
			w.bar.setValue(0);
			w.totalLabel.setText("文件总数：" + fileLength);
			w.bar.setMaximum(fileLength);
			w.setVisible(true);//显示窗口
			
			Tracker.println(task.getDisplayName() + "：打包中...");
			zipfilepath = task.getSaveDir() + File.separator + task.getRealSaveDirName() + ".zip";
			fos = new FileOutputStream(zipfilepath);
			out = new ZipOutputStream(fos);
	        bo = new BufferedOutputStream(out);  
	        //zip(out, inputFile, inputFile.getName(), bo);
			for(int i = 0; i < pics.size(); i ++){
				boolean succ = zip(out, new File(task.getSaveDir() + File.separator + pics.get(i).getName()), pics.get(i).getName(), bo);
				if(succ){
					success ++;
				}
			}
			if(success == 0){
				w.bar.setValue(0);
				//out = new ZipOutputStream(new FileOutputStream(task.getSaveDir() + File.separator + task.getRealSaveDirName() + ".zip"));  
		        bo = new BufferedOutputStream(out);  
				for(int i = 0; i < pics.size(); i ++){
					boolean succ = zip(out, new File(task.getSaveDir() + File.separator + pics.get(i).getNum() + ".jpg"), pics.get(i).getNum() + ".jpg", bo);
					if(succ){
						success ++;
					}
				}
				if(success > 0){
					Desktop.getDesktop().open(new File(task.getSaveDir()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bo != null){
				try{
					bo.close();  
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(out != null){
				try{
					out.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(fos != null){
				try{
					fos.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(success == 0 && zipfilepath != null){
				Tracker.println(task.getDisplayName() + "：不存在可以打包的图片");
				File zipfile = new File(zipfilepath);//删除空的zip文件
				if(zipfile.exists()){
					zipfile.delete();
				}
			}
			Tracker.println(task.getDisplayName() + "：打包完成（保存在下载目录）");
			w.dispose();
		}
		return null;
	}
	
	private boolean zip(ZipOutputStream out, File f, String base,  
            BufferedOutputStream bo) throws Exception { 
		if(f == null || !f.exists()){
			w.bar.setValue(w.bar.getValue() + 1);
			return false;
		}
		FileInputStream in = null;
		BufferedInputStream bi = null;
		try{
	        out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base  
	        //System.out.println(base);  
	        in = new FileInputStream(f);  
	        bi = new BufferedInputStream(in);  
	        byte[] buf = new byte[1024];
	        int len = bi.read(buf);
	        while (len >= 0) {  
	            bo.write(buf, 0, len); // 将字节流写入当前zip目录  
	            len = bi.read(buf);
	        }
	        bo.flush();
		}catch(Exception e){
			return false;
		}finally{
			if(bi != null)
				try{
					bi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			if(in != null)
				try{
					in.close(); // 输入流关闭
				}catch(Exception e){
					e.printStackTrace();
				}
			w.bar.setValue(w.bar.getValue() + 1);
		}
		return true;
    }  
	 
}
