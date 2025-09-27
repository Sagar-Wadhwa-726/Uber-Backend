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
import com.sagarw.project.uber.uberApp.services.PaymentService;
import com.sagarw.project.uber.uberApp.services.RideRequestService;
import com.sagarw.project.uber.uberApp.services.RideService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    private final PaymentService paymentService;

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        // Check if the ride is already accepted or not
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new RuntimeException("Ride Request can't be accepted, status is : " + rideRequest.getRideRequestStatus());
        }

        /*Check if current driver able to accept the ride or not*/
        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver can't accept ride due to unavailability !");
        }

        /*Mark the current driver as unavailable now*/
        Driver savedDriver = updateDriverAvailability(currentDriver, false);

        /*If the driver is able to accept the ride and also the driver is available, now we need to create a ride*/
        Ride ride = rideService.createNewRide(rideRequest, savedDriver);

        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        // Ride can be cancelled only if the ride has been accepted and not started
        Ride ride = rideService.getRideById(rideId);

        // Check if the driver owns this ride or not
        Driver driver = getCurrentDriver();
        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier !");
        }

        // Ride can only be cancelled only if it is confirmed
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride can't be cancelled, invalid status : " + ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);

        // Free the driver as well :)
        updateDriverAvailability(driver, true);

        return modelMapper.map(ride, RideDto.class);
    }


    // When starting the ride create a payment object as well
    @Override
    public RideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        /*Check if this driver owns the ride, only then they can start the ride*/
        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver can't start the ride as they have not accepted this ride earlier !");
        }

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride Status is not confirmed, hence the ride can't be started ! Status is : " + ride.getRideStatus());
        }

        if (!otp.equals(ride.getOtp())) {
            throw new RuntimeException("OTP is not valid !" + otp);
        }

        /*Now we can accept the ride*/
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        paymentService.createNewPayment(savedRide);


        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver can't end the ride as he is not the owner of the ride !");
        }

        if (!ride.getRideStatus().equals(RideStatus.ONGOING)) {
            throw new RuntimeException("Ride status is not ONGOING hence can't be ended, status : " + ride.getRideStatus());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(driver, true);
        paymentService.processPayment(ride);
        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        // TODO : Get the current driver using Spring Security
        return driverRepository.findById(2L).orElseThrow(() -> (
                new ResourceNotFoundException("Current Driver not found with id : 2")
        ));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }
}
