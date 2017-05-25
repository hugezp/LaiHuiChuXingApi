package com.lhcx.model;

import java.util.Date;

public class DriverLocation {
    private Integer id;

    private String companyid;

    private String licenseid;

    private Integer driverregioncode;

    private String vehicleno;

    private Date positiontime;

    private String longitude;

    private String latitude;

    private String phone;

    private int encrypt;

    private Integer direction;

    private Double elevation;

    private Double speed;

    private int bizstatus;

    private String orderid;

    private int isdel;
    
    private	Date loginTime;
    
    private Date logoutTime;
    
    private double distance;
    
    private String driverPhone;
    
    private Integer orderStatus;
    
    public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid == null ? null : companyid.trim();
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid == null ? null : licenseid.trim();
    }

    public Integer getDriverregioncode() {
        return driverregioncode;
    }

    public void setDriverregioncode(Integer driverregioncode) {
        this.driverregioncode = driverregioncode;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno == null ? null : vehicleno.trim();
    }

    public Date getPositiontime() {
		return positiontime;
	}

	public void setPositiontime(Date positiontime) {
		this.positiontime = positiontime;
	}

	public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public int getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public int getBizstatus() {
        return bizstatus;
    }

    public void setBizstatus(int bizstatus) {
        this.bizstatus = bizstatus;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
    
}