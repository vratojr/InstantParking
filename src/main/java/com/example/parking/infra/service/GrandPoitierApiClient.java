package com.example.parking.infra.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.application.service.ParkingProviderApiClient;
import com.example.parking.domain.ParkingProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GrandPoitierApiClient implements ParkingProviderApiClient {

  private final RestClient restClient;

  private static record GrandPoitierResponse(int total, List<GrandPoitierRecord> results) {
  }

  private static record GrandPoitierRecord(String _geopoint, Integer Capacite, String Nom, Integer Places, Integer Id) {
  }

  public GrandPoitierApiClient(@Qualifier("parkingProviderRestClientBuilder") RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.build();
  }

  @Override
  public boolean isProviderSupported(ParkingProvider provider) {
    return provider.getName() == ParkingProviderName.GrandPoitiers;
  }

  @Override
  @Async
  public CompletableFuture<List<ParkingDtoOut>> fetchParkings(ParkingProvider provider) {

    try {
      GrandPoitierResponse response = restClient.get()
          .uri(provider.getApiUrl())
          .retrieve()
          .body(GrandPoitierResponse.class);

      if (response == null || response.results() == null) {
        return CompletableFuture.completedFuture(List.of());
      }

      return CompletableFuture.completedFuture(convertFormat(response));
    }
    catch (Exception e) {
      // TODO proper handling to be implemented
      log.warn("Error fetching parkings from {}", provider.getApiUrl(), e);
      return CompletableFuture.failedFuture(e);
    }
  }

  private List<ParkingDtoOut> convertFormat(GrandPoitierResponse response) {
    return response.results().stream()
        .map(record -> {
          ParkingDtoOut dto = new ParkingDtoOut();
          dto.setId(record.Id());
          dto.setName(record.Nom());
          dto.setAvailablePlaces(record.Places());
          dto.setCapacity(record.Capacite());
          setLatLon(dto, record);
          return dto;
        })
        .filter(dto -> dto.getLat() != 0 && dto.getLng() != 0)
        .collect(Collectors.toList());
  }

  private void setLatLon(ParkingDtoOut dto, GrandPoitierRecord rec) {

    if (rec._geopoint() == null || rec._geopoint().isEmpty()) {
      // Log error and return null, it will be discarded later
      return;
    }

    String[] latLon = rec._geopoint().split(",");

    if (latLon.length != 2) {
      // Log error and return null, it will be discarded later
      return;
    }
    dto.setLat(Double.parseDouble(latLon[0]));
    dto.setLng(Double.parseDouble(latLon[1]));
  }

}
