package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class StarStatistics {

    private static final Long INITIAL_VALUE = 0L;

    private Long totalCost;
    private Long attempts;
    private Long destruction;

    public StarStatistics() {
        totalCost = INITIAL_VALUE;
        attempts = INITIAL_VALUE;
        destruction = INITIAL_VALUE;
    }

    public StarStatistics addCost(Long cost) {
        return new StarStatistics(totalCost + cost, attempts, destruction);
    }

    public StarStatistics addAttempts(Long cost) {
        return new StarStatistics(totalCost + cost, attempts + 1, destruction);
    }

    // 리펙토링중
    public StarStatistics addDestruction() {
        return new StarStatistics(totalCost, attempts, destruction + 1);
    }
}