package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentLevel {

    private static final Integer TEMP_LEVEL = 250;

    Integer level;

    public EquipmentLevel() {
        level = TEMP_LEVEL;
    }
}
