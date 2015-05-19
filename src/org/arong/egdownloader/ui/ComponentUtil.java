package org.arong.egdownloader.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

public final class ComponentUtil {
	
	/**
	 * 将若干组件依次添加到容器中
	 * @param container
	 * @param components
	 */
	public static void addComponents(Container container , Component ...components){
		if (container != null && components != null) {
			for (Component comp : components) {
				if(comp != null){
					container.add(comp);
				}
			}
		}
	}
	
	/**
	 * 释放所有窗口
	 * @param windows
	 */
	public static void disposeAll(Window...windows){
		if(windows != null && windows.length > 0){
			for(int i = 0; i < windows.length; i ++){
				if(windows[i] != null){
					windows[i].dispose();
				}
			}
		}
	}
}
