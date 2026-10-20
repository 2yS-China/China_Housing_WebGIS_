package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProvinceStatsDTO {

    private String provinceName;
    private BigDecimal centerLon;
    private BigDecimal centerLat;
    private Short year;
    private Short month;
    private Integer cityCount;
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal medianPrice;
    private BigDecimal stddevPrice;
    private String minCity;
    private String maxCity;
}
