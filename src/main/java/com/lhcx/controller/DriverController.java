package com.lhcx.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lhcx.model.DriverInfo;
import com.lhcx.model.DriverInfoType;
import com.lhcx.model.DriverLocation;
import com.lhcx.model.Order;
import com.lhcx.model.OrderStatus;
import com.lhcx.model.PayCashLog;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.model.response.PaycashLogResponse;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPayCashLogService;
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.Utils;

/**
 * 
 * @author Jiawl
 *
 */
@Controller
@RequestMapping(value = "/driver")
public class DriverController {
	private static Logger log = Logger.getLogger(DriverController.class);
	@Autowired
	private IOrderService orderService;
	@Autowired
	private HttpSession session;
	@Autowired
	private IDriverInfoService driverInfoService;
	@Autowired
	private IPayCashLogService payCashLogService;
	@Autowired
	private IDriverLocationService driverLocationService;

	/**
	 * 获取乘客未完成的订单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/noFinished", method = RequestMethod.POST)
	public ResponseEntity<String> noFinished() {
		// 取得参数值
		String jsonpCallback = "";
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			Order newOrder = orderService.selectNewOrderByDriverPhone(user
					.getUserphone());
			if (newOrder != null) {
				result.put("OrderId", newOrder.getOrderid());
				result.put("Status", newOrder.getStatus());
			}

			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"获取成功！", result);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"获取失败！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 获取司机基本信息
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verification", method = RequestMethod.POST)
	public ResponseEntity<String> info() {
		// 取得参数值
		String jsonpCallback = "";
		ResultBean<?> resultBean = null;
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> info = new HashMap<String, Object>();
		List<Map<String, Object>> error = new ArrayList<Map<String, Object>>();
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			DriverInfo driverInfo = driverInfoService.selectByPhone(user
					.getUserphone());

			info.put("phone", user.getUserphone());
			info.put("photo", driverInfo.getPhoto());
			info.put("addressName", driverInfo.getAddressname());
			info.put("driverName", driverInfo.getDrivername());
			info.put("licenseId", driverInfo.getLicenseid());
			info.put("driverNation", driverInfo.getDrivernation());
			info.put("driverNationlity", driverInfo.getDrivernationality());
			info.put("address", driverInfo.getAddress());

			// 证件信息
			info.put("licensePhoto", driverInfo.getLicensephoto());
			info.put("getDriverLicenseDate",
					DateUtils.dateFormat2(driverInfo.getGetdriverlicensedate()));
			info.put("driverLicenseOn",
					DateUtils.dateFormat2(driverInfo.getDriverlicenseon()));
			info.put("driverLicenseOff",
					DateUtils.dateFormat2(driverInfo.getDriverlicenseoff()));
			info.put("fullTimeDriver", driverInfo.getFulltimedriver());
			info.put("VehicleNo", driverInfo.getVehicleNo());

			// 假数据错误信息
			Map<String, Object> map0 = new HashMap<String, Object>();
			map0.put("codeIndex", DriverInfoType.PHOTO.value());
			map0.put("codeName", DriverInfoType.PHOTO.codeName());
			map0.put("message", DriverInfoType.PHOTO.message());
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("codeIndex", DriverInfoType.DRIVERNAME.value());
			map1.put("codeName", DriverInfoType.DRIVERNAME.codeName());
			map1.put("message", DriverInfoType.DRIVERNAME.message());
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("codeIndex", DriverInfoType.LICENSEID.value());
			map2.put("codeName", DriverInfoType.LICENSEID.codeName());
			map2.put("message", DriverInfoType.LICENSEID.message());
			Map<String, Object> map3 = new HashMap<String, Object>();
			map3.put("codeIndex", DriverInfoType.DRIVERNATIONALITY.value());
			map3.put("codeName", DriverInfoType.DRIVERNATIONALITY.codeName());
			map3.put("message", DriverInfoType.DRIVERNATIONALITY.message());
			Map<String, Object> map4 = new HashMap<String, Object>();
			map4.put("codeIndex", DriverInfoType.DRIVERNATION.value());
			map4.put("codeName", DriverInfoType.DRIVERNATION.codeName());
			map4.put("message", DriverInfoType.DRIVERNATION.message());
			Map<String, Object> map5 = new HashMap<String, Object>();
			map5.put("codeIndex", DriverInfoType.ADDRESSNAME.value());
			map5.put("codeName", DriverInfoType.ADDRESSNAME.codeName());
			map5.put("message", DriverInfoType.ADDRESSNAME.message());
			Map<String, Object> map6 = new HashMap<String, Object>();
			map6.put("codeIndex", DriverInfoType.LICENSEPHOTO.value());
			map6.put("codeName", DriverInfoType.LICENSEPHOTO.codeName());
			map6.put("message", DriverInfoType.LICENSEPHOTO.message());
			Map<String, Object> map7 = new HashMap<String, Object>();
			map7.put("codeIndex", DriverInfoType.GETDRIVERLICENSEDATE.value());
			map7.put("codeName", DriverInfoType.GETDRIVERLICENSEDATE.codeName());
			map7.put("message", DriverInfoType.GETDRIVERLICENSEDATE.message());
			Map<String, Object> map8 = new HashMap<String, Object>();
			map8.put("codeIndex", DriverInfoType.DRIVERLICENSEON.value());
			map8.put("codeName", DriverInfoType.DRIVERLICENSEON.codeName());
			map8.put("message", DriverInfoType.DRIVERLICENSEON.message());
			Map<String, Object> map9 = new HashMap<String, Object>();
			map9.put("codeIndex", DriverInfoType.DRIVERLICENSEOFF.value());
			map9.put("codeName", DriverInfoType.DRIVERLICENSEOFF.codeName());
			map9.put("message", DriverInfoType.DRIVERLICENSEOFF.message());
			Map<String, Object> map10 = new HashMap<String, Object>();
			map10.put("codeIndex", DriverInfoType.FULLTIMEDRIVER.value());
			map10.put("codeName", DriverInfoType.FULLTIMEDRIVER.codeName());
			map10.put("message", DriverInfoType.FULLTIMEDRIVER.message());
			error.add(map0);
			error.add(map1);
			error.add(map2);
			error.add(map3);
			error.add(map4);
			error.add(map5);
			error.add(map6);
			error.add(map7);
			error.add(map8);
			error.add(map9);
			error.add(map10);

			data.put("status", 1);
			data.put("info", info);
			data.put("error", error);

			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"获取成功！", data);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"获取失败！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 司机端更新
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> update(@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			DriverInfo driverInfo = new DriverInfo(jsonRequest);
			driverInfo.setDriverphone(user.getUserphone());
			driverInfo.setFlag(2);
			driverInfo.setUpdatetime(DateUtils.currentTimestamp());
			driverInfoService.updateByPhoneSelective(driverInfo);

			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"更新成功！");

		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"更新失败！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 司机端流水线
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cash", method = RequestMethod.POST)
	public ResponseEntity<String> cash(@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
			String driverPhone = user.getUserphone();
			//当日流水
			BigDecimal cashToday = payCashLogService.selectCashByDriverPhoneToday(driverPhone);
			result.put("cashToday", cashToday);
			//总接单数
			int totalCount = orderService.selectTotalCountByDriverPhone(driverPhone, null);
			int cancelCount = orderService.selectTotalCountByDriverPhone(driverPhone, OrderStatus.CANCEL.value());
			result.put("orderTotalCount", totalCount);
			result.put("orderSuccessCount", totalCount - cancelCount);
			//听单
			long onTime = 0;//毫秒
			DriverLocation driverLocation = driverLocationService.selectByPhone(driverPhone);
			Date startTime = driverLocation.getLoginTime();
			if ( startTime != null) {
				Date endTime = driverLocation.getLogoutTime() != null ? driverLocation.getLogoutTime():new Date();
				onTime = endTime.getTime() - startTime.getTime();
			}
			result.put("onTime", onTime);
			
			//支付列表
			List<PayCashLog> cashLogs = payCashLogService.selectByDriverPhone(driverPhone, 1, 5);
			if (cashLogs.size() > 0) {
				List<PaycashLogResponse> cashResponseList = new ArrayList<PaycashLogResponse>();
				for (PayCashLog payCashLog : cashLogs) {
					PaycashLogResponse cashResponse = new PaycashLogResponse(payCashLog);
					if (cashResponse != null) {
						cashResponseList.add(cashResponse);
					}
				}
				result.put("payCashLog", cashLogs);
			}
			
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					ResponseCode.SUCCESS.message(),result);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
		}
		
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

}
