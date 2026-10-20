package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpatialClusterDTO {

    private String cityCode;
    private String cityName;
    private String provinceName;
    private BigDecimal lon;
    private BigDecimal lat;
    private Short clusterLabel;
    private String clusterDesc;
}
