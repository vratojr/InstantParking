package com.example.parking.application.gateway;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.example.parking.domain.ParkingProvider;

public interface ParkingProviderGateway {

  /**
   * @param lat
   * @param lng
   * @return the nearest provider (if any) to the given coordinates.
   */
  public CompletableFuture<Optional<ParkingProvider>> getNearestProvider(double lat, double lng);

}
