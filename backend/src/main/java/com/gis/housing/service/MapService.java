package com.gis.housing.service;

import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.dto.ProvinceStatsDTO;

import java.util.List;

public interface MapService {

    List<CityPriceDTO> getChoroplethData(Short year, Short month);

    List<ProvinceStatsDTO> getProvinceAggregation(Short year, Short month);

    List<CityPriceDTO> getBubbleData(Short year, Short month);

    List<PriceChangeDTO> getMomChangeMap(Short year, Short month);

    List<PriceChangeDTO> getYoyChangeMap(Short year);
}
