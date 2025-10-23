package com.example.parking.infra.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.application.service.ParkingProviderApiClient.ParkingProviderName;
import com.example.parking.domain.ParkingProvider;
import com.example.parking.infra.config.ParkingProviderRestClientConfig;

@RestClientTest
@Import({ ParkingProviderRestClientConfig.class })
class GrandPoitierApiClientTest {

  private GrandPoitierApiClient sut;

  @Autowired
  private RestClient.Builder restClientBuilder;

  private MockRestServiceServer mockServer;

  private ParkingProvider provider;

  @BeforeEach
  void setUp() {
    // Reset the mock server
    mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();

    sut = new GrandPoitierApiClient(restClientBuilder);

    provider = new ParkingProvider();
    provider.setName(ParkingProviderName.GrandPoitiers);
    provider.setApiUrl("https://api.example.com/parkings");
  }

  @Test
  void fetchParkings_shouldReturnParkings_whenApiReturnsData() throws Exception {

    // Arrange
    String jsonResponse = """
            {
           "total":8,
           "results":[
              {
                 "infos_parkingsgeo_point":"46.58383455409422, 0.33779491061805567",
                 "_rand":419552,
                 "_geopoint":"46.58383455409422, 0.33779491061805567",
                 "Capacite":320,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:50+02:00",
                 "_i":32569679781007,
                 "Id":3,
                 "Nom":"THEATRE",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":85.625,
                 "Places":46,
                 "_score":null,
                 "_id":"26a6ce94f8d2ece150a6caa06e71f7d032764e0638a47b58e868257bb3dea578"
              },
              {
                 "infos_parkingsgeo_point":"46.58595804860371, 0.3512954265806957",
                 "_rand":141094,
                 "_geopoint":"46.58595804860371, 0.3512954265806957",
                 "Capacite":228,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:50+02:00",
                 "_i":32569679781006,
                 "Id":12,
                 "Nom":"PALAIS DE JUSTICE",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":49.122807017543856,
                 "Places":116,
                 "_score":null,
                 "_id":"115175dafa22e3159003105401f27980690a156ea5190aa769e33a7e1394b09e"
              },
              {
                 "infos_parkingsgeo_point":"46.5793235337795, 0.3385507838016221",
                 "_rand":328545,
                 "_geopoint":"46.5793235337795, 0.3385507838016221",
                 "Capacite":625,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:50+02:00",
                 "_i":32569679781005,
                 "Id":2,
                 "Nom":"HOTEL DE VILLE",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":55.04,
                 "Places":281,
                 "_score":null,
                 "_id":"68a90152c04505f5f2f397b31c13447056085e8176be9daa43b182b9d731b2e4"
              },
              {
                 "infos_parkingsgeo_point":"46.58358353103216, 0.3348348830917244",
                 "_rand":899051,
                 "_geopoint":"46.58358353103216, 0.3348348830917244",
                 "Capacite":640,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:49+02:00",
                 "_i":32569679781004,
                 "Id":9,
                 "Nom":"GARE TOUMAI",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":62.65625,
                 "Places":239,
                 "_score":null,
                 "_id":"8d81fb9d8d154c5e6d6840b806b2c52e6285fb84c30152a00a09619f41b70523"
              },
              {
                 "_rand":927291,
                 "Capacite":480,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:49+02:00",
                 "_i":32569679781003,
                 "Id":5,
                 "Nom":"GARE EFFIA",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":51.24999999999999,
                 "Places":234,
                 "_score":null,
                 "_id":"5e3322c682b4e46a737ec3a18be48fc20ba87309ae7f942cef08eb51f2a6537e"
              },
              {
                 "_rand":603898,
                 "Capacite":290,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:49+02:00",
                 "_i":32569679781002,
                 "Id":6,
                 "Nom":"CORDELIERS",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":20.344827586206893,
                 "Places":231,
                 "_score":null,
                 "_id":"0e71c9d61a801b86235274dfab92b15dfb859514f351bf1627e1bc07bca5b60c"
              },
              {
                 "infos_parkingsgeo_point":"46.57505317559496, 0.337126307915689",
                 "_rand":941209,
                 "_geopoint":"46.57505317559496, 0.337126307915689",
                 "Capacite":665,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:48+02:00",
                 "_i":32569679781001,
                 "Id":0,
                 "Nom":"BLOSSAC TISON",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":43.15789473684211,
                 "Places":378,
                 "_score":null,
                 "_id":"a37bbbb0764cc5bf8d8c251cb1c95e3d2e142934792916ccb362ceb5902d9f0b"
              },
              {
                 "infos_parkingsgeo_point":"46.583793004495156, 0.3349825350533068",
                 "_rand":794686,
                 "_geopoint":"46.5837930044951, 0.33498253505330",
                 "Capacite":137,
                 "Dernière_mise_à_jour_Base":"2025-10-23T10:35:48+02:00",
                 "_i":32569679781000,
                 "Id":11,
                 "Nom":"ARRET MINUTE",
                 "_updatedAt":"2025-10-23T08:35:10.134Z",
                 "taux_doccupation":57.664233576642346,
                 "Places":58,
                 "_score":null,
                 "_id":"71447c96d68bdc65906c5092de19f2d27634b0fb59a86baac726a961b4de4228"
              }
           ]
        }
        """;

    List<ParkingDtoOut> expectedParkings = List.of(
        createParkingDto(3, 46.58383455409422, 0.33779491061805567, "THEATRE", 320, 46),
        createParkingDto(12, 46.58595804860371, 0.3512954265806957, "PALAIS DE JUSTICE", 228, 116),
        createParkingDto(2, 46.5793235337795, 0.3385507838016221, "HOTEL DE VILLE", 625, 281),
        createParkingDto(9, 46.58358353103216, 0.3348348830917244, "GARE TOUMAI", 640, 239),
        createParkingDto(0, 46.57505317559496, 0.337126307915689, "BLOSSAC TISON", 665, 378),
        createParkingDto(11, 46.5837930044951, 0.33498253505330, "ARRET MINUTE", 137, 58));

    mockServer.expect(requestTo(provider.getApiUrl()))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

    // Act
    var future = sut.fetchParkings(provider);
    List<ParkingDtoOut> result = future.get();

    // Assert
    assertThat(result)
        .as("The parkings are the expected ones. Those withouth coordinates are ignored.")
        .usingRecursiveFieldByFieldElementComparator().containsAll(expectedParkings);

    mockServer.verify();
  }

  @Test
  void isProviderSupported_shouldReturnTrueForGrandPoitiers() {
    assertTrue(sut.isProviderSupported(provider));
  }

  private ParkingDtoOut createParkingDto(int id, double lat, double lng, String name, int capacity,
      int availablePlaces) {
    ParkingDtoOut dto = new ParkingDtoOut();
    dto.setId(id);
    dto.setLat(lat);
    dto.setLng(lng);
    dto.setName(name);
    dto.setCapacity(capacity);
    dto.setAvailablePlaces(availablePlaces);
    return dto;
  }
}
