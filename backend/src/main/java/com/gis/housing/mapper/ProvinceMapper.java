package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.ProvinceStatsDTO;
import com.gis.housing.entity.Province;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProvinceMapper extends BaseMapper<Province> {

    List<ProvinceStatsDTO> selectProvinceStats(@Param("year") Short year, @Param("month") Short month);

    ProvinceStatsDTO selectOneProvinceStats(@Param("provinceName") String provinceName,
                                            @Param("year") Short year,
                                            @Param("month") Short month);
}
