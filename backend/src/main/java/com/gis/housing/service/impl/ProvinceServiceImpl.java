package com.gis.housing.service.impl;

import com.gis.housing.dto.ProvinceStatsDTO;
import com.gis.housing.entity.Province;
import com.gis.housing.mapper.ProvinceMapper;
import com.gis.housing.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceMapper provinceMapper;

    @Override
    public List<Province> listAll() {
        return provinceMapper.selectList(null);
    }

    @Override
    public ProvinceStatsDTO getProvinceStats(String provinceName, Short year, Short month) {
        return provinceMapper.selectOneProvinceStats(provinceName, year, month);
    }
}
