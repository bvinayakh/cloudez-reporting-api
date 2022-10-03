package com.cez.api.v1.report;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reporting/public")
public class PublicEndpointsController
{
  @GetMapping("/ping")
  String ping()
  {
    return ("cloudez-reporting-api");
  }
}
