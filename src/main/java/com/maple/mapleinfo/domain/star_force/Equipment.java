package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Equipment {

    Integer star;
    Long price;
    boolean destroyed;

    public void increaseStar() {
        star++;
    }

    public void decreaseStar() {
        star--;
    }

    public void reset() {
        star = 0;
        destroyed = false;
    }
}
