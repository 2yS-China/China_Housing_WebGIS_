package com.gis.housing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gis.housing.dto.MoranDTO;
import com.gis.housing.dto.SpatialClusterDTO;
import com.gis.housing.entity.MoranResult;
import com.gis.housing.entity.SpatialCluster;
import com.gis.housing.mapper.LisaResultMapper;
import com.gis.housing.mapper.MoranResultMapper;
import com.gis.housing.mapper.SpatialClusterMapper;
import com.gis.housing.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final MoranResultMapper moranResultMapper;
    private final LisaResultMapper lisaResultMapper;
    private final SpatialClusterMapper spatialClusterMapper;

    @Override
    public MoranDTO getMoranResult(Short year, Short month) {
        LambdaQueryWrapper<MoranResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MoranResult::getAnalysisYear, year);
        if (month != null) {
            wrapper.eq(MoranResult::getAnalysisMonth, month);
        } else {
            wrapper.isNull(MoranResult::getAnalysisMonth);
        }
        wrapper.orderByDesc(MoranResult::getCreatedAt).last("LIMIT 1");
        MoranResult moran = moranResultMapper.selectOne(wrapper);
        if (moran == null) {
            return null;
        }

        MoranDTO dto = new MoranDTO();
        dto.setAnalysisYear(moran.getAnalysisYear());
        dto.setAnalysisMonth(moran.getAnalysisMonth());
        dto.setGlobalMoranI(moran.getGlobalMoranI());
        dto.setPValue(moran.getPValue());
        dto.setZScore(moran.getZScore());
        dto.setLisaItems(lisaResultMapper.selectLisaWithCity(moran.getId()));
        return dto;
    }

    @Override
    public List<SpatialClusterDTO> getClusterResult(String taskName) {
        return spatialClusterMapper.selectClusterWithCity(taskName);
    }

    @Override
    public List<String> listClusterTasks() {
        List<SpatialCluster> list = spatialClusterMapper.selectList(null);
        return list.stream()
                .map(SpatialCluster::getTaskName)
                .distinct()
                .collect(Collectors.toList());
    }
}
