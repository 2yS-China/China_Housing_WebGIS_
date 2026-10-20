package com.gis.housing.service;

import com.gis.housing.dto.MoranDTO;
import com.gis.housing.dto.SpatialClusterDTO;

import java.util.List;

public interface AnalysisService {

    MoranDTO getMoranResult(Short year, Short month);

    List<SpatialClusterDTO> getClusterResult(String taskName);

    List<String> listClusterTasks();
}
