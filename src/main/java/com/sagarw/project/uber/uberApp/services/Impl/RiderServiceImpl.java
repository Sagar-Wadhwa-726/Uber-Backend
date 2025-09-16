package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.dto.DriverDto;
import com.sagarw.project.uber.uberApp.dto.RideDto;
import com.sagarw.project.uber.uberApp.dto.RideRequestDto;
import com.sagarw.project.uber.uberApp.dto.RiderDto;
import com.sagarw.project.uber.uberApp.entities.Driver;
import com.sagarw.project.uber.uberApp.entities.RideRequest;
import com.sagarw.project.uber.uberApp.entities.Rider;
import com.sagarw.project.uber.uberApp.entities.User;
import com.sagarw.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.sagarw.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.sagarw.project.uber.uberApp.repositories.RideRequestRepository;
import com.sagarw.project.uber.uberApp.repositories.RiderRepository;
import com.sagarw.project.uber.uberApp.services.RiderService;
import com.sagarw.project.uber.uberApp.strategies.RideStrategyManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();

        RideRequest rideRequest = modelMapper.map(
                rideRequestDto, RideRequest.class
        );

        rideRequest.setRider(rider);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);
        return modelMapper.map(savedRideRequest, RideRequestDto.class);

        // TODO : Send a notification to all the drivers about this ride request
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyRides() {
        return List.of();
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider.builder().rating(5.00).user(user).build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        // TODO Implement spring security to get the context of the current rider, to fetch it's rating
        return riderRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException(
            "Rider not found with Id : " + 1
        ));
    }
}