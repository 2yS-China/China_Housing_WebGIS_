package com.gis.housing.service;

import com.gis.housing.dto.ProvinceStatsDTO;
import com.gis.housing.entity.Province;

import java.util.List;

public interface ProvinceService {

    List<Province> listAll();

    ProvinceStatsDTO getProvinceStats(String provinceName, Short year, Short month);
}
