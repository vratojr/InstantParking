package com.example.parking.infra.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.example.parking.application.service.DistanceProviderApiClient;

/**
 * This is just a stub for now.
 */
@Service
public class GoogleDistanceProviderApiClient implements DistanceProviderApiClient {

  @Override
  public CompletableFuture<Integer> getDistanceInMeters(double lat1, double lng1, double lat2, double lng2) {
    return CompletableFuture.completedFuture(10);
  }

}
