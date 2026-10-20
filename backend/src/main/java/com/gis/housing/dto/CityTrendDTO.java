package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CityTrendDTO {

    private String cityCode;
    private String cityName;
    private String provinceName;
    private List<TrendPoint> points;

    @Data
    public static class TrendPoint {
        private Short year;
        private Short month;
        private BigDecimal price;
    }
}
