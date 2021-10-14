package ru.noughtscrosses.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.noughtscrosses.interceptor.AuthJwtInterceptor;
import ru.noughtscrosses.service.AuthService;

@Configuration
//@Order(1)
public class InterceptorConfig implements WebMvcConfigurer {

  private final AuthService authService;

  public InterceptorConfig(AuthService authService) {
    this.authService = authService;
  }

  @Bean
  public AuthJwtInterceptor authJwtInterceptor(AuthService authService) {
    return new AuthJwtInterceptor(authService);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authJwtInterceptor(authService));
  }
}
