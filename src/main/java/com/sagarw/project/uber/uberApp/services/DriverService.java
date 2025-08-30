package com.sagarw.project.uber.uberApp.services;

import com.sagarw.project.uber.uberApp.dto.DriverDto;
import com.sagarw.project.uber.uberApp.dto.RideDto;
import com.sagarw.project.uber.uberApp.dto.RiderDto;

import java.util.List;

public interface DriverService {

    RideDto acceptRide(Long rideId);

    // Not passing driver ID, calculated automatically by Spring Security Context Holder
    // Check if this ride belongs to this driver only then they will be able to cancel
    // the ride
    RideDto cancelRide(Long rideId);

    RideDto startRide(Long rideId);

    RideDto endRide(Long rideId);

    RiderDto rateRider(Long rideId, Integer rating);

    DriverDto getMyProfile();

    List<RideDto> getAllMyRides();
}

