package com.example.parking.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.parking.domain.ParkingProvider;

/**
 * Here we use the strategy pattern to get the correct client given a provider.
 * We could use the friendly field 'name' of the provider to identify the provider. 
 * We should assure that the 'name' is unique in the db.
 * Will throw exception if the provider is not found.
 */
@Service
public class ParkingProviderApiClientFactory {

  private final List<ParkingProviderApiClient> clients;

  public ParkingProviderApiClientFactory(List<ParkingProviderApiClient> clients) {
    this.clients = clients;
  }

  public Optional<ParkingProviderApiClient> getConverter(ParkingProvider parkingProvider) {
    return Optional
        .of(clients.stream().filter(client -> client.isProviderSupported(parkingProvider)).findFirst().get());
  }
}
