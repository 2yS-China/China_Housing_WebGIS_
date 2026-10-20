package com.gis.housing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gis.housing.dto.SpatialClusterDTO;
import com.gis.housing.entity.SpatialCluster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpatialClusterMapper extends BaseMapper<SpatialCluster> {

    List<SpatialClusterDTO> selectClusterWithCity(@Param("taskName") String taskName);
}
