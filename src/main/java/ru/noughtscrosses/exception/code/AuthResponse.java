package ru.noughtscrosses.exception.code;

import org.springframework.http.HttpStatus;

public class AuthResponse extends AbstractNoughtsCrossesResponse {

  private static final HttpStatus UNAUTHORIZED_STATUS = HttpStatus.UNAUTHORIZED;
  static final int MIN_CODE = 2000;
  static final int MAX_CODE = 2999;

  public static final AuthResponse UNKNOWN_CLIENT_ID = new AuthResponse(2000, "Unknown client_id");
  public static final AuthResponse REFRESH_TOKEN_REQUIRED =
      new AuthResponse(2001, "refresh_token is required", HttpStatus.BAD_REQUEST);
  public static final AuthResponse INVALID_USER_NAME_OR_PASSWORD =
      new AuthResponse(2002, "Invalid User Name or Password specified", HttpStatus.BAD_REQUEST);
  public static final AuthResponse REFRESH_TOKEN_INVALID =
      new AuthResponse(2003, "Invalid refresh_token", HttpStatus.BAD_REQUEST);
  public static final AuthResponse GRANT_TYPE_INVALID =
      new AuthResponse(2004, "Invalid grant_type", HttpStatus.BAD_REQUEST);
  public static final AuthResponse TOKEN_INVALID = new AuthResponse(2005, "Invalid token");
  public static final AuthResponse TOKEN_EXPIRED = new AuthResponse(2006, "Token expired");
  public static final AuthResponse AUTH_NOT_PRESENT =
      new AuthResponse(2007, "Authorization is not present");
  public static final AuthResponse AUTH_INVALID =
      new AuthResponse(2008, "Authorization type bearer only supported");
  public static final AuthResponse REFRESH_TOKEN_EXPIRED =
      new AuthResponse(2009, "Refresh token expired");
  public static final AuthResponse PATH_NOT_ALLOWED =
      new AuthResponse(2010, "Specified path is not allowed");
  public static final AuthResponse NO_ALLOWED_SCOPES =
      new AuthResponse(2011, "No allowed scopes found", HttpStatus.INTERNAL_SERVER_ERROR);
  public static final AuthResponse SCOPE_NOT_ALLOWED =
      new AuthResponse(2012, "Specified scope is not allowed");
  public static final AuthResponse USER_NOT_ACTIVE = new AuthResponse(2012, "User is not active");
  public static final AuthResponse INVALID_TOKEN_TYPE =
      new AuthResponse(2013, "Invalid token type", HttpStatus.BAD_REQUEST);

  AuthResponse(int code, String error) {
    super(code, error, UNAUTHORIZED_STATUS);
  }

  AuthResponse(int code, String error, HttpStatus status) {
    super(code, error, status);
  }

  @Override
  public AbstractNoughtsCrossesResponse withDescription(String description) {
    AuthResponse code = new AuthResponse(this.code, this.error, this.status);
    code.description = description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withStatus(HttpStatus status) {
    AuthResponse code = new AuthResponse(this.code, this.error, status);
    code.description = this.description;
    return code;
  }

  @Override
  public AbstractNoughtsCrossesResponse withError(String error) {
    AuthResponse code = new AuthResponse(this.code, error, this.status);
    code.description = this.description;
    return code;
  }
}
