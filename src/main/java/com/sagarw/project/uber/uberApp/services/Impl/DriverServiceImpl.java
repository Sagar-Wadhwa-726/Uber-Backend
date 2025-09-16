package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.dto.DriverDto;
import com.sagarw.project.uber.uberApp.dto.RideDto;
import com.sagarw.project.uber.uberApp.dto.RiderDto;
import com.sagarw.project.uber.uberApp.entities.Driver;
import com.sagarw.project.uber.uberApp.entities.Ride;
import com.sagarw.project.uber.uberApp.entities.RideRequest;
import com.sagarw.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.sagarw.project.uber.uberApp.entities.enums.RideStatus;
import com.sagarw.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.sagarw.project.uber.uberApp.repositories.DriverRepository;
import com.sagarw.project.uber.uberApp.services.DriverService;
import com.sagarw.project.uber.uberApp.services.RideRequestService;
import com.sagarw.project.uber.uberApp.services.RideService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*One service can use another service, this is how it goes in microservices as well*/
// All the validation logic should be called first and then the business logic should be called
// Validation is also known as Sanitizing
@Service
@RequiredArgsConstructor

public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        // Check if the ride is already accepted or not
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("Ride Request can't be accepted, status is : " + rideRequest.getRideRequestStatus());
        }

        /*Check if current driver able to accept the ride or not*/
        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw new RuntimeException("Driver can't accept ride due to unavailability !");
        }

        /*Mark the current driver as unavailable now*/
        currentDriver.setAvailable(false);
        Driver savedDriver = driverRepository.save(currentDriver);

        /*If the driver is able to accept the ride and also the driver is available, now we need to create a ride*/
        Ride ride = rideService.createNewRide(rideRequest, savedDriver);

        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        /*Check if this driver owns the ride, only then they can start the ride*/
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver can't start the ride as they have not accepted this ride earlier !");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride Status is not confirmed, hence the ride can't be started ! Status is : " + ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("OTP is not valid !" + otp);
        }

        /*Now we can accept the ride*/
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);
        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RideDto endRide(Long rideId) {
        return null;
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public DriverDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyRides() {
        return List.of();
    }

    @Override
    public Driver getCurrentDriver() {
        // TODO : Get the current driver using Spring Security
        return driverRepository.findById(2L).orElseThrow(() -> (
                new ResourceNotFoundException("Current Driver not found with id : 2")
                ));
    }
}
