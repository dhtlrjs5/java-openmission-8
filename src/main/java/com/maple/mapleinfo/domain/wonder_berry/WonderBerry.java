package com.maple.mapleinfo.domain.wonder_berry;

import java.util.List;

public class WonderBerry {

    private static final Long WONDER_BERRY_COST = 5500L;
    private static final int USE_TEN_TIMES = 10;

    private final WonderBerryItems items;
    private final WonderStatistics statistics = new WonderStatistics();

    public WonderBerry(List<Item> items) {
        this.items = new WonderBerryItems(items);
    }

    public WonderResult useWonderBerry() {

        Item randomItem = items.findItemByRandomValue();

        statistics.addCount();
        statistics.addCost(WONDER_BERRY_COST);
        statistics.addItem(randomItem.getName());

        return new WonderResult(randomItem, statistics);
    }

    public WonderResult useTenTimes() {

        for (int i = 0; i < USE_TEN_TIMES; i++) {
            useWonderBerry();
        }

        return new WonderResult(null, statistics);
    }

    public WonderResult reset() {
        statistics.reset();

        return new WonderResult(null, statistics);
    }
}
