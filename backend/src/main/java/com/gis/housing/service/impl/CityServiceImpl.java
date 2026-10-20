package com.gis.housing.service.impl;

import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.CityTrendDTO;
import com.gis.housing.dto.FilterParam;
import com.gis.housing.entity.City;
import com.gis.housing.entity.MonthlyPrice;
import com.gis.housing.mapper.CityMapper;
import com.gis.housing.mapper.MonthlyPriceMapper;
import com.gis.housing.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityMapper cityMapper;
    private final MonthlyPriceMapper monthlyPriceMapper;

    @Override
    public List<City> searchCity(String keyword) {
        return cityMapper.searchCity(keyword);
    }

    @Override
    public City getCityDetail(String cityCode) {
        return cityMapper.selectById(cityCode);
    }

    @Override
    public CityTrendDTO getCityTrend(String cityCode) {
        City city = cityMapper.selectById(cityCode);
        if (city == null) {
            return null;
        }
        List<MonthlyPrice> prices = monthlyPriceMapper.selectCityTrend(cityCode);

        CityTrendDTO dto = new CityTrendDTO();
        dto.setCityCode(city.getCityCode());
        dto.setCityName(city.getCityName());
        dto.setProvinceName(city.getProvinceName());

        List<CityTrendDTO.TrendPoint> points = new ArrayList<>();
        for (MonthlyPrice mp : prices) {
            CityTrendDTO.TrendPoint point = new CityTrendDTO.TrendPoint();
            point.setYear(mp.getYear());
            point.setMonth(mp.getMonth());
            point.setPrice(mp.getPrice());
            points.add(point);
        }
        dto.setPoints(points);
        return dto;
    }

    @Override
    public List<CityTrendDTO> compareCities(List<String> cityCodes) {
        List<CityTrendDTO> result = new ArrayList<>();
        for (String code : cityCodes) {
            CityTrendDTO trend = getCityTrend(code);
            if (trend != null) {
                result.add(trend);
            }
        }
        return result;
    }

    @Override
    public List<CityPriceDTO> filterCities(FilterParam param) {
        return cityMapper.filterCities(param);
    }
}
