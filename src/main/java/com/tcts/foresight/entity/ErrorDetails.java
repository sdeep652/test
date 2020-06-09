package com.tcts.foresight.entity;

import com.tcts.foresight.util.Constant;

public class ErrorDetails {

	private String errorCode;
	private String errorReason;
	private String errorMessage;
	
	
	
	
	public ErrorDetails(String errorCode, String errorReason, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorReason = errorReason;
		this.errorMessage = errorMessage;
	}
	
	
	public static ErrorDetails getInternalServerError()
	{
		return new ErrorDetails("500", "Internal Server Error", "Some internal error occurred, please try after some time");
	}
	
	public static ErrorDetails getInternalServerError(Exception e)
	{
		return new ErrorDetails("500", "Internal Server Error", "Some internal error occurred, please try after some time. Exception is as follows: "+e.getMessage());
	}
	
	public static ErrorDetails getUnAuthorized()
	{
		return new ErrorDetails("401", Constant.User_Does_Not_Have_Authorization, Constant.User_Does_Not_Have_Authorization);
	}
	
	public static ErrorDetails NOCONTENT()
	{
		return new ErrorDetails("204", "No data found for this request.", "No data found for this request.");
	}
	
	public static ErrorDetails BADREQUEST()
	{
		return new ErrorDetails("400", "Bad Request", "Please Re-check API or Paramteres ");
	}	
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
