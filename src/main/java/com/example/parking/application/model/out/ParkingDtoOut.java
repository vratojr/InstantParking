package com.example.parking.application.model.out;

import lombok.Data;

@Data
public class ParkingDtoOut {

  private int id;

  private double lat;

  private double lng;

  private Integer availablePlaces;

  private Integer capacity;

  private Integer distance_m;

  private String name;

}
