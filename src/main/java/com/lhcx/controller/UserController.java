package com.lhcx.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户登录与注册
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/user")
public class UserController {
	private static Logger log = Logger.getLogger(UserController.class);
}
