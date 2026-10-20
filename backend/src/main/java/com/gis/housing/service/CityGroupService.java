package com.gis.housing.service;

import com.gis.housing.dto.CityGroupStatsDTO;
import com.gis.housing.entity.CityGroup;

import java.util.List;

public interface CityGroupService {

    List<CityGroup> listGroups();

    List<CityGroupStatsDTO> getGroupStats(Integer groupId, Short year, Short month);
}
