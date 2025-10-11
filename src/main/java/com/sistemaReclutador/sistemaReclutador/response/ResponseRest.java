package com.sistemaReclutador.sistemaReclutador.response;

import java.time.LocalDateTime;

public class ResponseRest<T> {

	private boolean success;
	private String message;
	private T data;
	private LocalDateTime timestamp;
	private String errorCode;

	public ResponseRest() {
	}

	public ResponseRest(boolean success, String message, T data, LocalDateTime timestamp, String errorCode) {
		this.success = success;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp;
		this.errorCode = errorCode;

	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
