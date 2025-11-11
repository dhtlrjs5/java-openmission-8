package com.maple.mapleinfo.domain.wonder_berry;

import java.util.List;
import java.util.Random;

public class WonderBerry {

    private final List<Item> items;
    private final WonderStatistics statistics;
    private final Random random = new Random();

    public WonderBerry(List<Item> items) {
        this.items = items;
        statistics = new WonderStatistics();
    }

    public WonderResult useWonderBerry() {

        double randomValue = random.nextDouble() * 100;
        double cumulative = 0L;

        for (Item item : items) {
            cumulative += item.getProbability();

            if (randomValue <= cumulative) {
                statistics.addCount();
                statistics.addCost(5500L);
                statistics.addItem(item.getName());

                return new WonderResult(item, statistics);
            }
        }

        throw new IllegalStateException("확률 합계가 100%가 아닙니다.");
    }

    public WonderResult useTenTimes() {

        for (int i = 0; i < 10; i++) {
            useWonderBerry();
        }

        return new WonderResult(null, statistics);
    }

    public WonderResult reset() {
        statistics.reset();

        return new WonderResult(null, statistics);
    }
}
