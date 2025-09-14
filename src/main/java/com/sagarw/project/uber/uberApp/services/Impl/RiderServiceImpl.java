package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.dto.DriverDto;
import com.sagarw.project.uber.uberApp.dto.RideDto;
import com.sagarw.project.uber.uberApp.dto.RideRequestDto;
import com.sagarw.project.uber.uberApp.dto.RiderDto;
import com.sagarw.project.uber.uberApp.entities.Rider;
import com.sagarw.project.uber.uberApp.entities.User;
import com.sagarw.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.sagarw.project.uber.uberApp.repositories.RideRequestRepository;
import com.sagarw.project.uber.uberApp.repositories.RiderRepository;
import com.sagarw.project.uber.uberApp.services.RiderService;
import com.sagarw.project.uber.uberApp.strategies.DriverMatchingStrategy;
import com.sagarw.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import com.sagarw.project.uber.uberApp.entities.RideRequest;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final DriverMatchingStrategy driverMatchingStrategy;
    private final RideFareCalculationStrategy ridefarecalculation;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        RideRequest rideRequest = modelMapper.map(
                rideRequestDto, RideRequest.class
        );
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

        Double fare = ridefarecalculation.calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        driverMatchingStrategy.findMatchingDriver(rideRequest);
        return modelMapper.map(rideRequest, RideRequestDto.class);
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
}