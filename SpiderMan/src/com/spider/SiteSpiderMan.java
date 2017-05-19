package com.spider;

import com.spider.core.JsonJobSpiderMan;
import com.spider.core.SpiderMan;

public class SiteSpiderMan
{
	public static void main(String[] args)
	{
		String file = "D:/work/git/branches/Spider/SpiderMan/resource/example.txt";
		SpiderMan spiderMan = new JsonJobSpiderMan(file);
		spiderMan.run();
	}
}
