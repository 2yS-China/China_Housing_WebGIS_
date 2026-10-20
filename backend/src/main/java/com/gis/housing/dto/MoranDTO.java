package com.gis.housing.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MoranDTO {

    private Short analysisYear;
    private Short analysisMonth;
    private BigDecimal globalMoranI;
    private BigDecimal pValue;
    private BigDecimal zScore;
    private List<LisaItem> lisaItems;

    @Data
    public static class LisaItem {
        private String cityCode;
        private String cityName;
        private BigDecimal lon;
        private BigDecimal lat;
        private BigDecimal localI;
        private BigDecimal pValue;
        private String clusterType;
    }
}
