package com.spider.media;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.spider.utils.MongodbUtils;
import com.spider.utils.StringUtils;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class ImageSpider extends BreadthCrawler {
	private static Logger logger = LoggerFactory.getLogger(ImageSpider.class);
	private static final String savePath = "F:/images";
	private String folder;
	private static MongodbUtils mongo = MongodbUtils.getInstance();

	public static void main(String[] args) throws Exception {
		String src1 = "http://icon.nipic.com/BannerPic/20161102/home/20161102123234_1.jpg";
		String src2 = "http://120.img.pp.sohu.com/images/blog/2008/3/24/23/10/1197fc52a47.jpg";
		String src3 = "http://119.img.pp.sohu.com/images/blog/2008/3/24/23/27/1197fc3d917.jpg";
		ImageSpider.downloadImage(Lists.newArrayList(src1, src2, src3), "www.baidu.com");
	}
	
	@SuppressWarnings("unchecked")
	public static void run(){
		logger.info("图片采集程序开始工作！");
		List<Document> imagesList = mongo.queryImages();
		AtomicInteger counter = new AtomicInteger(0);
		int size = imagesList.size();
		for (Document document : imagesList) {
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
					type = "其他";
				}
				folder+="/"+type.trim();
				if(Strings.isNullOrEmpty(title)){
					title = "未知标题";
				}
				title = title.replaceAll(",|，| | |\"|“|!|！", "").trim();
				folder+="/"+title;
				if(null!=images && images.size()>0){
					logger.info(String.format("【%s】下载图片开始！", id));
					ImageSpider.downloadImage(images, folder);
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
				int count = counter.incrementAndGet();
				logger.info(String.format("图片Document当前已采集完【%s】个，剩余【%s】", count, size-count));
			}
		}
		logger.info("图片采集程序已停止！");
	}
	
	public synchronized static void downloadImage(String src, String folder) throws Exception {
		ImageSpider spider = new ImageSpider("./data", folder);
		spider.addSeed(src);
		spider.start(1);
		spider.setExecuteInterval(500);
		spider.setMaxExecuteCount(5);
	}
	
	public synchronized static void downloadImage(List<String> srcs, String folder) throws Exception {
		ImageSpider spider = new ImageSpider("./data", folder);
		Links links = new Links(srcs);
		spider.addSeed(links);
		spider.start(1);
	}

	public ImageSpider(String crawlPath, String folder) {
		super(crawlPath, true);
		this.folder = folder;
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
			String contentType = page.getResponse().getContentType();
			if (!Strings.isNullOrEmpty(contentType) && (contentType.contains("image") || contentType.contains("IMAGE"))) {
				byte[] imageByte = page.getContent();
				File filePath = buildFilePath(savePath, folder, imageByte);
				if(!filePath.exists()){
					fos = new FileOutputStream(filePath);
					fos.write(imageByte);
					logger.info(String.format("%s 图片保存成功！", filePath.getPath()));
				}else{
					logger.info(String.format("%s 已存在！此次将不会写入硬盘！", filePath.getPath()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally{
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
