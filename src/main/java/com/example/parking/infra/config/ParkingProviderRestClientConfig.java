package com.example.parking.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ParkingProviderRestClientConfig {

  @Bean
  // TODO Client can be configured more if necessary
  public RestClient.Builder parkingProviderRestClientBuilder() {
    return RestClient.builder();
  }

}