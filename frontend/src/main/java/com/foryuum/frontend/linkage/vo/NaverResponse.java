package com.foryuum.frontend.linkage.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverResponse {
	private List<NaverResponseData> data;
	private String timetamp;

	public List<NaverResponseData> getData() {
		return data;
	}

	public void setData(List<NaverResponseData> data) {
		this.data = data;
	}

	public String getTimetamp() {
		return timetamp;
	}

	public void setTimetamp(String timetamp) {
		this.timetamp = timetamp;
	}
}
