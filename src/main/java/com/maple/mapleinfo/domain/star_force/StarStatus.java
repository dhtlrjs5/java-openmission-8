package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarStatus {

    private static final Integer DESTROY_STAR = 12;

    Integer star;
    Long price;
    boolean destroyed;

    public StarStatus() {
        star = 0;
        price = 0L;
        destroyed = false;
    }

    public StarStatus increaseStar() {
        return new StarStatus(star + 1, price, destroyed);
    }

    public StarStatus destroyed() {
        return new StarStatus(DESTROY_STAR, price, true);
    }

    public StarStatus newPrice(Long newPrice) {
        return new StarStatus(star, newPrice, destroyed);
    }

    public StarStatus repair() {
        return new StarStatus(star, price, false);
    }
}
