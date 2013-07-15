package com.hiputto.common4android.exception;

public class HP_ErrorHttpStatusException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3079472145770953494L;

	@Override
	public String getMessage() {
		return "error http status";
	}

	@Override
	public String getLocalizedMessage() {
		return "error http status";
	}

}
