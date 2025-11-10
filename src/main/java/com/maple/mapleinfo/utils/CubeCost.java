package com.maple.mapleinfo.utils;

import lombok.Getter;

@Getter
public enum CubeCost {
    DEFAULT_RARE(4_500_000),
    DEFAULT_EPIC(18_000_000),
    DEFAULT_UNIQUE(38_250_000),
    DEFAULT_LEGENDARY(45_000_000),

    ADDITIONAL_RARE(22_000_000),
    ADDITIONAL_EPIC(61_600_000),
    ADDITIONAL_UNIQUE(74_800_000),
    ADDITIONAL_LEGENDARY(88_000_000);

    private final int cost;

    CubeCost(int cost) {
        this.cost = cost;
    }
}
