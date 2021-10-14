package ru.noughtscrosses.exception.code;

import org.springframework.http.HttpStatus;

public class DbResponse extends AbstractNoughtsCrossesResponse {

  static final int MIN_CODE = 1000;
  static final int MAX_CODE = 1999;

  public static final DbResponse DATA_SELECT_ERROR =
      new DbResponse(1000, "Failed to select database");
  public static final DbResponse DATA_UPDATE_ERROR = new DbResponse(1001, "Unable to save data");

  DbResponse(int code, String error) {
    super(code, error);
  }

  @Override
  public AbstractNoughtsCrossesResponse withDescription(String description) {
    AbstractNoughtsCrossesResponse code = new DbResponse(this.code, this.error);
    code.status = this.status;
    code.description = description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withStatus(HttpStatus status) {
    AbstractNoughtsCrossesResponse code = new DbResponse(this.code, this.error);
    code.status = status;
    code.description = this.description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withError(String error) {
    AbstractNoughtsCrossesResponse code = new DbResponse(this.code, error);
    code.status = this.status;
    code.description = this.description;
    return code;
  }
}
