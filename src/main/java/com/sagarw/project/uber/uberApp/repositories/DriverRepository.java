package com.sagarw.project.uber.uberApp.repositories;

import com.sagarw.project.uber.uberApp.entities.Driver;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*We need to find the Drivers who are within the vicinity of the pickup location
 * Geospatial Database is very efficient in these distance related calculations*/
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query(value = "select d.*, ST_Distance(d.current_location, :pickupLocation)" +
            " FROM driver d" +
            " WHERE d.available = true" +
            " AND ST_DWithin(d.current_location, :pickupLocation, 10000)" +
            "ORDER BY distance" +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickupLocation);


    /*This method will help to find the top 10 rated drivers which are nearby to the
     * pickup location*/
    @Query(value = "SELECT d.* " +
            "FROM Driver d " +
            "WHERE d.available = true " +
            "AND ST_DWithin(d.current_location, :pickupLocation, 15000) " +
            "ORDER BY d.rating DESC " +
            "LIMIT 10 ", nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDriver(Point pickupLocation);
}
