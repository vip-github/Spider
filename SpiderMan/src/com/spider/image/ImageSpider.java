package com.spider.image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.utils.MongodbUtils;
import com.common.utils.StringUtils;
import com.google.common.base.Strings;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.Config;

public class ImageSpider extends BreadthCrawler
{
	private static Logger logger = LoggerFactory.getLogger(ImageSpider.class);
	private static final String savePath = "F:/images";
	private String folder;
	private static MongodbUtils mongo = MongodbUtils.getInstance();
	private String domain;

	public static void run(String domain, int limit) throws Exception
	{
		try
		{
			long start = System.currentTimeMillis();
			logger.info(String.format("图片采集程序开始工作！domain=%s", domain));
			ExecutorService executor = Executors.newFixedThreadPool(limit);
			while (true)
			{
				Thread.sleep(3000);
				int active = ImageSpiderWorker.current.size();
				if (active < limit)
				{
					List<Document> list = mongo.queryImages(domain, limit);
					if (null == list || list.size() == 0)
					{
						break;
					} else
					{
						for (Document document : list)
						{
							executor.execute(new ImageSpiderWorker(document));
						}
						long count = mongo.countImages(domain);
						if (count > 0)
						{
							logger.info(String.format("%s状态为0的余下数量为：%s", domain, count));
						}
					}
				}
			}
			executor.shutdown();
			long end = System.currentTimeMillis();
			logger.info(String.format("%s图片采集程序已停止！耗时：%s秒", domain, (end - start) / 1000));
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public static void downloadImage(String id, List<String> srcs, String folder, String domain) throws Exception
	{
		Config.MAX_RECEIVE_SIZE = 999999999;
		Config.TIMEOUT_CONNECT = 1000 * 60 * 3;
		Config.TIMEOUT_READ = 1000 * 60 * 3;
		ImageSpider spider = new ImageSpider(String.format("./data/%s", id), folder, domain);
		Links links = new Links(srcs);
		spider.addSeed(links);
		spider.setExecuteInterval(100);
		spider.setMaxExecuteCount(5);
		spider.setThreads(50);
		spider.setResumable(true);
		spider.start(1);
	}

	public ImageSpider(String crawlPath, String folder, String domain)
	{
		super(crawlPath, true);
		this.folder = folder;
		this.domain = domain;
		File file = new File(savePath);
		if (!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	public void visit(Page page, CrawlDatums crawlDatums)
	{
		FileOutputStream fos = null;
		try
		{
			String src = page.getUrl();
			String contentType = page.getResponse().getContentType();
			if (!Strings.isNullOrEmpty(contentType) && (contentType.contains("image") || contentType.contains("IMAGE")))
			{
				byte[] imageByte = page.getContent();
				if (!mongo.existsImageBinary(imageByte))
				{
					File filePath = buildFilePath(savePath, folder, imageByte);
					if (!filePath.exists())
					{
						fos = new FileOutputStream(filePath);
						fos.write(imageByte);
						Thread.sleep(50);
						logger.info(String.format("%s 图片保存成功！", filePath.getPath()));
					} else
					{
						logger.info(String.format("%s 已存在！此次将不会写入硬盘！", filePath.getPath()));
					}
					mongo.saveImageBinary(this.domain, imageByte, src);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally
		{
			if (null != fos)
			{
				try
				{
					fos.flush();
					fos.close();
				} catch (Exception e)
				{
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
	}

	private String buildFileName(byte[] imageByte)
	{
		return StringUtils.md5(new String(imageByte));
	}

	private File buildFilePath(String savePath, String folder, byte[] imageByte)
	{
		StringBuilder path = new StringBuilder();
		path.append(savePath);
		path.append("/");
		if (!Strings.isNullOrEmpty(folder))
		{
			path.append(folder + "/");
		}
		path.append(buildFileName(imageByte) + ".png");
		File file = new File(path.toString());
		if (!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}
		return file;
	}
}
