package com.spider;

import com.spider.core.JsonJobSpiderMan;
import com.spider.core.SpiderMan;

public class SiteSpiderMan
{
	public static void main(String[] args)
	{
		String file = "E:/git/Spider/SpiderMan/resource/susu78-girl.txt";
		SpiderMan spiderMan = new JsonJobSpiderMan(file);
		spiderMan.run();
	}
}
