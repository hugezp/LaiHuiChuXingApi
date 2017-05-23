package com.lhcx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lhcx.model.User;
import com.lhcx.service.IUserService;
import com.lhcx.utils.Utils;

/**
 * Created by Administrator on 2017/4/11.
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private IUserService userService;
	@Autowired  
    private HttpSession session;  

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {
        // System.out.println("afterCompletion---");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
        // System.out.println("postHandle---");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
    	
        String token = request.getHeader("Token");
        if(Utils.isNullOrEmpty(token)){
        	response.setContentType("application/json");
        	response.sendRedirect("/api/user/noLogin");
        	return false;
        }
        try {
        	User user = userService.selectByToken(token);
        	if(user != null && user.getFlag() != 1){
        		session.setAttribute("CURRENT_USER", user);
        		return true;
        	}
        	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 非法请求 转到登录失败接口
		response.sendRedirect("/api/user/noLogin");
		return false;
	}
}
