package org.arong.egdownloader.ui;

import java.awt.Cursor;
/**
 * 光标管理器
 * @author 阿荣
 * @since 2013-8-25
 *
 */
public final class CursorManager {
	private static Cursor pointerCursor;
	public static Cursor getPointerCursor(){
		if(pointerCursor == null){
			//手形光标
			pointerCursor = new Cursor(Cursor.HAND_CURSOR);
		}
		return pointerCursor;
	}
}
