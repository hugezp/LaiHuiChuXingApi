package com.lhcx.model;

import java.util.Date;

public class VerificationLogs {
    private Integer id;

    private String driverPhone;

    private String verificationContent;
    
    private String verificationName;

    private Integer verificationStatus;

    private Date verificationTime;

    private Integer errorCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public String getVerificationContent() {
        return verificationContent;
    }

    public void setVerificationContent(String verificationContent) {
        this.verificationContent = verificationContent == null ? null : verificationContent.trim();
    }

    public String getVerificationName() {
		return verificationName;
	}

	public void setVerificationName(String verificationName) {
		this.verificationName = verificationName;
	}

	public Integer getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Integer verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Date getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(Date verificationTime) {
        this.verificationTime = verificationTime;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}