package com.maple.mapleinfo.domain.cube;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CubeStatistics {

    private Long defaultCount = 0L;
    private Long additionalCount = 0L;
    private Long defaultCost = 0L;
    private Long additionalCost = 0L;
    private Long totalCost = 0L;

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
