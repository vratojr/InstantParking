package com.example.parking.application.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.domain.ParkingProvider;

public interface ParkingProviderApiClient {

  /**
   * We could have used a string, but an enum is more type-safe and reduces the risks of creating two providers with the same name.
   */
  static public enum ParkingProviderName {
    GrandPoitiers
  }

  CompletableFuture<List<ParkingDtoOut>> fetchParkings(ParkingProvider parkingProvider);

  boolean isProviderSupported(ParkingProvider parkingProvider);

}
