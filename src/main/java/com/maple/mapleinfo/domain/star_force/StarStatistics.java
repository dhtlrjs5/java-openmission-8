package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StarStatistics {

    Long totalCost = 0L;
    Long attempts = 0L;
    Long destruction = 0L;

    public void addCost(Long cost) {
        totalCost += cost;
    }

    public void addAttempts() {
        attempts++;
    }

    public void addDestruction() {
        destruction++;
    }
}
