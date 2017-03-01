package com.spider;

import com.spider.core.JsonJobSpiderMan;
import com.spider.core.SpiderMan;

public class SpiderClient {

	public static void main(String[] args) {
		//String file = "D:/work/workspace/SpiderMan/resource/example.txt";
		String file = "D:/work/workspace/SpiderMan/resource/chinawealth.txt";
		SpiderMan spiderMan = new JsonJobSpiderMan(file);
		spiderMan.run();
	}

}
