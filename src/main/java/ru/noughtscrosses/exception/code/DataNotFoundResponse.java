package ru.noughtscrosses.exception.code;

import org.springframework.http.HttpStatus;

public class DataNotFoundResponse extends AbstractNoughtsCrossesResponse {

  private static final HttpStatus NOT_FOUND_STATUS = HttpStatus.NOT_FOUND;
  static final int MIN_CODE = 3000;
  static final int MAX_CODE = 3999;

  public static final DataNotFoundResponse USER_NOT_FOUND =
      new DataNotFoundResponse(3000, "User not found");

  public static final DataNotFoundResponse ROUND_NOT_FOUND =
      new DataNotFoundResponse(3001, "Round not found");

  DataNotFoundResponse(int code, String error) {
    super(code, error, NOT_FOUND_STATUS);
  }

  @Override
  public AbstractNoughtsCrossesResponse withDescription(String description) {
    AbstractNoughtsCrossesResponse code = new DataNotFoundResponse(this.code, this.error);
    code.status = this.status;
    code.description = description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withStatus(HttpStatus status) {
    AbstractNoughtsCrossesResponse code = new DataNotFoundResponse(this.code, this.error);
    code.status = status;
    code.description = this.description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withError(String error) {
    AbstractNoughtsCrossesResponse code = new DataNotFoundResponse(this.code, error);
    code.status = this.status;
    code.description = this.description;
    return code;
  }
}
