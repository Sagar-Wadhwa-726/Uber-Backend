package com.sagarw.project.uber.uberApp.strategies;

import com.sagarw.project.uber.uberApp.dto.RideRequestDto;

public interface RideFareCalculationStrategy {
    double calculateFare(RideRequestDto rideRequestDto);
}
