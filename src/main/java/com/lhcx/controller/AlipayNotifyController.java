package com.lhcx.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.lhcx.model.AlipayLog;
import com.lhcx.model.Order;
import com.lhcx.model.OrderLog;
import com.lhcx.model.OrderStatus;
import com.lhcx.model.PayActionType;
import com.lhcx.model.PayCashLog;
import com.lhcx.model.PushNotification;
import com.lhcx.service.IAlipayLogService;
import com.lhcx.service.IOrderLogService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPayCashLogService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.AppJsonUtils;
import com.lhcx.utils.ConfigUtils;
import com.lhcx.utils.JpushClientUtil;
import com.lhcx.utils.MD5Kit;
import com.lhcx.utils.PayConfigUtils;
import com.lhcx.utils.XmlParse;

@Controller
public class AlipayNotifyController {

	private static Logger log = Logger.getLogger(AlipayNotifyController.class);

	private static final String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMJraVXS8hS70cfN6pltYLc4JuIZjP02xtyVpk/a610en27zHYNeP4qRqwA7xSPPmTGaSDWAjTvMTaoB3M95vGgFNLcal7fhsRdKFTxlo8RpIoACVoElkG39QJsCMoIkMlxbrX0elgJFSprDfDcOeMYxCe3emcTTzqxK9DeED7BQIDAQAB";
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private IAlipayLogService alipayLogService;
	@Autowired
	private IPushNotificationService pushNotificationService;
	@Autowired
	private IOrderLogService orderLogService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IPayCashLogService payCashLogService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/wxpay/notify", method = RequestMethod.POST)
	public void wxPay(HttpServletResponse response) {
		String response_content = "<xml> \n"
				+ "  <return_code><![CDATA[FAIL]]></return_code>\n"
				+ "  <return_msg><![CDATA[ERROR]]></return_msg>\n"
				+ "</xml> \n";
		PrintWriter out = null;
		try {
			out = response.getWriter();
			// step1: 获取回执参数
			System.out.println("-----开始处理微信通知------");
			InputStream is;
			String return_xml = null;
			is = request.getInputStream();
			return_xml = IOUtils.toString(is, "utf-8");
			boolean is_success = false;
			Document doc;
			Map<String, String> parameterMap = new HashMap<>();
			doc = DocumentHelper.parseText(return_xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			Iterator return_code = rootElt.elementIterator("return_code"); // 获取根节点下的子节点return_code
			while (return_code.hasNext()) {
				Element recordEle = (Element) return_code.next();
				String code = recordEle.getText(); // 拿到return_code返回值
				log.debug("code:" + code);
				if (code != null && code.equals("SUCCESS")) {
					is_success = true;
					break;
				}
			}
			if (is_success) {
				parameterMap = XmlParse.parse(return_xml);
				String sign = parameterMap.get("sign");
				String result_code = parameterMap.get("result_code");
				List<String> keys = new ArrayList<>(parameterMap.keySet());
				keys.remove("sign");
				String result_parameter = "";
				Collections.sort(keys);
				for (String str : keys) {
					result_parameter = result_parameter + str + "="
							+ parameterMap.get(str) + "&";
				}
				result_parameter = result_parameter + "key="
						+ PayConfigUtils.getWx_laihui_app_secret_key();
				String current_sign = MD5Kit.encode("MD5",result_parameter)
						.toUpperCase();
				if (current_sign.equals(sign)) {
					is_success = true;
				} else {
					is_success = false;
				}
				System.out.println("微信支付签名校验：" + is_success);

				if (is_success) {
					if (result_code.equals("SUCCESS")) {
						// 1：查询是否已经收到异步通知,如果已收到则停止执行
						// 2：未收到通知开始创建支付log
						Order order = alipayLogService
								.alipayNotify(parameterMap);
						response.reset();
						out = response.getWriter();
						response_content = "<xml> \n"
								+ "  <return_code><![CDATA[SUCCESS]]></return_code>\n"
								+ "  <return_msg><![CDATA[OK]]></return_msg>\n"
								+ "</xml> \n";

						// 3：推送给司机
						String orderId = order.getOrderid();
						String driverPhone = order.getDriverphone();
						String passengerPhone = order.getPassengerphone();
						String content = "【来回出行】手机号为"
								+ passengerPhone
								+ "的乘客通过微信支付了"
								+ new BigDecimal(Integer.parseInt(parameterMap
										.get("total_fee")) / 100d)
								+ "元。请查验，订单编号为：" + orderId;

						Map<String, String> extrasParam = new HashMap<String, String>();
						extrasParam.put("OrderId", orderId);

						int flag = JpushClientUtil.getInstance(
								ConfigUtils.JPUSH_APP_KEY,
								ConfigUtils.JPUSH_MASTER_SECRET)
								.sendToRegistrationId("11", driverPhone,
										content, content, content, extrasParam);

						if (flag == 1) {
							PushNotification pushNotification = new PushNotification();
							pushNotification.setPushPhone(driverPhone);
							pushNotification.setReceivePhone(passengerPhone);
							pushNotification.setOrderId(orderId);
							pushNotification.setAlert(content);
							pushNotification.setPushType(1);
							pushNotification.setData(extrasParam.toString());
							pushNotification.setFlag(3);
							pushNotificationService
									.insertSelective(pushNotification);
						}

					} else {
						response.reset();
						out = response.getWriter();
						response_content = "<xml> \n"
								+ "  <return_code><![CDATA[FAIL]]></return_code>\n"
								+ "  <return_msg><![CDATA[result code fail]]></return_msg>\n"
								+ "</xml> \n";
					}
				} else {
					// 直接停止执行
					response.reset();
					out = response.getWriter();
					response_content = "<xml> \n"
							+ "  <return_code><![CDATA[FAIL]]></return_code>\n"
							+ "  <return_msg><![CDATA[Signature verification error]]></return_msg>\n"
							+ "</xml> \n";
				}

			} else {
				// 直接停止执行
				response.reset();
				out = response.getWriter();
				response_content = "<xml> \n"
						+ "  <return_code><![CDATA[FAIL]]></return_code>\n"
						+ "  <return_msg><![CDATA[return code fail]]></return_msg>\n"
						+ "</xml> \n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(response_content);
			out.close();
		}
	}

	/**
	 * 支付宝回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/alipay/notify", method = RequestMethod.POST)
	public ResponseEntity<String> departure(HttpServletResponse response) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json;charset=UTF-8");
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		JSONObject result = new JSONObject();
		String json = "";
		boolean is_success;
		Map<String, String[]> parameterMap = request.getParameterMap();

		int size = parameterMap.size() - 2;
		String[] parameters = new String[size];
		Map<String, String> params = new HashMap<>();
		int i = 0;
		for (String key : parameterMap.keySet()) {
			if (!key.equals("sign") && !key.equals("sign_type")) {
				parameters[i] = key;
				params.put(key, parameterMap.get(key)[0]);
				i++;
			}
		}
		String result_parameter = "";
		Arrays.sort(parameters);
		for (String str : parameters) {
			result_parameter = result_parameter + str + "="
					+ parameterMap.get(str)[0] + "&";
		}
		result_parameter = result_parameter.substring(0,
				result_parameter.length() - 1);

		String sign = request.getParameter("sign");

		System.out.println("-----开始处理支付宝通知------");
		is_success = verify(result_parameter, sign);
		System.out.println("verify校验：" + is_success);

		String notify_time = request.getParameter("notify_time");
		String notify_type = request.getParameter("notify_type");
		String notify_id = request.getParameter("notify_id");

		String trade_no = request.getParameter("trade_no");
		String out_trade_no = request.getParameter("out_trade_no");
		String price = request.getParameter("total_amount");
		String receipt_amount = request.getParameter("receipt_amount");
		String buyer_pay_amount = request.getParameter("buyer_pay_amount");
		String point_amount = request.getParameter("point_amount");
		String seller_id = request.getParameter("seller_id");
		String seller_email = request.getParameter("seller_email");

		String buyer_email = request.getParameter("buyer_logon_id");
		String buyer_id = request.getParameter("buyer_id");
		String trade_status = request.getParameter("trade_status");

		if (is_success) {
			if (trade_status != null) {
				AlipayLog alipay = new AlipayLog();

				alipay.setBuyerEmail(buyer_email);
				alipay.setBuyerId(buyer_id);
				alipay.setSellerEmail(seller_email);
				alipay.setSellerId(seller_id);
				alipay.setTradeStatus(trade_status);
				alipay.setPrice(new BigDecimal(price));
				alipay.setReceiptAmount(new BigDecimal(receipt_amount));
				alipay.setBuyerPayAmount(new BigDecimal(buyer_pay_amount));
				alipay.setPointAmount(new BigDecimal(point_amount));
				alipay.setOutTradeNo(out_trade_no);
				alipay.setTradeNo(trade_no);
				alipay.setNotifyType(notify_type);
				try {
					alipay.setNotifyTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
							.parse(notify_time));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				alipay.setNotifyId(notify_id);
				alipay.setPaySource(0);

				List<AlipayLog> alipayNotifyList = alipayLogService
						.selectList(alipay);
				if (alipayNotifyList.size() > 0) {
					PrintWriter out = null;
					try {
						response.reset();
						out = response.getWriter();
						out.write("success");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						out.close();
					}
					json = AppJsonUtils.returnSuccessJsonString(result, "响应成功");
					return new ResponseEntity<>(json, responseHeaders,
							HttpStatus.OK);
				}
				// 插入日志
				int flag = alipayLogService.insertSelective(alipay);
				System.out.println("支付log创建成功！");
				boolean is_pay_success = false;
				if (trade_status.equals("TRADE_SUCCESS")
						|| trade_status.equals("TRADE_FINISHED")) {
					is_pay_success = true;
				}
				if (flag == 1 && is_pay_success) {
					//更新订单状态
					Order order = orderService.selectByOrderId(out_trade_no);
					order.setOldstatus(order.getStatus());
					order.setStatus(OrderStatus.PAY.value());
					orderService.updateByOrderIdSelective(order);
					OrderLog orderLog = new OrderLog();
					orderLog.setOrderid(out_trade_no);
					orderLog.setOperatorphone(order.getPassengerphone());
					orderLog.setOperatortime(new Date());
					orderLog.setOperatorstatus(OrderStatus.PAY.value());
					orderLog.setOperatordescription(OrderStatus.PAY.message());
					orderLogService.insertSelective(orderLog);
					//2.3：创建账户流水线日志：乘客支出，司机收入
					PayCashLog cashLog = new PayCashLog();
					cashLog.setOrderid(out_trade_no);
					cashLog.setPassengerphone(order.getPassengerphone());
					cashLog.setDriverphone(order.getDriverphone());
					cashLog.setCash(new BigDecimal(price));
					cashLog.setPaytype(0);//支付宝支付
					cashLog.setStatus(2);//支付完成
					cashLog.setActiontype(PayActionType.spending.value());//乘客支出
					cashLog.setDescription("乘客支付宝支付记录");
					payCashLogService.insertSelective(cashLog);
					
					cashLog.setActiontype(PayActionType.income.value());//司机收入
					cashLog.setDescription("司机支付宝收入记录");
					payCashLogService.insertSelective(cashLog);
					
					// 3：推送给司机
					String orderId = order.getOrderid();
					String driverPhone = order.getDriverphone();
					String passengerPhone = order.getPassengerphone();
					String content = "【来回出行】手机号为"
							+ passengerPhone
							+ "的乘客通过支付宝支付了"
							+ new BigDecimal(price)
							+ "元。请查验，订单编号为：" + orderId;

					Map<String, String> extrasParam = new HashMap<String, String>();
					extrasParam.put("OrderId", orderId);

					int count = JpushClientUtil.getInstance(
							ConfigUtils.JPUSH_APP_KEY,
							ConfigUtils.JPUSH_MASTER_SECRET)
							.sendToRegistrationId("11", driverPhone,
									content, content, content, extrasParam);

					if (count == 1) {
						PushNotification pushNotification = new PushNotification();
						pushNotification.setPushPhone(driverPhone);
						pushNotification.setReceivePhone(passengerPhone);
						pushNotification.setOrderId(orderId);
						pushNotification.setAlert(content);
						pushNotification.setPushType(1);
						pushNotification.setData(extrasParam.toString());
						pushNotification.setFlag(3);
						pushNotificationService
								.insertSelective(pushNotification);
					}
					PrintWriter out = null;
					try {
						response.reset();
						out = response.getWriter();
						out.write("success");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						out.close();
					}
				}
			}
		}
		json = AppJsonUtils.returnSuccessJsonString(result, "响应成功");
		return new ResponseEntity<>(json, responseHeaders, HttpStatus.OK);
	}

	/**
	 * 校验sign
	 * 
	 * @param plainText
	 * @param sign
	 * @return
	 */
	public static boolean verify(String plainText, String sign) {
		try {
			boolean is_passed = AlipaySignature.rsaCheckContent(plainText,
					sign, publickey, "utf-8");
			// 验证签名是否正常
			return is_passed;
		} catch (Throwable e) {
			System.out.println("校验签名失败");
			e.printStackTrace();
			return false;
		}
	}
}
