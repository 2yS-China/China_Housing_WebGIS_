package com.gis.housing.service;

import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.CityTrendDTO;
import com.gis.housing.dto.FilterParam;
import com.gis.housing.entity.City;

import java.util.List;

public interface CityService {

    List<City> searchCity(String keyword);

    City getCityDetail(String cityCode);

    CityTrendDTO getCityTrend(String cityCode);

    List<CityTrendDTO> compareCities(List<String> cityCodes);

    List<CityPriceDTO> filterCities(FilterParam param);
}
