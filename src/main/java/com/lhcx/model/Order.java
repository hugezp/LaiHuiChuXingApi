package com.lhcx.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.utils.DateUtils;

public class Order {
    private Integer id;

    private String companyid;

    private Integer address;

    private String orderid;

    private String passengerphone;

    private Date departtime;

    private Date ordertime;

    private String passengernote;

    private String departure;

    private String deplongitude;

    private String deplatitude;

    private String destination;

    private String destlongitude;

    private String destlatitude;

    private Integer encrypt;

    private String faretype;

    private BigDecimal fee;

    private String driverphone;

    private String licenseid;

    private String vehicleno;

    private String longitude;

    private String latitude;

    private Date distributetime;

    private Date canceltime;

    private String operator;

    private String canceltypecode;

    private String cancelreason;
    
    private Integer status;
    
    private Integer orderType;
    
    private Integer carType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid == null ? null : companyid.trim();
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
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

    public Date getDeparttime() {
        return departtime;
    }

    public void setDeparttime(Date departtime) {
        this.departtime = departtime;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public String getPassengernote() {
        return passengernote;
    }

    public void setPassengernote(String passengernote) {
        this.passengernote = passengernote == null ? null : passengernote.trim();
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure == null ? null : departure.trim();
    }

    public String getDeplongitude() {
        return deplongitude;
    }

    public void setDeplongitude(String deplongitude) {
        this.deplongitude = deplongitude == null ? null : deplongitude.trim();
    }

    public String getDeplatitude() {
        return deplatitude;
    }

    public void setDeplatitude(String deplatitude) {
        this.deplatitude = deplatitude == null ? null : deplatitude.trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public String getDestlongitude() {
        return destlongitude;
    }

    public void setDestlongitude(String destlongitude) {
        this.destlongitude = destlongitude == null ? null : destlongitude.trim();
    }

    public String getDestlatitude() {
        return destlatitude;
    }

    public void setDestlatitude(String destlatitude) {
        this.destlatitude = destlatitude == null ? null : destlatitude.trim();
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public String getFaretype() {
        return faretype;
    }

    public void setFaretype(String faretype) {
        this.faretype = faretype == null ? null : faretype.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone == null ? null : driverphone.trim();
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid == null ? null : licenseid.trim();
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno == null ? null : vehicleno.trim();
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

    public Date getDistributetime() {
        return distributetime;
    }

    public void setDistributetime(Date distributetime) {
        this.distributetime = distributetime;
    }

    public Date getCanceltime() {
        return canceltime;
    }

    public void setCanceltime(Date canceltime) {
        this.canceltime = canceltime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getCanceltypecode() {
        return canceltypecode;
    }

    public void setCanceltypecode(String canceltypecode) {
        this.canceltypecode = canceltypecode == null ? null : canceltypecode.trim();
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason == null ? null : cancelreason.trim();
    }
    
    public Order(){
    	
    }
    
    public Order(JSONObject jsonRequest) throws ParseException{
    	if (jsonRequest != null) {
    		this.passengerphone = jsonRequest.getString("PassengerPhone");
    		this.departtime = DateUtils.toDateTime(jsonRequest.getLong("DePartTime")) ;
			this.ordertime = DateUtils.toDateTime(jsonRequest.getLong("OrderTime"));
			this.passengernote = jsonRequest.getString("PassengerNote");
			this.departure = jsonRequest.getString("Departure");
			this.deplongitude = jsonRequest.getString("DepLongitude");
			this.deplatitude = jsonRequest.getString("DepLatitude");
			this.destination = jsonRequest.getString("Destination");
			this.destlongitude = jsonRequest.getString("DestLongitude");
			this.destlatitude = jsonRequest.getString("DestLatitude");
			this.fee = jsonRequest.getBigDecimal("Fee");
			this.orderType = jsonRequest.getInteger("OrderType");
			this.carType = jsonRequest.getInteger("CarType");
			
		}
    }

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}
}