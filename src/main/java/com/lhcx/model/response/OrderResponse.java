package com.lhcx.model.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lhcx.model.Order;
import com.lhcx.model.OrderLog;
import com.lhcx.utils.DateUtils;

/**
 * 订单基本信息返回类
 * @author lh
 *
 */
public class OrderResponse {
	private String OrderId;
	
	private String PassengerPhone;
	
	private String DepartTime;
	
	private String PassengerNote;
	
	private String Departure;

    private String DepLongitude;

    private String DepLatitude;

    private String Destination;

    private String DestLongitude;

    private String DestLatitude;
    
    private BigDecimal Fee;

    private String DriverPhone;

    private Integer Status;
    
    private Integer OrderType;
    
    private Integer CarType;
    
    //订单行程总距离
    private double TotalDistance;
    
    //司机据目的地实时距离
    private double OnTimeTotalDistance;
    
    //司机据乘客上车地实时距离
    private double OnTimeDistance;
    
    private List<OrderLogResponse> OrderLogs;

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getPassengerPhone() {
		return PassengerPhone;
	}

	public void setPassengerPhone(String passengerPhone) {
		PassengerPhone = passengerPhone;
	}

	public String getDepartTime() {
		return DepartTime;
	}

	public void setDepartTime(String departTime) {
		DepartTime = departTime;
	}

	public String getPassengerNote() {
		return PassengerNote;
	}

	public void setPassengerNote(String passengerNote) {
		PassengerNote = passengerNote;
	}

	public String getDeparture() {
		return Departure;
	}

	public void setDeparture(String departure) {
		Departure = departure;
	}

	public String getDepLongitude() {
		return DepLongitude;
	}

	public void setDepLongitude(String depLongitude) {
		DepLongitude = depLongitude;
	}

	public String getDepLatitude() {
		return DepLatitude;
	}

	public void setDepLatitude(String depLatitude) {
		DepLatitude = depLatitude;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

	public String getDestLongitude() {
		return DestLongitude;
	}

	public void setDestLongitude(String destLongitude) {
		DestLongitude = destLongitude;
	}

	public String getDestLatitude() {
		return DestLatitude;
	}

	public void setDestLatitude(String destLatitude) {
		DestLatitude = destLatitude;
	}

	public BigDecimal getFee() {
		return Fee;
	}

	public void setFee(BigDecimal fee) {
		Fee = fee;
	}

	public String getDriverPhone() {
		return DriverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		DriverPhone = driverPhone;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public Integer getOrderType() {
		return OrderType;
	}

	public void setOrderType(Integer orderType) {
		OrderType = orderType;
	}

	public Integer getCarType() {
		return CarType;
	}

	public void setCarType(Integer carType) {
		CarType = carType;
	}

	public double getTotalDistance() {
		return TotalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		TotalDistance = totalDistance;
	}

	public double getOnTimeTotalDistance() {
		return OnTimeTotalDistance;
	}

	public void setOnTimeTotalDistance(double onTimeTotalDistance) {
		OnTimeTotalDistance = onTimeTotalDistance;
	}

	public double getOnTimeDistance() {
		return OnTimeDistance;
	}

	public void setOnTimeDistance(double onTimeDistance) {
		OnTimeDistance = onTimeDistance;
	}

	public List<OrderLogResponse> getOrderLogs() {
		return OrderLogs;
	}

	public void setOrderLogs(List<OrderLogResponse> orderLogs) {
		OrderLogs = orderLogs;
	}
	
	public OrderResponse() {
	}
	
	public OrderResponse(Order order) {
		if (order != null) {
			this.OrderId = order.getOrderid();
			this.PassengerPhone = order.getPassengerphone();
			this.DepartTime = DateUtils.dateFormat(order.getDeparttime()) ;
			this.PassengerNote = order.getPassengernote();
			this.Departure = order.getDeparture();
			this.DepLongitude = order.getDeplongitude();
			this.DepLatitude = order.getDeplatitude();
			this.Destination = order.getDestination();
			this.DestLongitude = order.getDestlongitude();
			this.DestLatitude = order.getDestlatitude();
		    this.Fee = order.getFee();
		    this.DriverPhone = order.getDriverphone();
		    this.Status = order.getStatus();
		    this.OrderType = order.getOrderType();
		    this.CarType = order.getCarType();
		    this.TotalDistance = order.getTotalDistance();
		    this.OnTimeTotalDistance = order.getOnTimeTotalDistance();
		    this.OnTimeDistance = order.getOnTimeDistance();
		    this.OrderLogs = new ArrayList<OrderLogResponse>();
		    for (OrderLog orderLog : order.getOrderLogs()) {
		    	OrderLogResponse orderLogResponse = new OrderLogResponse(orderLog);
		    	this.OrderLogs.add(orderLogResponse );
			}

		}
	}
	

}
