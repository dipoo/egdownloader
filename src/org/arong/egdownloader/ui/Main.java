package org.arong.egdownloader.ui;

import java.awt.Font;

import javax.swing.UIManager;

import org.arong.egdownloader.ui.window.InitWindow;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 * 启动程序的唯一入口
 * 
 * @author 阿荣
 * @since 2013-8-25
 */
public class Main {

	public static void main(String[] args) {
		// 调整默认字体
		for (int i = 0; i < FontConst.DEFAULT_FONT.length; i++)
			UIManager.put(FontConst.DEFAULT_FONT[i], new Font("微软雅黑",
					Font.BOLD, 12));
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {

		}
		new InitWindow();
		// 异步执行
		/*SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new InitWindow();
			}
		});*/
	}
}
