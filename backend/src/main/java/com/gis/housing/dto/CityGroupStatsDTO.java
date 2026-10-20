package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CityGroupStatsDTO {

    private Integer groupId;
    private String groupName;
    private Short year;
    private Short month;
    private Integer cityCount;
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal stddevPrice;
}
