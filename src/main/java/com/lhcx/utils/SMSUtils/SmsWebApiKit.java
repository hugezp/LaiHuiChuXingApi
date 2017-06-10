package com.lhcx.utils.SMSUtils;




/**
 * 服务端发起验证请求验证移动端(手机)发送的短信
 * @author Administrator
 *
 */
public class SmsWebApiKit {
	

	//AndroidiOS_driver  1e93451c36962
	private static String ios_driver_appkey ="1e93451c36962" ;
	private static String ios_passenger_appkey ="1e933f780f224" ;
	private static String android_driver_appkey ="1e93504c893b8" ;
	private static String android_passenger_appkey ="1e9355ec941b8" ;
	
//	public SmsWebApiKit(String appkey) {
//		super();
//		this.appkey = appkey;
//	}
	private SmsWebApiKit() {}
	private static SmsWebApiKit smsWebApiKit=null;
	//静态工厂方法
	public static SmsWebApiKit getInstance() {
		if (smsWebApiKit == null) {
			smsWebApiKit = new SmsWebApiKit();
		}
		return smsWebApiKit;
	}
	
	public String checkcode(String phone,String zone,String code,String userType,String source) throws Exception{
	
	String address = "https://webapi.sms.mob.com/sms/verify";
	MobClient client = null;
	try {
		client = new MobClient(address);
		//iOS乘客
		if (userType.equals("passenger")&&source.equals("0")) {
			client.addParam("appkey", ios_passenger_appkey).addParam("phone", phone)
			.addParam("zone", zone).addParam("code", code);
		//iOS司机
		}else if (userType.equals("driver")&&source.equals("0")) {
			client.addParam("appkey", ios_driver_appkey).addParam("phone", phone)
			.addParam("zone", zone).addParam("code", code);
		//andriod乘客
		}else if (userType.equals("passenger")&&source.equals("1")) {
			client.addParam("appkey", android_passenger_appkey).addParam("phone", phone)
			.addParam("zone", zone).addParam("code", code);
		//andriod司机
		}else {
			client.addParam("appkey", android_driver_appkey).addParam("phone", phone)
			.addParam("zone", zone).addParam("code", code);
		}
		
		//client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		client.addRequestProperty("Accept", "application/json");
		String result = client.post().substring(10,13);

		return result;
	} finally {
		client.release();
	}
}

	/**
	 * 服务端发起发送短信请求
	 * @return
	 * @throws Exception
	 */
//	public  String sendMsg(String phone,String zone) throws Exception{
//		
//		String address = "https://xxxx";
//		MobClient client = null;
//		try {
//			client = new MobClient(address);
//			client.addParam("appkey", appkey).addParam("phone", phone)
//					.addParam("zone", zone);
//			client.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//			client.addRequestProperty("Accept", "application/json");
//			String result = client.post();
//			return result;
//		} finally {
//			client.release();
//		}
//	}
	
	/**
	 * 服务端发验证服务端发送的短信
	 * @return
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		
		//System.out.println(SmsWebApiKit.checkcode("15738961936","86","2197"));
		
	}
	
	
	

}
