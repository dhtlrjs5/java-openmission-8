package com.maple.mapleinfo.service;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.utils.CubeCost;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CubeStatisticsService {

    private final CubeStatistics statistics = new CubeStatistics();

    public CubeStatistics updateStatistics(CubeType cubeType, Grade grade) {

        String key = cubeType + "_" + grade;
        Long cost = CubeCost.valueOf(key).getCost();

        if (cubeType.equals(CubeType.DEFAULT)) {
            statistics.increaseDefaultCount();
            statistics.addDefaultCost(cost);
            statistics.addTotalCost(cost);

            return statistics;
        }

        statistics.increaseAdditionalCount();
        statistics.addAdditionalCost(cost);
        statistics.addTotalCost(cost);

        return statistics;
    }
}
