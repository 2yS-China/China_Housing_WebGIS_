package com.gis.housing.controller;

import com.gis.housing.dto.FilterParam;
import com.gis.housing.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @PostMapping("/excel")
    public void exportExcel(@RequestBody FilterParam param, HttpServletResponse response) {
        exportService.exportFilteredData(param, response);
    }
}
