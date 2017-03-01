package com.spider.core;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.spider.bean.Header;
import com.spider.bean.Job;
import com.spider.bean.Link;
import com.spider.bean.Page;

public class ParseJob {

	public static void main(String[] args) throws Exception {
		File file = new File("D:/work/workspace/SpiderMan/resource/chinawealth.txt");
		Job job = new ParseJob().parseJsonJob(FileUtils.readFileToString(file));
		System.out.println(job);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Job parseJsonJob(String json) throws Exception {
		Job job = null;
		if (StringUtils.isNotBlank(json)) {
			//Feature.OrderedField解决Map中排序的问题
			LinkedHashMap<String, Object> jsonMap = JSON.parseObject(json, LinkedHashMap.class, Feature.OrderedField);
			if (null != jsonMap && !jsonMap.isEmpty()) {
				if (jsonMap.containsKey("link")) {
					job = new Job();
					for (Entry<String, Object> entry : jsonMap.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						if (null != value) {
							if (key.equals("link")) {
								if (value instanceof Map) {
									Map linkMap = (Map)value;
									String url = (String)linkMap.get("url");
									if(StringUtils.isNotBlank(url)){
										Link link = new Link();
										String max = (String)linkMap.get("max");
										if(url.contains("{page}")){
											if(StringUtils.isBlank(max)){
												max = "100";
											}
											int maxInt = Integer.parseInt(max);
											String[] urls = new String[maxInt+1];
											for(int i=0; i<=maxInt; i++){
												String tempUrl = url.replace("{page}", String.valueOf(i));
												urls[i] = tempUrl;
											}
											link.setUrl(urls);
											link.setMax(max);
										}else{
											link.setUrl(new String[]{StringUtils.strip(url)});
										}
										job.setLink(link);
									}else{
										throw new Exception("url不能为空！");
									}
								} else {
									throw new Exception("link必须是Map类型！");
								}
							} else if (key.equals("header")) {
								if (value instanceof Map) {
									Header header = new Header();
									BeanUtils.populate(header, (Map) value);
									job.setHeader(header);
								} else {
									throw new Exception("header必须是Map类型！");
								}
							} else if (key.equals("page")) {
								if (value instanceof List) {
									List<Page> pageList = new ArrayList<>();
									for (Map<String, Object> page : (List<Map<String, Object>>) value) {
										Page PAGE = new Page();
										for (Entry<String, Object> pageEntry : page.entrySet()) {
											String pKey = pageEntry.getKey();
											Object pValue = pageEntry.getValue();
											if (pKey.equalsIgnoreCase("priority")) {
												PAGE.setPriority(Integer.parseInt((String) pValue));
											} else if (pKey.equalsIgnoreCase("type")) {
												PAGE.setType((String) pValue);
											} else if (pKey.equalsIgnoreCase("selector")) {
												if (pValue instanceof Map) {
													PAGE.setSelector((Map) pValue);
												}
											} else if (pKey.equalsIgnoreCase("field")) {
												if (pValue instanceof List) {
													PAGE.setField((List) pValue);
												}
											} else if (pKey.equalsIgnoreCase("valueMulti")) {
												PAGE.setValueMulti(Boolean.parseBoolean((String) pValue));
											} else if (pKey.equalsIgnoreCase("formatter")) {
												if (pValue instanceof Map) {
													PAGE.setFormatter((Map) pValue);
												}
											}
										}
										pageList.add(PAGE);
									}
									job.setPage(pageList);
								} else {
									throw new Exception("page必须是List类型！");
								}
							}
						}
					}
				}
			}
		}
		return job;
	}
}
