package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Equipment {

    Integer level;
    Integer star;
    Long price;
    boolean destroyed;

    public Equipment() {
        level = 0;
        star = 0;
        price = 0L;
        destroyed = false;
    }

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

    public Equipment newPrice(Long price) {
        return new Equipment(level, star, price, destroyed);
    }
}
