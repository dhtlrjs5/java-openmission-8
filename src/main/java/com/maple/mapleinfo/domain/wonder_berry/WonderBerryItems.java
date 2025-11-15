package com.maple.mapleinfo.domain.wonder_berry;

import java.util.List;
import java.util.Random;

import static com.maple.mapleinfo.utils.ErrorMessages.ERROR_INVALID_PROBABILITY_SUM;

public class WonderBerryItems {

    private static final double PROBABILITY_SCALE = 100.0;

    private final List<Item> items;
    private final Random random;

    public WonderBerryItems(List<Item> items) {
        this.items = items;
        this.random = new Random();
    }

    public Item findItemByRandomValue() {
        double randomValue = random.nextDouble() * PROBABILITY_SCALE;
        double cumulative = 0.0;

        for (Item item : items) {
            cumulative += item.getProbability();

            if (randomValue <= cumulative) {
                return item;
            }
        }

        throw new IllegalStateException(ERROR_INVALID_PROBABILITY_SUM);
    }
}
