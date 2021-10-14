package ru.noughtscrosses.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.AuthResponse;
import ru.noughtscrosses.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthJwtInterceptor implements HandlerInterceptor {
  private static final Logger log = LoggerFactory.getLogger(AuthJwtInterceptor.class);

  private final AuthService authService;
  private final Set<String> publicApis = new HashSet<>();

  public AuthJwtInterceptor(AuthService authService) {
    this.authService = authService;

    publicApis.add("token");
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    authService.getClaims().set(null);

    if (publicApis.contains(normalize(request.getRequestURI()))) {
      return true;
    }

    try {
      authService.parse(request);
      return true;
    } catch (ExpiredJwtException e) {
      throw new ApiException(AuthResponse.TOKEN_EXPIRED);
    }
  }

  private static String normalize(String path) {
    return path.replaceAll("^\\/{1,}|\\/{1,}$", "");
  }
}
