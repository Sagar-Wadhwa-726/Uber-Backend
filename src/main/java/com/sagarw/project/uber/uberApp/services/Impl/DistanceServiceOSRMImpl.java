package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {
    @Override
    public double calculateDistance(Point src, Point dest) {
        // TODO :: the third party API - OSRM to fetch the distance between the two points
        return 0;
    }
}
