package com.cez.api.v1.report;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudezReportingApiApplication
{
  private List<String> allowedHeaders = new ArrayList<>();

  public static void main(String[] args)
  {
    SpringApplication.run(CloudezReportingApiApplication.class, args);

  }

//  @Bean
//  public FilterRegistrationBean<CorsFilter> simpleCorsFilter()
//  {
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    CorsConfiguration config = new CorsConfiguration();
//
//    allowedHeaders.add("Access-Control-Allow-Origin");
//    allowedHeaders.add("CSRF-Token");
//    allowedHeaders.add("X-Requested-By");
//    allowedHeaders.add("Authorization");
//    allowedHeaders.add("Content-Type");
//    allowedHeaders.add("Referer");
//    allowedHeaders.add("User-Agent");
//
//    // config.setAllowCredentials(true);
//
//    // config.setAllowedOriginPatterns(Collections.singletonList("*"));
//
//    config.setAllowedOrigins(Collections.singletonList("*"));
//
//    config.setAllowedHeaders(allowedHeaders);
//
//    config.setAllowedMethods(Collections.singletonList("*"));
//
//    config.setMaxAge(1800L);
//
//    source.registerCorsConfiguration("/**", config);
//    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
//    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    return bean;
//  }

}
