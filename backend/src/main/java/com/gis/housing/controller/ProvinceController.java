package com.gis.housing.controller;

import com.gis.housing.common.Result;
import com.gis.housing.dto.ProvinceStatsDTO;
import com.gis.housing.entity.Province;
import com.gis.housing.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/province")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping("/list")
    public Result<List<Province>> list() {
        return Result.ok(provinceService.listAll());
    }

    @GetMapping("/stats")
    public Result<ProvinceStatsDTO> stats(@RequestParam String provinceName,
                                           @RequestParam Short year,
                                           @RequestParam Short month) {
        return Result.ok(provinceService.getProvinceStats(provinceName, year, month));
    }
}
