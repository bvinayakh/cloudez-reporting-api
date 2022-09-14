package com.cez.api.v1.report;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
  private String PUBLIC_ENDPOINT = "/public/**";
  private String PROTECTED_ENDPOINT = "/protected/**";

  @Value("${spring.security.debug:false}")
  private boolean securityDebug;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception
  {
    return http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).authorizeRequests(auth -> {
      auth.antMatchers(PUBLIC_ENDPOINT).permitAll();
      auth.antMatchers(PROTECTED_ENDPOINT).permitAll();
    }).httpBasic(Customizer.withDefaults()).build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource()
  {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
