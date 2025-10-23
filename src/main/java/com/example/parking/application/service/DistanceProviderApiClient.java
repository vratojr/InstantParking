package com.example.parking.application.service;

import java.util.concurrent.CompletableFuture;

/**
 * Given a pair of points, we return the distance between them.
 * This will let us hide the complexity of calling, for example, google maps api.
 */
public interface DistanceProviderApiClient {

  public CompletableFuture<Integer> getDistanceInMeters(double lat1, double lng1, double lat2, double lng2);

}
