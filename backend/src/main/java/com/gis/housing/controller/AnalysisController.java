package com.gis.housing.controller;

import com.gis.housing.common.Result;
import com.gis.housing.dto.MoranDTO;
import com.gis.housing.dto.SpatialClusterDTO;
import com.gis.housing.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/moran")
    public Result<MoranDTO> moran(@RequestParam Short year,
                                   @RequestParam(required = false) Short month) {
        MoranDTO result = analysisService.getMoranResult(year, month);
        return result != null ? Result.ok(result) : Result.fail("暂无该时间段的分析结果");
    }

    @GetMapping("/cluster/tasks")
    public Result<List<String>> clusterTasks() {
        return Result.ok(analysisService.listClusterTasks());
    }

    @GetMapping("/cluster")
    public Result<List<SpatialClusterDTO>> cluster(@RequestParam String taskName) {
        return Result.ok(analysisService.getClusterResult(taskName));
    }
}
