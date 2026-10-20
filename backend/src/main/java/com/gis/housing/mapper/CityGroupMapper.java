package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.CityGroupStatsDTO;
import com.gis.housing.entity.CityGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CityGroupMapper extends BaseMapper<CityGroup> {

    List<CityGroupStatsDTO> selectGroupStats(@Param("groupId") Integer groupId,
                                             @Param("year") Short year,
                                             @Param("month") Short month);
}
