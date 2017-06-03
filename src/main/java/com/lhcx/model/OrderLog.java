package com.lhcx.model;

import java.util.Date;

public class OrderLog {
    private Integer id;

    private String orderid;

    private String operatorphone;
    
    private String identityToken;

    private Date operatortime;

    private String operatordescription;

    private Integer operatorstatus;

    private Integer oldstatus;

    private Integer operatortype;

    private Integer canceltypecode;

    private String description;

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

    public String getOperatorphone() {
        return operatorphone;
    }

    public void setOperatorphone(String operatorphone) {
        this.operatorphone = operatorphone == null ? null : operatorphone.trim();
    }

    public Date getOperatortime() {
        return operatortime;
    }

    public void setOperatortime(Date operatortime) {
        this.operatortime = operatortime;
    }

    public String getOperatordescription() {
        return operatordescription;
    }

    public void setOperatordescription(String operatordescription) {
        this.operatordescription = operatordescription == null ? null : operatordescription.trim();
    }

    public Integer getOperatorstatus() {
        return operatorstatus;
    }

    public void setOperatorstatus(Integer operatorstatus) {
        this.operatorstatus = operatorstatus;
    }

    public Integer getOldstatus() {
        return oldstatus;
    }

    public void setOldstatus(Integer oldstatus) {
        this.oldstatus = oldstatus;
    }

    public Integer getOperatortype() {
        return operatortype;
    }

    public void setOperatortype(Integer operatortype) {
        this.operatortype = operatortype;
    }

    public Integer getCanceltypecode() {
        return canceltypecode;
    }

    public void setCanceltypecode(Integer canceltypecode) {
        this.canceltypecode = canceltypecode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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

	public String getIdentityToken() {
		return identityToken;
	}

	public void setIdentityToken(String identityToken) {
		this.identityToken = identityToken;
	}
}