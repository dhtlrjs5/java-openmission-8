package com.maple.mapleinfo.domain.wonder_berry;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WonderStatistics {

    private Long count = 0L;
    private Long cost = 0L;
    private final Map<String, Long> itemCount = new HashMap<>();

    public void addCount() {
        count++;
    }

    public void addCost(long amount) {
        cost += amount;
    }

    public void addItem(String itemName) {
        itemCount.put(itemName, itemCount.getOrDefault(itemName, 0L) + 1);
    }

    public void reset() {
        count = 0L;
        cost = 0L;
        itemCount.clear();
    }
}
