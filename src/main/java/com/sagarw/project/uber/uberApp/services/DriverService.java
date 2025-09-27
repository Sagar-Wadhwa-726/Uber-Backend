package com.sagarw.project.uber.uberApp.services;

import com.sagarw.project.uber.uberApp.dto.DriverDto;
import com.sagarw.project.uber.uberApp.dto.RideDto;
import com.sagarw.project.uber.uberApp.dto.RiderDto;
import com.sagarw.project.uber.uberApp.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {

    RideDto acceptRide(Long rideRequestId);

    // Not passing driver ID, calculated automatically by Spring Security Context Holder
    // Check if this ride belongs to this driver only then they will be able to cancel
    // the ride
    RideDto cancelRide(Long rideId);

    RideDto startRide(Long rideId, String otp);

    RideDto endRide(Long rideId);

    RiderDto rateRider(Long rideId, Integer rating);

    DriverDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriverAvailability(Driver driver, boolean available);
}

