package com.lhcx.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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

    private Integer status;
    
    private Integer oldstatus;
    
    private Integer orderType;
    
    private Integer carType;
    
    //订单行程总距离
    private double totalDistance;
    
    //司机据目的地实时距离
    private double onTimeTotalDistance;
    
    //司机据乘客上车地实时距离
    private double onTimeDistance;
    
    private List<OrderLog> orderLogs;
    

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

    public void setDriverphone() {
        this.driverphone = driverphone == null ? null : driverphone.trim();
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

	public double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public double getOnTimeTotalDistance() {
		return onTimeTotalDistance;
	}

	public void setOnTimeTotalDistance(double onTimeTotalDistance) {
		this.onTimeTotalDistance = onTimeTotalDistance;
	}

	public double getOnTimeDistance() {
		return onTimeDistance;
	}

	public void setOnTimeDistance(double onTimeDistance) {
		this.onTimeDistance = onTimeDistance;
	}

	public List<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(List<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
		
		//设置接单司机及订单状态
		if (this.orderLogs != null ) {
			this.status = orderLogs.get(0).getOperatorstatus();
			this.oldstatus = orderLogs.get(0).getOldstatus();
			for (OrderLog orderLogTemp : this.orderLogs) {
				if(orderLogTemp.getOperatorstatus() == OrderStatus.Receiving.value()){
					this.driverphone = orderLogTemp.getOperatorphone();
					return;
				}
			}
		}
	}

	public Integer getOldstatus() {
		return oldstatus;
	}

	public void setOldstatus(Integer oldstatus) {
		this.oldstatus = oldstatus;
	}
}