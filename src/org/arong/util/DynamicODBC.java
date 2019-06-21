package org.arong.util;
import java.text.SimpleDateFormat;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

/**
 * 利用开源包registry-3.1.3修改注册表，注意dll的引用，有32位和64位的区别
 * @author E-mail: gulijun2001@163.com
 * QQ: 23796788
 * @version 创建时间：2013-10-31 上午10:23:20
 * 类说明
 */
public class DynamicODBC {
	
    static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 把信息存储到注册表HKEY_LOCAL_MACHINE下的某个节点的某一变量中，有则修改，无则创建
        public static boolean setValue(String folder, String subKeyNode,String subKeyName,String subKeyValue) {
            try {
                RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
                RegistryKey subKey = software.createSubKey(subKeyNode, "");
                subKey.setValue(new RegStringValue(subKey, subKeyName,subKeyValue));
                subKey.closeKey();
                return true;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        // 删除注册表中某节点下的某个变量
        public static boolean deleteValue(String folder, String subKeyNode,String subKeyName) {
            try {
                RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
                RegistryKey subKey = software.createSubKey(subKeyNode, "");
                subKey.deleteValue(subKeyName);
                subKey.closeKey();
                return true;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        // 删除注册表中某节点下的某节点
        public static boolean deleteSubKey(String folder, String subKeyNode) {
            try {
                RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
                software.deleteSubKey(subKeyNode);
                software.closeKey();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        // 打开注册表项并读出相应的变量名的值
        public static String getValue(String folder, String subKeyNode,
                String subKeyName) {
            String value = "";
            try {
                RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
                RegistryKey subKey = software.openSubKey(subKeyNode);
                value = subKey.getStringValue(subKeyName);
                subKey.closeKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }
        
        public static void creatItem(){
            //创建注册表项并设置相应的值
            try {
              RegistryKey software = Registry.HKEY_CURRENT_USER.openSubKey("Software").openSubKey("ODBC").openSubKey("ODBC.INI");//操作权限是通过RegistryKey来获取的。
              RegistryKey subKey = software.createSubKey("dds", "");
              subKey.setValue(new RegStringValue(subKey, "subKey1","subKey1Value"));
              subKey.setValue(new RegStringValue(subKey, "subKey2","subKey2Value"));
              subKey.closeKey();
            } catch (NoSuchKeyException e) {
              e.printStackTrace();
            } catch (RegistryException e) {
              e.printStackTrace();
            }
        }
        
        public static void readItem(){
            //打开注册表项并读出相应的值
            try {
              RegistryKey software = Registry.HKEY_CURRENT_USER.openSubKey("SOFTWARE");
              RegistryKey subKey = software.openSubKey("SubKeyName");
              String subKey1Value = subKey.getStringValue("subKey1");
              String subKey2Value = subKey.getStringValue("subKey2");
              System.out.println(subKey1Value);
              System.out.println(subKey2Value);
              subKey.closeKey();
            } catch (NoSuchKeyException e) {
              e.printStackTrace();
            } catch (RegistryException e) {
              e.printStackTrace();
            } 
        }

	/**
	 * @param args
	 */
	 public static void main(String[] str) {
	        try {
	              RegistryKey child = Registry.HKEY_CURRENT_USER.openSubKey("Software").openSubKey("ODBC").openSubKey("ODBC.INI").openSubKey("dss",RegistryKey.ACCESS_ALL);//操作权限是通过RegistryKey来获取的。
	              String de = "glj";  //我的DBF数据的目录
	              //其中，data_0930是我第一次设置的数据源的一个注册表的名称
	              System.out.println("StringValue================"+child.getStringValue("LastUser"));
	              child.setValue(new RegStringValue(child,"LastUser",de));
	              System.out.println("getFullName================"+child.getFullName());
	              
	              child = Registry.HKEY_CURRENT_USER.openSubKey("Software").openSubKey("ODBC").openSubKey("ODBC.INI",RegistryKey.ACCESS_ALL);//操作权限是通过RegistryKey来获取的。
	              //child.deleteSubKey("dds");
	              // child.closeKey();
	              // child.createSubKey("dds", "ww");
	              child.closeKey();
	              creatItem();
	        } catch (Exception e) {
	             e.printStackTrace();
	        }
	    }
}
