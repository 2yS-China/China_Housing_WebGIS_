package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.CityRankDTO;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.entity.MonthlyPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonthlyPriceMapper extends BaseMapper<MonthlyPrice> {

    List<MonthlyPrice> selectCityTrend(@Param("cityCode") String cityCode);

    List<CityRankDTO> selectCityRank(@Param("year") Short year,
                                     @Param("month") Short month,
                                     @Param("order") String order,
                                     @Param("limit") Integer limit);

    List<PriceChangeDTO> selectMomChange(@Param("year") Short year, @Param("month") Short month);

    List<PriceChangeDTO> selectCustomChange(@Param("startYear") Short startYear,
                                            @Param("startMonth") Short startMonth,
                                            @Param("endYear") Short endYear,
                                            @Param("endMonth") Short endMonth,
                                            @Param("order") String order,
                                            @Param("limit") Integer limit);
}
