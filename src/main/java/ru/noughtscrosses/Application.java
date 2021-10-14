package ru.noughtscrosses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

//  @Bean
//  ExecutorService executorService() {
//    return Executors.newWorkStealingPool();
//  }
//
//  @Bean RestTemplate restTemplate() {
//    return new RestTemplate(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
//  }
}
