package com.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转控制器
 * @author czq
 * @date   2017年2月17日 下午5:39:48
 */
@Controller
public class TargetController {
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
}
