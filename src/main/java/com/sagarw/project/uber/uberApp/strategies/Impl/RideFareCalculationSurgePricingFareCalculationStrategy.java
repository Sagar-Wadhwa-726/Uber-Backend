package com.sagarw.project.uber.uberApp.strategies.Impl;

import com.sagarw.project.uber.uberApp.entities.RideRequest;
import com.sagarw.project.uber.uberApp.services.DistanceService;
import com.sagarw.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareCalculationSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    /*surge factor can be decided from another API call, like weather API to tell whether it is raining at a particular location or not, if raining then apply the surge*/
    private static final double SURGE_FACTOR = 2;
    private final DistanceService distanceService;

    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(),
                rideRequest.getDropOffLocation());

        /*We can use another API to get the multiplier dynamically from third party API*/
        return distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
    }
}
