package com.maple.mapleinfo.utils;

import lombok.Getter;

@Getter
public enum CubeCost {
    DEFAULT_RARE(4_500_000L),
    DEFAULT_EPIC(18_000_000L),
    DEFAULT_UNIQUE(38_250_000L),
    DEFAULT_LEGENDARY(45_000_000L),

    ADDITIONAL_RARE(22_000_000L),
    ADDITIONAL_EPIC(61_600_000L),
    ADDITIONAL_UNIQUE(74_800_000L),
    ADDITIONAL_LEGENDARY(88_000_000L);

    private final Long cost;

    CubeCost(Long cost) {
        this.cost = cost;
    }
}
