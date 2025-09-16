package com.sagarw.project.uber.uberApp.services;

import com.sagarw.project.uber.uberApp.dto.RideRequestDto;
import com.sagarw.project.uber.uberApp.entities.Driver;
import com.sagarw.project.uber.uberApp.entities.Ride;
import com.sagarw.project.uber.uberApp.entities.RideRequest;
import com.sagarw.project.uber.uberApp.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

// This service is not allowed to be called directly by the entity, own service like Rider / Driver
// service can call this Ride Service, which is used to manage all the rides.
public interface RideService {
    Ride getRideById(Long rideId);
    void matchWithDriver(RideRequestDto rideRequestDto);
    Ride createNewRide(RideRequest rideRequest, Driver driver);
    Ride updateRideStatus(Ride ride, RideStatus rideStatus);
    Page<Ride> getAllRidesOfRider(Long riderId, PageRequest pageRequest);
    Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest);

}
