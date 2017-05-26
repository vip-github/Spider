package zzyq.bean;

import java.sql.Timestamp;

import org.mongodb.morphia.annotations.Id;

/**
 * 公用实体
 * @author czq
 * @date   2016年12月22日 上午9:21:36
 */
public class CommonEntity
{
	/**
	 * id
	 */
	@Id
	protected String id;
	
	/**
	 * 创建时间/采集时间
	 */
	protected Timestamp createTime;
	
	/**
	 * 标题
	 */
	protected String title;
	
	/**
	 * url链接
	 */
	protected String url;
	
	/**
	 * 种子类型
	 **/
	protected String seedType;
	
	/**
	 * 域名相关
	 */
	protected String domainId;
	protected String domain;
	protected String domainName;
	protected String domainSign;
	
	/**
	 * 站点频道相关
	 */
	protected String websiteName;
	protected String websiteId;
	protected String websiteSign;
	protected String websiteUrl;
	
	/**
	 * 数据来源
	 * input 手动新增
	 */
	protected String dataSource;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getSeedType()
	{
		return seedType;
	}
	public void setSeedType(String seedType)
	{
		this.seedType = seedType;
	}
	public String getDomainId()
	{
		return domainId;
	}
	public void setDomainId(String domainId)
	{
		this.domainId = domainId;
	}
	public String getDomain()
	{
		return domain;
	}
	public void setDomain(String domain)
	{
		this.domain = domain;
	}
	public String getDomainName()
	{
		return domainName;
	}
	public void setDomainName(String domainName)
	{
		this.domainName = domainName;
	}
	public String getDomainSign()
	{
		return domainSign;
	}
	public void setDomainSign(String domainSign)
	{
		this.domainSign = domainSign;
	}
	public String getWebsiteName()
	{
		return websiteName;
	}
	public void setWebsiteName(String websiteName)
	{
		this.websiteName = websiteName;
	}
	public String getWebsiteId()
	{
		return websiteId;
	}
	public void setWebsiteId(String websiteId)
	{
		this.websiteId = websiteId;
	}
	public String getWebsiteSign()
	{
		return websiteSign;
	}
	public void setWebsiteSign(String websiteSign)
	{
		this.websiteSign = websiteSign;
	}
	public String getWebsiteUrl()
	{
		return websiteUrl;
	}
	public void setWebsiteUrl(String websiteUrl)
	{
		this.websiteUrl = websiteUrl;
	}
	public Timestamp getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
}
