package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StarStatistics {

    Long totalCost;
    Long attempts;
    Long destruction;

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
