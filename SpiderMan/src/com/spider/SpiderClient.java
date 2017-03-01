package com.spider;

import com.spider.core.JsonJobSpiderMan;
import com.spider.core.SpiderMan;

public class SpiderClient {

	public static void main(String[] args) {
		String file = "E:/git/Spider/SpiderMan/resource/dd24.txt";
		SpiderMan spiderMan = new JsonJobSpiderMan(file);
		spiderMan.run();
	}

}
