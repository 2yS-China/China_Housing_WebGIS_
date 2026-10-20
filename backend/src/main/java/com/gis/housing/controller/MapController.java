package com.gis.housing.controller;

import com.gis.housing.common.Result;
import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.dto.ProvinceStatsDTO;
import com.gis.housing.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/choropleth")
    public Result<List<CityPriceDTO>> choropleth(@RequestParam Short year,
                                                  @RequestParam Short month) {
        return Result.ok(mapService.getChoroplethData(year, month));
    }

    @GetMapping("/province")
    public Result<List<ProvinceStatsDTO>> provinceAggregation(@RequestParam Short year,
                                                               @RequestParam Short month) {
        return Result.ok(mapService.getProvinceAggregation(year, month));
    }

    @GetMapping("/bubble")
    public Result<List<CityPriceDTO>> bubble(@RequestParam Short year,
                                              @RequestParam Short month) {
        return Result.ok(mapService.getBubbleData(year, month));
    }

    @GetMapping("/change/mom")
    public Result<List<PriceChangeDTO>> momChange(@RequestParam Short year,
                                                   @RequestParam Short month) {
        return Result.ok(mapService.getMomChangeMap(year, month));
    }

    @GetMapping("/change/yoy")
    public Result<List<PriceChangeDTO>> yoyChange(@RequestParam Short year) {
        return Result.ok(mapService.getYoyChangeMap(year));
    }
}
