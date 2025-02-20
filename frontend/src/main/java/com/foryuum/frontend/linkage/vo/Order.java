package com.foryuum.frontend.linkage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
	private String ordererName;
	private String ordererId;
	private String orderDate;

	public String getOrdererName() {
		return ordererName;
	}

	public void setOrdererName(String ordererName) {
		this.ordererName = ordererName;
	}


	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrdererId() {
		return ordererId;
	}

	public void setOrdererId(String ordererId) {
		this.ordererId = ordererId;
	}
}
