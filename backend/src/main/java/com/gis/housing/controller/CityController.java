package com.gis.housing.controller;

import com.gis.housing.common.Result;
import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.CityTrendDTO;
import com.gis.housing.dto.FilterParam;
import com.gis.housing.entity.City;
import com.gis.housing.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/search")
    public Result<List<City>> search(@RequestParam String keyword) {
        return Result.ok(cityService.searchCity(keyword));
    }

    @GetMapping("/{cityCode}")
    public Result<City> detail(@PathVariable String cityCode) {
        City city = cityService.getCityDetail(cityCode);
        return city != null ? Result.ok(city) : Result.fail("城市不存在");
    }

    @GetMapping("/{cityCode}/trend")
    public Result<CityTrendDTO> trend(@PathVariable String cityCode) {
        CityTrendDTO trend = cityService.getCityTrend(cityCode);
        return trend != null ? Result.ok(trend) : Result.fail("城市不存在");
    }

    @GetMapping("/compare")
    public Result<List<CityTrendDTO>> compare(@RequestParam List<String> codes) {
        return Result.ok(cityService.compareCities(codes));
    }

    @PostMapping("/filter")
    public Result<List<CityPriceDTO>> filter(@RequestBody FilterParam param) {
        return Result.ok(cityService.filterCities(param));
    }
}
