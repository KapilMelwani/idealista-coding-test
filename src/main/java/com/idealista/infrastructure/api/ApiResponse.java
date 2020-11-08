package com.idealista.infrastructure.api;

public class ApiResponse {
	private Integer status;
	private String message;
	private Object response;

	public ApiResponse(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public ApiResponse(Integer status, String message,Object response) {
		this.status = status;
		this.message = message;
		this.response = response;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
}
