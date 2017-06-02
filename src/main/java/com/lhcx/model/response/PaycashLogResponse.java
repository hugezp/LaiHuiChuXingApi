package com.lhcx.model.response;

import java.math.BigDecimal;

import com.lhcx.model.PayCashLog;
import com.lhcx.utils.DateUtils;

public class PaycashLogResponse {
	private String orderId;

    private String passengerPhone;

    private String driverPhone;

    private BigDecimal cash;

    private Integer actionType;

    private String payAccount;

    private Integer payType;

    private Integer status;

    private String description;

    private String errorDesc;

    private String createTime;

    private String updateTime;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPassengerPhone() {
		return passengerPhone;
	}

	public void setPassengerPhone(String passengerPhone) {
		this.passengerPhone = passengerPhone;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public PaycashLogResponse(){
		
	}
	
	public PaycashLogResponse(PayCashLog cashLog){
		if (cashLog != null) {
			this.orderId = cashLog.getOrderid();
			this.passengerPhone = cashLog.getPassengerphone();
			this.driverPhone = cashLog.getDriverphone();
			this.cash = cashLog.getCash();
			this.actionType = cashLog.getActiontype();
			this.payAccount = cashLog.getPayaccount();
			this.payType = cashLog.getPaytype();
			this.status = cashLog.getStatus();
			this.description = cashLog.getDescription();
			this.errorDesc = cashLog.getErrordesc();
			this.createTime = DateUtils.dateFormat(cashLog.getCreatetime());
			this.updateTime = DateUtils.dateFormat(cashLog.getUpdatetime());
		}
	}
}
