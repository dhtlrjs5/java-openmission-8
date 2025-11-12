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

    public void failEnhancement() {
        if (star >= 16 && star != 20) {
            decreaseStar();
        }
    }

    private void decreaseStar() {
        star--;
    }

    public void destroyedEquipment() {
        star = 12;
        destroyed = true;
    }

    public void reset() {
        star = 0;
        destroyed = false;
    }
}
