package com.spider.images;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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
	private static final String savePath = "D:/images";
	private String domain;
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
		for (Document document : imagesList) {
			String id = null;
			try {
				id = document.getString("_id");
				List<String> images = (List<String>)document.get("images");
				String domain = document.getString("domain");
				if(null!=images && images.size()>0){
					logger.info(String.format("【%s】下载图片开始！", id));
					ImageSpider.downloadImage(images, domain);
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
			}
		}
		logger.info("图片采集程序已停止！");
	}
	
	public synchronized static void downloadImage(String src, String domain) throws Exception {
		ImageSpider spider = new ImageSpider("./data", domain);
		spider.addSeed(src);
		spider.start(1);
	}
	
	public synchronized static void downloadImage(List<String> srcs, String domain) throws Exception {
		ImageSpider spider = new ImageSpider("./data", domain);
		Links links = new Links(srcs);
		spider.addSeed(links);
		spider.start(1);
	}

	public ImageSpider(String crawlPath, String domain) {
		super(crawlPath, true);
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
			String contentType = page.getResponse().getContentType();
			if (!Strings.isNullOrEmpty(contentType) && (contentType.contains("image") || contentType.contains("IMAGE"))) {
				byte[] imageByte = page.getContent();
				File filePath = buildFilePath(savePath, this.domain, imageByte);
				if(!filePath.exists()){
					fos = new FileOutputStream(filePath);
					fos.write(imageByte);
					logger.info(String.format("图片已保存=>%s", filePath.getPath()));
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
		return StringUtils.md5(new String(imageByte)).substring(8, 24);
	}
	
	private File buildFilePath(String savePath, String domain, byte[] imageByte){
		StringBuilder path = new StringBuilder();
		path.append(savePath);
		path.append("/");
		if(!Strings.isNullOrEmpty(domain)){
			path.append(domain+"/");
		}
		path.append(buildFileName(imageByte) + ".png");
		File file = new File(path.toString());
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		return file;
	}
}
