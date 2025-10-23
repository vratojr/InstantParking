package com.example.parking.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.parking.application.gateway.ParkingProviderGateway;
import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.application.service.DistanceProviderApiClient;
import com.example.parking.application.service.ParkingProviderApiClient;
import com.example.parking.application.service.ParkingProviderApiClientFactory;
import com.example.parking.domain.ParkingProvider;

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
public class FindParkingsNearToCoordinatesUC {

  private final ParkingProviderGateway parkingProviderGateway;

  private final ParkingProviderApiClientFactory apiConverterRepo;

  private final DistanceProviderApiClient distanceProviderApiClient;

  public FindParkingsNearToCoordinatesUC(ParkingProviderGateway parkingProviderGateway,
      ParkingProviderApiClientFactory parkingProviderApiConverterRepository,
      DistanceProviderApiClient distanceProviderApiClient) {

    this.parkingProviderGateway = parkingProviderGateway;
    this.apiConverterRepo = parkingProviderApiConverterRepository;
    this.distanceProviderApiClient = distanceProviderApiClient;
  }

  public CompletableFuture<List<ParkingDtoOut>> execute(double lat, double lng) {
    return parkingProviderGateway.getNearestProvider(lat, lng)
        .thenCompose(providerOpt -> providerOpt
            .map(provider -> fetchParkingListFromProvider(provider, lat, lng))
            .orElse(CompletableFuture.completedFuture(List.of())));
  }

  private CompletableFuture<List<ParkingDtoOut>> fetchParkingListFromProvider(ParkingProvider provider, double lat,
      double lng) {

    Optional<ParkingProviderApiClient> apiClient = apiConverterRepo.getConverter(provider);
    return apiClient
        .map(client -> client.fetchParkings(provider).thenCompose(parkings -> setParkingsDistance(parkings, lat, lng)))
        .orElse(CompletableFuture.completedFuture(List.of()));
  }

  private CompletableFuture<List<ParkingDtoOut>> setParkingsDistance(List<ParkingDtoOut> parkings, double lat,
      double lng) {

    List<CompletableFuture<ParkingDtoOut>> parkingFutures = parkings.stream()
        .map(parking -> distanceProviderApiClient
            .getDistanceInMeters(lat, lng, parking.getLat(), parking.getLng())
            .thenApply(distance -> {
              parking.setDistance_m(distance);
              return parking;
            }))
        .collect(Collectors.toList());

    return combineAndSortParkings(parkingFutures);
  }

  private CompletableFuture<List<ParkingDtoOut>> combineAndSortParkings(
      List<CompletableFuture<ParkingDtoOut>> parkingFutures) {

    return CompletableFuture.allOf(parkingFutures.toArray(new CompletableFuture[0]))
        .thenApply(v -> parkingFutures.stream()
            .map(CompletableFuture::join)
            .sorted((p1, p2) -> p1.getDistance_m() - p2.getDistance_m())
            .collect(Collectors.toList()));
  }

}
