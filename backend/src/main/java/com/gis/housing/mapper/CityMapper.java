package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.FilterParam;
import com.gis.housing.entity.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CityMapper extends BaseMapper<City> {

    List<City> searchCity(@Param("keyword") String keyword);

    List<CityPriceDTO> selectCityPriceMap(@Param("year") Short year, @Param("month") Short month);

    List<CityPriceDTO> filterCities(@Param("param") FilterParam param);
}
