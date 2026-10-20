package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.entity.YearlyPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface YearlyPriceMapper extends BaseMapper<YearlyPrice> {

    List<PriceChangeDTO> selectYoyChange(@Param("year") Short year);
}
