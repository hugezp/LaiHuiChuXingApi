package com.lhcx.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;

/**
 * 司机
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/order")
public class DriverController {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IDriverLocationService driverLocationService;
	@Autowired  
    private HttpSession session;  

	

}
