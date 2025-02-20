package com.foryuum.frontend.linkage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverResponseData {
	private ProductOrder productOrder;
	private Delivery delivery;
	private Order order;

	public ProductOrder getProductOrder() {
		return productOrder;
	}

	public void setProductOrder(ProductOrder productOrder) {
		this.productOrder = productOrder;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}