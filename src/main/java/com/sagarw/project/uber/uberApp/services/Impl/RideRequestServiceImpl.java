package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.entities.RideRequest;
import com.sagarw.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.sagarw.project.uber.uberApp.repositories.RideRequestRepository;
import com.sagarw.project.uber.uberApp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*Created a different service for Ride Request and did not use the Ride Request repository directly, reason for this is encapsulation and loose coupling*/
/*Also because Repository should be accessed through their services only*/
@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId).orElseThrow(() -> (
                new ResourceNotFoundException("Ride Request not found with id : " + rideRequestId)
        ));
    }

    @Override
    public void update(RideRequest rideRequest) {
        /*Since we want to update the ride request check if this ride request exists in the database*/
        RideRequest toSave = rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> (
                        new ResourceNotFoundException("Ride Request not found with id : " + rideRequest.getId())
                ));
        rideRequestRepository.save(rideRequest);
    }
}
