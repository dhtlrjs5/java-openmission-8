package com.maple.mapleinfo.domain.cube;

import lombok.Getter;

@Getter
public class CubeStatistics {

    private Long defaultCount;
    private Long additionalCount;
    private Long defaultCost;
    private Long additionalCost;
    private Long totalCost;

    public void increaseDefaultCount() {
        ++defaultCount;
    }

    public void increaseAdditionalCount() {
        ++additionalCount;
    }

    public void addDefaultCost(Long cost) {
        defaultCost += cost;
    }

    public void addAdditionalCost(Long cost) {
        additionalCost += cost;
    }

    public void addTotalCost(Long cost) {
        totalCost += cost;
    }
}
