package com.example.parking.domain;

import com.example.parking.application.service.ParkingProviderApiClient.ParkingProviderName;

import lombok.Data;

/**
 * A ParkingProvider represents an API service that provides parking data for a given area.
 * This area is defined by a center (identified by lat and lng) and a range (in km).
 * In order to improve the search performance, the area is also defined by its bounding box (minlat, minlng, maxlat, maxlng).
 * NOTE: I decided to use double, but BigDecimal should be considered if the precision is important or we are going to perform some operations on the values.
 */
@Data
public class ParkingProvider {

  private int id;

  private ParkingProviderName name;

  // URL of the API to fetch the data
  private String apiUrl;

  // Latitude of the center of the area
  private double lat;

  // Longitude of the center of the area
  private double lng;

  // Range of the area in km
  private double range_km;

  // Minimum latitude of the bounding box
  private double minLat;

  // Minimum longitude of the bounding box
  private double minLng;

  // Maximum latitude of the bounding box
  private double maxLat;

  // Maximum longitude of the bounding box
  private double maxLng;
}
