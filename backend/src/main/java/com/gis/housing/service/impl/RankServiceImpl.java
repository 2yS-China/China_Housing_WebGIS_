package com.gis.housing.service.impl;

import com.gis.housing.dto.CityRankDTO;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.mapper.MonthlyPriceMapper;
import com.gis.housing.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    private final MonthlyPriceMapper monthlyPriceMapper;

    @Override
    public List<CityRankDTO> getCityRank(Short year, Short month, String order, Integer limit) {
        return monthlyPriceMapper.selectCityRank(year, month, order, limit);
    }

    @Override
    public List<PriceChangeDTO> getChangeRank(Short startYear, Short startMonth,
                                               Short endYear, Short endMonth,
                                               String order, Integer limit) {
        return monthlyPriceMapper.selectCustomChange(startYear, startMonth,
                endYear, endMonth, order, limit);
    }
}
