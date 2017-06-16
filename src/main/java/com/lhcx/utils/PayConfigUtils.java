package com.lhcx.utils;



/**
 * Created by zhu on 2016/12/27.
 */
public class PayConfigUtils {

    //来回支付宝支付配置
    private final static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIwmtpVdLyFLvRx83qmW1gtzgm4hmM/TbG3JWmT9rrXR6fbvMdg14/ipGrADvFI8+ZMZpINYCNO8xNqgHcz3m8aAU0txqXt+GxF0oVPGWjxGkigAJWgSWQbf1AmwIygiQyXFutfR6WAkVKmsN8Nw54xjEJ7d6ZxNPOrEr0N4QPsFAgMBAAECgYA1+UbBmQRohzmKOhCqMRu3h0GI1kl6aArO8Qdt80CQZwn6fj2s7XwhLEbJ2HZzGWZoHyz7JDEqEf2h0J4JbKZBpR1nwW7sAt5t45B8LVY6X2hPHCgt2UD+1Q8g49PCitCO5QhgrKchryWfzeaKbM73H/TqvY/NCU1hwgb8E0oeIQJBAN70e3oK96VGQAnkACEom1XAS6A0VqEKDgQGNkcW/hueY92l1QN+z1ahT8yd92t/XtUyI1ariX8kxVBjcrIq94kCQQCg7G5C1UM6VPKA6yfURhHgfiKPURzJzEIKP9WCqYeRvn5G6BH1rpI9smNSSNeUpJST51xamPWH/dhFlHgR9sydAkBJ1h2uZUNucL10iRWh5ZjL5UsmWy71ViceHhCLqomtC9924ByTc8OmpPWQhiAScbQuVtRtN5HpkXvnC4hIiQJBAkAD2GJyGmJ23FzHE9dpzRrUQG9W+Vs0vzq8v/W8H4zrwJ+H1jfHpRo9eyvAZZkxJhWeyyJ7z7fePgSS9Q9BbqJJAkEA2ZBBIPIR0nfBK7EH8e4YVjlof/W0uMWIt4aTYgOw09XMLKU35nLQJ4q9pvxpPx6ChD+kqttIgnELnUJd/rN3Ww==";
    private final static String alipay_public_key ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMJraVXS8hS70cfN6pltYLc4JuIZjP02xtyVpk/a610en27zHYNeP4qRqwA7xSPPmTGaSDWAjTvMTaoB3M95vGgFNLcal7fhsRdKFTxlo8RpIoACVoElkG39QJsCMoIkMlxbrX0elgJFSprDfDcOeMYxCe3emcTTzqxK9DeED7BQIDAQAB";
    private final static String app_id = "2017041806789390";
   //来回微信支付配置
    private final static String wx_laihui_app_id = "wx60adfb5f380811b7";//公众号id
    private final static String wx_laihui_mch_id = "1463727102";//商户号
    private final static String wx_laihui_app_secret_key = "2bfca107a5dac789f2fdhg1ca5ee7695";
   
    //测试通知地址
     //来回微信支付回调地址
    private final static String wx_pay_laihui_notify_url = "http://192.168.1.18:8080/wxpay/notify";
    //支付宝支付回调地址
    private final static String alipay_notify_url = "https://api.laihuichuxing.com/alipay/notify";


    public static String getAlipay_public_key() {
        return alipay_public_key;
    }

    public static String getApp_id() {
        return app_id;
    }

    public static String getPrivate_key() {
        return private_key;
    }

   
    public static String getAlipay_notify_url() {
        return alipay_notify_url;
    }

    public static String getWx_laihui_app_id() {
        return wx_laihui_app_id;
    }

    public static String getWx_laihui_mch_id() {
        return wx_laihui_mch_id;
    }

    public static String getWx_laihui_app_secret_key() {
        return wx_laihui_app_secret_key;
    }
    
    
    public static String getWx_pay_laihui_notify_url() {
        return wx_pay_laihui_notify_url;
    }
   
}
