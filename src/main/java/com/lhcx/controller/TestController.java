package com.lhcx.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhcx.service.IVerificationCodeService;


@Controller
@ResponseBody
@RequestMapping(value = "/api")
public class TestController {
    private static Logger log = Logger.getLogger(TestController.class);
    @Autowired
    private IVerificationCodeService verificationCodeService;

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void test(HttpServletRequest request) {
    	try {
    		verificationCodeService.test();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}

    }
}