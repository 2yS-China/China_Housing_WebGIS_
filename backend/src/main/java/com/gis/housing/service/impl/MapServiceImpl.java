package com.gis.housing.service.impl;

import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.dto.ProvinceStatsDTO;
import com.gis.housing.mapper.CityMapper;
import com.gis.housing.mapper.MonthlyPriceMapper;
import com.gis.housing.mapper.ProvinceMapper;
import com.gis.housing.mapper.YearlyPriceMapper;
import com.gis.housing.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final CityMapper cityMapper;
    private final ProvinceMapper provinceMapper;
    private final MonthlyPriceMapper monthlyPriceMapper;
    private final YearlyPriceMapper yearlyPriceMapper;

    @Override
    public List<CityPriceDTO> getChoroplethData(Short year, Short month) {
        return cityMapper.selectCityPriceMap(year, month);
    }

    @Override
    public List<ProvinceStatsDTO> getProvinceAggregation(Short year, Short month) {
        return provinceMapper.selectProvinceStats(year, month);
    }

    @Override
    public List<CityPriceDTO> getBubbleData(Short year, Short month) {
        return cityMapper.selectCityPriceMap(year, month);
    }

    @Override
    public List<PriceChangeDTO> getMomChangeMap(Short year, Short month) {
        return monthlyPriceMapper.selectMomChange(year, month);
    }

    @Override
    public List<PriceChangeDTO> getYoyChangeMap(Short year) {
        return yearlyPriceMapper.selectYoyChange(year);
    }
}
