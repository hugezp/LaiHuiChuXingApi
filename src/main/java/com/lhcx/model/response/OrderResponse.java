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
	private String orderId;
	
	private String passengerPhone;
	
	private String departTime;
	
	private String passengerNote;
	
	private String departure;

    private String depLongitude;

    private String depLatitude;

    private String destination;

    private String destLongitude;

    private String destLatitude;
    
    private BigDecimal fee;

    private String driverPhone;

    private Integer status;
    
    private Integer orderType;
    
    private Integer carType;
    
    //订单行程总距离
    private double totalDistance;
    
    //司机据目的地实时距离
    private double onTimeTotalDistance;
    
    //司机据乘客上车地实时距离
    private double onTimeDistance;
    
    private List<OrderLogResponse> orderLogs;

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

	public String getDepartTime() {
		return departTime;
	}

	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}

	public String getPassengerNote() {
		return passengerNote;
	}

	public void setPassengerNote(String passengerNote) {
		this.passengerNote = passengerNote;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDepLongitude() {
		return depLongitude;
	}

	public void setDepLongitude(String depLongitude) {
		this.depLongitude = depLongitude;
	}

	public String getDepLatitude() {
		return depLatitude;
	}

	public void setDepLatitude(String depLatitude) {
		this.depLatitude = depLatitude;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(String destLongitude) {
		this.destLongitude = destLongitude;
	}

	public String getDestLatitude() {
		return destLatitude;
	}

	public void setDestLatitude(String destLatitude) {
		this.destLatitude = destLatitude;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public List<OrderLogResponse> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(List<OrderLogResponse> orderLogs) {
		this.orderLogs = orderLogs;
	}
	
	public OrderResponse() {
	}
	
	public OrderResponse(Order order) {
		if (order != null) {
			this.orderId = order.getOrderid();
			this.passengerPhone = order.getPassengerphone();
			this.departTime = DateUtils.dateFormat(order.getDeparttime()) ;
			this.passengerNote = order.getPassengernote();
			this.departure = order.getDeparture();
			this.depLongitude = order.getDeplongitude();
			this.depLatitude = order.getDeplatitude();
			this.destination = order.getDestination();
			this.destLongitude = order.getDestlongitude();
			this.destLatitude = order.getDestlatitude();
		    this.fee = order.getFee();
		    this.driverPhone = order.getDriverphone();
		    this.status = order.getStatus();
		    this.orderType = order.getOrderType();
		    this.carType = order.getCarType();
		    this.totalDistance = order.getTotalDistance();
		    this.onTimeTotalDistance = order.getOnTimeTotalDistance();
		    this.onTimeDistance = order.getOnTimeDistance();
		    this.orderLogs = new ArrayList<OrderLogResponse>();
		    for (OrderLog orderLog : order.getOrderLogs()) {
		    	OrderLogResponse orderLogResponse = new OrderLogResponse(orderLog);
		    	this.orderLogs.add(orderLogResponse );
			}

		}
	}
	

}
