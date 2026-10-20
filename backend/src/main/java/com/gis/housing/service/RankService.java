package com.gis.housing.service;

import com.gis.housing.dto.CityRankDTO;
import com.gis.housing.dto.PriceChangeDTO;

import java.util.List;

public interface RankService {

    List<CityRankDTO> getCityRank(Short year, Short month, String order, Integer limit);

    List<PriceChangeDTO> getChangeRank(Short startYear, Short startMonth,
                                       Short endYear, Short endMonth,
                                       String order, Integer limit);
}
