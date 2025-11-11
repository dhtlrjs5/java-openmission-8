package com.maple.mapleinfo.domain.wonder_berry;

import com.maple.mapleinfo.utils.Rarity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {

    private final String name;
    private final double probability;
    private final Rarity rarity;
}
