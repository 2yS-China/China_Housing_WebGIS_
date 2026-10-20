package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FilterParam {

    private List<String> provinces;
    private Short startYear;
    private Short startMonth;
    private Short endYear;
    private Short endMonth;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal minChangePct;
    private BigDecimal maxChangePct;
}
