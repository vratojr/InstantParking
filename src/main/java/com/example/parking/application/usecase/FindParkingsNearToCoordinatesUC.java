package com.example.parking.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.parking.application.exceptions.ApplicationError;
import com.example.parking.application.gateway.ParkingProviderGateway;
import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.application.service.DistanceProviderApiClient;
import com.example.parking.application.service.ParkingProviderApiClient;
import com.example.parking.application.service.ParkingProviderApiClientFactory;
import com.example.parking.domain.ParkingProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * Use case to find the nearest parking provider to a given location.
 * Sequence of operations:
 * 1. Get the nearest parking provider to the given location
 * 2. Pick the correct ProviderApiClient
 * 3. Fetch the parkings from the provider using the client
 * 4. Calculate the distance for each parking
 * 5. Sort the parkings by distance
 * 6. Return the sorted list of parkings
 */
@Service
@Slf4j
public class FindParkingsNearToCoordinatesUC {

  private final ParkingProviderGateway parkingProviderGtw;

  private final ParkingProviderApiClientFactory apiClientFactory;

  private final DistanceProviderApiClient distanceProviderApiClient;

  public FindParkingsNearToCoordinatesUC(ParkingProviderGateway parkingProviderGateway,
      ParkingProviderApiClientFactory parkingProviderApiConverterRepository,
      DistanceProviderApiClient distanceProviderApiClient) {

    this.parkingProviderGtw = parkingProviderGateway;
    this.apiClientFactory = parkingProviderApiConverterRepository;
    this.distanceProviderApiClient = distanceProviderApiClient;
  }

  public CompletableFuture<List<ParkingDtoOut>> execute(double lat, double lng) {

    return parkingProviderGtw.getNearestProvider(lat, lng)
        .thenCompose(providerOpt -> {

          if (providerOpt.isEmpty()) {
            throw new ApplicationError("No provider nearby");
          }

          return fetchParkingListFromProvider(providerOpt.get(), lat, lng);
        });
  }

  private CompletableFuture<List<ParkingDtoOut>> fetchParkingListFromProvider(ParkingProvider provider, double lat,
      double lng) {

    Optional<ParkingProviderApiClient> apiClient = apiClientFactory.getConverter(provider);

    if (apiClient.isEmpty()) {
      throw new ApplicationError("No api client found for provider " + provider.getName());
    }

    return apiClient.get()
        .fetchParkings(provider)
        .exceptionally(e -> {
          throw new ApplicationError("No parking available");
        })
        .thenCompose(parkings -> setParkingsDistance(parkings, lat, lng));
  }

  private CompletableFuture<List<ParkingDtoOut>> setParkingsDistance(List<ParkingDtoOut> parkings, double lat,
      double lng) {

    List<CompletableFuture<Optional<ParkingDtoOut>>> parkingFutures = parkings.stream()// This could be parallelized
        .map(parking -> distanceProviderApiClient
            .getDistanceInMeters(lat, lng, parking.getLat(), parking.getLng())
            .thenApply(distance -> {
              parking.setDistance_m(distance);
              return Optional.of(parking);
            })
            .exceptionally(e -> {
              log.error("Error getting distance for parking {}", parking, e);
              // We are checking one parking at a time, we decide to skip the one in error
              return Optional.empty();
            }))
        .collect(Collectors.toList());

    return combineAndSortParkings(parkingFutures);
  }

  private CompletableFuture<List<ParkingDtoOut>> combineAndSortParkings(
      List<CompletableFuture<Optional<ParkingDtoOut>>> parkingFutures) {

    return CompletableFuture.allOf(parkingFutures.toArray(new CompletableFuture[0]))
        .thenApply(v -> parkingFutures.stream()
            .map(CompletableFuture::join)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted((p1, p2) -> p1.getDistance_m() - p2.getDistance_m())
            .collect(Collectors.toList()));
  }

}
