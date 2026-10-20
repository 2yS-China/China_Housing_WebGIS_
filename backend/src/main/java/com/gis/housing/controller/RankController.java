package com.gis.housing.controller;

import com.gis.housing.common.Result;
import com.gis.housing.dto.CityRankDTO;
import com.gis.housing.dto.PriceChangeDTO;
import com.gis.housing.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping("/price")
    public Result<List<CityRankDTO>> priceRank(@RequestParam Short year,
                                                @RequestParam Short month,
                                                @RequestParam(defaultValue = "desc") String order,
                                                @RequestParam(defaultValue = "20") Integer limit) {
        return Result.ok(rankService.getCityRank(year, month, order, limit));
    }

    @GetMapping("/change")
    public Result<List<PriceChangeDTO>> changeRank(@RequestParam Short startYear,
                                                    @RequestParam Short startMonth,
                                                    @RequestParam Short endYear,
                                                    @RequestParam Short endMonth,
                                                    @RequestParam(defaultValue = "desc") String order,
                                                    @RequestParam(defaultValue = "20") Integer limit) {
        return Result.ok(rankService.getChangeRank(startYear, startMonth, endYear, endMonth, order, limit));
    }
}
