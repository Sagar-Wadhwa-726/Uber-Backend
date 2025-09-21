package com.sagarw.project.uber.uberApp.strategies;

import com.sagarw.project.uber.uberApp.strategies.Impl.DriverMatchingHighestRatedDriverStrategy;
import com.sagarw.project.uber.uberApp.strategies.Impl.DriverMatchingNearestDriverStrategy;
import com.sagarw.project.uber.uberApp.strategies.Impl.RideFareCalculationDefaultFareCalculationStrategy;
import com.sagarw.project.uber.uberApp.strategies.Impl.RideFareCalculationSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/*Responsibility of the strategy manager to return any of the strategy*/
@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareCalculationSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RideFareCalculationDefaultFareCalculationStrategy defaultFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating) {
        if (riderRating >= 4.8)
            return highestRatedDriverStrategy;
        return nearestDriverStrategy;
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if (isSurgeTime)
            return surgePricingFareCalculationStrategy;
        return defaultFareCalculationStrategy;
    }
}