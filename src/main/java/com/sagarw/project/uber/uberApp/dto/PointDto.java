/*Jackson should have enough information on how to serialize or deserialize the data
* To tell how to deserialize or serialize the Point data. The point does not have any jackson specified
* We have to provide our own specification
*
* 1 - To define serialization for Point
* 2 - To create another DTO for Point*/

package com.sagarw.project.uber.uberApp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointDto {
    private double[] coordinates;

    /*Represents the Geo-Spatial point*/
    private String type = "Point";

    public PointDto(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
