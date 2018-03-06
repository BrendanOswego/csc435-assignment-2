package mainpackage.models;

import java.util.List;

public class ResponseModel {
  private int status;
  private String message;
  private String href;

  public ResponseModel() {
  }

  public ResponseModel(int status, String message, String href) {
    this.status = status;
    this.message = message;
    this.href = href;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getHref() {
    return href;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public void setSQLException(String href) {
    message = "There was an internal SQL exception";
    status = 500;
    this.href = href;
  }

}