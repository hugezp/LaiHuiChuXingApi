package com.lhcx.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayCashLog {
    private Integer id;

    private String orderid;

    private String passengerphone;

    private String driverphone;
    
    private String driverIdentityToken;
    
    private String passengerIdentityToken;

    private BigDecimal cash;

    private Integer actiontype;

    private String payaccount;

    private Integer paytype;

    private Integer status;

    private String description;

    private String errordesc;

    private Date createtime;

    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getPassengerphone() {
        return passengerphone;
    }

    public void setPassengerphone(String passengerphone) {
        this.passengerphone = passengerphone == null ? null : passengerphone.trim();
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone == null ? null : driverphone.trim();
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public Integer getActiontype() {
        return actiontype;
    }

    public void setActiontype(Integer actiontype) {
        this.actiontype = actiontype;
    }

    public String getPayaccount() {
        return payaccount;
    }

    public void setPayaccount(String payaccount) {
        this.payaccount = payaccount == null ? null : payaccount.trim();
    }

    public Integer getPaytype() {
        return paytype;
    }

    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
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
        this.description = description == null ? null : description.trim();
    }

    public String getErrordesc() {
        return errordesc;
    }

    public void setErrordesc(String errordesc) {
        this.errordesc = errordesc == null ? null : errordesc.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public String getDriverIdentityToken() {
		return driverIdentityToken;
	}

	public void setDriverIdentityToken(String driverIdentityToken) {
		this.driverIdentityToken = driverIdentityToken;
	}

	public String getPassengerIdentityToken() {
		return passengerIdentityToken;
	}

	public void setPassengerIdentityToken(String passengerIdentityToken) {
		this.passengerIdentityToken = passengerIdentityToken;
	}
}