package com.lhcx.model;

/**
 * Created by lh on 2017/5/10.
 */

import java.io.Serializable;

/**
 * 杩斿洖缁撴灉
 * @author william
 * desc锛� 鎺ュ彛杩斿洖缁撴灉
 * @param <T>
 */
public class ResultBean<T> implements Serializable {
    private int code = ResultCode.SUCCESS.code;
    private String message = ResultCode.SUCCESS.message;
    private T data;

    /**
     * 榛樿鏃燚ata鐨勬瀯閫犲嚱鏁帮紝缁撴灉涓猴細鎴愬姛
     */
    public ResultBean() {

    }

    /**
     * 榛樿鏈塂ata鐨勬瀯閫犲嚱鏁帮紝缁撴灉涓猴細鎴愬姛
     * @param data
     */
    public ResultBean(T data) {
        this.data = data;
    }

    /**
     * 绯荤粺璁惧畾鐨勫父鐢ㄨ繑鍥炵爜锛岀粨鏋滀负锛氳嚜瀹氫箟
     * @param mResultCode
     */
    public ResultBean(ResultCode mResultCode) {
        this.code = mResultCode.code;
        this.message = mResultCode.message;
    }

    /**
     * 鍙伒娲讳娇鐢ㄧ殑鏋勯�犲嚱鏁帮紝缁撴灉涓猴細鑷畾涔�
     * @param code
     * @param message
     */
    public ResultBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 鍙伒娲讳娇鐢ㄧ殑鏋勯�犲嚱鏁帮紝鏂逛究杩斿洖鏁版嵁锛岀粨鏋滀负锛氳嚜瀹氫箟
     * @param code
     * @param message
     * @param data
     */
    public ResultBean(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum ResultCode {
        SUCCESS(1,"璇锋眰鎴愬姛"), FAILURE(400,"璇锋眰澶辫触"), ERROR_ARGS(500,"鍙傛暟閿欒");

        private int code;
        private String message;
        private ResultCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}

