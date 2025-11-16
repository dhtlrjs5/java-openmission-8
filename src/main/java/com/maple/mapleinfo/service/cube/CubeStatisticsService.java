package com.maple.mapleinfo.service.cube;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.utils.CubeCost;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CubeStatisticsService {

    private CubeStatistics statistics = new CubeStatistics();

    public CubeStatistics updateStatistics(CubeType cubeType, Grade grade) {
        String key = cubeType + "_" + grade;
        CubeCost cubeCost = CubeCost.valueOf(key);
        Long cost = cubeCost.getCost();

        statistics = statistics.update(cubeType, cost);

        return statistics;
    }

    public CubeStatistics resetStatistics() {
        statistics = new CubeStatistics();

        return statistics;
    }
}
