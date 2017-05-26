package zzyq.bean;

import java.sql.Timestamp;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

/**
 * 微博
 * @author czq
 * @date   2016年12月22日 下午5:40:24
 */
@Entity(value="weibo", noClassnameStored=true)
public class Weibo extends CommonEntity
{
	/**
	 * 时间戳
	 */
	@Indexed(options=@IndexOptions(background=true, name="index_timestamp"))
	private long timestamp;
	// 博主ID
	private String blogerId;
	// 博主主页
	private String blogerUrl;
	// 博主注册地址
	private String blogerAddr;
	// 原贴转发数
	private long origForwardNum;
	// 原帖回复数
	private long origReplyCount;
	// 原帖点赞数
	private long origLikeNum;
	// 转发贴转发数
	private long repForwardNum;
	// 转发贴回复数
	private long repRelyCount;
	// 转发贴点赞数
	private long repLikeNum;
	// 转帖原帖
	private String isForward;
	// 微博发表时间
	private Timestamp publishTime;
	// 转载时间
	private Timestamp forwardTime;
	// 博主描述
	private String blogerDec;
	// 博主昵称
	private String blogerName;
	//转发内容
	private String fcontent;
	//转发博主名称
	private String forgiBologerName;
	//微博url
	private String microUrl;
	//原始博主名称
	private String origblogerName;
	//微博内容
	private String microContent;
	
	public String getBlogerId()
	{
		return blogerId;
	}

	public void setBlogerId(String blogerId)
	{
		this.blogerId = blogerId;
	}

	public String getBlogerUrl()
	{
		return blogerUrl;
	}

	public void setBlogerUrl(String blogerUrl)
	{
		this.blogerUrl = blogerUrl;
	}

	public String getBlogerAddr()
	{
		return blogerAddr;
	}

	public void setBlogerAddr(String blogerAddr)
	{
		this.blogerAddr = blogerAddr;
	}

	public long getOrigForwardNum()
	{
		return origForwardNum;
	}

	public void setOrigForwardNum(long origForwardNum)
	{
		this.origForwardNum = origForwardNum;
	}

	public long getOrigReplyCount()
	{
		return origReplyCount;
	}

	public void setOrigReplyCount(long origReplyCount)
	{
		this.origReplyCount = origReplyCount;
	}

	public long getOrigLikeNum()
	{
		return origLikeNum;
	}

	public void setOrigLikeNum(long origLikeNum)
	{
		this.origLikeNum = origLikeNum;
	}

	public long getRepForwardNum()
	{
		return repForwardNum;
	}

	public void setRepForwardNum(long repForwardNum)
	{
		this.repForwardNum = repForwardNum;
	}

	public long getRepRelyCount()
	{
		return repRelyCount;
	}

	public void setRepRelyCount(long repRelyCount)
	{
		this.repRelyCount = repRelyCount;
	}

	public long getRepLikeNum()
	{
		return repLikeNum;
	}

	public void setRepLikeNum(long repLikeNum)
	{
		this.repLikeNum = repLikeNum;
	}

	public String getIsForward()
	{
		return isForward;
	}

	public void setIsForward(String isForward)
	{
		this.isForward = isForward;
	}

	public Timestamp getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime)
	{
		this.publishTime = publishTime;
	}

	public Timestamp getForwardTime()
	{
		return forwardTime;
	}

	public void setForwardTime(Timestamp forwardTime)
	{
		this.forwardTime = forwardTime;
	}

	public String getWebsiteName()
	{
		return websiteName;
	}

	public void setWebsiteName(String websiteName)
	{
		this.websiteName = websiteName;
	}

	public String getBlogerDec()
	{
		return blogerDec;
	}

	public void setBlogerDec(String blogerDec)
	{
		this.blogerDec = blogerDec;
	}

	public String getBlogerName()
	{
		return blogerName;
	}

	public void setBlogerName(String blogerName)
	{
		this.blogerName = blogerName;
	}

	public String getFcontent()
	{
		return fcontent;
	}

	public void setFcontent(String fcontent)
	{
		this.fcontent = fcontent;
	}

	public String getForgiBologerName()
	{
		return forgiBologerName;
	}

	public void setForgiBologerName(String forgiBologerName)
	{
		this.forgiBologerName = forgiBologerName;
	}

	public String getMicroUrl()
	{
		return microUrl;
	}

	public void setMicroUrl(String microUrl)
	{
		this.microUrl = microUrl;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getOrigblogerName()
	{
		return origblogerName;
	}

	public void setOrigblogerName(String origblogerName)
	{
		this.origblogerName = origblogerName;
	}

	public String getMicroContent()
	{
		return microContent;
	}

	public void setMicroContent(String microContent)
	{
		this.microContent = microContent;
	}

	@Override
	public String toString() {
		return "Weibo [timestamp=" + timestamp + ", blogerId=" + blogerId + ", blogerUrl=" + blogerUrl + ", blogerAddr="
				+ blogerAddr + ", origForwardNum=" + origForwardNum + ", origReplyCount=" + origReplyCount
				+ ", origLikeNum=" + origLikeNum + ", repForwardNum=" + repForwardNum + ", repRelyCount=" + repRelyCount
				+ ", repLikeNum=" + repLikeNum + ", isForward=" + isForward + ", publishTime=" + publishTime
				+ ", forwardTime=" + forwardTime + ", blogerDec=" + blogerDec + ", blogerName=" + blogerName
				+ ", fcontent=" + fcontent + ", forgiBologerName=" + forgiBologerName + ", microUrl=" + microUrl
				+ ", origblogerName=" + origblogerName + ", microContent=" + microContent + "]";
	}
}
