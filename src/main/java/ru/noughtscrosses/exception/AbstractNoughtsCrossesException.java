package ru.noughtscrosses.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.noughtscrosses.exception.code.INoughtsCrossesResponse;

public abstract class AbstractNoughtsCrossesException extends RuntimeException {

  private static final long serialVersionUID = -333997923236342035L;

  @JsonProperty("result")
  protected INoughtsCrossesResponse result;

  public AbstractNoughtsCrossesException(INoughtsCrossesResponse error) {
    this.result = error;
  }

  public INoughtsCrossesResponse getResult() {
    return result;
  }

  public void setResult(INoughtsCrossesResponse error) {
    this.result = error;
  }

  @Override
  public String getMessage() {
    return result != null ? result.toString() : "";
  }

  @Override
  public String getLocalizedMessage() {
    return result != null ? result.toString() : "";
  }
}
