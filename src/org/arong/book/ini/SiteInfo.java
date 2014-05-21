package org.arong.book.ini;


/**
 * 用于存储每个采集站点的信息
 * 
 * @author 阿荣
 * @since 2013-8-18
 * 
 */
public class SiteInfo {
	// section的名称，形如[aiqi]
	private String sectionName;
	// 采集站点名称
	private String siteName;
	// 采集站点的分类url主域名
	private String href;
	// 采集站点的分类typeIdentify之前的前缀,形如/wuxia/[list]1
	private String prefix;
	// 采集站点的分类typeIdentify之后的后缀，形如list1[.htm],wuxia/2[.html],type1/list1[.html]
	private String suffix;
	// 采集站点的分类url是否被/隔开类型标识符和分页数字
	private Boolean isGeKai;
	// 采集站点的分类url被什么字符隔开类型标识符和分页数字
	private String geKaiFu;
	// 全部分类标识符的数组，形如yanqing,wuxia
	private String[] typeIdentify;
	// 全部分类在下拉框所对应的名称，要与typeIdentify一一对应
	private String[] typeName;
	// 全部分类在书籍电子书中所对应的分类id,要与typeIdentify一一对应
	private String[] typeId;
	// 全部分类所采集到的页数，要与typeIdentify一一对应
	private String[] typeCurrentPage;
	// 采集站点的下载文件后缀
	private String fileSuffix = "txt";
	// 采集站点的页面响应编码
	private String responseEncoding = "UTF-8";

	/* 以下为程序运行时需要动态改变的属性 */
	// 下拉框选择的分类索引
	private Integer typeIndex = 0;

	private Integer startPage = 1;

	private Integer endPage = 1;

	private Boolean intoDao = false;

	public SiteInfo() {
	}

	public SiteInfo(String sectionName, String siteName, String href,
			Boolean isGeKai, String geKaiFu, String prefix, String suffix,
			String[] typeIdentify, String[] typeName, String[] typeId,
			String[] typeCurrentPage, String fileSuffix, String responseEncoding) {
		super();
		this.sectionName = sectionName;
		this.siteName = siteName;
		this.href = href;
		this.isGeKai = isGeKai;
		this.geKaiFu = geKaiFu;
		this.typeIdentify = typeIdentify;
		this.typeName = typeName;
		this.typeId = typeId;
		this.typeCurrentPage = typeCurrentPage;
		this.prefix = prefix;
		this.suffix = suffix;
		this.fileSuffix = fileSuffix;
		this.responseEncoding = responseEncoding;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Boolean getIsGeKai() {
		return isGeKai;
	}

	public void setIsGeKai(Boolean isGeKai) {
		this.isGeKai = isGeKai;
	}

	public String[] getTypeName() {
		return typeName;
	}

	public void setTypeName(String[] typeName) {
		this.typeName = typeName;
	}

	public String[] getTypeId() {
		return typeId;
	}

	public void setTypeId(String[] typeId) {
		this.typeId = typeId;
	}

	public String[] getTypeIdentify() {
		return typeIdentify;
	}

	public void setTypeIdentify(String[] typeIdentify) {
		this.typeIdentify = typeIdentify;
	}

	public String[] getTypeCurrentPage() {
		return typeCurrentPage;
	}

	public void setTypeCurrentPage(String[] typeCurrentPage) {
		this.typeCurrentPage = typeCurrentPage;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getResponseEncoding() {
		return responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}

	public Integer getTypeIndex() {
		return typeIndex;
	}

	public void setTypeIndex(Integer typeIndex) {
		this.typeIndex = typeIndex;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getEndPage() {
		return endPage;
	}

	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}

	public Boolean getIntoDao() {
		return intoDao;
	}

	public void setIntoDao(Boolean intoDao) {
		this.intoDao = intoDao;
	}

	public String getGeKaiFu() {
		return geKaiFu;
	}

	public void setGeKaiFu(String geKaiFu) {
		this.geKaiFu = geKaiFu;
	}

	// 为动态变化的属性赋值
	public void setChangable(Integer typeIndex, Integer startPage,
			Integer endPage, Boolean intoDao) {
		this.typeIndex = typeIndex;
		this.startPage = startPage;
		this.endPage = endPage;
		this.intoDao = intoDao;
	}
}
