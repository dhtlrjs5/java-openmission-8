package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Equipment {

    Integer level;
    Integer star;
    Long price = 0L;
    boolean destroyed;

    public void increaseStar() {
        star++;
    }

    public void destroyedEquipment() {
        star = 12;
        destroyed = true;
    }

    public void repair() {
        destroyed = false;
    }

    public void reset() {
        star = 0;
        destroyed = false;
    }
}
