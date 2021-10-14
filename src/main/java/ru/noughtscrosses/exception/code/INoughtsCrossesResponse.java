package ru.noughtscrosses.exception.code;

import org.springframework.http.HttpStatus;

public interface INoughtsCrossesResponse {

  /**
   * Short code which represents a business issue.
   *
   * @return int business code.
   */
  int getCode();

  String getError();

  /**
   * Full description of the business code.
   *
   * @return String description.
   */
  String getDescription();

  HttpStatus getStatus();

  public INoughtsCrossesResponse withDescription(String description);

  public INoughtsCrossesResponse withStatus(HttpStatus status);

  public INoughtsCrossesResponse withError(String error);
}
