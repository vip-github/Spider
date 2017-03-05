package com.spider.image;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.spider.utils.MongodbUtils;

public class ImageSpiderWorker implements Runnable
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Document document;
	private MongodbUtils mongo = MongodbUtils.getInstance();
	public static LinkedHashSet<String> current = new LinkedHashSet<>();

	public ImageSpiderWorker(Document document)
	{
		this.document = document;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		if (document.containsKey("images"))
		{
			String id = document.getString("_id");
			if (!current.contains(id))
			{
				try
				{
					current.add(id);
					List<String> images = (List<String>) document.get("images");
					String domain = document.getString("domain");
					String type = document.getString("type");
					String title = document.getString("title");
					String folder = null;
					if (!Strings.isNullOrEmpty(domain))
					{
						folder = domain;
					}
					if (Strings.isNullOrEmpty(type))
					{
						type = "未知分类";
					}
					folder += "/" + type.trim();
					if (Strings.isNullOrEmpty(title))
					{
						title = "未知标题";
					}
					title = title.replaceAll(",|，| | |\"|“|!|！|\\?|？|\\+", "").trim();
					folder += "/" + title;
					if (null != images && images.size() > 0)
					{
						logger.info(String.format("%s【%s】下载图片开始！", Thread.currentThread().getName(), id));
						ImageSpider.downloadImage(id, images, folder, domain);
						logger.info(String.format("%s【%s】下载图片完成！", Thread.currentThread().getName(), id));
						mongo.updateStatus(id, 1);
						logger.info(String.format("【%s】状态更新成功！", id));
					}
					current.remove(id);
				} catch (Exception e)
				{
					e.printStackTrace();
					logger.info(e.getMessage());
				} finally
				{
					try
					{
						File file = new File(ImageSpiderWorker.class.getResource("/").getPath().replace("/target/classes", ""), String.format("/data/%s", id));
						FileUtils.deleteDirectory(file);
					} catch (Exception e)
					{
						e.printStackTrace();
						logger.error(e.getMessage());
					}
				}
			}
		} else
		{
			String id = document.getString("_id");
			mongo.updateStatus(id, 1);
			logger.info(String.format("【%s】未找到images字段，状态更新成功！", id));
		}
	}
}
