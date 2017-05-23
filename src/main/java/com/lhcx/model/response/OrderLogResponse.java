package com.lhcx.model.response;

import com.lhcx.model.OrderLog;
import com.lhcx.utils.DateUtils;

public class OrderLogResponse {
	
	private String OperatorPhone;

    private String OperatorTime;

    private String OperatorDescription;

    private Integer CurrentStatus;

    private Integer OldStatus;

    private Integer CancelTypeCode;

    private String Description;
    
    public OrderLogResponse() {
	}
    
    public OrderLogResponse(OrderLog log) {
    	if (log != null) {
    		this.OperatorPhone = log.getOperatorphone();
    		this.OperatorTime = DateUtils.dateFormat(log.getOperatortime()) ;
    		this.OperatorDescription = log.getOperatordescription();
    		this.CurrentStatus = log.getOperatorstatus();
    		this.OldStatus = log.getOldstatus();
    		this.CancelTypeCode = log.getCanceltypecode();
    		this.Description = log.getDescription();
		}
	}

	public String getOperatorPhone() {
		return OperatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		OperatorPhone = operatorPhone;
	}

	public String getOperatorTime() {
		return OperatorTime;
	}

	public void setOperatorTime(String operatorTime) {
		OperatorTime = operatorTime;
	}

	public String getOperatorDescription() {
		return OperatorDescription;
	}

	public void setOperatorDescription(String operatorDescription) {
		OperatorDescription = operatorDescription;
	}

	public Integer getCurrentStatus() {
		return CurrentStatus;
	}

	public void setCurrentStatus(Integer currentStatus) {
		CurrentStatus = currentStatus;
	}

	public Integer getOldStatus() {
		return OldStatus;
	}

	public void setOldStatus(Integer oldStatus) {
		OldStatus = oldStatus;
	}
	
	public Integer getCancelTypeCode() {
		return CancelTypeCode;
	}

	public void setCancelTypeCode(Integer cancelTypeCode) {
		CancelTypeCode = cancelTypeCode;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}
