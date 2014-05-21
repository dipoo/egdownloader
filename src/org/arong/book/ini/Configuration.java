package org.arong.book.ini;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

/**
 * 解析ini文件，将采集站点信息封转到集合中.<br>
 * 封转了更新ini文件的一些方法
 * 
 * @author 阿荣
 * @since 2013-8-18
 * 
 */
public final class Configuration {
	public final static String INI_FILE_NAME = "sites.ini";
	// 代表站点配置文件的对象
	private static Ini ini;
	// 站点配置文件的路径
	private static String iniPath;
	// 全部采集站点的信息集合
	private static List<SiteInfo> sites;
	// 全部采集站点的名称数组
	private static String[] siteNames;

	// 加载并且解析ini文件
	public static void configure() throws InvalidFileFormatException,
			FileNotFoundException, IOException, IniException {
		String d = "/config/";
		URL dir = Configuration.class.getResource(d);
		if (dir == null) {
			throw new FileNotFoundException(Configuration.class.getName()
					+ ":"+ d);
		} else {
			iniPath = URLDecoder.decode(dir.getPath()
					+ Configuration.INI_FILE_NAME, "UTF-8");
			try {
				ini = new Ini(new File(iniPath));
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException("没有找到" + iniPath + "文件");
			}
			Set<Entry<String, Section>> sectionsMap = ini.entrySet();

			sites = new ArrayList<SiteInfo>();
			
			String key;
			Section section;
			// 先获取可用的站点数组长度
			int length = 0;
			for (Entry<String, Section> entry : sectionsMap) {
				// sectionName
				key = entry.getKey();
				// section
				section = entry.getValue();
				//display
				String display = section.get(IniPropertyName.DISPLAY);
				if (display != null && "false".equals(display)) {
					continue;
				}
				length ++;
			}
			// 采集站点名称字符串数组
			siteNames = new String[length];
			int i = 0;
			for (Entry<String, Section> entry : sectionsMap) {
				// sectionName
				key = entry.getKey();
				// section
				section = entry.getValue();
				
				//display
				String display = section.get(IniPropertyName.DISPLAY);
				if (display != null && "false".equals(display)) {
					continue;
				}
				// siteName
				String siteName = section.get(IniPropertyName.SITE_NAME);
				if (IniPropertyValidator.isNull(siteName)) {
					throw new IniException(key.trim(),
							IniPropertyName.SITE_NAME, IniErrorMsg.NULL_MESSAGE);
				}
				siteNames[i] = siteName.trim();
				i++;
				// href
				String href = section.get(IniPropertyName.HREF);
				if (IniPropertyValidator.isNull(href)) {
					throw new IniException(href.trim(), IniPropertyName.HREF,
							IniErrorMsg.NULL_MESSAGE);
				} else if (IniPropertyValidator.invalidHref(href)) {
					throw new IniException(key.trim(), IniPropertyName.HREF,
							IniErrorMsg.HREF_MESSAGE + ".\n" + href.trim());
				}
				// isGekai
				String isGeKai = section.get(IniPropertyName.IS_GEKAI);
				if (IniPropertyValidator.isNull(isGeKai)) {
					throw new IniException(key.trim(),
							IniPropertyName.IS_GEKAI, IniErrorMsg.NULL_MESSAGE);
				} else if (IniPropertyValidator.notBoolean(isGeKai)) {
					throw new IniException(key.trim(),
							IniPropertyName.IS_GEKAI,
							IniErrorMsg.BOOLEAN_MESSAGE);
				}
				// geKaiFu
				String geKaiFu = section.get(IniPropertyName.GEKAIFU);
				if ("true".equals(isGeKai.trim())
						&& (geKaiFu == null || "".equals(geKaiFu))) {
					throw new IniException(key.trim(), IniPropertyName.GEKAIFU,
							IniErrorMsg.NULL_MESSAGE);
				}
				// prefix
				String prefix = section.get(IniPropertyName.PREFIX);
				// suffix
				String suffix = section.get(IniPropertyName.SUFFIX);
				// typeIdentify
				String typeIdentify = section
						.get(IniPropertyName.TYPE_IDENTIFY);
				if (IniPropertyValidator.isNull(typeIdentify)) {
					throw new IniException(key.trim(),
							IniPropertyName.TYPE_IDENTIFY,
							IniErrorMsg.NULL_MESSAGE);
				}
				int arraynum = typeIdentify.trim().split(",").length;
				// typeName
				String typeName = section.get(IniPropertyName.TYPE_NAME);
				if (IniPropertyValidator.isNull(typeName)) {
					throw new IniException(key.trim(),
							IniPropertyName.TYPE_NAME, IniErrorMsg.NULL_MESSAGE);
				} else if (IniPropertyValidator.invalidArrayNum(typeName,
						arraynum)) {
					throw new IniException(key.trim(),
							IniPropertyName.TYPE_NAME,
							IniErrorMsg.ARRAY_NUM_MESSAGE + ".\n[" + typeName
									+ "],个数应该为" + arraynum);
				}
				// typeId
				String typeId = section.get(IniPropertyName.TYPE_ID);
				if (IniPropertyValidator.isNull(typeId)) {
					throw new IniException(key.trim(), IniPropertyName.TYPE_ID,
							IniErrorMsg.NULL_MESSAGE);
				} else if (IniPropertyValidator.invalidArrayNum(typeId,
						arraynum)) {
					throw new IniException(key.trim(), IniPropertyName.TYPE_ID,
							IniErrorMsg.ARRAY_NUM_MESSAGE + ".\n[" + typeId
									+ "],个数应该为" + arraynum);
				} else if (IniPropertyValidator.invalidPositiveInteger(typeId)) {
					throw new IniException(key.trim(), IniPropertyName.TYPE_ID,
							IniErrorMsg.NUMBER_MESSAGE);
				}
				// typeCurrentPage
				String typeCurrentPage = section
						.get(IniPropertyName.TYPE_CURRENT_PAGE);
				if (IniPropertyValidator.isNull(typeCurrentPage)) {
					throw new IniException(key.trim(),
							IniPropertyName.TYPE_CURRENT_PAGE,
							IniErrorMsg.NULL_MESSAGE);
				} else if (IniPropertyValidator.invalidArrayNum(
						typeCurrentPage, arraynum)) {
					throw new IniException(key.trim(),
							IniPropertyName.TYPE_CURRENT_PAGE,
							IniErrorMsg.ARRAY_NUM_MESSAGE + ".\n[" + typeCurrentPage
									+ "],个数应该为" + arraynum);
				} else if (IniPropertyValidator
						.invalidPositiveInteger(typeCurrentPage)) {
					throw new IniException(key.trim(),
							IniPropertyName.TYPE_CURRENT_PAGE,
							IniErrorMsg.NUMBER_MESSAGE);
				}
				// fileSuffix
				String fileSuffix = section.get(IniPropertyName.FILE_SUFFIX);
				// responseEncoding
				String responseEncoding = section
						.get(IniPropertyName.RESPONSE_ENCODING);
				// SiteInfo构造函数
				SiteInfo siteInfo = new SiteInfo(key.trim(), siteName.trim(),
						href.trim(), "true".equals(isGeKai.trim()) ? true
								: false, geKaiFu.trim(), prefix == null ? ""
								: prefix.trim(), suffix == null ? ""
								: suffix.trim(),
						typeIdentify.trim().split(","), typeName.trim().split(
								","), typeId.split(","), typeCurrentPage.trim()
								.split(","), fileSuffix == null
								|| "".equals(fileSuffix) ? "txt"
								: fileSuffix.trim(), responseEncoding == null
								|| "".equals(responseEncoding) ? "UTF-8"
								: responseEncoding.trim());
				sites.add(siteInfo);
			}
		}
	}

	// 返回采集站点的信息
	public static List<SiteInfo> getSites() throws InvalidFileFormatException,
			FileNotFoundException, IOException, IniException {
		if (sites == null) {
			configure();
		}
		return sites;
	}

	// 返回采集站点名称的数组
	public static String[] getSiteNames() throws InvalidFileFormatException,
			FileNotFoundException, IOException, IniException {
		if (siteNames == null) {
			configure();
		}
		return siteNames;
	}

	// 保存文件
	public static void store(SiteInfo siteInfo) throws IniException {
		if (siteInfo.getIntoDao()) {
			FileWriter fileWriter = null;
			//再次加载ini文件，保持数据同步（缺点：重复IO,耗时;优点?：可以动态更新主窗口里的siteInfo集合）
			try {
				Configuration.configure();
			} catch (Exception e1) {
				throw new IniException(e1.getMessage());
			}
			Section section = ini.get(siteInfo.getSectionName());
			String[] typeCurrentPage = siteInfo.getTypeCurrentPage();
			String page = typeCurrentPage[siteInfo.getTypeIndex()];
			// 源文件的当前页数
			int currentPage = Integer.parseInt(page);
			// 如果源文件的当前页数不大于此次采集的结束页数，则重新储存
			if (currentPage <= siteInfo.getEndPage()) {
				typeCurrentPage[siteInfo.getTypeIndex()] = (siteInfo
						.getEndPage() + 1) + "";
				section.put(IniPropertyName.TYPE_CURRENT_PAGE,
						arrayToString(typeCurrentPage));
				try {
					fileWriter = new FileWriter(iniPath);
					ini.store(fileWriter);
					//开发阶段，二次存储
					try {
						iniPath = "c://" + INI_FILE_NAME;
						fileWriter = new FileWriter(iniPath);
						ini.store(fileWriter);
					} catch (IOException e) {
						throw new IniException("(二次保存)保存" + iniPath + "出错");
					}
				} catch (IOException e) {
					throw new IniException("保存" + iniPath + "出错");
				} finally {
					try {
						fileWriter.close();
					} catch (IOException e) {
						throw new IniException("保存" + iniPath + "时输出流关闭出错");
					}
				}
			}
		}
	}
	
	// 将字符串数组按照(,)逗号分隔重组为字符串
	private static String arrayToString(String[] arr) {
		StringBuffer sb = new StringBuffer();
		if (arr != null) {
			for (String string : arr) {
				sb.append("," + string);
			}
		}
		return sb.substring(1).toString();
	}
}