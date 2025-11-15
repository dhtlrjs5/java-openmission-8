package com.maple.mapleinfo.domain.wonder_berry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WonderStatusSummary {

    private final Long count;
    private final Long cost;

    public WonderStatusSummary() {
        this.count = 0L;
        this.cost = 0L;
    }

    public WonderStatusSummary addCount() {
        return new WonderStatusSummary(count + 1, cost);
    }

    public WonderStatusSummary addCost(Long amount) {
        return new WonderStatusSummary(count, cost + amount);
    }
}
