package zzyq.bean;

import java.sql.Timestamp;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

/**
 * 论坛
 * @author czq
 * @date   2016年12月21日 下午5:50:39
 */
@Entity(value="bbs", noClassnameStored=true)
public class Bbs extends CommonEntity
{
	/**
	 * 时间戳
	 */
	@Indexed(options=@IndexOptions(background=true, name="index_timestamp"))
	private long timestamp;
	
	/**
	 * 发布时间
	 */
	private Timestamp publishTime;
	
	/**
	 * 来源
	 **/
	private String source;

	/**
	 * 作者
	 **/
	private String author;

	/**
	 * 回复数
	 **/
	private long replyCount;

	/**
	 * 浏览数
	 **/
	private long viewCount;
	
	/**
	 * 正文
	 **/
	private String content;
	/**
	 * 关键词
	 **/
	private String keyWords;

	/**
	 * 是否原创
	 */
	private String isOriginal;
	
	/**
	 * 点赞数
	 */
	private long likeCount;
	

	public Timestamp getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime)
	{
		this.publishTime = publishTime;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public long getReplyCount()
	{
		return replyCount;
	}

	public void setReplyCount(long replyCount)
	{
		this.replyCount = replyCount;
	}

	public long getViewCount()
	{
		return viewCount;
	}

	public void setViewCount(long viewCount)
	{
		this.viewCount = viewCount;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getKeyWords()
	{
		return keyWords;
	}

	public void setKeyWords(String keyWords)
	{
		this.keyWords = keyWords;
	}

	public String getIsOriginal()
	{
		return isOriginal;
	}

	public void setIsOriginal(String isOriginal)
	{
		this.isOriginal = isOriginal;
	}

	public long getLikeCount()
	{
		return likeCount;
	}

	public void setLikeCount(long likeCount)
	{
		this.likeCount = likeCount;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
}
