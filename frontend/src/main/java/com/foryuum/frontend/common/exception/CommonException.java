package com.foryuum.frontend.common.exception;

public class CommonException extends Exception{ 
	
	private static final long serialVersionUID = 1L;

	public CommonException(String strMessage, Exception ex) {
		super(strMessage, ex);
	}
	
	public CommonException(String strMessage) {
		super(strMessage);
	}
	
	public CommonException(Exception aE) {
		super(aE);
	}  
}
