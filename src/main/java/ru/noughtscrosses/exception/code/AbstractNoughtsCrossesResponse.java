package ru.noughtscrosses.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public abstract class AbstractNoughtsCrossesResponse implements INoughtsCrossesResponse {
  int code;

  @JsonIgnore HttpStatus status;

  String error;
  String description;

  AbstractNoughtsCrossesResponse(int code, String error) {
    this(code, error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  AbstractNoughtsCrossesResponse(int code, String error, HttpStatus status) {
    this(code, error, status, null);
  }

  AbstractNoughtsCrossesResponse(int code, String error, String description) {
    this(code, error, HttpStatus.INTERNAL_SERVER_ERROR, description);
  }

  AbstractNoughtsCrossesResponse(int code, String error, HttpStatus status, String description) {
    this.code = code;
    this.error = error;
    this.description = description;
    this.status = status;
  }

  public int getCode() {
    return code;
  }

  public String getError() {
    return error;
  }

  public String getDescription() {
    return description;
  }

  public AbstractNoughtsCrossesResponse setError(String error) {
    this.error = error;
    return this;
  }

  public abstract AbstractNoughtsCrossesResponse withDescription(String description);

  public abstract AbstractNoughtsCrossesResponse withStatus(HttpStatus status);

  public abstract AbstractNoughtsCrossesResponse withError(String error);

  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getClass().getName());
    builder.append(" [code=");
    builder.append(code);
    builder.append(", result=");
    builder.append(error);
    if (StringUtils.hasText(description)) {
      builder.append(", description=");
      builder.append(description);
    }

    builder.append(", status=");
    builder.append(status);

    builder.append("]");
    return builder.toString();
  }
}
