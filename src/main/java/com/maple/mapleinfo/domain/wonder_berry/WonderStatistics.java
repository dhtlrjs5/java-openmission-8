package com.maple.mapleinfo.domain.wonder_berry;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WonderStatistics {

    private WonderStatusSummary summary;
    private final Map<String, Long> itemCount;

    public WonderStatistics() {
        summary = new WonderStatusSummary();
        itemCount = new HashMap<>();
    }

    public void addCount() {
        summary = summary.addCount();
    }

    public void addCost(long amount) {
        summary = summary.addCost(amount);
    }

    public void addItem(String itemName) {
        long count = itemCount.getOrDefault(itemName, 0L);

        itemCount.put(itemName, count + 1);
    }

    public void reset() {
        summary = new WonderStatusSummary();
        itemCount.clear();
    }

    public Long getCount() {
        return summary.getCount();
    }

    public Long getCost() {
        return summary.getCost();
    }

    public Map<String, Long> getItemCount() {
        return itemCount.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}
