package com.maple.mapleinfo.domain.cube;

import com.maple.mapleinfo.utils.CubeType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CubeStatistics {

    private CubeTypeStatistics defaultStatistics;
    private CubeTypeStatistics additionalStatistics;

    public CubeStatistics() {
        defaultStatistics = new CubeTypeStatistics();
        additionalStatistics = new CubeTypeStatistics();
    }

    public CubeStatistics update(CubeType type, Long cost) {
        if (type == CubeType.DEFAULT) {
            CubeTypeStatistics newStatistics = defaultStatistics.update(cost);

            return new CubeStatistics(newStatistics, additionalStatistics);
        }

        CubeTypeStatistics newStatistics = additionalStatistics.update(cost);

        return new CubeStatistics(defaultStatistics, newStatistics);
    }

    public Long getDefaultCount() {
        return defaultStatistics.getCount();
    }

    public Long getDefaultCost() {
        return defaultStatistics.getCost();
    }

    public Long getAdditionalCount() {
        return additionalStatistics.getCount();
    }


    public Long getAdditionalCost() {
        return additionalStatistics.getCost();
    }

    public Long getTotalCost() {
        Long defaultCost = defaultStatistics.getCost();
        Long additionalCost = additionalStatistics.getCost();

        return defaultCost + additionalCost;
    }
}
