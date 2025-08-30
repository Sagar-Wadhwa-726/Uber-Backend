package com.sagarw.project.uber.uberApp.strategies.Impl;

import com.sagarw.project.uber.uberApp.dto.RideRequestDto;
import com.sagarw.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import org.springframework.stereotype.Service;

@Service
public class RideFareCalculationSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequestDto rideRequestDto) {
        return 0;
    }
}
