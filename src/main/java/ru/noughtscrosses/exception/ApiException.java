package ru.noughtscrosses.exception;

import ru.noughtscrosses.exception.code.INoughtsCrossesResponse;

import java.text.MessageFormat;

public class ApiException extends AbstractNoughtsCrossesException {

  private static final long serialVersionUID = -8417956452140526704L;

  public ApiException(INoughtsCrossesResponse error) {
    super(error);
  }

  public ApiException(INoughtsCrossesResponse error, Object... params) {
    super(error);
    if (params != null) {
      error = error.withError(MessageFormat.format(error.getError(), params));
      setResult(error);
    }
  }
}
