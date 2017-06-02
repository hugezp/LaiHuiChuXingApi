package com.lhcx.model.response;

import com.lhcx.model.OrderLog;
import com.lhcx.utils.DateUtils;

public class OrderLogResponse {
	
	private String operatorPhone;

    private String operatorTime;

    private String operatorDescription;

    private Integer currentStatus;

    private Integer oldStatus;

    private Integer cancelTypeCode;

    private String description;
    
    public OrderLogResponse() {
	}
    
    public OrderLogResponse(OrderLog log) {
    	if (log != null) {
    		this.operatorPhone = log.getOperatorphone();
    		this.operatorTime = DateUtils.dateFormat(log.getOperatortime()) ;
    		this.operatorDescription = log.getOperatordescription();
    		this.currentStatus = log.getOperatorstatus();
    		this.oldStatus = log.getOldstatus();
    		this.cancelTypeCode = log.getCanceltypecode();
    		this.description = log.getDescription();
		}
	}

	public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

	public String getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}

	public String getOperatorDescription() {
		return operatorDescription;
	}

	public void setOperatorDescription(String operatorDescription) {
		this.operatorDescription = operatorDescription;
	}

	public Integer getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Integer getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Integer oldStatus) {
		this.oldStatus = oldStatus;
	}
	
	public Integer getCancelTypeCode() {
		return cancelTypeCode;
	}

	public void setCancelTypeCode(Integer cancelTypeCode) {
		this.cancelTypeCode = cancelTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
