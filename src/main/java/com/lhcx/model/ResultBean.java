package com.lhcx.model;

/**
 * Created by lh on 2017/5/10.
 */

import java.io.Serializable;

/**
 * @author： william
 * description：返回结果类
 * @param <T>
 */
@SuppressWarnings("serial")
public class ResultBean<T> implements Serializable {
    private int code = ResultCode.SUCCESS.code;
    private String message = ResultCode.SUCCESS.message;
    private T data;

    /**
	 * 默认无Data的构造函数，结果为：成功
	 */
	public ResultBean() {
		
	}
	
	/**
	 * 默认有Data的构造函数，结果为：成功
	 * @param data
	 */
	public ResultBean(T data) {
		this.data = data;
	}
	
	/**
	 * 系统设定的常用返回码，结果为：自定义
	 * @param mResultCode
	 */
	public ResultBean(ResultCode mResultCode) {
		this.code = mResultCode.code;
		this.message = mResultCode.message;
	}
	
	/**
	 * 可灵活使用的构造函数，结果为：自定义
	 * @param code
	 * @param message
	 */
	public ResultBean(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 可灵活使用的构造函数，方便返回数据，结果为：自定义
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
		SUCCESS(1,"请求成功"), FAILURE(400,"请求失败"), ERROR_ARGS(500,"参数错误");
		
		private int code;
		private String message;
		private ResultCode(int code, String message) {
			this.code = code;
			this.message = message;
		}
	}
}

