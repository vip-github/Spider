package com.spider;

import com.spider.image.ImageSpider;

public class ImageSpiderMan
{
	public static void main(String[] args) throws Exception
	{
		String domain = "www.dd224.com";
		ImageSpider.run(domain, 20);
	}
}
