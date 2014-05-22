package org.arong.egdownloader.ui;

import java.awt.Component;
import java.awt.Container;

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
}
