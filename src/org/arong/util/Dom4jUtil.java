package org.arong.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * dom4j工具类
 * @author 阿荣
 * @since 2014-05-29
 */
public final class Dom4jUtil {
	public static boolean locked;
	public Dom4jUtil(){}

    public static Document getDOM(File file) throws Exception{
        return new SAXReader().read(file);
    }
    
    public static Document getDOM(String filePath) throws Exception{
        return new SAXReader().read(new File(filePath));
    }
    
    public static void createElement(Document doc, String ele){
    	doc.addElement(ele);
    }
    
    public static void appendElement(Element parent, Element ele){
    	parent.add(ele);
    }
    
    public static void deleteElement(Element parent, Element ele){
    	parent.remove(ele);
    }
    
    public static void writeDOM2XML(String file_path, Document doc) throws Exception{
    	FileUtil.ifNotExistsThenCreate(file_path.substring(0, file_path.lastIndexOf("/")));
        writeDOM2XML(new File(file_path), doc);
    }

    public static void writeDOM2XML(File file, Document doc) throws Exception{
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), format);
        writer.write(doc);
        writer.close();
    }
}
