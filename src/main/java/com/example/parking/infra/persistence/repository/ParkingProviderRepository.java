package com.example.parking.infra.persistence.repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.parking.application.gateway.ParkingProviderGateway;
import com.example.parking.application.service.ParkingProviderApiClient.ParkingProviderName;
import com.example.parking.domain.ParkingProvider;

/**
 * Fake repository implementation :) 
 * Here we could implement a search with a query like:
 * SELECT id, lat, lng, range_km, api_url, name
 * FROM parking_provider 
 * WHERE lat BETWEEN min_lat AND max_lat
 *   AND lng BETWEEN min_lng AND max_lng
 *   AND POW(lat - lat1, 2) + POW(lng - lng1, 2) <= POW(range_km, 2);
 * Or even better... :)
 * */
@Component
public class ParkingProviderRepository implements ParkingProviderGateway {

  private final ParkingProvider theProvider = new ParkingProvider();

  public ParkingProviderRepository() {
    theProvider.setId(1);
    theProvider.setLat(48.8566);
    theProvider.setLng(2.3522);
    theProvider.setRange_km(10);
    theProvider.setApiUrl(
        "https://data.grandpoitiers.fr/data-fair/api/v1/datasets/mobilites-stationnement-des-parkings-en-temps-reel/lines");
    theProvider.setName(ParkingProviderName.GrandPoitiers);
  }

  @Override
  @Async // This would be async if we were using a real database
  public CompletableFuture<Optional<ParkingProvider>> getNearestProvider(double lat, double lng) {
    return CompletableFuture.completedFuture(Optional.of(theProvider));
  }

}
