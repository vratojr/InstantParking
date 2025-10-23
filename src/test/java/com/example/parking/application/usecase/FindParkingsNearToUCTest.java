package com.example.parking.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.application.service.ParkingProviderApiClient;
import com.example.parking.application.service.ParkingProviderApiClientFactory;
import com.example.parking.domain.ParkingProvider;
import com.example.parking.infra.persistence.repository.ParkingProviderRepository;
import com.example.parking.infra.service.GoogleDistanceProviderApiClient;

/**
 * In a normal test, I would have stubbed all dependencies (expecially since they are part of the 'infra' package 
 * and this package should not have dependency on it). But since they are already implemented as stubs, I've stubbed only the real one :)
 * This test is not absolutely, in any way, to be considered comprehensive. I just set it up for me to speed up
 * dev and testing.
 */
@ExtendWith(MockitoExtension.class)
class FindParkingsNearToUCTest {

  private FindParkingsNearToCoordinatesUC sut;

  @Mock
  private ParkingProviderApiClientFactory apiClientRepo;

  @Mock
  private ParkingProviderApiClient parkingProviderApiClient;

  @BeforeEach
  void setUp() {
    sut = new FindParkingsNearToCoordinatesUC(
        new ParkingProviderRepository(),
        apiClientRepo,
        new GoogleDistanceProviderApiClient());

    // Install our stub apiclient
    when(apiClientRepo.getConverter(any(ParkingProvider.class)))
        .thenReturn(Optional.of(parkingProviderApiClient));
  }

  @Test
  void thatParkingsAreCorrectlyReturned() throws Exception {

    // Given
    List<ParkingDtoOut> expectedParkings = List.of(
        createParkingDto(1, 48.8550, 2.3515),
        createParkingDto(2, 48.8570, 2.3530));

    // Mock the API client to return test parkings
    when(parkingProviderApiClient.fetchParkings(any(ParkingProvider.class)))
        .thenReturn(CompletableFuture.completedFuture(expectedParkings));

    // When
    CompletableFuture<List<ParkingDtoOut>> resultFuture = sut.execute(1, 1);

    // Then
    List<ParkingDtoOut> result = resultFuture.get();
    assertThat(result)
        .as("The expected number of parking is returned.")
        .hasSize(2)
        .as("The parkings are the expected ones.")
        .usingRecursiveFieldByFieldElementComparator().containsAll(expectedParkings)
        .as("The parkings are sorted by distance.")
        .isSortedAccordingTo((p1, p2) -> Integer.compare(p1.getDistance_m(), p2.getDistance_m()))
        .as("Distance have been set by the distance provider")
        .extracting(d -> d.getDistance_m()).containsOnly(10, 10);
  }

  private ParkingDtoOut createParkingDto(int id, double lat, double lng) {
    ParkingDtoOut dto = new ParkingDtoOut();
    dto.setId(id);
    dto.setLat(lat);
    dto.setLng(lng);
    dto.setName("Parking " + id);
    return dto;
  }

}