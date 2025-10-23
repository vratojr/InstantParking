package com.example.parking.infra.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.application.model.out.ParkingDtoOut;
import com.example.parking.application.usecase.FindParkingsNearToCoordinatesUC;

@RestController
@RequestMapping("/api/")
public class ParkingController {

  private final FindParkingsNearToCoordinatesUC findParkingsNearToUC;

  public ParkingController(FindParkingsNearToCoordinatesUC findParkingsNearToUC) {
    this.findParkingsNearToUC = findParkingsNearToUC;
  }

  @GetMapping("parkings")
  public CompletableFuture<List<ParkingDtoOut>> findParkings(
      @RequestParam("lat") double lat,
      @RequestParam("lng") double lng) {
    return findParkingsNearToUC.execute(lat, lng);
  }

}
