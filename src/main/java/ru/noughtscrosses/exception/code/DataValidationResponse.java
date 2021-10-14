package ru.noughtscrosses.exception.code;

import org.springframework.http.HttpStatus;

public class DataValidationResponse extends AbstractNoughtsCrossesResponse {

  private static final HttpStatus BAD_STATUS = HttpStatus.BAD_REQUEST;
  static final int MIN_CODE = 4000;
  static final int MAX_CODE = 4999;

  public static final DataValidationResponse ROUND_IS_INACTIVE =
      new DataValidationResponse(4000, "Round is inactive");

  DataValidationResponse(int code, String error) {
    super(code, error, BAD_STATUS);
  }

  @Override
  public AbstractNoughtsCrossesResponse withDescription(String description) {
    AbstractNoughtsCrossesResponse code = new DataValidationResponse(this.code, this.error);
    code.status = this.status;
    code.description = description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withStatus(HttpStatus status) {
    AbstractNoughtsCrossesResponse code = new DataValidationResponse(this.code, this.error);
    code.status = status;
    code.description = this.description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withError(String error) {
    AbstractNoughtsCrossesResponse code = new DataValidationResponse(this.code, error);
    code.status = this.status;
    code.description = this.description;
    return code;
  }
}
