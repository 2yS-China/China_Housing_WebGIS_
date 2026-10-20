package com.gis.housing.controller;

import com.gis.housing.common.Result;
import com.gis.housing.dto.CityGroupStatsDTO;
import com.gis.housing.entity.CityGroup;
import com.gis.housing.service.CityGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/citygroup")
@RequiredArgsConstructor
public class CityGroupController {

    private final CityGroupService cityGroupService;

    @GetMapping("/list")
    public Result<List<CityGroup>> list() {
        return Result.ok(cityGroupService.listGroups());
    }

    @GetMapping("/stats")
    public Result<List<CityGroupStatsDTO>> stats(@RequestParam(required = false) Integer groupId,
                                                   @RequestParam Short year,
                                                   @RequestParam Short month) {
        return Result.ok(cityGroupService.getGroupStats(groupId, year, month));
    }
}
