package com.maple.mapleinfo.domain.cube;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CubeTypeStatistics {

    private Long count;
    private Long cost;

    private static final Long INITIAL_VALUE = 0L;

    public CubeTypeStatistics() {
        count = INITIAL_VALUE;
        cost = INITIAL_VALUE;
    }

    public CubeTypeStatistics update(Long amount) {
        return new CubeTypeStatistics(count + 1, cost + amount);
    }
}
