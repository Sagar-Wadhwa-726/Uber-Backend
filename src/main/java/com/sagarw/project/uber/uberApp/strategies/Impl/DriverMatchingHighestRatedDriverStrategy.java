package com.sagarw.project.uber.uberApp.strategies.Impl;

import com.sagarw.project.uber.uberApp.dto.RideRequestDto;
import com.sagarw.project.uber.uberApp.entities.Driver;
import com.sagarw.project.uber.uberApp.strategies.DriverMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Driver> findMatchingDriver(RideRequestDto rideRequestDto) {
        return List.of();
    }
}
