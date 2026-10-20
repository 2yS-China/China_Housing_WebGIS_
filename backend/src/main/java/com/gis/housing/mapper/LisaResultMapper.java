package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.MoranDTO;
import com.gis.housing.entity.LisaResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LisaResultMapper extends BaseMapper<LisaResult> {

    List<MoranDTO.LisaItem> selectLisaWithCity(@Param("moranId") Integer moranId);
}
