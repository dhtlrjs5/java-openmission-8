package com.maple.mapleinfo.service.cube;

import com.maple.mapleinfo.domain.cube.CubeStatistics;
import com.maple.mapleinfo.utils.CubeCost;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CubeStatisticsService {

    public CubeStatistics updateStatistics(CubeStatistics statistics, CubeType cubeType, Grade grade) {
        String key = cubeType + "_" + grade;
        CubeCost cubeCost = CubeCost.valueOf(key);
        Long cost = cubeCost.getCost();

        return statistics.update(cubeType, cost);
    }

    public CubeStatistics resetStatistics() {
        return new CubeStatistics();
    }
}
