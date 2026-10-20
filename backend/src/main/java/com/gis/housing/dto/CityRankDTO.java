package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CityRankDTO {

    private String cityCode;
    private String cityName;
    private String provinceName;
    private BigDecimal lon;
    private BigDecimal lat;
    private BigDecimal price;
    private Integer rank;
}
