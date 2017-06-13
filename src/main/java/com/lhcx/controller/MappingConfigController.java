package com.lhcx.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 静态页面映射类
 * 
 * @author pangzhenpeng
 *
 */
@RequestMapping(value = "/api/lhcx")
@Controller
public class MappingConfigController {

	// 关于出行
	@RequestMapping(value = "/lhcx_about")
	public String lhcx_about(HttpServletRequest request) {

		return "lhcx_about";

	}

	//用户指南
	@RequestMapping(value = "/lhcx_guide")
	public String lhcx_guide(HttpServletRequest request) {

		return "lhcx_guide";

	}

}
