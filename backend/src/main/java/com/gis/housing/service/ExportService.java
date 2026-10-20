package com.gis.housing.service;

import com.gis.housing.dto.FilterParam;
import jakarta.servlet.http.HttpServletResponse;

public interface ExportService {

    void exportFilteredData(FilterParam param, HttpServletResponse response);
}
