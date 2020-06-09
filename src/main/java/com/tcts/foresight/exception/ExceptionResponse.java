package com.tcts.foresight.exception;

import java.util.Date;

public class ExceptionResponse {
  private Date timestamp;
  private String errorMessage;
  private String details;

  public ExceptionResponse(Date timestamp, String errorMessage, String details) {
    super();
    this.timestamp = timestamp;
    this.errorMessage = errorMessage;
    this.details = details;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getDetails() {
    return details;
  }

}