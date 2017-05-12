package com.lhcx.model;

/**
 * Created by zhu on 2016/8/15.
 */
public class ResponseCode {
    /**
     * 鎴愬姛浠ｇ爜
     */
    private static final int success = 200; //璇锋眰鎴愬姛
    /**
     * 鐧婚檰妯″潡閿欒浠ｇ爜
     */
    private static final int token_expired = 301; //鏃犳晥token
    private static final int sms_send_failed = 305; //楠岃瘉鐮佸彂閫佸け璐�
    private static final int sms_times_limit = 306; //鍙戦�侀獙璇佺爜杩囦簬棰戠箒
    private static final int sms_checked_failed = 307;//楠岃瘉鐮佹牎楠屽け璐�

    private static final int departure_order_expired = 309; //鍙告満鍙戣溅鍗曞け鏁�
    private static final int departure_order_unEnabled = 310; //鍙告満鍙戣溅鍗曞骇浣嶅凡婊�

    private static final int parameter_wrong = 401; //鑾峰彇鍙傛暟鏈夎
    private static final int is_car_owner = 403; //杞︿富璁よ瘉鐘舵�佹湁璇�
    private static final int is_validated = 402; //韬唤璁よ瘉鐘舵�佹湁璇�
    private static final int booking_times_limit = 405; //棰勫畾杞﹀崟娆℃暟瓒呰繃闄愬埗
    private static final int booking_order_is_existing = 407; //褰撳墠瀛樺湪璁㈠崟锛屾彁閱掑厛鍒犻櫎褰撳墠璁㈠崟
    private static final int booking_order_is_not_existing = 406; //褰撳墠璁㈠崟涓嶅瓨鍦�
    private static final int order_is_aleardy_grabed = 501; //褰撳墠璁㈠崟涓嶅瓨鍦�
    private static final int order_grabed_unable_cancle = 502; //褰撳墠璁㈠崟涓嶅瓨鍦�
    private static final int order_is_self = 100; //鍙告満涓嶈兘鎶㈣嚜宸辩殑鍗曞瓙
    private static final int invite_time = 601; //閭�璇疯繃浜庨绻�

    private static final int no_data = 408;

    public static int getSuccess(){ return  success; }

    public static int getInvite_time() {
        return invite_time;
    }


    public static int getOrder_grabed_unable_cancle() {
        return order_grabed_unable_cancle;
    }

    public static int getOrder_is_self() {
        return order_is_self;
    }

    public static int getOrder_is_aleardy_grabed() {
        return order_is_aleardy_grabed;
    }

    public static int getToken_expired() {
        return token_expired;
    }

    public static int getSms_send_failed() {
        return sms_send_failed;
    }

    public static int getSms_times_limit() {
        return sms_times_limit;
    }

    public static int getSms_checked_failed() {
        return sms_checked_failed;
    }

    public static int getParameter_wrong() {
        return parameter_wrong;
    }

    public static int getIs_car_owner() {
        return is_car_owner;
    }

    public static int getBooking_times_limit() {
        return booking_times_limit;
    }

    public static int getBooking_order_is_existing() {
        return booking_order_is_existing;
    }

    public static int getDeparture_order_expired() {
        return departure_order_expired;
    }

    public static int getDeparture_order_unEnabled() {
        return departure_order_unEnabled;
    }

    public static int getBooking_order_is_not_existing() {
        return booking_order_is_not_existing;
    }

    public static int getIs_validated() {
        return is_validated;
    }

    public static int getNo_data() {
        return no_data;
    }
}
