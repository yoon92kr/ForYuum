package com.foryuum.frontend.linkage.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrder {
	private int quantity; // 주무 수량
	private String productId; // 상품 ID
	private String productName; // 상품명
	private String productOption; // 상품 옵션
	private String shippingMemo; // 배송 메모
	private NaverShippingInfo shippingAddress; // 배송 정보

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductOption() {
		return productOption;
	}

	public void setProductOption(String productOption) {
		this.productOption = productOption;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public NaverShippingInfo getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(NaverShippingInfo shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingMemo() {
		return shippingMemo;
	}

	public void setShippingMemo(String shippingMemo) {
		this.shippingMemo = shippingMemo;
	}

}
