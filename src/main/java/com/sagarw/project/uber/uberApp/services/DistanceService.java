package com.sagarw.project.uber.uberApp.services;

import org.locationtech.jts.geom.Point;

public interface DistanceService {
    // OSRM or G-Maps implementation
    double calculateDistance(Point src, Point dest);
}
