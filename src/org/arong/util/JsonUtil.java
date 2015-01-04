package org.arong.util;

import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 封装了将各种对象、集合转换成json字符串的方法
 * @author arong
 * @since 1.0.10
 */
public final class JsonUtil {

	/**
	 * 将javabean对象转换为json字符串
	 * 
	 * @param bean
	 *            带着数据的javabean对象
	 * @param excludes
	 *            对javabean的一些字段排除
	 * 
	 * @return 符合json规范的字符串
	 */
	public static <T> String bean2Json(T bean, String... excludes) {
		StringBuilder sb = new StringBuilder("{");
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) bean.getClass();
			Method[] methods = clazz.getDeclaredMethods();
			String methodName = null;
			String name = null;
			Object value = null;
			for (int i = 0; i < methods.length; i++) {
				methodName = methods[i].getName();
				if (methodName.startsWith("get") || methodName.startsWith("is")) {
					int len = methodName.startsWith("get") ? 3 : 2;
					// 通过Introspector类的静态方法decapitalize获取每个get方法对应的属性名
					name = Introspector.decapitalize(methodName.substring(len,
							methodName.length()));
					// 排除字段
					if (!Arrays.asList(excludes).contains(name)) {
						// 执行get方法，获得返回值
						value = methods[i].invoke(bean);
						if (value == null)
							value = "null";
						executeAppend(sb, "{", name, value);
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 将一个带着数据的javabean集合对象转化成符合JSON规范的字符串。<br>
	 * 返回的json字符串 键/值 都是String类型，如果某个属性没有数据则为"null"，<br>
	 * 如果某个属性为另外的javabean，则返回Object.toString()生成的值。
	 * <p>
	 * 此方法中要求javabean必须符合命名规范，生成的json键与javabean中的属性名一致。
	 * </p>
	 * 
	 * @param list
	 *            带着数据的javabean集合
	 * @param excludes
	 *            对javabean的一些字段排除
	 * @return 符合json规范的字符串
	 * @author arong
	 * @since 1.0.2
	 */
	public static <T> String beanList2Json(List<T> list, String... excludes) {
		// 定义json的可变字符串
		StringBuilder json = new StringBuilder("[");
		T bean = null;
		for (int i = 0; i < list.size(); i++) {
			// 获取bean对象
			bean = list.get(i);
			json.append((i != 0 ? "," : "") + bean2Json(bean, excludes));
		}
		json.append("]");
		return json.toString();
	}

	/**
	 * 将一个javabean集合转化为json字符串<br>
	 * 此方法生成的字符串长度比beanList2Json方法要短<br>
	 * 格式：{字段1:[值,值,值],字段2:[值,值,值]……}
	 * 
	 * @param list
	 *            带着数据的javabean集合
	 * @param excludes
	 *            对javabean的一些字段排除
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> String beanList2Json2(List<T> list, String... excludes) {
		// 定义json的可变字符串
		StringBuilder json = new StringBuilder("{");
		StringBuilder sb = null;
		// bean方法名
		String methodName = null;
		// bean属性名
		String name = null;
		// bean属性值
		Object value = null;
		T bean = null;  
		Integer length = list.size();
		if (length > 0) {
			Class<T> clazz = (Class<T>) list.get(0).getClass();
			Method[] methods = clazz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				// 获取方法名
				methodName = methods[i].getName();
				if (methodName.startsWith("get") || methodName.startsWith("is")) {
					int len = methodName.startsWith("get") ? 3 : 2;
					// 通过Introspector类的静态方法decapitalize获取每个get方法对应的属性名
					name = Introspector.decapitalize(methodName.substring(len,
							methodName.length()));
					// 排除字段
					if (!Arrays.asList(excludes).contains(name)) {
						sb = new StringBuilder(name + ":[");
						for (int j = 0; j < length; j++) {
							// 获取bean对象
							bean = list.get(j);
							// 定义一个可变字符串，用于构造js数组[]中的内容
							try {
								// 执行get方法，获得返回值
								value = methods[i].invoke(bean);
								if (value == null)
									value = "null";
								executeAppend2(sb, name + ":[", value);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						sb.append("]");
						if (json.toString().equals("{")) {
							json.append(sb.toString());
						} else {
							json.append("," + sb.toString());
						}
					}
				}
			}
		}
		json.append("}");
		return json.toString();
	}

	/**
	 * 将一个map对象转换为json字符串
	 * 
	 * @param map
	 *            map对象
	 * @return
	 */
	public static String map2Json(Map<Object, Object> map) {
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		list.add(map);
		return mapList2Json(list);
	}

	/**
	 * 将一个Map<Object, Object>的集合转换为json字符串
	 * 
	 * @param list
	 *            Map<Object, Object>的集合
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public static String mapList2Json(List<Map<Object, Object>> list) {
		// 定义json的可变字符串
		StringBuilder json = new StringBuilder("[");
		// 根据json的格式，{}对的个数为集合的长度，每个{}对代表一个集合中的一个map对象
		// map值
		Object value = null;
		StringBuilder sb = null;
		Map<Object, Object> map = null;
		for (int i = 0; i < list.size(); i++) {
			// 定义一个可变字符串，用于构造{}中的内容
			sb = new StringBuilder("{");
			map = list.get(i);
			Set<Object> set = map.keySet();
			for (Object name : set) {
				value = map.get(name);
				if (value == null)
					value = "null";
				//如果还包含有集合，则递归
				if(value instanceof List){
					executeAppend(sb, "{", name.toString(), mapList2Json((List)value));
				}else{
					executeAppend(sb, "{", name.toString(), value);
				}
			}
			// 拼接成完整的{}对
			sb.append("}");
			if (json.toString().equals("[")) {
				json.append(sb.toString());
			} else {
				json.append("," + sb.toString());
			}
		}
		json.append("]");
		return json.toString();
	}

	/**
	 * 将一个数组转换为json字符串，key为数组的索引，从0开始
	 * 
	 * @param array
	 *            数组对象
	 * @return
	 */
	public static String array2Json(Object[] array) {
		List<Object[]> list = new ArrayList<Object[]>();
		list.add(array);
		return arrayList2Json(list);
	}

	/**
	 * 将一个数组集合转换为json字符串
	 * 
	 * @param list
	 *            数组集合
	 * @return
	 */
	public static String arrayList2Json(List<Object[]> list) {
		// 定义json的可变字符串
		StringBuilder json = new StringBuilder("[");
		// 根据json的格式，{}对的个数为集合的长度，每个{}对代表一个集合中的一个array对象
		// map值
		Object value = null;
		StringBuilder sb = null;
		Object[] array = null;
		for (int i = 0; i < list.size(); i++) {
			// 定义一个可变字符串，用于构造{}中的内容
			sb = new StringBuilder("{");
			array = list.get(i);
			for (int j = 0; j < array.length; j++) {
				value = array[j];
				if (value == null)
					value = "null";
				executeAppend(sb, "{", j + "", value);
			}
			// 拼接成完整的{}对
			sb.append("}");
			if (json.toString().equals("[")) {
				json.append(sb.toString());
			} else {
				json.append("," + sb.toString());
			}
		}
		json.append("]");
		return json.toString();
	}

	/**
	 * 内部方法，用于StringBuilder的json字符串追加
	 * 
	 * @param sb
	 *            StringBuilder对象
	 * @param prefix
	 *            StringBuilder对象初始的值
	 * @param name
	 *            键
	 * @param value
	 *            值
	 */
	private static void executeAppend(StringBuilder sb, String prefix,
			String name, Object value) {
		if (sb.toString().equals(prefix)) {
			if (value instanceof java.lang.String
					|| value instanceof java.util.Date) {
				sb.append(name + ":\'" + stringtoJSON(value.toString()) + "\'");
			} else {
				sb.append(name + ":" + stringtoJSON(value.toString()));
			}
		} else {
			if (value instanceof java.lang.String
					|| value instanceof java.util.Date) {
				sb.append("," + name + ":\'" + stringtoJSON(value.toString())
						+ "\'");
			} else {
				sb.append("," + name + ":" + stringtoJSON(value.toString()));
			}
		}
	}

	/**
	 * 内部方法，用于StringBuilder的json字符串追加
	 * 
	 * @param sb
	 * @param prefix
	 * @param value
	 */
	private static void executeAppend2(StringBuilder sb, String prefix,
			Object value) {
		if (sb.toString().equals(prefix)) {
			if (value instanceof java.lang.String
					|| value instanceof java.util.Date) {
				sb.append("\"" + stringtoJSON(value.toString()) + "\"");
			} else {
				sb.append(stringtoJSON(value.toString()));
			}
		} else {
			if (value instanceof java.lang.String
					|| value instanceof java.util.Date) {
				sb.append("," + "\"" + stringtoJSON(value.toString()) + "\"");
			} else {
				sb.append("," + stringtoJSON(value.toString()));
			}
		}
	}

	/**
	 * 将一个字符串s转换为能在json中存储而不破坏其格式的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String stringtoJSON(String s) {
		if (s == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 用于重写javabean中的toString()方法<br>
	 * 当一个bean中包含另外一个bean时，则另一个bean中应该重写toString()方法
	 * @param bean
	 * @param excludes
	 * @return
	 */
	public static String toString(Object bean, String... excludes){
		StringBuilder sb = new StringBuilder("{");
		Class<?> clazz = bean.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		String methodName = null;
		String name = null;
		Object value = null;
		Integer len = null;
		for (int i = 0; i < methods.length; i++) {
			methodName = methods[i].getName();
			if(methodName.startsWith("get") || methodName.startsWith("is")){
				len = methodName.startsWith("get")?3:2;
				name = Introspector.decapitalize(methodName.substring(len, methodName.length()));
				//排除字段
				if(!Arrays.asList(excludes).contains(name)){
					try {
						value = methods[i].invoke(bean);
						if(value == null)
							value = "null";
						executeAppend(sb, "{", name, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 简单(不嵌套)json字符串转换为map<br>
	 * 约定：key必须要有引号,引号为双引号
	 * @param json
	 * @return Map
	 */
	public static Map<String, String> json2Map(String json){
		if(json != null && !"".equals(json.trim())){
			if('{' == json.charAt(0)){
				if('}' == json.charAt(json.length() - 1)){
					json = json.substring(2, json.length() - 1);
					String[] arr = json.split(",\"");
					String[] kv;
					Map<String, String> data = new HashMap<String, String>();
					for(int i = 0; i < arr.length; i ++){
						if(arr[i].indexOf("\":") != -1){
							kv = arr[i].split("\":");
							if('"' == kv[1].charAt(0) && '"' == kv[1].charAt(kv[1].length() - 1)){
								data.put(kv[0], kv[1].substring(1, kv[1].length() - 1));
							}else{
								data.put(kv[0], kv[1]);
							}
						}else{
							throw new RuntimeException("json格式错误");
						}
					}
					return data;
				}else{
					throw new RuntimeException("json尾字符不为：}");
				}
			}else{
				throw new RuntimeException("json首字符不为：{");
			}
		}else{
			throw new RuntimeException("json为空");
		}
	}
	
	/**
	 * 简单(不嵌套)json字符串转换为javabean<br>
	 * javabean中的字段有set方法，且字段名与json的key一致
	 * @param <T>
	 * @param clazz
	 * @param json
	 * @return T
	 */
	public static <T> T json2bean(Class<T> clazz, String json){
		Map<String, String> map = json2Map(json);
		T t = null;
		try {
			t = clazz.newInstance();
			if(map != null){
				for(String key : map.keySet()){
					BeanUtil.setProperty(t, key, new String[]{map.get(key)});
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 简单(不嵌套)json数组字符串转换为map集合
	 * @param json
	 * @return List<Map<String,String>>
	 */
	public static List<Map<String, String>> jsonArray2MapList(String json){
		if(json != null && !"".equals(json.trim())){
			if('[' == json.charAt(0)){
				if(']' == json.charAt(json.length() - 1)){
					json = json.substring(2, json.length() - 2);
					String[] arr = json.split("\\},\\{");
					List<Map<String, String>> maps = new ArrayList<Map<String,String>>();
					for(int i = 0; i < arr.length; i ++){
						maps.add(json2Map("{" + arr[i] + "}"));
					}
					return maps;
				}else{
					throw new RuntimeException("json尾字符不为：]");
				}
			}else{
				throw new RuntimeException("json首字符不为：[");
			}
		}else{
			throw new RuntimeException("json为空");
		}
	}
	
	/**
	 * 简单(不嵌套)json数组字符串转换为javabean集合
	 * @param json
	 * @return List<T>
	 */
	public static <T> List<T> jsonArray2beanList(Class<T> clazz, String json){
		if(json != null && !"".equals(json.trim())){
			if('[' == json.charAt(0)){
				if(']' == json.charAt(json.length() - 1)){
					json = json.substring(2, json.length() - 2);
					String[] arr = json.split("\\},\\{");
					List<T> beans = new ArrayList<T>();
					for(int i = 0; i < arr.length; i ++){
						beans.add(json2bean(clazz, "{" + arr[i] + "}"));
					}
					return beans;
				}else{
					throw new RuntimeException("json尾字符不为：]");
				}
			}else{
				throw new RuntimeException("json首字符不为：[");
			}
		}else{
			throw new RuntimeException("json为空");
		}
	}
	
	public static void main(String[] args) {
		String json = "{\"name\":\"m0000.jpg\",\"total\":12,\"score\":96.5,\"isCompleted\":false,\"url\":\"http://www.baidu.com/s/77eo9ec5bb/672439-1\"}";
		System.out.println(json2Map(json));
		/*List<Map<Object, Object>> list = new ArrayList<Map<Object,Object>>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", "zsr");
		map.put("gender", "male");
		map.put("age", "18");
		map.put("job", "java工程师");
		List<Map> in_list = new ArrayList<Map>();
		Map map_1 = new HashMap();
		map_1.put("er", "1232");
		map_1.put("gp", "12342");
		map_1.put("lk", "23123");
		in_list.add(map_1);
		Map map_2 = new HashMap();
		map_2.put("er", "1122");
		map_2.put("gp", "11342");
		map_2.put("lk", "24123");
		in_list.add(map_2);
		map.put("datas", in_list);
		list.add(map);
		String sss = mapList2Json(list);
		System.out.println(sss);*/
	}
}