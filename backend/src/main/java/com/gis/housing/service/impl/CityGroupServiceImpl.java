package com.gis.housing.service.impl;

import com.gis.housing.dto.CityGroupStatsDTO;
import com.gis.housing.entity.CityGroup;
import com.gis.housing.mapper.CityGroupMapper;
import com.gis.housing.service.CityGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityGroupServiceImpl implements CityGroupService {

    private final CityGroupMapper cityGroupMapper;

    @Override
    public List<CityGroup> listGroups() {
        return cityGroupMapper.selectList(null);
    }

    @Override
    public List<CityGroupStatsDTO> getGroupStats(Integer groupId, Short year, Short month) {
        return cityGroupMapper.selectGroupStats(groupId, year, month);
    }
}
