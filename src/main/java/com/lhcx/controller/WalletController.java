package com.lhcx.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.PayCashLog;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IPayCashLogService;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

@Controller
public class WalletController {
	private static Logger log = Logger.getLogger(DriverController.class);

	@Autowired
	private IPayCashLogService payCashLogService;
	@Autowired
	private HttpSession session;

	@ResponseBody
	@RequestMapping(value = "/wallet", method = RequestMethod.POST)
	public ResponseEntity<String> passengerWallet(HttpServletRequest request,
			@RequestBody JSONObject jsonRequest) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		List<PayCashLog> list = new ArrayList<PayCashLog>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!VerificationUtils.walletValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		} else {
			try {
				String startTime = jsonRequest.getString("time")+"-00 00:00:00";
				String endTime = jsonRequest.getString("time")+"-32 00:00:00";
				int page = Integer.parseInt(jsonRequest
						.getString("page"));
				int size = Integer.parseInt(jsonRequest
						.getString("size"));
				User user = (User) session.getAttribute("CURRENT_USER");
				String identityToken = user.getIdentityToken();
				try {
					list = payCashLogService.selectByIdentityTokenAndTime(identityToken,
							page, size,startTime,endTime);
					if (list.size()==0) {
						resultBean = new ResultBean<Object>(
								ResponseCode.NO_DATA.value(), "暂无数据！", map);
					} else {
						map.put("data", list);
						map.put("overage", user.getWallet());
						resultBean = new ResultBean<Object>(
								ResponseCode.SUCCESS.value(), "数据获取成功！", map);
					}

				} catch (Exception e) {
					e.getMessage();
					resultBean = new ResultBean<Object>(
							ResponseCode.SYSTEM_SELECT_FAILED.value(),
							"数据查询失败！", map);
				}

			} catch (Exception e) {
				log.error(e.getMessage());
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());

			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);

	}
	
	/**
	 * 提现记录
	 * @param request
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/withdrawals/record", method = RequestMethod.POST)
	public ResponseEntity<String> ithdrawalsRecord(HttpServletRequest request,
			@RequestBody JSONObject jsonRequest) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		List<PayCashLog> list = new ArrayList<PayCashLog>();
		if (!VerificationUtils.ithdrawalsValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		} else {
			try {
				int page = Integer.parseInt(jsonRequest
						.getString("page"));
				int size = Integer.parseInt(jsonRequest
						.getString("size"));
				int actiontype = Integer.parseInt(jsonRequest
						.getString("actiontype"));
				User user = (User) session.getAttribute("CURRENT_USER");
				String identityToken = user.getIdentityToken();
				try {
					list = payCashLogService.selectByIdentityTokenAndActiontype(identityToken,
							page, size,actiontype);
					if (list.size() == 0) {
						resultBean = new ResultBean<Object>(
								ResponseCode.NO_DATA.value(), "暂无数据！", list);
					} else {
						resultBean = new ResultBean<Object>(
								ResponseCode.SUCCESS.value(), "数据获取成功！", list);
					}

				} catch (Exception e) {
					e.getMessage();
					resultBean = new ResultBean<Object>(
							ResponseCode.SYSTEM_SELECT_FAILED.value(),
							"数据查询失败！", list);
				}

			} catch (Exception e) {
				log.error(e.getMessage());
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());

			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);

	}
}
