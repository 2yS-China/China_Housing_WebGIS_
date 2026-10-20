package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceChangeDTO {

    private String cityCode;
    private String cityName;
    private String provinceName;
    private BigDecimal lon;
    private BigDecimal lat;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private BigDecimal changePct;
}
