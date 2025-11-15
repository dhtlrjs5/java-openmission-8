package com.maple.mapleinfo.domain.star_force;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Equipment {

    EquipmentLevel level;
    StarStatus status;

    public Equipment() {
        level = new EquipmentLevel();
        status = new StarStatus();
    }

    public Equipment increaseStar() {
        StarStatus newStatus = status.increaseStar();

        return new Equipment(level, newStatus);
    }

    public Equipment destroyed() {
        StarStatus newStatus = status.destroyed();

        return new Equipment(level, newStatus);
    }

    public Equipment repair() {
        StarStatus newStatus = status.repair();

        return new Equipment(level, newStatus);
    }

    public Equipment newPrice(Long price) {
        StarStatus newStatus = status.newPrice(price);

        return new Equipment(level, newStatus);
    }

    public Integer getLevel() {
        return level.getLevel();
    }

    public Long getPrice() {
        return status.getPrice();
    }

    public Integer getStar() {
        return status.getStar();
    }

    public boolean isDestroyed() {
        return status.isDestroyed();
    }
}
