package com.spider.media;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.spider.utils.MongodbUtils;
import com.spider.utils.StringUtils;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.Config;

public class ImageSpider extends BreadthCrawler{
	private static Logger logger = LoggerFactory.getLogger(ImageSpider.class);
	private static final String savePath = "F:/images";
	private String folder;
	private static MongodbUtils mongo = MongodbUtils.getInstance();
	private String domain;

	public static void run(String domain) throws Exception{
		ImageSpider spider = new ImageSpider("./data", savePath, domain);
		long start = System.currentTimeMillis();
		logger.info(!Strings.isNullOrEmpty(domain)?domain:""+"图片采集程序开始工作！");
		List<Document> imagesList = mongo.queryImages(domain);
		ExecutorService service = Executors.newFixedThreadPool(1000);
		CountDownLatch countDownLatch = new CountDownLatch(imagesList.size());
		for (final Document document : imagesList) {
			if(document.containsKey("images")){
				service.execute(spider.new Worker(document, countDownLatch, imagesList.size()));
			}
		}
		countDownLatch.await();
		service.shutdown();
		long end = System.currentTimeMillis();
		logger.info(!Strings.isNullOrEmpty(domain)?domain:""+String.format("图片采集程序已停止！耗时：%s秒", (end-start)/1000));
	}
	
	public class Worker implements Runnable
	{
		private Document document;
		private CountDownLatch countDownLatch;
		private int size;
		
		public Worker(Document document, CountDownLatch countDownLatch, int size) {
			this.document = document;
			this.countDownLatch = countDownLatch;
			this.size = size;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			String id = null;
			try {
				id = document.getString("_id");
				List<String> images = (List<String>)document.get("images");
				String domain = document.getString("domain");
				String type = document.getString("type");
				String title = document.getString("title");
				String folder = null;
				if(!Strings.isNullOrEmpty(domain)){
					folder = domain;
				}
				if(Strings.isNullOrEmpty(type)){
					type = "未知分类";
				}
				folder+="/"+type.trim();
				if(Strings.isNullOrEmpty(title)){
					title = "未知标题";
				}
				title = title.replaceAll(",|，| | |\"|“|!|！|?|？|+", "").trim();
				folder+="/"+title;
				if(null!=images && images.size()>0){
					logger.info(String.format("【%s】下载图片开始！", id));
					ImageSpider.downloadImage(images, folder, domain);
					logger.info(String.format("【%s】下载图片完成！", id));
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} finally{
				if(null!=id){
					mongo.updateStatus(id, 1);
					logger.info(String.format("【%s】状态更新成功！", id));
				}
				countDownLatch.countDown();
				long count = countDownLatch.getCount();
				logger.info(String.format("%s-Document当前已下载【%s】个,剩余【%s】个", Thread.currentThread().getName(), size-count, count));
			}
		}
	}
	
	public synchronized static void downloadImage(String src, String folder, String domain) throws Exception {
		Config.TIMEOUT_CONNECT = 1000*60*3;
		Config.TIMEOUT_READ = 1000*60*3;
		ImageSpider spider = new ImageSpider("./data", folder, domain);
		spider.addSeed(src);
		spider.setExecuteInterval(100);
		spider.setMaxExecuteCount(5);
		spider.setThreads(50);
		spider.setResumable(true);
		spider.start(1);
	}
	
	public synchronized static void downloadImage(List<String> srcs, String folder, String domain) throws Exception {
		Config.TIMEOUT_CONNECT = 1000*60*3;
		Config.TIMEOUT_READ = 1000*60*3;
		ImageSpider spider = new ImageSpider("./data", folder, domain);
		Links links = new Links(srcs);
		spider.addSeed(links);
		spider.setExecuteInterval(100);
		spider.setMaxExecuteCount(5);
		spider.setThreads(50);
		spider.setResumable(true);
		spider.start(1);
	}
	
	public ImageSpider(String crawlPath, String folder, String domain) {
		super(crawlPath, true);
		this.folder = folder;
		this.domain = domain;
		File file = new File(savePath);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void visit(Page page, CrawlDatums crawlDatums) {
		FileOutputStream fos = null;
		try {
			String src = page.getUrl();
			String contentType = page.getResponse().getContentType();
			if (!Strings.isNullOrEmpty(contentType) && (contentType.contains("image") || contentType.contains("IMAGE"))) {
				byte[] imageByte = page.getContent();
				if(!mongo.existsImageBinary(imageByte)){
					File filePath = buildFilePath(savePath, folder, imageByte);
					if(!filePath.exists()){
						fos = new FileOutputStream(filePath);
				        fos.write(imageByte);
						logger.info(String.format("%s 图片保存成功！", filePath.getPath()));
						mongo.saveImageBinary(this.domain, imageByte, src);
					}else{
						logger.info(String.format("%s 已存在！此次将不会写入硬盘！", filePath.getPath()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			if(null!=fos){
				try {
					fos.flush();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
	}
	
	private String buildFileName(byte[] imageByte){
		return StringUtils.md5(new String(imageByte));
	}
	
	private File buildFilePath(String savePath, String folder, byte[] imageByte){
		StringBuilder path = new StringBuilder();
		path.append(savePath);
		path.append("/");
		if(!Strings.isNullOrEmpty(folder)){
			path.append(folder+"/");
		}
		path.append(buildFileName(imageByte) + ".png");
		File file = new File(path.toString());
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		return file;
	}
}
